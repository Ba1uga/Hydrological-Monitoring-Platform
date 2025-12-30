/* =================== 调度预案翻页功能 =================== */
/* 实现调度预案的翻页功能，包括按钮文字切换和页面显示/隐藏 */

// 初始化调度预案翻页功能
async function initDispatchPager() {
  const pagerButton = document.getElementById('dispatchPager');
  const pages = document.querySelectorAll('.dispatch-page');
  let pagerText = pagerButton.querySelector('.pager-text');
  
  if (!pagerButton || pages.length === 0 || !pagerText) return;
  
  // 添加一个额外的span来显示"调度预案"
  const nextPageText = document.createElement('span');
  nextPageText.className = 'pager-text next-page';
  nextPageText.textContent = '调度预案';
  pagerButton.appendChild(nextPageText);
  
  let currentPage = 1;
  const totalPages = pages.length;
  let hoverTimeout = null;
  
  // 翻页功能
  pagerButton.addEventListener('click', () => {
    // 隐藏当前页面
    const currentPageEl = document.querySelector(`.dispatch-page[data-page="${currentPage}"]`);
    if (currentPageEl) {
      currentPageEl.classList.remove('active');
    }
    
    // 切换到下一页
    currentPage = currentPage === totalPages ? 1 : currentPage + 1;
    
    // 显示新页面
    const newPageEl = document.querySelector(`.dispatch-page[data-page="${currentPage}"]`);
    if (newPageEl) {
      newPageEl.classList.add('active');
    }
    
    // 更新按钮文字
    const mainText = pagerButton.querySelector('.pager-text:not(.next-page)');
    mainText.textContent = currentPage === 1 ? '启动响应' : '调度预案';
    nextPageText.textContent = currentPage === 1 ? '调度预案' : '启动响应';
  });
  
  // 鼠标悬停效果：显示下一页的文字提示
  pagerButton.addEventListener('mouseenter', () => {
    // 延迟500ms（与CSS动画时间匹配）后切换文字
    hoverTimeout = setTimeout(() => {
      const mainText = pagerButton.querySelector('.pager-text:not(.next-page)');
      mainText.style.opacity = '0';
      nextPageText.style.opacity = '1';
      // 移除过渡延迟，让文字立即出现
      nextPageText.style.transitionDelay = '0s';
    }, 500);
  });
  
  pagerButton.addEventListener('mouseleave', () => {
    // 清除延迟
    clearTimeout(hoverTimeout);
    
    // 恢复原文字
    const mainText = pagerButton.querySelector('.pager-text:not(.next-page)');
    mainText.style.opacity = '1';
    mainText.style.transitionDelay = '0s';
    nextPageText.style.opacity = '0';
    nextPageText.style.transitionDelay = '0s';
  });
}

// 导出函数
export { initDispatchPager };
