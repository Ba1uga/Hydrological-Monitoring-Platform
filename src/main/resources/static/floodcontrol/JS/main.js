/* =================== 程序入口 =================== */
/* 加载顺序、初始化所有模块 */

// 引入所有模块
import { initModeSwitch, updateTime } from './ui/modeSwitch.js';
import { initParticles } from './effects/particles.js';
import { initMap, initRiskLegend, initStationListHover, getCurrentMode } from './map/initMap.js';
import { initWarningScroll } from './ui/warnings.js';
import { initNumberAnimations, startAutoRefresh } from './effects/numberAnimation.js';
import { initRadarChart } from './charts/initRadarChart.js';
import { initDispatchPager } from './ui/dispatchPager.js';
import { initResourceInteractions } from './ui/resources.js';
import { initStationList } from './ui/stationList.js';

// 页面加载完成入口
window.onload = function () {
  // 1. 顶部时间动态刷新
  updateTime();
  setInterval(updateTime, 1000);

  // 2. 注册模式切换按钮事件（防汛 / 抗旱）
  initModeSwitch();

  // 3. 初始化背景粒子特效（雨滴 / 热浪）
  initParticles();

  // 4. 初始化 ECharts 地图与监测点
  initMap();

  // 5. 初始化左侧预警信息滚动
  initWarningScroll();

  // 6. 初始化左上角风险等级图例交互
  initRiskLegend();
  
  // 7. 初始化站点列表的鼠标悬停事件
  initStationListHover();
  
  // 8. 初始化调度预案翻页功能
  initDispatchPager();

  // 9. 初始化资源面板交互 (传入 getCurrentMode 函数以避免循环依赖)
  initResourceInteractions(getCurrentMode);

  // 10. 初始化站点列表 (搜索 + 动态加载)
  initStationList();
  
  // 11. 初始化雷达图
  setTimeout(initRadarChart, 600);
  
  // 10. 启动自动刷新机制，确保 DOM 元素已加载
  setTimeout(startAutoRefresh, 500);
  
  // 10. 移除加载开场动画
  setTimeout(() => {
    const loader = document.getElementById('loading-screen');
    loader.style.opacity = 0;
    setTimeout(() => loader.remove(), 1000);
  }, 650); // 假装加载 0.65秒
};
