/* =================== 程序入口 =================== */
/* 加载顺序、初始化所有模块 */

// 引入所有模块
import { initModeSwitch, updateTime } from './ui/modeSwitch.js';
import { initParticles } from './effects/particles.js';
import { initMap, initRiskLegend, getCurrentMode } from './map/initMap.js';
import { initWarningScroll } from './ui/warnings.js';
import { initNumberAnimations, startAutoRefresh } from './effects/numberAnimation.js';
import { initRadarChart } from './charts/initRadarChart.js';
import { initDispatchPager } from './ui/dispatchPager.js';
import { initResourceInteractions } from './ui/resources.js';
import { initStationList } from './ui/stationList.js';

async function bootstrap() {
  updateTime();
  setInterval(updateTime, 1000);

  await initModeSwitch();
  await initParticles();

  await initMap();
  await initRiskLegend();

  await initWarningScroll();
  await initDispatchPager();
  await initResourceInteractions(getCurrentMode);
  await initStationList();

  await initRadarChart();
  startAutoRefresh();

  const loader = document.getElementById('loading-screen');
  if (loader) {
    const transitionDuration = getComputedStyle(loader).transitionDuration;
    if (!transitionDuration || transitionDuration === '0s') {
      loader.remove();
      return;
    }
    loader.addEventListener('transitionend', () => loader.remove(), { once: true });
    loader.classList.add('fade-out');
  }
}

window.addEventListener('load', () => {
  bootstrap().catch((e) => {
    console.error('初始化失败', e);
  });
});
