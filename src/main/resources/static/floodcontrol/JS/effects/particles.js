/* =================== 粒子特效 =================== */
/* 雨滴 / 热浪粒子特效（canvas实现） */

// 粒子系统核心类 - 独立运行，平滑过渡
class ParticleSystem {
  constructor() {
    this.currentMode = 'flood';
    this.canvas = null;
    this.ctx = null;
    this.particles = [];
    this.particleCount = 150;
    this.animationId = null;
    this.isInitialized = false;
    
    // 初始化粒子系统
    this.init();
    
    // 绑定事件监听
    this.bindEvents();
  }
  
  // 初始化粒子系统
  init() {
    // 获取或创建粒子容器
    const particleContainer = document.getElementById('particles');
    if (!particleContainer) {
      console.error('Particle container not found!');
      return;
    }
    
    // 获取或创建Canvas元素
    let canvas = particleContainer.querySelector('canvas');
    if (!canvas) {
      canvas = document.createElement('canvas');
      canvas.style.position = 'fixed'; // 使用fixed定位，独立于其他元素
      canvas.style.top = '0';
      canvas.style.left = '0';
      canvas.style.width = '100%';
      canvas.style.height = '100%';
      canvas.style.pointerEvents = 'none';
      canvas.style.zIndex = '10'; // 提高层级，确保在地图之上
      canvas.style.willChange = 'transform'; // 提示浏览器优化性能
      canvas.style.transform = 'translateZ(0)'; // 启用硬件加速
      particleContainer.appendChild(canvas);
    }
    
    this.canvas = canvas;
    this.ctx = canvas.getContext('2d');
    
    // 设置初始Canvas尺寸
    this.resizeCanvas();
    
    // 创建粒子
    this.createParticles();
    
    // 开始动画循环
    this.startAnimation();
    
    this.isInitialized = true;
  }
  
  // 创建粒子
  createParticles() {
    this.particles = [];
    for (let i = 0; i < this.particleCount; i++) {
      this.particles.push(this.createParticle());
    }
  }
  
  // 创建单个粒子
  createParticle() {
    return {
      x: Math.random() * this.canvas.width,
      y: this.currentMode === 'flood' ? -20 : Math.random() * this.canvas.height,
      vx: (Math.random() - 0.5) * 2,
      vy: this.currentMode === 'flood' ? Math.random() * 3 + 2 : Math.random() * -3 - 2,
      size: Math.random() * 3 + 1,
      opacity: Math.random() * 0.5 + 0.5,
      type: this.currentMode === 'flood' ? 'rain' : 'heat'
    };
  }
  
  // 调整Canvas尺寸
  resizeCanvas() {
    // 使用设备像素比处理高清屏幕
    const dpr = window.devicePixelRatio || 1;
    const rect = this.canvas.getBoundingClientRect();
    
    this.canvas.width = rect.width * dpr;
    this.canvas.height = rect.height * dpr;
    this.canvas.style.width = rect.width + 'px';
    this.canvas.style.height = rect.height + 'px';
    
    // 缩放上下文以匹配设备像素比
    this.ctx.scale(dpr, dpr);
  }
  
  // 更新粒子位置与生命周期
  updateParticles() {
    this.particles.forEach(particle => {
      particle.x += particle.vx;
      particle.y += particle.vy;
      particle.opacity -= 0.01;
      
      // 粒子重置条件
      let shouldReset = false;
      if (particle.type === 'rain') {
        if (particle.y > this.canvas.height / (window.devicePixelRatio || 1) + 20 || particle.opacity <= 0) {
          shouldReset = true;
        }
      } else {
        if (particle.y < -20 || particle.opacity <= 0) {
          shouldReset = true;
        }
      }
      
      // 重置粒子
      if (shouldReset) {
        const newParticle = this.createParticle();
        particle.x = newParticle.x;
        particle.y = newParticle.y;
        particle.vx = newParticle.vx;
        particle.vy = newParticle.vy;
        particle.opacity = newParticle.opacity;
        particle.type = this.currentMode === 'flood' ? 'rain' : 'heat';
      }
    });
  }
  
  // 渲染粒子
  renderParticles() {
    // 清空画布
    const dpr = window.devicePixelRatio || 1;
    this.ctx.clearRect(0, 0, this.canvas.width / dpr, this.canvas.height / dpr);
    
    this.particles.forEach(particle => {
      this.ctx.save();
      this.ctx.globalAlpha = particle.opacity;
      
      if (particle.type === 'rain') {
        // 防汛模式：蓝色雨滴
        this.ctx.fillStyle = '#00ffff';
        this.ctx.beginPath();
        this.ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
        this.ctx.fill();
      } else {
        // 抗旱模式：橙色热浪 + 波纹
        this.ctx.fillStyle = '#ffaa00';
        this.ctx.beginPath();
        this.ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
        this.ctx.fill();

        // 绘制波纹
        this.ctx.strokeStyle = '#ff6600';
        this.ctx.lineWidth = 0.5;
        this.ctx.globalAlpha = particle.opacity * 0.3;
        this.ctx.beginPath();
        this.ctx.arc(particle.x, particle.y, particle.size * 2, 0, Math.PI * 2);
        this.ctx.stroke();
      }
      
      this.ctx.restore();
    });
  }
  
  // 动画循环
  animate() {
    this.updateParticles();
    this.renderParticles();
    this.animationId = requestAnimationFrame(() => this.animate());
  }
  
  // 开始动画
  startAnimation() {
    if (!this.animationId) {
      this.animate();
    }
  }
  
  // 停止动画
  stopAnimation() {
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
  }
  
  // 平滑切换模式
  switchMode(mode) {
    if (this.currentMode === mode) return;
    
    this.currentMode = mode;
    
    // 更新所有粒子类型，不重新创建
    this.particles.forEach(particle => {
      particle.type = mode === 'flood' ? 'rain' : 'heat';
      
      // 平滑调整粒子速度方向，而不是突然改变
      const targetVy = mode === 'flood' ? Math.random() * 3 + 2 : Math.random() * -3 - 2;
      particle.vy = particle.vy * 0.9 + targetVy * 0.1; // 平滑过渡速度
    });
  }
  
  // 绑定事件
  bindEvents() {
    // 窗口大小变化 - 使用防抖处理
    let resizeTimeout;
    window.addEventListener('resize', () => {
      clearTimeout(resizeTimeout);
      resizeTimeout = setTimeout(() => {
        this.resizeCanvas();
      }, 100);
    });
    
    // 页面可见性变化时暂停/恢复动画
    document.addEventListener('visibilitychange', () => {
      if (document.hidden) {
        this.stopAnimation();
      } else {
        this.startAnimation();
      }
    });
  }
}

// 全局粒子系统实例
let particleSystem = null;

// 初始化粒子系统
async function initParticles() {
  if (!particleSystem) {
    particleSystem = new ParticleSystem();
  }
  particleSystem.startAnimation();
}

// 切换为防汛模式粒子效果（雨滴）
function updateParticlesForFlood() {
  if (particleSystem) {
    particleSystem.switchMode('flood');
  }
}

// 切换为抗旱模式粒子效果（热浪）
function updateParticlesForDrought() {
  if (particleSystem) {
    particleSystem.switchMode('drought');
  }
}

// 导出函数
export { 
  initParticles, 
  updateParticlesForFlood, 
  updateParticlesForDrought 
};
