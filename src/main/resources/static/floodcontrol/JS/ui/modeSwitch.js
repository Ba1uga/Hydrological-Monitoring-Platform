/* =================== 模式切换 =================== */
/* 防汛 ↔ 抗旱模式切换逻辑 */

// 引入相关模块
import { updateMapForFlood, updateMapForDrought, refreshMonitoringPointsOnMap, initRiskLegend, renderRiskLegend, RISK_LEVELS, getCurrentMode, getCurrentView, setCurrentMode, setCurrentView, getChart, initStationListHover } from '../map/initMap.js';
import { updateParticlesForFlood, updateParticlesForDrought } from '../effects/particles.js';
import { triggerSidePanelAnimations, initNumberAnimations } from '../effects/numberAnimation.js';
import { update3DMapForCurrentMode, switchTo3DMap, switchTo2DMap } from '../map/map3D.js';
import { updateRadarChart, switchToRadarView } from '../charts/initRadarChart.js';
import { updateResourcesForMode } from '../ui/resources.js';
import { refreshStationList } from '../ui/stationList.js';
import { getIsChartVisible, hideChart } from '../charts/stationHistoryChart.js';

// 当前可见的风险等级（通过图例点击控制）——默认全部可见
let activeRisks = {
  extreme: true,
  high: true,
  medium: true,
  low: true,
  safe: true
};

// 当前通过点击选中的风险等级（单选），null 表示显示全部
let selectedClickRisk = null;

// 实时更新时间显示
function updateTime() {
  const now = new Date();
  const timeStr = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
  document.getElementById('timeWeather').innerHTML = `${timeStr} | 晴 | 温度: 22°C`;
}

// 绑定模式切换按钮点击事件
function initModeSwitch() {
  const modeBtns = document.querySelectorAll('.mode-btn');
  modeBtns.forEach(btn => {
    btn.addEventListener('click', function () {
      const mode = this.dataset.mode;
      if (this.id === 'view3dBtn') {
        toggle3DView();
      } else {
        switchMode(mode);
      }
    });
  });
}

// 切换防汛 / 抗旱模式
async function switchMode(mode) {
  if (getCurrentMode() === mode) return;

  // 1. 更新按钮选中状态
  document.querySelectorAll('.mode-btn').forEach(btn => {
    btn.classList.remove('active');
  });
  document.querySelector(`[data-mode="${mode}"]`).classList.add('active');
  
  // 保留 3D 视图按钮的 active 状态
  const view3dBtn = document.getElementById('view3dBtn');
  if (getCurrentView() === '3d') {
    view3dBtn.classList.add('active');
  }

  // 2. 记录当前模式
  // 使用setCurrentMode函数更新全局模式
  setCurrentMode(mode);

  // 3. 切换 body 上的类，触发 CSS 变量变化（颜色、背景等）
  if (mode === 'drought') {
    document.body.classList.add('drought-mode');
    updateParticlesForDrought();
  } else {
    document.body.classList.remove('drought-mode');
    updateParticlesForFlood();
  }

  // 4. 切换模式时重置风险筛选（恢复为全部等级可见）并重绘图例
  selectedClickRisk = null;
  RISK_LEVELS.forEach(level => {
    activeRisks[level] = true;
  });
  renderRiskLegend();

  // 5. 更新左侧信息卡片内容
  // 移除硬编码的 updateSidePanels 调用，改为只更新标题
  updatePanelTitles(mode);
  
  // 5.1 更新右侧资源面板数据
  updateResourcesForMode(mode);

  // 5.2 更新关键站点列表 (根据模式过滤)
  refreshStationList();
  
  // 切换模式时，如果详情页是打开的，需要关闭详情页并重置雷达图
  const detailView = document.getElementById('resourceDetailView');
  if (detailView && detailView.classList.contains('active')) {
    detailView.classList.remove('active');
    detailView.setAttribute('aria-hidden', 'true');
    // 恢复卡片可访问性
    const cards = document.querySelectorAll('.resource-stats .stat-card');
    cards.forEach(c => c.setAttribute('tabindex', '0'));
    
    // 强制切换回雷达图视图
    switchToRadarView();
  }
  
  // 5.2 更新雷达图数据
  updateRadarChart();
  
  // 6. 重新发起 API 请求并更新数据动画
  initNumberAnimations(mode);
  
  // 7. 检查是否当前显示的是图表，如果是，隐藏图表
  if (getIsChartVisible()) {
    hideChart();
  }
  
  // 8. 根据当前视图状态更新地图
  if (getCurrentView() === '3d') {
    // 如果是3D视图，直接更新3D地图的样式和数据
    update3DMapForCurrentMode();
  } else {
    // 如果是2D视图，更新2D地图
    if (mode === 'drought') {
      await updateMapForDrought();
    } else {
      await updateMapForFlood();
    }
  }
}

// 更新左侧面板标题（仅更新标题，不重置数值DOM）
function updatePanelTitles(mode) {
  const summarySection = document.querySelector('.summary-section h3');
  const stationsSection = document.querySelector('.stations-section h3');
  
  if (summarySection) {
    summarySection.textContent = mode === 'flood' ? '当前雨情' : '当前旱情';
  }
  
  if (stationsSection) {
    stationsSection.textContent = mode === 'flood' ? '关键水情站点' : '关键旱情站点';
  }
  
  // 切换模式后，触发侧边栏动画（不再硬编码数值，而是重新请求）
  // triggerSidePanelAnimations(); // 移除旧的硬编码动画触发
}

// 移除旧的 updateSidePanels 函数
// function updateSidePanels() { ... }

// 切换3D/2D视图
async function toggle3DView() {
  const view3dBtn = document.getElementById('view3dBtn');
  
  // 切换视图状态
  const newView = getCurrentView() === '2d' ? '3d' : '2d';
  setCurrentView(newView);
  
  // 更新按钮样式和文本
  view3dBtn.classList.toggle('active', newView === '3d');
  view3dBtn.textContent = newView === '3d' ? '2D视图' : '3D视图';
  
  // 根据新的视图状态执行相应操作
  if (newView === '3d') {
    // 切换到3D视图
    switchTo3DMap();
  } else {
    // 切换回2D视图
    switchTo2DMap();
    
    // 根据当前模式更新2D地图
    if (getCurrentMode() === 'flood') {
      await updateMapForFlood();
    } else {
      await updateMapForDrought();
    }
  }
}

// 导出函数
export { 
  initModeSwitch, 
  switchMode, 
  updateTime, 
  toggle3DView
};
