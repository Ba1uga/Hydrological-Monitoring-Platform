const TOAST_CONTAINER_ID = 'global-toast-container';

function ensureToastContainer() {
  let container = document.getElementById(TOAST_CONTAINER_ID);
  if (container) return container;
  container = document.createElement('div');
  container.id = TOAST_CONTAINER_ID;
  container.style.position = 'fixed';
  container.style.right = '16px';
  container.style.bottom = '16px';
  container.style.zIndex = '99999';
  container.style.display = 'flex';
  container.style.flexDirection = 'column';
  container.style.gap = '10px';
  document.body.appendChild(container);
  return container;
}

function showToast(message) {
  if (!message || typeof document === 'undefined') return;
  const container = ensureToastContainer();
  const toast = document.createElement('div');
  toast.textContent = String(message);
  toast.style.padding = '10px 12px';
  toast.style.borderRadius = '10px';
  toast.style.background = 'rgba(0,0,0,0.75)';
  toast.style.color = '#fff';
  toast.style.fontSize = '12px';
  toast.style.lineHeight = '1.35';
  toast.style.maxWidth = '360px';
  toast.style.boxShadow = '0 8px 24px rgba(0,0,0,0.35)';
  toast.style.border = '1px solid rgba(255,255,255,0.12)';
  toast.style.backdropFilter = 'blur(6px)';
  toast.style.transform = 'translateY(10px)';
  toast.style.opacity = '0';
  toast.style.transition = 'opacity 180ms ease, transform 180ms ease';
  container.appendChild(toast);

  requestAnimationFrame(() => {
    toast.style.opacity = '1';
    toast.style.transform = 'translateY(0)';
  });

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(6px)';
    toast.addEventListener('transitionend', () => toast.remove(), { once: true });
  }, 2800);
}

function attachAxiosInterceptors() {
  if (typeof window === 'undefined' || !window.axios) return;
  if (window.__axiosInterceptorsAttached) return;
  window.__axiosInterceptorsAttached = true;

  window.axios.defaults.timeout = 8000;

  window.axios.interceptors.response.use(
    (response) => {
      const data = response && response.data ? response.data : null;
      if (data && typeof data === 'object' && 'code' in data && data.code !== 200) {
        const msg = data.message || '请求失败';
        showToast(msg);
      }
      return response;
    },
    (error) => {
      const msg = error && error.message ? error.message : '网络连接失败';
      showToast(msg.includes('timeout') ? '网络连接超时，正在重试...' : `网络连接失败：${msg}`);
      return Promise.reject(error);
    }
  );
}

if (typeof window !== 'undefined') {
  if (document.readyState === 'loading') {
    window.addEventListener('DOMContentLoaded', attachAxiosInterceptors, { once: true });
  } else {
    attachAxiosInterceptors();
  }
}

