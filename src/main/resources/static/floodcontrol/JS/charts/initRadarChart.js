/* =================== 雷达图初始化 =================== */
/* 资源与风险分析雷达图 */

import { getCurrentMode } from '../map/initMap.js';

let radarChart = null;

// 初始化雷达图
function initRadarChart() {
  const container = document.getElementById('radarChart');
  if (!container) return;
  
  // 初始化ECharts实例
  radarChart = echarts.init(container);
  
  // 初始化图表
  updateRadarChart();
  
  // 窗口大小变化时，自动调整图表尺寸
  window.addEventListener('resize', () => {
    if (radarChart) radarChart.resize();
  });
  
  // 绑定返回按钮事件
  const backBtn = document.getElementById('backToRadarBtn');
  if (backBtn) {
    backBtn.addEventListener('click', () => {
      // 使用浏览器后退，或者直接调用恢复逻辑
      switchToRadarView();
    });
  }
  
  // 监听浏览器历史记录
  window.addEventListener('popstate', (event) => {
    if (event.state && event.state.view === 'radar') {
      switchToRadarView();
    }
  });
}

// 切换到饼图视图
function switchToPieChart(title, data) {
  if (!radarChart) return;
  
  // 更新标题
  const titleEl = document.getElementById('analysisTitle');
  if (titleEl) titleEl.textContent = title + '分布';
  
  // 隐藏雷达图图例，显示返回按钮
  const legendEl = document.getElementById('radarLegend');
  if (legendEl) legendEl.style.display = 'none';
  
  const backBtn = document.getElementById('backToRadarBtn');
  if (backBtn) backBtn.style.display = 'block';
  
  // 更新分析文本
  const analysisEl = document.getElementById('analysisText');
  if (analysisEl) analysisEl.textContent = `📊 正在展示${title}详细数据分布`;
  
  // 清空并重新设置 Option 为饼图
  radarChart.clear();
  
  radarChart.setOption({
    color: ['#00f3ff', '#00e676', '#ff9800', '#ff3c00', '#d500f9', '#2979ff'],
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      borderColor: '#00f3ff',
      textStyle: { color: '#fff' }
    },
    series: [
      {
        name: title,
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 5,
          borderColor: '#000',
          borderWidth: 1
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '14',
            fontWeight: 'bold',
            color: '#fff'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  }, true); // true 表示不合并，完全重置
  
  // 添加历史记录
  history.pushState({ view: 'pie' }, null, '');
}

// 恢复雷达图视图
function switchToRadarView() {
  if (!radarChart) return;
  
  console.log('Switching back to Radar View...');

  // 恢复标题
  const titleEl = document.getElementById('analysisTitle');
  if (titleEl) titleEl.textContent = '能力评估与差距分析';
  
  // 显示图例
  const legendEl = document.getElementById('radarLegend');
  if (legendEl) legendEl.style.display = 'flex';
  
  // 清除当前实例，防止图表类型冲突
  radarChart.clear();

  // 重新渲染雷达图
  updateRadarChart();
}

// 更新雷达图数据
function updateRadarChart() {
  if (!radarChart) return;
  
  const currentMode = getCurrentMode();
  const isFloodMode = currentMode === 'flood';
  
  // 根据当前模式获取数据
  const radarData = isFloodMode ? getFloodRadarData() : getDroughtRadarData();
  const highlightColor = isFloodMode ? '#00f3ff' : '#ff3c00';
  const targetColor = '#ff9800'; // 标准/目标颜色
  
  // 根据模式设置网格背景色
  const splitAreaColors = isFloodMode 
    ? ['rgba(255, 255, 255, 0.05)', 'rgba(255, 255, 255, 0.08)']
    : ['rgba(255, 60, 0, 0.05)', 'rgba(255, 60, 0, 0.08)'];
  
  // 生成智能分析文本
  generateAnalysis(isFloodMode, radarData);
  
  // 设置图表选项
  radarChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      borderColor: highlightColor,
      borderWidth: 1,
      textStyle: {
        color: '#ffffff'
      }
    },
    radar: {
      indicator: radarData.indicator,
      splitArea: {
        areaStyle: {
          color: splitAreaColors
        }
      },
      axisLine: {
        lineStyle: {
          color: highlightColor
        }
      },
      splitLine: {
        lineStyle: {
          color: highlightColor,
          opacity: 0.3
        }
      },
      name: {
        textStyle: {
          color: '#ffffff',
          fontSize: 10
        }
      },
      center: ['50%', '50%'],
      radius: '65%'
    },
    series: [
      {
        type: 'radar',
        data: [
          {
            value: radarData.series[0].value,
            name: '当前能力',
            itemStyle: { color: highlightColor },
            lineStyle: { color: highlightColor, width: 2 },
            areaStyle: {
              color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [
                { color: highlightColor, offset: 0 },
                { color: 'rgba(0, 0, 0, 0)', offset: 1 }
              ]),
              opacity: 0.5
            }
          },
          {
            value: radarData.series[1].value, // 标准线数据
            name: '应急标准',
            itemStyle: { color: targetColor },
            lineStyle: { type: 'dashed', color: targetColor, width: 1 },
            areaStyle: { opacity: 0 }, // 标准线不填充区域
            symbol: 'none'
          }
        ]
      }
    ]
  }, true); // true 表示不合并，完全重置
}

// 智能分析文本生成
function generateAnalysis(isFloodMode, data) {
  const analysisEl = document.getElementById('analysisText');
  if (!analysisEl) return;
  
  // 找出短板（当前值与标准值差距最大的项）
  const current = data.series[0].value;
  const target = data.series[1].value;
  const indicators = data.indicator;
  
  let maxGap = 0;
  let weakPoint = '';
  
  for (let i = 0; i < current.length; i++) {
    const gap = target[i] - current[i];
    if (gap > maxGap) {
      maxGap = gap;
      weakPoint = indicators[i].name;
    }
  }
  
  let text = '';
  if (isFloodMode) {
    if (maxGap > 15) {
      text = `⚠️ 警告：${weakPoint}低于标准${maxGap}%，建议立即增派资源！`;
    } else {
      text = `✅ 状态良好：防汛体系运行平稳，${weakPoint}需持续关注。`;
    }
  } else {
    if (maxGap > 15) {
      text = `⚠️ 预警：${weakPoint}缺口较大，需优先调度！`;
    } else {
      text = `💧 监测中：抗旱工作有序开展，重点保障${weakPoint}。`;
    }
  }
  
  // 打字机效果：重置内容以触发动画
  analysisEl.textContent = text;
  analysisEl.style.animation = 'none';
  analysisEl.offsetHeight; /* trigger reflow */
  analysisEl.style.animation = 'blink-caret 0.75s step-end infinite';
}

// 获取防汛模式下的雷达图数据
function getFloodRadarData() {
  return {
    indicator: [
      { name: '物资充足', max: 100 },
      { name: '队伍响应', max: 100 },
      { name: '设备完好', max: 100 },
      { name: '路网通达', max: 100 },
      { name: '医疗覆盖', max: 100 },
      { name: '风险抵御', max: 100 }
    ],
    series: [
      {
        name: '当前能力',
        value: [85, 78, 92, 88, 75, 90] // 当前值
      },
      {
        name: '应急标准',
        value: [90, 90, 95, 90, 90, 95] // 标准值（通常较高）
      }
    ]
  };
}

// 获取抗旱模式下的雷达图数据
function getDroughtRadarData() {
  return {
    indicator: [
      { name: '水源保障', max: 100 },
      { name: '灌溉覆盖', max: 100 },
      { name: '人饮安全', max: 100 },
      { name: '自救能力', max: 100 },
      { name: '设备运行', max: 100 },
      { name: '资金到位', max: 100 }
    ],
    series: [
      {
        name: '当前能力',
        value: [72, 65, 88, 80, 70, 85] // 当前值（抗旱形势通常较严峻）
      },
      {
        name: '应急标准',
        value: [85, 80, 100, 85, 85, 90] // 标准值
      }
    ]
  };
}

// 导出函数
export {
  initRadarChart,
  updateRadarChart,
  switchToPieChart,
  switchToRadarView
};
