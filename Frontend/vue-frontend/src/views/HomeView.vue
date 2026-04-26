<script setup>
import { computed } from 'vue';
import { useUserStore } from '../stores/userStore';

const userStore = useUserStore();

const isLoggedIn = computed(() => userStore.isLoggedIn);
const userName = computed(() => userStore.user?.nickname || '游客');
const userPoints = computed(() => userStore.user?.points || 0);
const isAdmin = computed(() => userStore.isAdmin);
</script>

<template>
  <div class="home-view">
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">欢迎使用打卡监督系统</h1>
        <p class="hero-subtitle">养成好习惯，从每天打卡开始</p>
        <div class="hero-actions" v-if="!isLoggedIn">
          <router-link to="/register" class="btn btn-primary">立即注册</router-link>
          <router-link to="/login" class="btn btn-secondary">登录</router-link>
        </div>
      </div>
    </section>

    <section class="stats-section" v-if="isLoggedIn">
      <div class="stats-container">
        <div class="stat-card">
          <div class="stat-icon">💰</div>
          <div class="stat-info">
            <span class="stat-value">{{ userPoints }}</span>
            <span class="stat-label">我的积分</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">👤</div>
          <div class="stat-info">
            <span class="stat-value">{{ userName }}</span>
            <span class="stat-label">{{ isAdmin ? '管理员' : '普通用户' }}</span>
          </div>
        </div>
      </div>
    </section>

    <section class="features-section">
      <h2 class="section-title">功能模块</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">📋</div>
          <h3>任务中心</h3>
          <p>发布和完成每日任务与阶段任务，获取积分奖励</p>
          <router-link to="/tasks" class="feature-btn">查看任务</router-link>
        </div>
        <div class="feature-card">
          <div class="feature-icon">💰</div>
          <h3>积分中心</h3>
          <p>查看积分明细和历史记录，了解积分变动情况</p>
          <router-link to="/points" class="feature-btn">查看积分</router-link>
        </div>
        <div class="feature-card">
          <div class="feature-icon">🛒</div>
          <h3>积分商城</h3>
          <p>使用积分兑换各种商品和奖励</p>
          <router-link to="/mall" class="feature-btn">进入商城</router-link>
        </div>
        <div class="feature-card admin-card" v-if="isAdmin">
          <div class="feature-icon">⚙️</div>
          <h3>管理后台</h3>
          <p>管理任务审核和商品上下架</p>
          <router-link to="/admin" class="feature-btn admin-btn">进入管理</router-link>
        </div>
      </div>
    </section>

    <section class="tips-section">
      <h2 class="section-title">使用提示</h2>
      <div class="tips-grid">
        <div class="tip-card">
          <div class="tip-number">01</div>
          <div class="tip-content">
            <h4>每日打卡</h4>
            <p>坚持每日完成任务，获取稳定积分收入</p>
          </div>
        </div>
        <div class="tip-card">
          <div class="tip-number">02</div>
          <div class="tip-content">
            <h4>阶段挑战</h4>
            <p>参与阶段任务，获得更高积分奖励</p>
          </div>
        </div>
        <div class="tip-card">
          <div class="tip-number">03</div>
          <div class="tip-content">
            <h4>积分兑换</h4>
            <p>积累积分，在商城兑换心仪商品</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

/* Hero Section */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 60px 40px;
  margin-bottom: 30px;
  color: white;
  text-align: center;
}

.hero-title {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 15px;
}

.hero-subtitle {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 30px;
}

.hero-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  display: inline-block;
  padding: 12px 28px;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  font-size: 16px;
  transition: all 0.3s ease;
}

.btn-primary {
  background: white;
  color: #667eea;
}

.btn-primary:hover {
  background: #f5f5f5;
  transform: translateY(-2px);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* Stats Section */
.stats-section {
  margin-bottom: 30px;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-icon {
  font-size: 40px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #888;
}

/* Features Section */
.features-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.feature-card h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 10px;
}

.feature-card p {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 20px;
}

.feature-btn {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 10px 24px;
  border-radius: 6px;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.feature-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.admin-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.admin-card h3,
.admin-card p {
  color: white;
}

.admin-btn {
  background: white;
  color: #667eea;
}

.admin-btn:hover {
  background: #f5f5f5;
}

/* Tips Section */
.tips-section {
  margin-bottom: 20px;
}

.tips-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.tip-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  display: flex;
  gap: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.tip-number {
  font-size: 32px;
  font-weight: bold;
  color: #667eea;
  opacity: 0.3;
  line-height: 1;
}

.tip-content h4 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.tip-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
  line-height: 1.4;
}

/* Responsive Design */
@media (max-width: 768px) {
  .home-view {
    padding: 0 15px 30px;
  }
  
  .hero-section {
    padding: 40px 20px;
    border-radius: 12px;
  }
  
  .hero-title {
    font-size: 28px;
  }
  
  .hero-subtitle {
    font-size: 16px;
  }
  
  .hero-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 100%;
    max-width: 200px;
    text-align: center;
  }
  
  .stats-container {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    padding: 20px;
  }
  
  .stat-icon {
    width: 50px;
    height: 50px;
    font-size: 32px;
  }
  
  .stat-value {
    font-size: 20px;
  }
  
  .section-title {
    font-size: 20px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .feature-card {
    padding: 25px 20px;
  }
  
  .tips-grid {
    grid-template-columns: 1fr;
  }
  
  .tip-card {
    padding: 20px;
  }
  
  .tip-number {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .hero-section {
    padding: 30px 15px;
  }
  
  .hero-title {
    font-size: 24px;
  }
  
  .hero-subtitle {
    font-size: 14px;
    margin-bottom: 20px;
  }
  
  .stat-card {
    flex-direction: column;
    text-align: center;
  }
  
  .stat-info {
    align-items: center;
  }
}
</style>