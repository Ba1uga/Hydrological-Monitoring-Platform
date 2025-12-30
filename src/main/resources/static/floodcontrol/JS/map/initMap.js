/* =================== 2D 地图初始化 =================== */
/* 2D 地图初始化、监测点渲染、图例交互 */

import { initStationHistoryChart } from '../charts/stationHistoryChart.js';

// 引入监测点数据
// import allMonitoringPoints from '../data/monitoringPoints.js';
export let allMonitoringPoints = [];

// 全局状态
let chart = null;
export let mapReady = false;
let currentMode = 'flood';
let currentView = '2d';
export const RISK_LEVELS = ['extreme', 'high', 'medium', 'low', 'safe'];

// 导出chart的getter和setter函数
export function getChart() {
  return chart;
}

export function setChart(newChart) {
  chart = newChart;
}

// 导出getter和setter函数
export function getCurrentMode() {
  return currentMode;
}

export function setCurrentMode(mode) {
  currentMode = mode;
  if (typeof document !== 'undefined' && document.body) {
    document.body.setAttribute('data-mode', mode);
  }
}

export function getCurrentView() {
  return currentView;
}

export function setCurrentView(view) {
  currentView = view;
}

// 当前可见的风险等级（通过图例点击控制）——默认全部可见
let activeRisks = {
  extreme: true,
  high: true,
  medium: true,
  low: true,
  safe: true
};

// 鼠标悬停在图例某一风险等级上时的高亮状态
let hoverRisk = null;

// 当前通过点击选中的风险等级（单选），null 表示显示全部
let selectedClickRisk = null;

// 当前鼠标悬停的站点名称，用于高亮显示特定站点
let hoverStation = null;

export function setHoverStation(name) {
  hoverStation = name;
  refreshMonitoringPointsOnMap();
}

// 根据当前模式，从点数据中取出对应的风险 key
function getRiskKeyByMode(point) {
  if (!point) return 'safe';
  if (currentMode === 'drought') {
    return point.droughtRisk || 'safe';
  }
  return point.floodRisk || 'safe';
}

// 根据风险等级返回颜色（与图例颜色保持一致）
function getColorByRisk(params) {
  const data = params && params.data ? params.data : params;
  const riskKey = getRiskKeyByMode(data);
  switch (riskKey) {
    case 'extreme':
      return '#ff003c';
    case 'high':
      return '#ff3c00';
    case 'medium':
      return '#ffff00';
    case 'low':
      return '#00ff99';
    case 'safe':
    default:
      return '#00f3ff';
  }
}

// 不同模式下的地图基础配色（背景、区域色、边界、强调色）
function getMapBaseTheme(mode) {
  const isDrought = mode === 'drought';
  return {
    backgroundColor: isDrought ? '#110500' : '#050a14', // 赛博背景色
    areaColor: isDrought ? '#1a0800' : '#0a1424',
    borderColor: isDrought ? 'rgba(255, 81, 0, 0.3)' : 'rgba(0, 243, 255, 0.2)',
    borderWidth: isDrought ? 0.6 : 0,
    emphasisAreaColor: isDrought ? '#ff3c00' : '#00f3ff', // 赛博高亮色
    emphasisBorderColor: isDrought ? '#ffffff' : '#ffffff',
    emphasisShadowColor: isDrought ? '#ff3c00' : '#00f3ff' // 赛博霓虹阴影
  };
}

// 根据当前过滤规则（悬浮 + 点击）返回需要展示的监测点
function getFilteredMonitoringPoints() {
  if (!allMonitoringPoints || !allMonitoringPoints.length) return [];

  return allMonitoringPoints.filter(point => {
    const risk = getRiskKeyByMode(point);

    // 鼠标悬浮在站点列表时：只显示当前悬浮的站点
    if (hoverStation) {
      return point.name === hoverStation;
    }

    // 鼠标悬浮在图例时：只显示当前悬浮的风险等级
    if (hoverRisk) {
      return risk === hoverRisk;
    }

    // 平时：根据 activeRisks（由点击图例控制）
    if (activeRisks[risk] === false) {
      return false;
    }
    return true;
  });
}

// 刷新地图上的监测点数据（应用过滤结果）
function refreshMonitoringPointsOnMap() {
  if (currentView === 'chart') return;
  const chart = getChart();
  if (!chart || !mapReady) return;
  chart.setOption({
    series: [
      {
        id: 'monitoringPoints',
        data: getFilteredMonitoringPoints()
      }
    ]
  });
}

// 初始化风险等级图例的鼠标事件（悬浮高亮 + 单选过滤）
async function initRiskLegend() {
  const legend = document.getElementById('riskLegend');
  if (!legend) return;

  // 初始渲染一次图例
  renderRiskLegend();
}

// 按当前模式渲染图例内容（防汛 / 抗旱显示不同文案）
function renderRiskLegend() {
  const legend = document.getElementById('riskLegend');
  if (!legend) return;

  const floodLabels = {
    extreme: '极高风险',
    high: '高风险',
    medium: '中风险',
    low: '低风险',
    safe: '安全'
  };

  const droughtLabels = {
    extreme: '特旱',
    high: '重旱',
    medium: '中旱',
    low: '轻旱',
    safe: '正常'
  };

  const labels = currentMode === 'drought' ? droughtLabels : floodLabels;

  let html = `
    <div class="risk-legend-header">
      <div class="risk-legend-title">站点风险等级</div>
      <button class="risk-reset-btn" title="重置筛选">
        <span style="font-size: 12px;">↺</span>
      </button>
    </div>
  `;
  RISK_LEVELS.forEach(key => {
    const label = labels[key] || key;
    html += `
      <div class="risk-legend-item${activeRisks[key] ? '' : ' disabled'}" data-risk="${key}">
        <span class="risk-color-box risk-${key}"></span>
        <span class="risk-label">${label}</span>
      </div>
    `;
  });
  legend.innerHTML = html;

  // 绑定交互事件
    const items = legend.querySelectorAll('.risk-legend-item');
    items.forEach(item => {
      const risk = item.dataset.risk;
      if (!risk) return;

      // 悬浮：临时只显示某一风险等级
      item.addEventListener('mouseenter', () => {
        hoverRisk = risk;
        refreshMonitoringPointsOnMap();
      });

      // 移出：恢复到当前点击筛选状态
      item.addEventListener('mouseleave', () => {
        hoverRisk = null;
        refreshMonitoringPointsOnMap();
      });

      // 点击：默认实现多选效果
      item.addEventListener('click', (event) => {
        // 多选模式：切换当前风险等级的可见性
        activeRisks[risk] = !activeRisks[risk];
        item.classList.toggle('disabled', !activeRisks[risk]);
        selectedClickRisk = null;
        refreshMonitoringPointsOnMap();
      });
    });
    
    // 绑定重置按钮事件
    const resetBtn = legend.querySelector('.risk-reset-btn');
    if (resetBtn) {
      resetBtn.addEventListener('click', () => {
        // 恢复所有风险等级可见
        RISK_LEVELS.forEach(level => {
          activeRisks[level] = true;
        });
        
        // 更新所有图例项状态
        items.forEach(it => it.classList.remove('disabled'));
        
        selectedClickRisk = null;
        refreshMonitoringPointsOnMap();
      });
    }
}

async function fetchStationData(mode) {
    try {
        // 使用新的整点数据API
        const res = await axios.get('/currentOverview/currentHourStations', {
            params: {
                mode: mode
            }
        });
        if (res.data.code === 200) {
            allMonitoringPoints = res.data.data.map(s => ({
                name: s.stationName,
                value: [s.longitude, s.latitude, s.currentValue],
                type: s.stationType,
                primaryMode: s.primaryMode,
                floodRisk: s.floodRisk,
                droughtRisk: s.droughtRisk
            }));
        }
    } catch (e) {
        console.error("获取站点数据失败", e);
    }
}

// 自动刷新数据函数
async function autoRefreshData() {
    console.log('自动刷新数据:', new Date().toLocaleString());
    await fetchStationData(currentMode);
    refreshMonitoringPointsOnMap();
    const refresh3DEvent = new CustomEvent('refresh3DMap');
    window.dispatchEvent(refresh3DEvent);
    
    // 更新站点列表
    // 由于initStationList是从其他模块导入的，我们需要使用事件来通知更新
    const refreshEvent = new CustomEvent('refreshStationList');
    window.dispatchEvent(refreshEvent);
}

// 检测整点并自动刷新的函数
function checkAndRefresh() {
    const now = new Date();
    const minutes = now.getMinutes();
    const seconds = now.getSeconds();
    
    // 如果当前是整点，执行刷新
    if (minutes === 0 && seconds === 0) {
        autoRefreshData();
    }
}

// 启动自动刷新检测
function startAutoRefresh() {
    // 每秒检查一次
    setInterval(checkAndRefresh, 1000);
    console.log('已启动整点自动刷新功能');
}

// 2D 地图初始化函数
async function initMap() {
  // 显示地图加载遮罩，阻止拖动与缩放
  const loadingEl = document.getElementById('mapLoading');
  if (loadingEl) loadingEl.style.display = 'flex';
  mapReady = false;

  // 启动自动刷新检测
  startAutoRefresh();

  // Fetch data
  await fetchStationData(currentMode);

  const loadJson = async (relativePath) => {
    const url = new URL(relativePath, import.meta.url);
    const res = await fetch(url);
    if (!res.ok) {
      throw new Error(`资源加载失败: ${url.pathname} (${res.status})`);
    }
    return res.json();
  };

  try {
    const [chinaGeo, provincesGeo, riversGeo] = await Promise.all([
      loadJson('../../geojson/countries_10m.geojson'),
      loadJson('../../geojson/states_provinces_10m.geojson'),
      loadJson('../../geojson/china_rivers_10m.geojson')
    ]);
    if (!chinaGeo || !provincesGeo || !riversGeo) {
      throw new Error('地图数据为空');
    }

    if (!Array.isArray(chinaGeo.features) || !Array.isArray(provincesGeo.features) || !Array.isArray(riversGeo.features)) {
      throw new Error('地图数据格式不正确');
    }

    // 使用省份边界作为底图
    echarts.registerMap('china_provinces', provincesGeo);

    // 如果可用，则启用 UniversalTransition（平滑动画）
    if (echarts && echarts.use && echarts.UniversalTransition) {
      echarts.use([echarts.UniversalTransition]);
    }

    // 1. 国界线（外轮廓）
    const countryBorderLines = [];
    chinaGeo.features.forEach(f => {
      if (
        f.properties.SOVEREIGNT === 'China' ||
        f.properties.ADMIN === 'China' ||
        f.properties.ADMIN === 'Taiwan' ||
        (f.properties.ADMIN && f.properties.ADMIN.includes('Hong Kong')) ||
        (f.properties.ADMIN && f.properties.ADMIN.includes('Macao'))
      ) {
        const coords = f.geometry.coordinates;
        if (f.geometry.type === 'Polygon') {
          coords.forEach(ring => {
            if (ring.length > 3) countryBorderLines.push({ coords: ring });
          });
        } else if (f.geometry.type === 'MultiPolygon') {
          coords.forEach(poly => {
            poly.forEach(ring => {
              if (ring.length > 3) countryBorderLines.push({ coords: ring });
            });
          });
        }
      }
    });

    // 2. 省界内部线
    const provinceLines = [];
    provincesGeo.features.forEach(f => {
      const coords = f.geometry.coordinates;
      if (f.geometry.type === 'Polygon') {
        coords.forEach(ring => {
          if (ring.length > 3) provinceLines.push({ coords: ring });
        });
      } else if (f.geometry.type === 'MultiPolygon') {
        coords.forEach(poly => {
          poly.forEach(ring => {
            if (ring.length > 3) provinceLines.push({ coords: ring });
          });
        });
      }
    });

    // 3. 河流线（统一宽度 2.0）
    const riverLines = [];
    riversGeo.features.forEach(f => {
      const p = f.properties;
      const g = f.geometry;
      if (!g || !p) return;
      if (p.featurecla && !p.featurecla.includes('River')) return;
      if (p.scalerank > 8) return;

      const coordsArray = g.type === 'LineString' ? [g.coordinates] : g.coordinates;
      coordsArray.forEach(segment => {
        if (segment.length < 2) return;
        riverLines.push({
          coords: segment,
          lineStyle: { width: 2.0 }
        });
      });
    });
    
    // 4. 为防汛模式准备河流流动光带数据
    const riverFlowData = [];
    riversGeo.features.forEach(f => {
      const p = f.properties;
      const g = f.geometry;
      if (!g || !p) return;
      if (p.featurecla && !p.featurecla.includes('River')) return;
      // 只处理主要河流（scalerank <= 6）
      if (p.scalerank > 6) return;

      const coordsArray = g.type === 'LineString' ? [g.coordinates] : g.coordinates;
      coordsArray.forEach(segment => {
        if (segment.length < 2) return;
        
        // 从河流线段中采样点，用于流动效果
        const sampleStep = Math.max(1, Math.floor(segment.length / 10));
        for (let i = 0; i < segment.length; i += sampleStep) {
          const point = segment[i];
          if (point && point.length >= 2) {
            riverFlowData.push({
              name: p.name || '河流',
              value: [point[0], point[1]],
              // 流动速度，主要河流更快
              flowSpeed: 0.5 + (8 - p.scalerank) * 0.1,
              // 河流等级
              riverLevel: p.scalerank
            });
          }
        }
      });
    });

    // 5. 初始化 ECharts 实例
    const mapEl = document.getElementById('map');
    if (!mapEl) {
      throw new Error('地图容器未找到');
    }
    const newChart = echarts.init(mapEl);
    setChart(newChart);

    const theme = getMapBaseTheme(currentMode);
    const chart = getChart();
    const isFlood = currentMode === 'flood';
    const countryLineColor = isFlood ? '#1e90ff' : '#ff6600';
    const provinceLineColor = isFlood ? '#4080ff' : '#ff8800';
    const riverLineColor = isFlood ? '#00ffff' : '#0066cc';
    const pointSymbol = isFlood ? 'circle' : 'triangle';

    chart.off('click');
    chart.on('click', (params) => {
      if (params.componentType === 'series' && params.seriesId === 'monitoringPoints') {
        initStationHistoryChart(params.name, currentMode);
        setHoverStation(params.name);
      }
    });

    chart.setOption({
      backgroundColor: theme.backgroundColor,
      tooltip: {
        trigger: 'item',
        formatter: function (params) {
          // 根据当前模式获取高亮颜色
          const highlightColor = currentMode === 'flood' ? '#00f3ff' : '#ff3c00';
          const bgGradientStart = currentMode === 'flood' ? 'rgba(5, 10, 20, 0.98)' : 'rgba(17, 5, 0, 0.98)';
          const bgGradientEnd = currentMode === 'flood' ? 'rgba(10, 20, 40, 0.95)' : 'rgba(26, 8, 0, 0.95)';
          const isProvince = params.seriesName === 'china_provinces' || !params.seriesName;
          
          // 省份悬浮信息样式
          if (isProvince) {
            return `
              <div style="
                padding: 12px 16px;
                background: linear-gradient(135deg, ${bgGradientStart}, ${bgGradientEnd});
                border-radius: 8px;
                border: 1px solid ${highlightColor};
                box-shadow: 
                  0 0 15px ${highlightColor},
                  inset 0 0 8px rgba(0, 255, 255, 0.1);
                font-family: 'Microsoft YaHei', Arial, sans-serif;
                color: #e0f7fa;
                backdrop-filter: blur(10px);
                min-width: 150px;
                text-align: center;
              ">
                <h3 style="
                  margin: 0;
                  color: ${highlightColor};
                  font-size: 18px;
                  font-weight: bold;
                  text-shadow: 0 0 8px ${highlightColor};
                  letter-spacing: 1px;
                  font-family: 'Orbitron', sans-serif;
                ">${params.name}</h3>
              </div>
            `;
          }
          
          // 其他元素使用默认样式
          return params.name;
        },
        backgroundColor: 'transparent', // 透明背景，让自定义样式生效
        borderColor: 'transparent',
        padding: 0,
        textStyle: {
          color: '#ffffff',
          fontSize: 12
        }
      },
      geo: {
        map: 'china_provinces',
        roam: true,
        zoom: 1.25,
        center: [105, 35],
        itemStyle: {
          areaColor: theme.areaColor,
          borderColor: theme.borderColor,
          borderWidth: theme.borderWidth
        },
        emphasis: {
            itemStyle: {
              // 保持区域颜色与正常状态一致
              areaColor: theme.areaColor,
              // 只显示边界高亮
              borderColor: theme.emphasisBorderColor,
              borderWidth: 3,
              // 确保没有阴影效果
              shadowBlur: 0,
              shadowColor: 'transparent'
            }
          },
        regions: [
          {
            name: '南海诸岛',
            itemStyle: {
              areaColor: '#0e2238',
              borderColor: '#1e90ff'
            }
          }
        ]
      },
      series: [
        // 1. 国界外轮廓
        {
          id: 'countryBorder',
          type: 'lines',
          coordinateSystem: 'geo',
          polyline: true,
          data: countryBorderLines,
          silent: true,
          lineStyle: {
            color: countryLineColor,
            width: 2,
            opacity: 0.8
          },
          zlevel: 1
        },

        // 2. 省界内部线
        {
          id: 'provinceBorder',
          type: 'lines',
          coordinateSystem: 'geo',
          polyline: true,
          data: provinceLines,
          silent: true,
          lineStyle: {
            color: provinceLineColor,
            width: 1.1,
            opacity: 0.6
          },
          zlevel: 2
        },

        // 3. 河流
        {
          id: 'riverLayer',
          type: 'lines',
          coordinateSystem: 'geo',
          polyline: true,
          data: riverLines,
          lineStyle: {
            color: riverLineColor,
            width: 2.0,
            opacity: isFlood ? 1 : 0.8
          },
          zlevel: 3
        },

        // 4. 监测点（按风险等级着色，带呼吸效果）
        {
          id: 'monitoringPoints',
          type: 'effectScatter',
          coordinateSystem: 'geo',
          data: getFilteredMonitoringPoints(),
          symbol: pointSymbol,
          symbolSize: 12,
          showEffectOn: 'render',
          rippleEffect: {
            brushType: 'stroke',
            scale: 4,
            period: 4
          },
          label: {
            show: false
          },
          emphasis: {
            scale: true,
            scaleSize: 20,
            label: {
              show: false
            }
          },
          itemStyle: {
            // 颜色根据风险等级动态计算
            color: getColorByRisk,
            shadowBlur: 10,
            shadowColor: '#333'
          },
          zlevel: 4,
          tooltip: {
            formatter: function (params) {
              const isFloodMode = currentMode === 'flood';
              const valueLabel = isFloodMode ? '水位' : '湿度';
              const unit = isFloodMode ? 'm' : '%';
              const typeLabel =
                params.data.type === 'river'
                  ? '河流'
                  : params.data.type === 'lake'
                  ? '湖泊'
                  : params.data.type === 'reservoir'
                  ? '水库'
                  : '监测站';
              
              // 获取风险等级文本
              const riskKey = getRiskKeyByMode(params.data);
              const riskLabels = {
                extreme: isFloodMode ? '极高风险' : '特旱',
                high: isFloodMode ? '高风险' : '重旱',
                medium: isFloodMode ? '中风险' : '中旱',
                low: isFloodMode ? '低风险' : '轻旱',
                safe: isFloodMode ? '安全' : '正常'
              };
              
              const riskLabel = riskLabels[riskKey] || '未知';
              const highlightColor = currentMode === 'flood' ? '#00f3ff' : '#ff3c00';
              const riskColor = getColorByRisk(params.data);
              
              // 获取坐标信息（格式化显示）
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
                      ">${params.value[2]}${unit}</span>
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
            backgroundColor: 'transparent', // 透明背景，让自定义样式生效
            borderColor: 'transparent',
            padding: 0,
            textStyle: {
              color: '#ffffff',
              fontSize: 12
            }
          }
        }
      ]
    });

    // 窗口大小变化时，自动调整地图尺寸
    window.onresize = () => {
      const chart = getChart();
      if (chart) chart.resize();
    };

    // 地图与数据全部加载完成，允许拖动缩放
    mapReady = true;
    if (loadingEl) loadingEl.style.display = 'none';
  } catch (err) {
    console.error(err);
    mapReady = false;
    if (loadingEl) {
      loadingEl.innerHTML = '<span>地图加载失败，请稍后重试</span>';
    } else {
      alert('地图加载失败：' + err.message);
    }
    throw err;
  }
}

// 切换地图为防汛模式
async function updateMapForFlood() {
  currentMode = 'flood';
  
  // 重新获取防汛模式的数据
  await fetchStationData('flood');
  
  if (currentView === 'chart') return;
  
  const chart = getChart();
  if (!chart || !mapReady) return;

  // 更新点击事件监听器
  chart.off('click');
  chart.on('click', (params) => {
      if (params.componentType === 'series' && params.seriesId === 'monitoringPoints') {
          initStationHistoryChart(params.name, currentMode);
          setHoverStation(params.name);
      }
  });

  const theme = getMapBaseTheme('flood');

  chart.setOption({
    animation: {
      duration: 1500,
      easing: 'cubicOut'
    },
    geo: {
      itemStyle: {
        areaColor: theme.areaColor,
        borderColor: theme.borderColor,
        borderWidth: theme.borderWidth
      },
      emphasis: {
        itemStyle: {
          // 保持区域颜色与正常状态一致
          areaColor: theme.areaColor,
          // 只显示边界高亮
          borderColor: theme.emphasisBorderColor,
          borderWidth: 3,
          // 确保没有阴影效果
          shadowBlur: 0,
          shadowColor: 'transparent'
        }
      }
    },
    backgroundColor: theme.backgroundColor,
    series: [
        {
          id: 'countryBorder',
          lineStyle: {
            color: '#1e90ff',
            width: 2,
            opacity: 0.8
          }
        },
        {
          id: 'provinceBorder',
          lineStyle: {
            color: '#4080ff',
            width: 1.1,
            opacity: 0.6
          }
        },
        {
          id: 'riverLayer',
          lineStyle: {
            color: '#00ffff',
            opacity: 1
          }
        },
        {
          id: 'monitoringPoints',
          type: 'effectScatter',
          symbol: 'circle',
          symbolSize: 12,
          showEffectOn: 'render',
          rippleEffect: {
            brushType: 'stroke',
            scale: 4,
            period: 4
          },
          itemStyle: {
            color: getColorByRisk,
            shadowBlur: 10,
            shadowColor: '#333'
          },
          data: getFilteredMonitoringPoints()
        }
      ]
  });
}

// 切换地图为抗旱模式
async function updateMapForDrought() {
  currentMode = 'drought';
  
  // 重新获取抗旱模式的数据
  await fetchStationData('drought');
  
  // 如果当前是图表视图，只更新数据，不刷新地图渲染
  if (currentView === 'chart') return;
  
  const chart = getChart();
  if (!chart || !mapReady) return;

  // 更新点击事件监听器
  chart.off('click');
  chart.on('click', (params) => {
      if (params.componentType === 'series' && params.seriesId === 'monitoringPoints') {
          initStationHistoryChart(params.name, currentMode);
          setHoverStation(params.name);
      }
  });

  const theme = getMapBaseTheme('drought');

  chart.setOption({
    animation: {
      duration: 1500,
      easing: 'cubicOut'
    },
    geo: {
      itemStyle: {
        areaColor: theme.areaColor,
        borderColor: theme.borderColor,
        borderWidth: theme.borderWidth
      },
      emphasis: {
        itemStyle: {
          // 保持区域颜色与正常状态一致
          areaColor: theme.areaColor,
          // 只显示边界高亮
          borderColor: theme.emphasisBorderColor,
          borderWidth: 3,
          // 确保没有阴影效果
          shadowBlur: 0,
          shadowColor: 'transparent'
        }
      }
    },
    backgroundColor: theme.backgroundColor,
    series: [
        {
          id: 'countryBorder',
          lineStyle: {
            color: '#ff6600',
            width: 2,
            opacity: 0.8
          }
        },
        {
          id: 'provinceBorder',
          lineStyle: {
            color: '#ff8800',
            width: 1.1,
            opacity: 0.6
          }
        },
        {
          id: 'riverLayer',
          lineStyle: {
            color: '#0066cc',
            opacity: 0.8
          }
        },
        {
          id: 'monitoringPoints',
          type: 'effectScatter',
          symbol: 'triangle',
          symbolSize: 12,
          showEffectOn: 'render',
          rippleEffect: {
            brushType: 'stroke',
            scale: 4,
            period: 4
          },
          itemStyle: {
            color: getColorByRisk,
            shadowBlur: 10,
            shadowColor: '#333'
          },
          data: getFilteredMonitoringPoints()
        }
      ]
  });
}

// 导出函数
export { 
  initMap, 
  updateMapForFlood, 
  updateMapForDrought, 
  initRiskLegend,
  renderRiskLegend,
  refreshMonitoringPointsOnMap,
  getColorByRisk,
  getMapBaseTheme
};
