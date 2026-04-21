/* =================== 数字翻牌动画 =================== */
/* 数字翻牌动画函数 animateNumber */

/**
 * @typedef {Object} DashboardCard
 * @property {number} alertStationCount
 * @property {string} trend
 * @property {'up'|'down'|'flat'} trendDirection
 * @property {number} affectedArea
 * @property {string} dataTime
 */

// 数字翻牌动画函数
// 支持整数和小数，自动处理单位和格式
import { buildQueryTimeParams, getSelectedQueryTime } from '../state/queryTimeContext.js';

function animateNumber(el, target, duration = 1200, options = {}) {
  const {
    unit = '',
    isDecimal = false,
    decimalPlaces = 2,
    prefix = ''
  } = options;
  
  // 总是从0开始动画，不管当前元素的文本内容是什么
  const start = 0;
  const startTime = performance.now();
  
  function update(now) {
    const p = Math.min((now - startTime) / duration, 1);
    let value = start + (target - start) * p;
    
    // 处理小数格式
    if (isDecimal) {
      value = value.toFixed(decimalPlaces);
    } else {
      value = Math.floor(value).toLocaleString();
    }
    
    el.textContent = `${prefix}${value}${unit}`;
    
    if (p < 1) {
      requestAnimationFrame(update);
    } else {
      // 动画完成，添加高光效果
      el.classList.add('number-highlight');
      
      // 移除高光效果，以便下次动画可以再次触发
      setTimeout(() => {
        el.classList.remove('number-highlight');
      }, 600); // 与CSS动画持续时间匹配
    }
  }
  
  requestAnimationFrame(update);
}

// 初始化所有数字动画
async function initNumberAnimations(mode = 'flood') {
  // 汇总信息数值动画（左侧面板）
  const summaryValues = document.querySelectorAll('.summary-value');
  const queryTimeStatus = document.getElementById('queryTimeStatus');
  if (summaryValues.length > 0) {
    try {
        // 使用新的实时卡片接口获取数据
        const cardRes = await axios.get('/currentOverview/realtime-card', {
          params: buildQueryTimeParams({ mode })
        });
        if (cardRes.data.code === 200) {
            const cardData = cardRes.data.data;
            if (queryTimeStatus) {
              queryTimeStatus.textContent = getSelectedQueryTime()
                ? `当前查看 ${getSelectedQueryTime()}`
                : '当前为最新整点数据';
            }
            
            // 1. 警戒站点数
            const warningCount = cardData.alertStationCount;
            animateNumber(summaryValues[0], warningCount, 1000, {
              unit: '',
              isDecimal: false
            });
            
            // 2. 趋势数据
            const trendValue = parseInt(cardData.trend);
            const trendDir = cardData.trendDirection === 'up' ? '↑ ' : (cardData.trendDirection === 'down' ? '↓ ' : '→ ');
            animateNumber(summaryValues[1], trendValue, 1000, {
              unit: '',
              isDecimal: false,
              prefix: trendDir
            });
            
            // 3. 受影响面积
            const area = cardData.affectedArea;
            animateNumber(summaryValues[2], area, 1000, {
              unit: ' km²',
              isDecimal: true,
              decimalPlaces: 2
            });
            
            // 4. 更新卡片标题（根据模式）
            updateCardTitle(mode);
        }
    } catch (e) {
        console.error("获取汇总数据失败", e);
    }
  }

  
  // 延迟触发站点数值动画，确保DOM已更新
  // setTimeout(() => {
  //   triggerStationAnimations();
  // }, 300);
  // 已移除硬编码的 triggerStationAnimations 调用，避免覆盖真实数据
}

// 触发站点数值动画 (已废弃，逻辑移至 stationList.js)
function triggerStationAnimations() {
  // const stationValues = document.querySelectorAll('.station-value');
  // if (stationValues.length > 0) {
  //   ...
  // }
}

// 根据模式更新卡片标题
function updateCardTitle(mode) {
  const cardTitle = document.querySelector('.summary-section h3');
  if (cardTitle) {
    if (mode === 'flood') {
      cardTitle.textContent = '当前雨情';
    } else if (mode === 'drought') {
      cardTitle.textContent = '当前旱情';
    }
  }
}

// 触发侧边栏数值动画
function triggerSidePanelAnimations(currentMode = 'flood') {
  // 汇总信息数值动画
  const summaryValues = document.querySelectorAll('.summary-value');
  if (summaryValues.length > 0) {
    if (currentMode === 'flood') {
      // 防汛模式汇总数值
      // 这里的硬编码也应该移除，因为 initNumberAnimations 已经从 API 获取了
      // animateNumber(summaryValues[0], 12, 1000, { unit: '' }); 
      // ...
    }
    // ...
  }
  
  // 站点数值动画 - 移除硬编码
  // const stationValues = document.querySelectorAll('.station-value');
  // if (stationValues.length > 0) { ... }
  
  // 资源统计数值动画（右侧面板）
  const statValues = document.querySelectorAll('.stat-value');
  if (statValues.length > 0) {
    // 无论什么模式，资源统计数值都保持不变
    // 但在模式切换时重新动画化，增强视觉效果
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        animateNumber(statValues[0], 25, 1000, { unit: '' });
        animateNumber(statValues[1], 8, 1000, { unit: '' });
        animateNumber(statValues[2], 15, 1000, { unit: '' });
        animateNumber(statValues[3], 30, 1000, { unit: '' });
      });
    });
  }
}

// 自动刷新机制：每过一个整点自动请求新数据
function startAutoRefresh() {
  // 1. 获取当前模式
  const getCurrentMode = () => {
    return document.body.getAttribute('data-mode') || 'flood';
  };
  
  // 2. 立即执行一次
  initNumberAnimations(getCurrentMode());
  
  // 3. 计算距离下一个整点的毫秒数
  const now = new Date();
  const nextHour = new Date(now);
  nextHour.setHours(now.getHours() + 1);
  nextHour.setMinutes(0, 0, 0);
  const delay = nextHour - now;
  
  // 4. 设置定时器，在下一个整点执行，并循环
  setTimeout(function refreshData() {
    // 执行刷新
    initNumberAnimations(getCurrentMode());
    
    // 重新设置定时器，每小时执行一次
    setTimeout(refreshData, 3600000);
  }, delay);
}


// 导出函数
export { 
  animateNumber, 
  initNumberAnimations, 
  triggerStationAnimations, 
  triggerSidePanelAnimations,
  startAutoRefresh
};
