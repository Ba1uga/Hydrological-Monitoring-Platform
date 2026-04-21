/* =================== 程序入口 =================== */
/* 加载顺序、初始化所有模块 */

import './utils/request.js';
import { QUERY_TIME_EVENT, isLatestQueryTime } from './state/queryTimeContext.js';

// 引入所有模块
import { initModeSwitch, updateTime } from './ui/modeSwitch.js';
import { initParticles } from './effects/particles.js';
import { initMap, initRiskLegend, getCurrentMode, applyCurrentHourStations, refreshCurrentHourStationsByHttp } from './map/initMap.js';
import { initWarningScroll } from './ui/warnings.js';
import { initNumberAnimations, startAutoRefresh } from './effects/numberAnimation.js';
import { initRadarChart } from './charts/initRadarChart.js';
import { initDispatchPager } from './ui/dispatchPager.js';
import { initResourceInteractions } from './ui/resources.js';
import { initStationList, applyStations } from './ui/stationList.js';

let stompClient = null;
let stationsSubscription = null;
let websocketReady = false;
let httpFallbackActive = false;
let httpFallbackTimerId = null;
let websocketRefreshTimerId = null;

function shouldUseRealtimeTransport() {
  return isLatestQueryTime();
}

function stopRealtimeSubscriptions() {
  websocketReady = false;
  httpFallbackActive = false;
  if (stationsSubscription) {
    stationsSubscription.unsubscribe();
    stationsSubscription = null;
  }
  if (httpFallbackTimerId != null) {
    clearTimeout(httpFallbackTimerId);
    httpFallbackTimerId = null;
  }
  if (websocketRefreshTimerId != null) {
    clearTimeout(websocketRefreshTimerId);
    websocketRefreshTimerId = null;
  }
}

function connectWebSocket() {
  if (!window.SockJS || !window.Stomp) return Promise.reject(new Error('SockJS/Stomp 未加载'));
  const socket = new window.SockJS('/ws');
  const client = window.Stomp.over(socket);
  client.debug = null;
  return new Promise((resolve, reject) => {
    client.connect({}, () => resolve(client), (err) => reject(err));
  });
}

function subscribeStations(mode) {
  if (!stompClient) return;
  if (stationsSubscription) {
    stationsSubscription.unsubscribe();
    stationsSubscription = null;
  }
  const topic = `/topic/currentHourStations/${mode}`;
  stationsSubscription = stompClient.subscribe(topic, (message) => {
    try {
      const stations = JSON.parse(message.body);
      applyCurrentHourStations(stations);
      applyStations(stations);
    } catch (e) {
      console.error('解析站点推送失败', e);
    }
  });
}

function requestStations(mode) {
  if (!stompClient || !shouldUseRealtimeTransport()) return;
  stompClient.send('/app/currentHourStations', {}, mode);
}

function scheduleNextHourRequest() {
  if (!websocketReady || !shouldUseRealtimeTransport()) return;
  const now = new Date();
  const nextHour = new Date(now);
  nextHour.setHours(now.getHours() + 1, 0, 0, 0);
  const delay = Math.max(0, nextHour - now);
  websocketRefreshTimerId = setTimeout(() => {
    websocketRefreshTimerId = null;
    if (!websocketReady || !shouldUseRealtimeTransport()) {
      // skip websocket refresh for historical query time
      return;
    }
    requestStations(getCurrentMode());
    scheduleNextHourRequest();
  }, delay);
}

function scheduleNextHourHttpRefresh() {
  if (!httpFallbackActive || !shouldUseRealtimeTransport()) return;
  if (httpFallbackTimerId != null) return;
  const now = new Date();
  const nextHour = new Date(now);
  nextHour.setHours(now.getHours() + 1, 0, 0, 0);
  const delay = Math.max(0, nextHour - now);
  httpFallbackTimerId = setTimeout(async () => {
    httpFallbackTimerId = null;
    if (!httpFallbackActive || !shouldUseRealtimeTransport()) return;
    const stations = await refreshCurrentHourStationsByHttp(getCurrentMode());
    applyStations(stations);
    scheduleNextHourHttpRefresh();
  }, delay);
}

async function initRealtime() {
  if (!shouldUseRealtimeTransport()) {
    stopRealtimeSubscriptions();
    return;
  }
  try {
    stompClient = await connectWebSocket();
    websocketReady = true;
    httpFallbackActive = false;
    if (httpFallbackTimerId != null) {
      clearTimeout(httpFallbackTimerId);
      httpFallbackTimerId = null;
    }
    subscribeStations(getCurrentMode());
    requestStations(getCurrentMode());
    scheduleNextHourRequest();
  } catch (e) {
    websocketReady = false;
    httpFallbackActive = true;
    console.warn('WebSocket 连接失败，回退到 HTTP 刷新', e);
    const stations = await refreshCurrentHourStationsByHttp(getCurrentMode());
    applyStations(stations);
    scheduleNextHourHttpRefresh();
  }
}

async function bootstrap() {
  updateTime();
  setInterval(updateTime, 1000);

  await initModeSwitch();
  await initParticles();

  await initMap();
  await initRiskLegend();

  await initRealtime();

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

window.addEventListener('modeChanged', async (event) => {
  const mode = event && event.detail ? event.detail.mode : getCurrentMode();
  if (websocketReady && stompClient && shouldUseRealtimeTransport()) {
    httpFallbackActive = false;
    if (httpFallbackTimerId != null) {
      clearTimeout(httpFallbackTimerId);
      httpFallbackTimerId = null;
    }
    subscribeStations(mode);
    requestStations(mode);
    return;
  }
  httpFallbackActive = true;
  const stations = await refreshCurrentHourStationsByHttp(mode);
  applyStations(stations);
  scheduleNextHourHttpRefresh();
});

window.addEventListener(QUERY_TIME_EVENT, async () => {
  const mode = getCurrentMode();
  if (!shouldUseRealtimeTransport()) {
    stopRealtimeSubscriptions();
    const stations = await refreshCurrentHourStationsByHttp(mode);
    applyStations(stations);
    await initNumberAnimations(mode);
    return;
  }
  await initRealtime();
  await initNumberAnimations(mode);
});
