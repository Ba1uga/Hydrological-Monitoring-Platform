
/* =================== 站点历史数据统计图 =================== */
/* 展示站点过去7天的水位变化，支持缩放和自动更新 */

import { getCurrentMode, setCurrentView } from '../map/initMap.js';
import { buildQueryTimeParams, getSelectedQueryTime } from '../state/queryTimeContext.js';

/**
 * @typedef {Object} StationHistoryItem
 * @property {number} id
 * @property {number} stationId
 * @property {string} stationName
 * @property {string} stationType
 * @property {string} primaryMode
 * @property {string} valueUnit
 * @property {number} currentValue
 * @property {string} recordDate
 * @property {boolean} isWarning
 * @property {string} floodRisk
 * @property {string} droughtRisk
 */

// 全局变量
let historyChart = null;
let currentStationName = null;
let isChartVisible = false;

/**
 * 初始化站点历史数据统计图
 * @param {string} stationName 站点名称
 */
export async function initStationHistoryChart(stationName, mode = getCurrentMode()) {
  currentStationName = stationName;
  
  if (isChartVisible && historyChart) {
    historyChart.showLoading({
      text: '数据加载中...',
      color: mode === 'flood' ? '#00f3ff' : '#ff3c00',
      textColor: '#fff',
      maskColor: 'rgba(0, 0, 0, 0.4)'
    });
    const data = await fetchStationHistoryData(stationName, mode);
    historyChart.hideLoading();
    renderHistoryChart(data || {date: [], data: []}, stationName, mode);
    return;
  }
  
  const mapElement = document.getElementById('map');
  if (!mapElement) return;

  showChartContainer();
  
  if (!historyChart || historyChart.isDisposed()) {
    historyChart = echarts.init(mapElement);
  }
  historyChart.showLoading({
    text: '数据加载中...',
    color: mode === 'flood' ? '#00f3ff' : '#ff3c00',
    textColor: '#fff',
    maskColor: 'rgba(0, 0, 0, 0.4)'
  });

  const data = await fetchStationHistoryData(stationName, mode);

  historyChart.hideLoading();
  renderHistoryChart(data || {date: [], data: []}, stationName, mode);
  
  createBackButton(mode);
  
  window.addEventListener('resize', resizeChart);
}

/**
 * 获取站点历史数据
 * @param {string} stationName 站点名称
 */
async function fetchStationHistoryData(stationName, mode) {
  try {
    const res = await axios.get('/currentOverview/sevenDaysHistory', {
      params: buildQueryTimeParams({ stationName, mode, queryTime: getSelectedQueryTime() })
    });
    
    if (res.data.code === 200) {
      const list = res.data.data;
      if (!list || list.length === 0) return null;
      
      const dates = [];
      const values = [];
      
      list.forEach(item => {
        // 假设 recordDate 是 ISO 字符串 '2023-12-01T10:00:00'
        // 使用 new Date 解析，注意时区问题，后端通常返回的是带时区或UTC时间
        // 这里简化处理，直接格式化
        const dateObj = new Date(item.recordDate);
        const dateStr = echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', dateObj);
        dates.push(dateStr);
        values.push(item.currentValue);
      });
      
      return {
        date: dates,
        data: values
      };
    }
    return null;
  } catch (e) {
    console.error("获取站点历史数据失败", e);
    return null;
  }
}

/**
 * 渲染历史数据图表
 * @param {Object} data 数据对象
 * @param {string} stationName 站点名称
 */
function renderHistoryChart(data, stationName, mode) {
  const isFlood = mode === 'flood';
  const valueLabel = isFlood ? '水位' : '湿度';
  const unit = isFlood ? 'm' : '%';
  const gradientStart = isFlood ? 'rgb(0, 243, 255)' : 'rgb(255, 60, 0)';
  const gradientEnd = isFlood ? 'rgb(0, 89, 255)' : 'rgb(255, 165, 0)';

  const option = {
    backgroundColor: isFlood ? 'rgba(5, 10, 20, 0.9)' : 'rgba(20, 5, 0, 0.9)',
    title: {
      text: `${stationName} - 7天${isFlood ? '防汛' : '抗旱'}监测趋势`,
      subtext: `数据量级: ${data.data.length} | 模式: ${isFlood ? '水位监测' : '旱情监测'}`,
      left: 'center',
      top: 10, // 稍微下移，避免和左上角按钮重叠
      textStyle: { color: '#fff' },
      subtextStyle: { color: '#aaa' }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow', // 阴影效果
        label: {
            backgroundColor: '#6a7985'
        }
      },
      formatter: function (params) {
        // 自定义 tooltip 展示
        const p = params[0];
        if (!p) return '';
        return `${p.name}<br/>
                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:${p.color}"></span>
                ${p.seriesName}: ${p.value} ${unit}`;
      }
    },
    toolbox: {
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        restore: {},
        saveAsImage: {}
      },
      iconStyle: {
        borderColor: '#fff'
      },
      right: 20,
      top: 10
    },
    grid: {
      bottom: 90, // 底部留白 90px
      left: 50,
      right: 50,
      top: 80
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100
      },
      {
        type: 'slider',
        show: true,
        start: 0,
        end: 100,
        height: 30,
        bottom: 30,
        borderColor: 'transparent',
        backgroundColor: 'rgba(47,69,84,0)',
        fillerColor: 'rgba(47,69,84,0.3)',
        handleIcon: 'path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
        handleSize: '80%',
        handleStyle: {
            color: '#fff',
            shadowBlur: 3,
            shadowColor: 'rgba(0, 0, 0, 0.6)',
            shadowOffsetX: 2,
            shadowOffsetY: 2
        },
        textStyle: { color: "#fff" }
      }
    ],
    xAxis: {
      type: 'category',
      data: data.date,
      boundaryGap: false,
      axisLine: { onZero: false, lineStyle: { color: '#fff' } },
      axisLabel: { color: '#fff' },
      splitLine: { show: false }, // 隐藏分割线
      splitArea: { show: false }  // 隐藏分割区域
    },
    yAxis: {
      type: 'value',
      name: `${valueLabel} (${unit})`,
      nameTextStyle: { color: '#fff' },
      axisLine: { lineStyle: { color: '#fff' } },
      axisLabel: { color: '#fff', formatter: `{value} ${unit}` },
      splitLine: { show: false }, // 隐藏分割线
      splitArea: { show: false }, // 隐藏分割区域
      scale: true // 不从0开始，自动缩放
    },
    series: [
      {
        name: `实时${valueLabel}`,
        type: 'line', // 使用 line 类型，配合 large 模式
        symbol: 'none',
        sampling: 'lttb', // 降采样策略
        itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: gradientStart },
          { offset: 1, color: gradientEnd }
        ])
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: gradientStart },
          { offset: 1, color: gradientEnd }
        ])
      },
        data: data.data,
        large: true, // 开启大数据量优化
        largeThreshold: 2000 // 超过2000点开启优化
      }
    ]
  };

  historyChart.setOption(option);
}

/**
 * 切换到图表视图（隐藏地图）
 */
function showChartContainer() {
  const mapElement = document.getElementById('map');
  const riskLegend = document.getElementById('riskLegend');
  const mapContainer = document.querySelector('.map-container');
  
  if (riskLegend) riskLegend.style.display = 'none';
  if (mapContainer) {
    mapContainer.classList.add('chart-mode'); // 可以通过CSS控制样式
    mapContainer.style.backgroundColor = 'rgba(5, 10, 20, 1)';
  }
  
  // 确保map元素用于显示图表
  mapElement.innerHTML = '';
  mapElement.removeAttribute('_echarts_instance_'); // 清除可能的旧实例标记
  isChartVisible = true;
  setCurrentView('chart');
}

/**
 * 隐藏图表，恢复地图
 */
export function hideChart() {
  const mapElement = document.getElementById('map');
  const riskLegend = document.getElementById('riskLegend');
  const mapContainer = document.querySelector('.map-container');
  
  // 销毁图表
  if (historyChart) {
    historyChart.dispose();
    historyChart = null;
  }
  
  // 移除返回按钮
  const backBtn = document.getElementById('chartBackButton');
  if (backBtn) backBtn.remove();
  
  // 恢复 UI
  if (riskLegend) riskLegend.style.display = 'block';
  if (mapContainer) {
    mapContainer.classList.remove('chart-mode');
    mapContainer.style.backgroundColor = '';
  }
  
  isChartVisible = false;
  setCurrentView('2d');
  window.removeEventListener('resize', resizeChart);
  
  // 重新初始化地图 (需要重新导入或调用全局的 initMap)
  // 这里我们动态导入 initMap 来重新渲染
  import('../map/initMap.js').then(module => {
     module.initMap();
  });
}

/**
 * 创建返回按钮
 * @param {string} mode 模式
 */
function createBackButton(mode) {
  if (document.getElementById('chartBackButton')) return;
  
  const btn = document.createElement('button');
  btn.id = 'chartBackButton';
  btn.innerHTML = '← 返回';
  btn.className = 'chart-back-btn'; 
  
  const isFlood = mode === 'flood';
  const color = isFlood ? '#00f3ff' : '#ff3c00';
  const bgColor = isFlood ? 'rgba(0, 243, 255, 0.2)' : 'rgba(255, 60, 0, 0.2)';
  const hoverBg = isFlood ? 'rgba(0, 243, 255, 0.4)' : 'rgba(255, 60, 0, 0.4)';

  btn.style.cssText = `
    position: absolute;
    left: 10px;
    top: 10px;
    z-index: 1000;
    padding: 4px 10px;
    background: ${bgColor};
    border: 1px solid ${color};
    color: #fff;
    cursor: pointer;
    border-radius: 4px;
    font-size: 12px;
    backdrop-filter: blur(4px);
    transition: all 0.3s;
  `;
  
  btn.onmouseover = () => btn.style.background = hoverBg;
  btn.onmouseout = () => btn.style.background = bgColor;
  
  btn.onclick = hideChart;
  
  document.querySelector('.map-container').appendChild(btn);
}

function resizeChart() {
  if (historyChart) historyChart.resize();
}

/**
 * 获取当前是否显示图表
 * @returns {boolean} 是否显示图表
 */
export function getIsChartVisible() {
  return isChartVisible;
}
