/* =================== 工具函数 =================== */
/* 风险颜色、时间格式等通用工具函数 */

// 根据风险等级返回颜色（与图例颜色保持一致）
function getColorByRisk(params, currentMode = 'flood') {
  const data = params && params.data ? params.data : params;
  const riskKey = getRiskKeyByMode(data, currentMode);
  switch (riskKey) {
    case 'extreme':
      return currentMode === 'flood' ? '#ff003c' : '#ff003c'; // 极高风险 / 特旱 - 霓虹红
    case 'high':
      return currentMode === 'flood' ? '#ff6600' : '#ff3c00'; // 高风险 / 重旱 - 霓虹橙
    case 'medium':
      return currentMode === 'flood' ? '#ffff00' : '#ff9900'; // 中风险 / 中旱 - 霓虹黄
    case 'low':
      return currentMode === 'flood' ? '#00ff99' : '#00ff99'; // 低风险 / 轻旱 - 霓虹绿
    case 'safe':
    default:
      return currentMode === 'flood' ? '#00f3ff' : '#00f3ff'; // 安全 / 正常 - 霓虹青
  }
}

// 根据当前模式，从点数据中取出对应的风险 key
function getRiskKeyByMode(point, currentMode = 'flood') {
  if (!point) return 'safe';
  if (currentMode === 'drought') {
    return point.droughtRisk || 'safe';
  }
  return point.floodRisk || 'safe';
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

// 格式化时间
function formatTime(date) {
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
}

// 渲染风险图例
// 注意：此函数已在 initMap.js 中实现，这里保留作为备用
function renderRiskLegend(currentMode = 'flood', activeRisks = {
  extreme: true,
  high: true,
  medium: true,
  low: true,
  safe: true
}) {
  const legend = document.getElementById('riskLegend');
  if (!legend) return;

  const RISK_LEVELS = ['extreme', 'high', 'medium', 'low', 'safe'];
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
}

// 导出函数
export { 
  getColorByRisk, 
  getRiskKeyByMode, 
  getMapBaseTheme, 
  formatTime, 
  renderRiskLegend 
};
