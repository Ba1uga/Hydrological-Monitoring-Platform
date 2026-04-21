export const QUERY_TIME_EVENT = 'floodcontrol:query-time-changed';

let selectedQueryTime = null;
let controlsInitialized = false;

function pad(value) {
  return String(value).padStart(2, '0');
}

function currentHourDate() {
  const now = new Date();
  now.setMinutes(0, 0, 0);
  return now;
}

export function formatQueryTime(dateLike) {
  const date = dateLike instanceof Date ? new Date(dateLike.getTime()) : new Date(dateLike);
  date.setMinutes(0, 0, 0);
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:00:00`;
}

function parseQueryTime(value) {
  if (!value) return null;
  const parsed = new Date(String(value).replace(' ', 'T'));
  if (Number.isNaN(parsed.getTime())) return null;
  parsed.setMinutes(0, 0, 0);
  return parsed;
}

function syncSelectValue(select, value) {
  if (select) {
    select.value = String(value);
  }
}

function updateStatus(message = '') {
  const status = document.getElementById('queryTimeStatus');
  if (status) {
    status.textContent = message;
  }
}

function setPanelVisibility(visible) {
  const trigger = document.getElementById('timeSelectorTrigger');
  const panel = document.getElementById('queryTimePanel');
  if (!trigger || !panel) return;
  trigger.setAttribute('aria-expanded', visible ? 'true' : 'false');
  panel.hidden = !visible;
}

function updateDayOptions() {
  const yearSelect = document.getElementById('queryYearSelect');
  const monthSelect = document.getElementById('queryMonthSelect');
  const daySelect = document.getElementById('queryDaySelect');
  if (!yearSelect || !monthSelect || !daySelect) return;

  const year = Number(yearSelect.value);
  const month = Number(monthSelect.value);
  const selectedDay = Number(daySelect.value || 1);
  const daysInMonth = new Date(year, month, 0).getDate();

  daySelect.innerHTML = '';
  for (let day = 1; day <= daysInMonth; day += 1) {
    const option = document.createElement('option');
    option.value = String(day);
    option.textContent = pad(day);
    daySelect.appendChild(option);
  }
  daySelect.value = String(Math.min(selectedDay, daysInMonth));
}

function populateControls(baseDate) {
  const yearSelect = document.getElementById('queryYearSelect');
  const monthSelect = document.getElementById('queryMonthSelect');
  const daySelect = document.getElementById('queryDaySelect');
  const hourSelect = document.getElementById('queryHourSelect');
  if (!yearSelect || !monthSelect || !daySelect || !hourSelect) return;

  if (yearSelect.options.length === 0) {
    for (let year = baseDate.getFullYear() - 5; year <= baseDate.getFullYear() + 1; year += 1) {
      const option = document.createElement('option');
      option.value = String(year);
      option.textContent = String(year);
      yearSelect.appendChild(option);
    }
  }

  if (monthSelect.options.length === 0) {
    for (let month = 1; month <= 12; month += 1) {
      const option = document.createElement('option');
      option.value = String(month);
      option.textContent = pad(month);
      monthSelect.appendChild(option);
    }
  }

  if (hourSelect.options.length === 0) {
    for (let hour = 0; hour <= 23; hour += 1) {
      const option = document.createElement('option');
      option.value = String(hour);
      option.textContent = pad(hour);
      hourSelect.appendChild(option);
    }
  }

  syncSelectValue(yearSelect, baseDate.getFullYear());
  syncSelectValue(monthSelect, baseDate.getMonth() + 1);
  updateDayOptions();
  syncSelectValue(daySelect, baseDate.getDate());
  syncSelectValue(hourSelect, baseDate.getHours());
}

function buildDateFromControls() {
  const fallback = currentHourDate();
  const year = Number(document.getElementById('queryYearSelect')?.value || fallback.getFullYear());
  const month = Number(document.getElementById('queryMonthSelect')?.value || fallback.getMonth() + 1);
  const day = Number(document.getElementById('queryDaySelect')?.value || fallback.getDate());
  const hour = Number(document.getElementById('queryHourSelect')?.value || fallback.getHours());
  return new Date(year, month - 1, day, hour, 0, 0, 0);
}

export function isLatestQueryTime() {
  return !selectedQueryTime;
}

export function getSelectedQueryTime() {
  return selectedQueryTime;
}

export function buildQueryTimeParams(base = {}) {
  return selectedQueryTime ? { ...base, queryTime: selectedQueryTime } : { ...base };
}

export function renderQueryTimeHeader(displayValue) {
  const trigger = document.getElementById('timeSelectorTrigger');
  if (!trigger) return;
  trigger.textContent = `${isLatestQueryTime() ? '最新数据' : '历史数据'} ${displayValue}`;
}

export function setSelectedQueryTime(value) {
  selectedQueryTime = value || null;
  renderQueryTimeHeader(selectedQueryTime || formatQueryTime(currentHourDate()));
  updateStatus(selectedQueryTime ? `当前查看 ${selectedQueryTime}` : '当前为最新整点数据');
  window.dispatchEvent(new CustomEvent(QUERY_TIME_EVENT, { detail: { queryTime: selectedQueryTime } }));
}

export function clearSelectedQueryTime() {
  setSelectedQueryTime(null);
}

export function initQueryTimeControls() {
  if (controlsInitialized) return;
  controlsInitialized = true;

  const trigger = document.getElementById('timeSelectorTrigger');
  const panel = document.getElementById('queryTimePanel');
  const yearSelect = document.getElementById('queryYearSelect');
  const monthSelect = document.getElementById('queryMonthSelect');
  const applyBtn = document.getElementById('applyQueryTimeBtn');
  if (!trigger || !panel || !yearSelect || !monthSelect || !applyBtn) return;

  populateControls(currentHourDate());
  renderQueryTimeHeader(formatQueryTime(currentHourDate()));
  updateStatus('当前为最新整点数据');

  trigger.addEventListener('click', () => {
    const opening = panel.hidden;
    if (opening) {
      populateControls(parseQueryTime(selectedQueryTime) || currentHourDate());
    }
    setPanelVisibility(opening);
  });

  yearSelect.addEventListener('change', updateDayOptions);
  monthSelect.addEventListener('change', updateDayOptions);

  panel.querySelectorAll('[data-quick-action]').forEach((button) => {
    button.addEventListener('click', () => {
      const action = button.getAttribute('data-quick-action');
      const baseDate = parseQueryTime(selectedQueryTime) || currentHourDate();
      if (action === 'latest') {
        clearSelectedQueryTime();
        populateControls(currentHourDate());
        setPanelVisibility(false);
        return;
      }

      const next = new Date(baseDate);
      if (action === 'prev-hour') {
        next.setHours(next.getHours() - 1);
      }
      if (action === 'yesterday-same-hour') {
        next.setDate(next.getDate() - 1);
      }
      populateControls(next);
      updateStatus(`待应用 ${formatQueryTime(next)}`);
    });
  });

  applyBtn.addEventListener('click', () => {
    setSelectedQueryTime(formatQueryTime(buildDateFromControls()));
    setPanelVisibility(false);
  });

  document.addEventListener('click', (event) => {
    if (panel.hidden) return;
    if (panel.contains(event.target) || trigger.contains(event.target)) return;
    setPanelVisibility(false);
  });
}
