import { getCurrentMode, setHoverStation } from '../map/initMap.js';
import { animateNumber } from '../effects/numberAnimation.js';
import { initStationHistoryChart } from '../charts/stationHistoryChart.js';

let allStations = [];

// 初始化站点列表模块
export async function initStationList() {
  const searchInput = document.getElementById('stationSearchInput');
  const listContainer = document.getElementById('stationListContainer');

  if (!searchInput || !listContainer) return;

  // 1. 获取站点数据
  await fetchAndRenderStations(listContainer);

  // 2. 绑定搜索事件
  searchInput.addEventListener('input', (e) => {
    const keyword = e.target.value.trim().toLowerCase();
    filterStations(keyword, listContainer);
  });
}

// 获取并渲染站点
async function fetchAndRenderStations(container) {
  try {
    // 使用新的整点数据API
    const res = await axios.get('/currentOverview/currentHourStations', {
      params: {
        mode: getCurrentMode()
      }
    });
    if (res.data.code === 200) {
      allStations = res.data.data;
      renderStationList(allStations, container);
    }
  } catch (e) {
    console.error("加载站点列表失败", e);
    container.innerHTML = '<div style="text-align:center; color:#666; padding:10px;">暂无数据</div>';
  }
}

// 监听刷新事件
window.addEventListener('refreshStationList', async () => {
  const listContainer = document.getElementById('stationListContainer');
  if (listContainer) {
    await fetchAndRenderStations(listContainer);
  }
});

export async function refreshStationList() {
  const listContainer = document.getElementById('stationListContainer');
  // 无论allStations是否有数据，模式切换时都重新获取对应模式的数据
  // 因为当前allStations可能是另一种模式的数据
  await fetchAndRenderStations(listContainer);
}

// 渲染站点列表
function renderStationList(stations, container) {
  container.innerHTML = '';
  const currentMode = getCurrentMode();

  if (!stations || stations.length === 0) {
    container.innerHTML = '<div style="text-align:center; color:#666; padding:10px;">未找到站点</div>';
    return;
  }

  stations.forEach(station => {
    // 简化过滤逻辑，只根据当前模式和valueUnit过滤
    // 确保抗旱模式下显示所有valueUnit为%的站点
    if (currentMode === 'flood') {
      // 防汛模式：只显示valueUnit为m的站点，过滤掉土壤湿度记录
      if (station.valueUnit !== 'm') {
        return;
      }
      if (station.stationName.includes('-土壤湿度')) {
        return;
      }
    } else {
      // 抗旱模式：只显示valueUnit为%的站点，过滤掉水位记录
      if (station.valueUnit !== '%') {
        return;
      }
      if (station.stationName.includes('-水位')) {
        return;
      }
    }

    // 构建列表项
    const item = document.createElement('div');
    item.className = 'station-item button';
    
    // 准备数据
    const name = station.stationName;
    const value = station.currentValue;
    // 根据数据本身的单位决定显示单位，而不是仅仅依赖模式
    // 因为后端返回的数据已经包含了 valueUnit
    const unit = station.valueUnit || (currentMode === 'flood' ? 'm' : '%');
    
    // 趋势 (假设后端返回 trendDirection: 'up' | 'down' | 'flat')
    // 如果后端没返回，暂时随机模拟一下或者默认为平稳，为了演示效果
    let trendArrow = '→';
    if (station.trendDirection === 'up') trendArrow = '↑';
    else if (station.trendDirection === 'down') trendArrow = '↓';
    
    // 设置趋势颜色
    let trendColor = 'var(--highlight-color)'; // 默认青色
    if (currentMode === 'drought') {
        // 抗旱模式下
        if (trendArrow === '↓') {
            trendColor = '#ff3c00'; // 下降（干旱加剧）用红色
        } else if (trendArrow === '↑') {
            trendColor = '#00e676'; // 上升（缓解）用绿色
        }
    } else {
        // 防汛模式下
        if (trendArrow === '↑') {
            trendColor = '#ff3c00'; // 上升（洪水风险）用红色
        } else if (trendArrow === '↓') {
            trendColor = '#00e676'; // 下降（缓解）用绿色
        }
    }

    // 构造HTML结构
    item.innerHTML = `
      <div class="button-wrapper">
        <div class="text">
          <span class="station-name">${name}</span>
          <div class="station-info-right">
            <span class="station-value" 
              data-target="${value}" 
              data-unit="${unit}" 
              data-decimal="true">
              ${value}${unit}
            </span>
            <span class="trend" style="color: ${trendColor}; font-weight: bold; margin-left: 5px; font-family: 'Share Tech Mono', monospace;">${trendArrow}</span>
          </div>
        </div>
        <div class="station-detail">查看详细</div>
      </div>
    `;

    // 绑定悬停事件 (高亮地图上的站点)
    item.addEventListener('mouseenter', () => {
      setHoverStation(name);
    });

    item.addEventListener('mouseleave', () => {
      setHoverStation(null);
    });
    
    // 绑定点击事件 (切换到站点历史数据图表)
    item.addEventListener('click', () => {
      // 初始化站点历史数据图表
      initStationHistoryChart(name, currentMode);
    });
    
    container.appendChild(item);
    
    // 触发数字动画
    const valueEl = item.querySelector('.station-value');
    
    animateNumber(valueEl, value, 1000, {
      unit: `${unit}`,
      isDecimal: true,
      decimalPlaces: 2
    });
  });
}

// 过滤站点
function filterStations(keyword, container) {
  if (!keyword) {
    renderStationList(allStations, container);
    return;
  }
  
  const filtered = allStations.filter(s => 
    s.stationName.toLowerCase().includes(keyword)
  );
  renderStationList(filtered, container);
}
