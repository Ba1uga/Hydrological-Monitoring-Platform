/* =================== 3D 地图构建 =================== */
/* 3D 地图构建、柱状切换逻辑 */

// 引入监测点数据
import { allMonitoringPoints, getColorByRisk, getMapBaseTheme, initMap, getCurrentMode, renderRiskLegend, getChart, setChart, getMapReady } from './initMap.js';

const AUTO_ROTATE_LOOP_SECONDS = 15;
const AUTO_ROTATE_SPEED = 360 / AUTO_ROTATE_LOOP_SECONDS;
const AUTO_ROTATE_AFTER_STILL_SECONDS = 2;

let autoRotateEnabled = true;
let autoRotateResumeTimer = null;
let rotateControlBtn = null;
let rotationOscillationTimer = null;
let rotationRampTimer = null;
let rotationOscillationEnabled = true;
const ROTATION_OSC_ALPHA_MIN = 32;
const ROTATION_OSC_ALPHA_MAX = 40;
const ROTATION_OSC_DISTANCE_MIN = 84;
const ROTATION_OSC_DISTANCE_MAX = 96;
const ROTATION_OSC_PERIOD_MS = 7000;

let refresh3DMapListener = null;

let cachedProvinceBorderLines3D = null;
let cachedProvinceBorderLines3DKey = null;

function canCreateWebGLContext() {
  try {
    const canvas = document.createElement('canvas');
    return !!(canvas.getContext('webgl') || canvas.getContext('experimental-webgl'));
  } catch (e) {
    return false;
  }
}

function getProvinceBorderLines3D(mapName, z, cacheKeyExtra = '') {
  const zKey = Number.isFinite(z) ? z.toFixed(3) : String(z);
  const key = `${mapName}:${zKey}:${cacheKeyExtra}`;
  if (cachedProvinceBorderLines3D && cachedProvinceBorderLines3DKey === key) {
    return cachedProvinceBorderLines3D;
  }

  const map = echarts && echarts.getMap ? echarts.getMap(mapName) : null;
  const geoJson = map ? map.geoJson : null;
  if (!geoJson || !Array.isArray(geoJson.features)) {
    return [];
  }

  const lines = [];
  geoJson.features.forEach((f) => {
    const g = f && f.geometry ? f.geometry : null;
    if (!g || !g.coordinates) return;
    if (g.type === 'Polygon') {
      g.coordinates.forEach((ring) => {
        if (ring && ring.length > 3) lines.push({ coords: ring.map((p) => [p[0], p[1], z]) });
      });
      return;
    }
    if (g.type === 'MultiPolygon') {
      g.coordinates.forEach((poly) => {
        poly.forEach((ring) => {
          if (ring && ring.length > 3) lines.push({ coords: ring.map((p) => [p[0], p[1], z]) });
        });
      });
    }
  });

  cachedProvinceBorderLines3D = lines;
  cachedProvinceBorderLines3DKey = key;
  return cachedProvinceBorderLines3D;
}

function clamp01(value) {
  return Math.max(0, Math.min(1, value));
}

function parseHexColor(hex) {
  if (!hex) return null;
  const raw = String(hex).trim().replace('#', '');
  if (raw.length !== 6) return null;
  const n = Number.parseInt(raw, 16);
  if (!Number.isFinite(n)) return null;
  return {
    r: (n >> 16) & 255,
    g: (n >> 8) & 255,
    b: n & 255
  };
}

function rgbToHex(rgb) {
  const toHex = (v) => v.toString(16).padStart(2, '0');
  return `#${toHex(rgb.r)}${toHex(rgb.g)}${toHex(rgb.b)}`;
}

function mixHexColor(aHex, bHex, t) {
  const a = parseHexColor(aHex);
  const b = parseHexColor(bHex);
  if (!a || !b) return aHex || bHex || '#ffffff';
  const k = clamp01(t);
  return rgbToHex({
    r: Math.round(a.r + (b.r - a.r) * k),
    g: Math.round(a.g + (b.g - a.g) * k),
    b: Math.round(a.b + (b.b - a.b) * k)
  });
}

function makeBarGradient(baseColor) {
  const top = mixHexColor(baseColor, '#ffffff', 0.50);
  const sheen = mixHexColor(baseColor, '#ffffff', 0.20);
  const body = mixHexColor(baseColor, '#000000', 0.04);
  const shadow = mixHexColor(baseColor, '#000000', 0.14);
  const bottom = mixHexColor(baseColor, '#000000', 0.38);
  return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
    { offset: 0.00, color: top },
    { offset: 0.12, color: sheen },
    { offset: 0.42, color: body },
    { offset: 0.72, color: shadow },
    { offset: 1.00, color: bottom }
  ]);
}

function applyAutoRotateOption(chart, enabled) {
  if (!chart) return;
  chart.setOption({
    geo3D: {
      viewControl: {
        autoRotate: !!enabled,
        autoRotateSpeed: AUTO_ROTATE_SPEED,
        autoRotateAfterStill: AUTO_ROTATE_AFTER_STILL_SECONDS
      }
    }
  });
}

function setAutoRotateSpeed(chart, speed) {
  if (!chart) return;
  chart.setOption({
    geo3D: {
      viewControl: {
        autoRotateSpeed: speed
      }
    }
  });
}

function stopRotationOscillation() {
  if (rotationOscillationTimer) {
    clearInterval(rotationOscillationTimer);
    rotationOscillationTimer = null;
  }
}

function startRotationOscillation(chart) {
  if (!chart) return;
  stopRotationOscillation();
  if (!rotationOscillationEnabled) return;
  const t0 = Date.now();
  rotationOscillationTimer = setInterval(() => {
    if (!autoRotateEnabled) return;
    const dt = (Date.now() - t0) % ROTATION_OSC_PERIOD_MS;
    const k = dt / ROTATION_OSC_PERIOD_MS;
    const ease = 0.5 - 0.5 * Math.cos(2 * Math.PI * k);
    const alpha =
      ROTATION_OSC_ALPHA_MIN + (ROTATION_OSC_ALPHA_MAX - ROTATION_OSC_ALPHA_MIN) * ease;
    const distance =
      ROTATION_OSC_DISTANCE_MIN + (ROTATION_OSC_DISTANCE_MAX - ROTATION_OSC_DISTANCE_MIN) * ease;
    chart.setOption({
      geo3D: {
        viewControl: {
          alpha: Math.round(alpha),
          distance: Math.round(distance)
        }
      }
    });
  }, 300);
}

function stopRamp() {
  if (rotationRampTimer) {
    clearInterval(rotationRampTimer);
    rotationRampTimer = null;
  }
}

function rampAutoRotateSpeed(chart, target, durationMs) {
  if (!chart) return;
  stopRamp();
  const steps = Math.max(1, Math.floor(durationMs / 50));
  let i = 0;
  setAutoRotateSpeed(chart, 0);
  rotationRampTimer = setInterval(() => {
    i++;
    const k = i / steps;
    const eased = k < 1 ? 0.5 - 0.5 * Math.cos(Math.PI * k) : 1;
    setAutoRotateSpeed(chart, target * eased);
    if (i >= steps) {
      stopRamp();
      setAutoRotateSpeed(chart, target);
    }
  }, 50);
}

function pauseAutoRotate(chart) {
  if (!chart) return;
  if (!autoRotateEnabled) return;
  applyAutoRotateOption(chart, false);
  stopRotationOscillation();
  if (autoRotateResumeTimer) {
    clearTimeout(autoRotateResumeTimer);
    autoRotateResumeTimer = null;
  }
  autoRotateResumeTimer = setTimeout(() => {
    autoRotateResumeTimer = null;
    if (autoRotateEnabled) {
      applyAutoRotateOption(chart, true);
      rampAutoRotateSpeed(chart, AUTO_ROTATE_SPEED, 600);
      startRotationOscillation(chart);
    }
  }, AUTO_ROTATE_AFTER_STILL_SECONDS * 1000);
}

function bindAutoRotateInteractionHandlers(chart) {
  if (!chart) return;
  if (chart.__autoRotateBound) return;
  chart.__autoRotateBound = true;

  const zr = chart.getZr && chart.getZr();
  if (!zr) return;

  zr.on('mousedown', () => pauseAutoRotate(chart));
  zr.on('mousewheel', () => pauseAutoRotate(chart));
  zr.on('click', () => pauseAutoRotate(chart));

  chart.on('mouseover', (p) => {
    if (p && p.componentType === 'series' && p.seriesType === 'bar3D') {
      pauseAutoRotate(chart);
    }
  });
}

function ensureRotateControlButton() {
  const mapEl = document.getElementById('map');
  if (!mapEl) return;
  const container = mapEl.parentElement;
  if (!container) return;

  if (!rotateControlBtn) {
    rotateControlBtn = document.createElement('button');
    rotateControlBtn.type = 'button';
    rotateControlBtn.setAttribute('aria-pressed', String(autoRotateEnabled));
    rotateControlBtn.style.position = 'absolute';
    rotateControlBtn.style.right = '12px';
    rotateControlBtn.style.top = '12px';
    rotateControlBtn.style.zIndex = '30';
    rotateControlBtn.style.padding = '8px 10px';
    rotateControlBtn.style.borderRadius = '8px';
    rotateControlBtn.style.border = '1px solid rgba(255,255,255,0.22)';
    rotateControlBtn.style.background = 'rgba(5, 10, 20, 0.55)';
    rotateControlBtn.style.color = '#ffffff';
    rotateControlBtn.style.fontSize = '12px';
    rotateControlBtn.style.cursor = 'pointer';
    rotateControlBtn.style.backdropFilter = 'blur(8px)';
    rotateControlBtn.style.boxShadow = '0 0 14px rgba(0,0,0,0.45)';
    rotateControlBtn.addEventListener('click', (e) => {
      const chart = getChart();
      if (e.shiftKey) {
        rotationOscillationEnabled = !rotationOscillationEnabled;
        if (rotationOscillationEnabled && autoRotateEnabled) {
          startRotationOscillation(chart);
        } else {
          stopRotationOscillation();
        }
      } else {
        autoRotateEnabled = !autoRotateEnabled;
        rotateControlBtn.setAttribute('aria-pressed', String(autoRotateEnabled));
        applyAutoRotateOption(chart, autoRotateEnabled);
        if (autoRotateEnabled) {
          rampAutoRotateSpeed(chart, AUTO_ROTATE_SPEED, 600);
          startRotationOscillation(chart);
        } else {
          stopRotationOscillation();
        }
      }
      ensureRotateControlButton();
    });
    container.style.position = 'relative';
    container.appendChild(rotateControlBtn);
  }

  const mode = getCurrentMode();
  const highlightColor = mode === 'flood' ? '#00f3ff' : '#ff3c00';
  rotateControlBtn.textContent = autoRotateEnabled
    ? (rotationOscillationEnabled ? '自动旋转：开（摆动）' : '自动旋转：开')
    : '自动旋转：关';
  rotateControlBtn.title = autoRotateEnabled ? '点击关闭自动旋转' : '点击开启自动旋转';
  rotateControlBtn.style.borderColor = highlightColor;
  rotateControlBtn.style.boxShadow = `0 0 14px ${highlightColor}55, 0 0 24px rgba(0,0,0,0.35)`;
}

function removeRotateControlButton() {
  if (!rotateControlBtn) return;
  rotateControlBtn.remove();
  rotateControlBtn = null;
  stopRotationOscillation();
  stopRamp();
}

// 根据当前模式，从点数据中取出对应的风险 key
function getRiskKeyByMode(point) {
  if (!point) return 'safe';
  if (getCurrentMode() === 'drought') {
    return point.droughtRisk || 'safe';
  }
  return point.floodRisk || 'safe';
}

// 把 allMonitoringPoints 转成 bar3D 需要的数据结构
function getBar3DData() {
  if (!allMonitoringPoints || !allMonitoringPoints.length) return [];
  const currentMode = getCurrentMode();
  const maxRawValue = allMonitoringPoints.reduce((maxV, p) => {
    const v = p && p.value ? Number(p.value[2]) : 0;
    return Number.isFinite(v) ? Math.max(maxV, v) : maxV;
  }, 0);

  const unit = currentMode === 'flood' ? 'm' : '%';
  const threshold = maxRawValue > 0 ? maxRawValue * 0.7 : 0;

  return allMonitoringPoints.map(point => {
    const baseColor = getColorByRisk(point);
    const rawValue = point && point.value ? Number(point.value[2]) : 0;
    const scaledValue = (Number.isFinite(rawValue) ? rawValue : 0) * 2;
    const showLabel = Number.isFinite(rawValue) && rawValue >= threshold && rawValue > 0;
    return {
      ...point,
      name: point.name,
      value: [
        point.value[0],
        point.value[1],
        scaledValue
      ],
      label: showLabel ? { show: true } : { show: false },
      emphasis: {
        itemStyle: {
          color: baseColor
        },
        label: {
          show: true,
          formatter: () => `${rawValue}${unit}`
        }
      },
      itemStyle: {
        color: baseColor
      },
      
    };
  });
}

// 构建3D地图配置选项
function build3DMapOption() {
  const currentMode = getCurrentMode();
  const theme = getMapBaseTheme(currentMode);
  const borderWidth3D = 2.8;
  const borderColor3D = currentMode === 'flood' ? '#ffffff' : '#ff8800';
  const regionHeight = 2.849;
  const borderZ = regionHeight + 0.25;
  const provinceBorderLines3D = getProvinceBorderLines3D('china_provinces', borderZ, `${currentMode}:${regionHeight}`);
  const barData = getBar3DData();
  const maxScaledValue = barData.reduce((maxV, d) => {
    const v = d && d.value ? Number(d.value[2]) : 0;
    return Number.isFinite(v) ? Math.max(maxV, v) : maxV;
  }, 0);

  return {
    backgroundColor: theme.backgroundColor,
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        if (!params.value) return '';
        const isFloodMode = currentMode === 'flood';
        const valueLabel = isFloodMode ? '水位' : '湿度';
        const unit = isFloodMode ? 'm' : '%';
        const actualValue = params.value[2] / 2;
        const ratio = maxScaledValue > 0 ? clamp01(params.value[2] / maxScaledValue) : 0;
        const ratioText = `${Math.round(ratio * 100)}%`;
        const typeLabel = params.data && params.data.type ? 
          (params.data.type === 'river' ? '河流' : 
           params.data.type === 'lake' ? '湖泊' :
           params.data.type === 'reservoir' ? '水库' : '监测站') : '监测站';
        
        const highlightColor = currentMode === 'flood' ? '#00f3ff' : '#ff3c00';
        
        const pointData = params.data ? params.data : { floodRisk: 'safe', droughtRisk: 'safe' };
        const riskKey = getRiskKeyByMode(pointData);
        const riskLabels = {
          extreme: isFloodMode ? '极高风险' : '特旱',
          high: isFloodMode ? '高风险' : '重旱',
          medium: isFloodMode ? '中风险' : '中旱',
          low: isFloodMode ? '低风险' : '轻旱',
          safe: isFloodMode ? '安全' : '正常'
        };
        const riskLabel = riskLabels[riskKey] || '未知';
        const riskColor = getColorByRisk(pointData);
        
        const lon = params.value[0].toFixed(2);
        const lat = params.value[1].toFixed(2);
        
        return `
          <div style="
            padding: 15px;
            background: linear-gradient(135deg, rgba(5, 10, 20, 0.98), rgba(10, 20, 40, 0.95));
            border-radius: 8px;
            border: 1px solid ${highlightColor};
            box-shadow: 
              0 0 20px ${highlightColor},
              inset 0 0 10px rgba(0, 255, 255, 0.1);
            font-family: 'Microsoft YaHei', Arial, sans-serif;
            color: #e0f7fa;
            backdrop-filter: blur(10px);
          ">
            <!-- 站点名称 -->
            <h3 style="
              margin: 0 0 12px 0;
              color: ${highlightColor};
              font-size: 18px;
              font-weight: bold;
              text-shadow: 0 0 8px ${highlightColor};
              border-bottom: 2px solid ${highlightColor};
              padding-bottom: 8px;
              letter-spacing: 1px;
              font-family: 'Orbitron', sans-serif;
            ">${params.name}</h3>
            
            <!-- 主要数据 -->
            <div style="margin: 12px 0;">
              <div style="
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 8px;
              ">
                <span style="
                  font-size: 14px;
                  color: #a0a0a0;
                  width: 80px;
                ">${valueLabel}:</span>
                <span style="
                  color: ${highlightColor};
                  font-weight: bold;
                  font-size: 18px;
                  font-family: 'Share Tech Mono', monospace;
                  text-shadow: 0 0 5px ${highlightColor};
                ">${actualValue}${unit}</span>
              </div>
              
              <div style="
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 10px;
              ">
                <span style="
                  font-size: 14px;
                  color: #a0a0a0;
                  width: 80px;
                ">高度占比:</span>
                <div style="
                  flex: 1;
                  height: 8px;
                  border-radius: 999px;
                  background: rgba(255,255,255,0.10);
                  border: 1px solid rgba(255,255,255,0.12);
                  overflow: hidden;
                ">
                  <div style="
                    width: ${ratioText};
                    height: 100%;
                    background: linear-gradient(90deg, ${highlightColor}, ${riskColor});
                    box-shadow: 0 0 12px ${highlightColor}66;
                  "></div>
                </div>
                <span style="
                  width: 42px;
                  text-align: right;
                  color: #ffffff;
                  font-family: 'Share Tech Mono', monospace;
                  font-size: 12px;
                  opacity: 0.9;
                ">${ratioText}</span>
              </div>

              <div style="
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 8px;
              ">
                <span style="
                  font-size: 14px;
                  color: #a0a0a0;
                  width: 80px;
                ">类型:</span>
                <span style="
                  color: #ffffff;
                  font-size: 14px;
                ">${typeLabel}</span>
              </div>
              
              <div style="
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 8px;
              ">
                <span style="
                  font-size: 14px;
                  color: #a0a0a0;
                  width: 80px;
                ">坐标:</span>
                <span style="
                  color: #00f3ff;
                  font-family: 'Share Tech Mono', monospace;
                  font-size: 12px;
                ">${lat}°N, ${lon}°E</span>
              </div>
              
              <div style="
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 8px;
              ">
                <span style="
                  font-size: 14px;
                  color: #a0a0a0;
                  width: 80px;
                ">风险等级:</span>
                <span style="
                  color: ${riskColor};
                  font-weight: bold;
                  font-size: 14px;
                  text-shadow: 0 0 8px ${riskColor};
                ">${riskLabel}</span>
                <span style="
                  width: 12px;
                  height: 12px;
                  background-color: ${riskColor};
                  border-radius: 50%;
                  box-shadow: 0 0 8px ${riskColor};
                "></span>
              </div>
            </div>
            
            <!-- 装饰性分隔线 -->
            <div style="
              height: 1px;
              background: linear-gradient(90deg, transparent, ${highlightColor}, transparent);
              margin: 10px 0;
            "></div>
            
            <!-- 模式提示 -->
            <div style="
              font-size: 11px;
              color: ${highlightColor};
              opacity: 0.8;
              text-align: center;
              text-transform: uppercase;
              letter-spacing: 2px;
            ">
              ${isFloodMode ? '防汛模式' : '抗旱模式'}
            </div>
          </div>
        `;
      },
      backgroundColor: 'transparent',
      borderColor: 'transparent',
      padding: 0,
      textStyle: {
        color: '#ffffff',
        fontSize: 12
      }
    },
    geo3D: {
      map: 'china_provinces', // 在 initMap 里注册过
      roam: true,
      zoom: 1.25,
      center: [105, 35],
      regionHeight,
      itemStyle: {
        color: theme.areaColor,       // 使用主题定义的区域颜色
        borderColor: borderColor3D,
        borderWidth: borderWidth3D,       // 使用主题定义的边框宽度
        opacity: 1.0            // 完全不透明
      },
      label: {
        show: false
      },
      shading: 'realistic',    // 使用更真实的光照效果
      light: {
        main: {
          intensity: 1.75,
          shadow: true,
          shadowQuality: 'high',
          alpha: 42,
          beta: 45,
          color: '#ffffff'
        },
        ambient: {
          intensity: 0.62,
          color: '#ffffff'
        },
        ambientCubemap: {
          texture: '',
          exposure: 1.15,
          diffuseIntensity: 0.75,
          specularIntensity: 0.9
        }
      },
      viewControl: {
        distance: 90,
        minDistance: 40,
        maxDistance: 200,
        alpha: 35,
        beta: 10,
        panMouseButton: 'left',
        rotateMouseButton: 'right',
        autoRotate: true,
        autoRotateSpeed: 1.5
      },
      groundPlane: {
        show: false,
        color: '#020514'
      },
      environment: '#020514',
      postEffect: {
        enable: true,
        bloom: { enable: true, intensity: 0.45 },
        SSAO: { enable: true, radius: 5, intensity: 1.2 }, // 增强环境光遮蔽效果
        depthOfField: { enable: false }
      },
      temporalSuperSampling: {
        enable: true
      }
    },
    series: [
      {
        type: 'lines3D',
        name: 'provinceBorderGlow',
        coordinateSystem: 'geo3D',
        polyline: true,
        silent: true,
        data: provinceBorderLines3D,
        blendMode: 'lighter',
        lineStyle: {
          color: borderColor3D,
          width: 7.2,
          opacity: 0.22
        }
      },
      {
        type: 'lines3D',
        name: 'provinceBorder',
        coordinateSystem: 'geo3D',
        polyline: true,
        silent: true,
        data: provinceBorderLines3D,
        blendMode: 'lighter',
        lineStyle: {
          color: borderColor3D,
          width: 2.4,
          opacity: 1
        }
      },
      {
        type: 'bar3D',
        name: '水位柱',
        coordinateSystem: 'geo3D',
        shading: 'lambert',
        bevelSmoothness: 6,
        bevelSize: 0.35,
        barSize: 1.25,
        minHeight: 1,
        data: barData,
        label: {
          show: true,
          distance: 2,
          formatter: (p) => {
            const v = p && p.value ? Number(p.value[2]) : 0;
            const actual = Number.isFinite(v) ? v / 2 : 0;
            const unit = currentMode === 'flood' ? 'm' : '%';
            return actual > 0 ? `${actual}${unit}` : '';
          },
          textStyle: {
            fontSize: 11,
            color: '#ffffff',
            backgroundColor: 'rgba(0,0,0,0.30)',
            padding: [2, 5],
            borderRadius: 4
          }
        }
      }
    ]
  };
}

// 切换到3D视图
function switchTo3DMap() {
  const chart = getChart();
  if (!chart || !getMapReady()) return false;
  if (chart.isDisposed && chart.isDisposed()) return false;
  if (!canCreateWebGLContext()) return false;

  try {
    const option3D = build3DMapOption();
    chart.setOption(option3D, true);
    removeRotateControlButton();
    if (!refresh3DMapListener) {
      refresh3DMapListener = () => {
        update3DMapForCurrentMode();
      };
      window.addEventListener('refresh3DMap', refresh3DMapListener);
    }
    return true;
  } catch (e) {
    return false;
  }
}

// 切换回2D视图
async function switchTo2DMap() {
  const oldChart = getChart();
  if (oldChart) {
    removeRotateControlButton();
    if (autoRotateResumeTimer) {
      clearTimeout(autoRotateResumeTimer);
      autoRotateResumeTimer = null;
    }
    oldChart.dispose();
    setChart(null);
  }

  if (refresh3DMapListener) {
    window.removeEventListener('refresh3DMap', refresh3DMapListener);
    refresh3DMapListener = null;
  }

  await initMap();
  renderRiskLegend();
}

// 更新 3D 地图为当前模式的样式
function update3DMapForCurrentMode() {
  const chart = getChart();
  if (!chart || !getMapReady()) return;
  if (chart.isDisposed && chart.isDisposed()) return;
  
  // 构建并显示当前模式的完整3D地图
  try {
    const option3D = build3DMapOption();
    chart.setOption(option3D, true);
    removeRotateControlButton();
  } catch (e) {
  }
}

// 导出函数
export { 
  switchTo3DMap, 
  switchTo2DMap, 
  update3DMapForCurrentMode,
  build3DMapOption
};
