export const QUERY_TIME_EVENT = 'floodcontrol:query-time-changed';

let selectedQueryTime = null;
let controlsInitialized = false;
let availableQueryTimeRange = null;
let availableRangePromise = null;

function pad(value) {
  return String(value).padStart(2, '0');
}

function currentHourDate() {
  const now = new Date();
  now.setMinutes(0, 0, 0);
  return now;
}

function parseDateLike(value) {
  if (!value) return null;
  const parsed = value instanceof Date ? new Date(value.getTime()) : new Date(String(value).replace(' ', 'T'));
  if (Number.isNaN(parsed.getTime())) return null;
  parsed.setMinutes(0, 0, 0);
  return parsed;
}

export function formatQueryTime(dateLike) {
  const date = parseDateLike(dateLike);
  if (!date) return '';
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:00:00`;
}

function parseQueryTime(value) {
  return parseDateLike(value);
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

function clampToAvailableRange(date) {
  if (!availableQueryTimeRange) {
    return { date, adjusted: false };
  }

  const minDate = parseDateLike(availableQueryTimeRange.minTime);
  const maxDate = parseDateLike(availableQueryTimeRange.maxTime);
  if (!minDate || !maxDate) {
    return { date, adjusted: false };
  }

  if (date < minDate) {
    return { date: minDate, adjusted: true };
  }
  if (date > maxDate) {
    return { date: maxDate, adjusted: true };
  }

  return { date, adjusted: false };
}

async function loadAvailableQueryTimeRange() {
  if (availableQueryTimeRange) {
    return availableQueryTimeRange;
  }
  if (availableRangePromise) {
    return availableRangePromise;
  }
  if (typeof window === 'undefined' || !window.axios) {
    return null;
  }

  availableRangePromise = window.axios.get('/currentOverview/queryTimeRange')
    .then((res) => {
      if (res?.data?.code === 200) {
        availableQueryTimeRange = res.data.data;
      }
      return availableQueryTimeRange;
    })
    .catch((error) => {
      console.warn('获取可用时间范围失败', error);
      return null;
    })
    .finally(() => {
      availableRangePromise = null;
    });

  return availableRangePromise;
}

function populateControls(baseDate) {
  const yearSelect = document.getElementById('queryYearSelect');
  const monthSelect = document.getElementById('queryMonthSelect');
  const daySelect = document.getElementById('queryDaySelect');
  const hourSelect = document.getElementById('queryHourSelect');
  if (!yearSelect || !monthSelect || !daySelect || !hourSelect) return;

  const clamped = clampToAvailableRange(baseDate).date;

  if (yearSelect.options.length === 0) {
    const minDate = parseDateLike(availableQueryTimeRange?.minTime) || clamped;
    const maxDate = parseDateLike(availableQueryTimeRange?.maxTime) || clamped;
    for (let year = minDate.getFullYear(); year <= maxDate.getFullYear(); year += 1) {
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

  syncSelectValue(yearSelect, clamped.getFullYear());
  syncSelectValue(monthSelect, clamped.getMonth() + 1);
  updateDayOptions();
  syncSelectValue(daySelect, clamped.getDate());
  syncSelectValue(hourSelect, clamped.getHours());
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
  if (selectedQueryTime) {
    updateStatus(`当前查看 ${selectedQueryTime}`);
  } else if (availableQueryTimeRange?.maxTime) {
    updateStatus(`当前为最新整点数据，可用到 ${availableQueryTimeRange.maxTime}`);
  } else {
    updateStatus('当前为最新整点数据');
  }
  window.dispatchEvent(new CustomEvent(QUERY_TIME_EVENT, { detail: { queryTime: selectedQueryTime } }));
}

export function clearSelectedQueryTime() {
  setSelectedQueryTime(null);
}

export async function initQueryTimeControls() {
  if (controlsInitialized) return;
  controlsInitialized = true;

  const trigger = document.getElementById('timeSelectorTrigger');
  const panel = document.getElementById('queryTimePanel');
  const yearSelect = document.getElementById('queryYearSelect');
  const monthSelect = document.getElementById('queryMonthSelect');
  const applyBtn = document.getElementById('applyQueryTimeBtn');
  if (!trigger || !panel || !yearSelect || !monthSelect || !applyBtn) return;

  await loadAvailableQueryTimeRange();

  const initial = clampToAvailableRange(currentHourDate()).date;
  populateControls(initial);
  renderQueryTimeHeader(formatQueryTime(initial));
  if (availableQueryTimeRange?.maxTime) {
    updateStatus(`当前为最新整点数据，可用到 ${availableQueryTimeRange.maxTime}`);
  } else {
    updateStatus('当前为最新整点数据');
  }

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
        populateControls(clampToAvailableRange(currentHourDate()).date);
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
      const result = clampToAvailableRange(next);
      populateControls(result.date);
      updateStatus(result.adjusted
        ? `已切换到最近可用时点 ${formatQueryTime(result.date)}`
        : `待应用 ${formatQueryTime(result.date)}`);
    });
  });

  applyBtn.addEventListener('click', () => {
    const result = clampToAvailableRange(buildDateFromControls());
    if (result.adjusted) {
      updateStatus(`已切换到最近可用时点 ${formatQueryTime(result.date)}`);
    }
    setSelectedQueryTime(formatQueryTime(result.date));
    setPanelVisibility(false);
  });

  document.addEventListener('click', (event) => {
    if (panel.hidden) return;
    if (panel.contains(event.target) || trigger.contains(event.target)) return;
    setPanelVisibility(false);
  });
}
