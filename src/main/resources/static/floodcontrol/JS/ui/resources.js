
import { animateNumber } from '../effects/numberAnimation.js';
import { switchToPieChart, switchToRadarView } from '../charts/initRadarChart.js';
import { getCurrentMode } from '../map/initMap.js';

/**
 * 资源面板交互逻辑
 */

// 资源数据定义
const RESOURCE_DATA = {
  flood: [
    { label: '救生转移', value: 850, unit: '件', trend: '↑ 15%', trendClass: 'up', progress: 85, progressColor: 'var(--highlight-color)', tooltip: '冲锋舟15艘、救生衣800件、救援机器人5台' },
    { label: '排水照明', value: 120, unit: '台', trend: '↑ 8%', trendClass: 'up', progress: 65, progressColor: '#00e676', tooltip: '大流量泵50台、发电机40台、照明车30辆' },
    { label: '生活安置', value: 3500, unit: '件', trend: '↑ 20%', trendClass: 'up', progress: 90, progressColor: 'var(--highlight-color)', tooltip: '帐篷500顶、折叠床1000张、食品2000箱' },
    { label: '医疗防疫', value: 1500, unit: '箱', trend: '→ 0%', trendClass: '', progress: 70, progressColor: '#ff9800', tooltip: '84消毒液、急救包、妇幼特殊包' }
  ],
  drought: [
    { label: '找水运水', value: 150, unit: '套', trend: '↑ 25%', trendClass: 'up', progress: 90, progressColor: 'var(--highlight-color)', tooltip: '打井机、潜水泵、运水车、净水设备' },
    { label: '节水保墒', value: 3000, unit: '套', trend: '↑ 15%', trendClass: 'up', progress: 75, progressColor: '#00e676', tooltip: '滴灌带、微喷头、保水剂、遮阴网' },
    { label: '生活应急', value: 5000, unit: '箱', trend: '↑ 30%', trendClass: 'up', progress: 85, progressColor: '#ff9800', tooltip: '瓶装水、储水桶、消毒湿巾、应急药品' },
    { label: '生产自救', value: 800, unit: '吨', trend: '↑ 10%', trendClass: 'up', progress: 60, progressColor: 'var(--highlight-color)', tooltip: '耐旱种子、饲草料、燃油、农机配件' }
  ]
};

export function initResourceInteractions(getCurrentModeFn) {
  const mapBtn = document.querySelector('.map-view-btn');
  if (mapBtn) {
    mapBtn.addEventListener('click', () => {
      // 模拟切换地图图层
      const mapContainer = document.getElementById('map');
      if (mapContainer) {
        // 这里可以添加实际的地图图层切换逻辑
        // 比如 echarts.setOption(...)
        console.log('Switching to Resource Distribution Map Layer');
        alert('正在加载资源分布热力图...');
      }
    });
  }

  // 为卡片添加点击事件（下钻详情）
  const cards = document.querySelectorAll('.resource-stats .stat-card');
  const detailView = document.getElementById('resourceDetailView');
  const detailContent = document.getElementById('detailContent');
  const detailTitle = document.querySelector('.detail-title');
  const backBtn = document.querySelector('.back-btn');

  // 返回按钮事件
  if (backBtn) {
    backBtn.addEventListener('click', (e) => {
      console.log('Resource Detail Back Button Clicked');
      e.stopPropagation(); // 防止冒泡
      closeDetailView();
      switchToRadarView(); // 恢复雷达图
    });
  }

  // 关闭详情视图函数
  function closeDetailView() {
    if (detailView) {
      detailView.classList.remove('active');
      detailView.setAttribute('aria-hidden', 'true');
      // 恢复卡片可访问性
      cards.forEach(c => c.setAttribute('tabindex', '0'));
    }
  }

  cards.forEach(card => {
    // 鼠标点击
    card.addEventListener('click', async () => {
        const label = card.querySelector('.stat-label').textContent;
        // 使用传入的函数获取当前模式，如果未传入则默认为 flood
        const currentMode = (getCurrentModeFn && typeof getCurrentModeFn === 'function') ? getCurrentModeFn() : 'flood';
        
        try {
            // 尝试从后端获取详细数据
            const res = await axios.get(`http://localhost:8080/resource/${currentMode}/details`, {
                params: { category: label }
            });

            if (res.data.code === 200 && res.data.data) {
                const detailData = res.data.data;
                // 直接传递对象数组，而不是字符串数组
                const items = detailData.items.map(item => ({
                    name: item.name,
                    value: item.value + item.unit,
                    spec: item.spec || ''
                }));
                
                // 显示详情视图
                showDetailView(label, items);
                
                // 触发底部饼图更新
                const pieData = detailData.items.map(item => ({
                    name: item.name,
                    value: item.value
                }));
                switchToPieChart(label, pieData);
            }
        } catch (error) {
            console.error('获取详情失败:', error);
            // 用户明确要求不使用降级数据，因此直接提示错误
            // alert('获取详情数据失败，请确保后端服务已启动。');
        }
    });
    
    // 键盘交互支持
    card.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            card.click();
        }
    });
  });

  // 显示详情视图函数
  function showDetailView(title, items) {
    if (!detailView || !detailContent) return;
    
    // 设置标题
    if (detailTitle) detailTitle.textContent = title + '详情';
    
    // 生成内容 HTML
    const html = items.map(item => {
      let name = '';
      let value = '';
      let spec = '';
      let location = '';

      if (typeof item === 'object' && item !== null) {
          // 如果已经是对象格式 { name, value, spec, location }
          name = item.name || '';
          value = item.value || '';
          spec = item.spec || '';
          location = item.location || '';
      } else {
          // 兼容旧的字符串格式 (兜底逻辑)
          const itemStr = String(item);
          // 尝试解析包含规格的格式
          const specMatch = itemStr.match(/^([^(（\[]+)[(（\[]([^)）\]]+)[)）\]](.*)$/);
          if (specMatch) {
              name = specMatch[1];
              spec = specMatch[2];
              value = specMatch[3];
          } else {
              // 普通格式解析
              const match = itemStr.match(/^([\u4e00-\u9fa5a-zA-Z]+)(\d+[a-zA-Z\u4e00-\u9fa5]+)$/);
              if (match) {
                name = match[1];
                value = match[2];
              } else {
                 const parts = itemStr.split(/[\s,，]+/);
                 if (parts.length > 1) {
                     name = parts[0];
                     value = parts[parts.length - 1];
                     if (parts.length > 2) spec = parts[1];
                 } else {
                     name = itemStr;
                 }
              }
          }
      }
      
      return `
        <div class="detail-item">
          <div class="info-group">
              <span class="name">${name}</span>
              ${spec ? `<span class="spec">${spec}</span>` : ''}
              ${location ? `<span class="location"><i class="fas fa-map-marker-alt"></i> ${location}</span>` : ''}
          </div>
          <span class="value">${value}</span>
        </div>
      `;
    }).join('');
    
    detailContent.innerHTML = html;
    
    // 显示视图
    detailView.classList.add('active');
    detailView.setAttribute('aria-hidden', 'false');
    
    // 临时禁用卡片焦点，防止Tab导航混乱
    cards.forEach(c => c.setAttribute('tabindex', '-1'));
    
    // 聚焦到返回按钮
    if (backBtn) backBtn.focus();
  }

  // 初始化时默认加载防汛资源数据
  updateResourcesForMode('flood');
}

/**
 * 根据模式更新资源面板数据
 * @param {string} mode - 'flood' 或 'drought'
 */
export async function updateResourcesForMode(mode) {
  try {
    const res = await axios.get(`http://localhost:8080/resource/${mode}`);
    if (res.data.code === 200) {
      const data = res.data.data;
      const cards = document.querySelectorAll('.resource-stats .stat-card');
      
      if (cards.length !== data.length) return;

      cards.forEach((card, index) => {
        const item = data[index];
        
        // 更新标签
        const labelEl = card.querySelector('.stat-label');
        if (labelEl) labelEl.textContent = item.label;
        
        // 更新数值 (使用 animateNumber 产生动画效果)
        const valueEl = card.querySelector('.stat-value');
        if (valueEl) {
          // 更新 data-target 属性，以便后续动画脚本使用
          valueEl.dataset.target = item.value;
          animateNumber(valueEl, item.value, 800, { unit: item.unit });
        }
        
        // 更新趋势
        const trendEl = card.querySelector('.stat-trend');
        if (trendEl) {
          if (item.trend) {
            trendEl.textContent = item.trend;
            trendEl.style.display = 'block';
            trendEl.className = 'stat-trend'; // 重置类
            if (item.trendClass) trendEl.classList.add(item.trendClass);
          } else {
            trendEl.style.display = 'none';
          }
        }
        
        // 更新进度条
        const progressBar = card.querySelector('.stat-progress-bar');
        if (progressBar) {
          progressBar.style.width = `${item.progress}%`;
          progressBar.style.setProperty('--bar-color', item.progressColor);
        }
        
        // 更新 Tooltip
        const tooltip = card.querySelector('.stat-tooltip');
        if (tooltip) {
          tooltip.textContent = item.tooltip;
        }
        
        // 更新 ARIA 标签
        const trendText = item.trend ? item.trend.replace('↑', '上升').replace('↓', '下降').replace('→', '持平') : '';
        card.setAttribute('aria-label', `${item.label}: ${item.value}${item.unit}, ${trendText}`);
      });
    }
  } catch (error) {
    console.error('获取资源数据失败:', error);
    // 降级处理：使用本地数据
    fallbackToLocalData(mode);
  }
}

// 降级处理：使用本地数据
function fallbackToLocalData(mode) {
  const data = RESOURCE_DATA[mode] || RESOURCE_DATA['flood'];
  const cards = document.querySelectorAll('.resource-stats .stat-card');
  
  if (cards.length !== data.length) return;

  cards.forEach((card, index) => {
    const item = data[index];
    
    // 更新标签
    const labelEl = card.querySelector('.stat-label');
    if (labelEl) labelEl.textContent = item.label;
    
    // 更新数值 (使用 animateNumber 产生动画效果)
    const valueEl = card.querySelector('.stat-value');
    if (valueEl) {
      // 更新 data-target 属性，以便后续动画脚本使用
      valueEl.dataset.target = item.value;
      animateNumber(valueEl, item.value, 800, { unit: item.unit });
    }
    
    // 更新趋势
    const trendEl = card.querySelector('.stat-trend');
    if (trendEl) {
      trendEl.textContent = item.trend;
      trendEl.className = 'stat-trend'; // 重置类
      if (item.trendClass) trendEl.classList.add(item.trendClass);
    }
    
    // 更新进度条
    const progressBar = card.querySelector('.stat-progress-bar');
    if (progressBar) {
      progressBar.style.width = `${item.progress}%`;
      progressBar.style.setProperty('--bar-color', item.progressColor);
    }
    
    // 更新 Tooltip
    const tooltip = card.querySelector('.stat-tooltip');
    if (tooltip) {
      tooltip.textContent = item.tooltip;
    }
    
    // 更新 ARIA 标签
    card.setAttribute('aria-label', `${item.label}: ${item.value}${item.unit}, ${item.trend.replace('↑', '上升').replace('↓', '下降').replace('→', '持平')}`);
  });
}

