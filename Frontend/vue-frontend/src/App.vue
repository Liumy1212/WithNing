<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { RouterLink, RouterView, useRouter } from 'vue-router';
import { useUserStore } from './stores/userStore';

const userStore = useUserStore();
const router = useRouter();
const isMobileMenuOpen = ref(false);

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
  if (isMobileMenuOpen.value) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = '';
  }
};

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false;
  document.body.style.overflow = '';
};

const handleLogout = () => {
  userStore.logout();
  closeMobileMenu();
  router.push('/');
};

const handleLogin = () => {
  closeMobileMenu();
  router.push('/login');
};

const handleRegister = () => {
  closeMobileMenu();
  router.push('/register');
};

const isAdmin = computed(() => userStore.isAdmin);

const handleResize = () => {
  if (window.innerWidth > 768 && isMobileMenuOpen.value) {
    closeMobileMenu();
  }
};

onMounted(() => {
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  document.body.style.overflow = '';
});
</script>

<template>
  <div class="app">
    <nav class="navbar">
      <div class="navbar-container">
        <h1 class="navbar-brand">
          <RouterLink to="/">打卡监督系统</RouterLink>
        </h1>
        
        <!-- 桌面端导航 -->
        <div class="desktop-nav">
          <ul class="navbar-menu">
            <li>
              <RouterLink to="/" class="nav-link">
                <span class="nav-icon">🏠</span>
                <span class="nav-text">首页</span>
              </RouterLink>
            </li>
            <li>
              <RouterLink to="/tasks" class="nav-link">
                <span class="nav-icon">📋</span>
                <span class="nav-text">任务中心</span>
              </RouterLink>
            </li>
            <li>
              <RouterLink to="/points" class="nav-link">
                <span class="nav-icon">💰</span>
                <span class="nav-text">积分中心</span>
              </RouterLink>
            </li>
            <li>
              <RouterLink to="/mall" class="nav-link">
                <span class="nav-icon">🛒</span>
                <span class="nav-text">积分商城</span>
              </RouterLink>
            </li>
            <li v-if="isAdmin">
              <RouterLink to="/admin" class="nav-link admin-link">
                <span class="nav-icon">⚙️</span>
                <span class="nav-text">管理后台</span>
              </RouterLink>
            </li>
          </ul>
          
          <div class="auth-section">
            <div v-if="userStore.isLoggedIn" class="user-info">
              <div class="user-avatar">{{ userStore.user?.nickname?.charAt(0) }}</div>
              <div class="user-details">
                <span class="user-name">{{ userStore.user?.nickname }}</span>
                <span class="user-role" :class="{ admin: isAdmin }">{{ isAdmin ? '管理员' : '普通用户' }}</span>
              </div>
              <button class="logout-btn" @click="handleLogout">
                <span class="btn-icon">🚪</span>
                <span class="btn-text">退出</span>
              </button>
            </div>
            <div v-else class="auth-buttons">
              <button class="login-btn" @click="handleLogin">
                <span class="btn-icon">🔑</span>
                <span class="btn-text">登录</span>
              </button>
              <button class="register-btn" @click="handleRegister">
                <span class="btn-icon">📝</span>
                <span class="btn-text">注册</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 移动端菜单按钮 -->
        <button 
          class="mobile-menu-btn" 
          @click="toggleMobileMenu" 
          :class="{ active: isMobileMenuOpen }"
          aria-label="菜单"
        >
          <span></span>
          <span></span>
          <span></span>
        </button>
        
        <!-- 移动端侧边栏 -->
        <div class="mobile-sidebar" :class="{ open: isMobileMenuOpen }">
          <div class="mobile-header">
            <span class="mobile-title">菜单</span>
            <button class="close-btn" @click="closeMobileMenu" aria-label="关闭">×</button>
          </div>
          
          <ul class="mobile-menu">
            <li @click="closeMobileMenu">
              <RouterLink to="/" class="nav-link">
                <span class="nav-icon">🏠</span>
                <span class="nav-text">首页</span>
              </RouterLink>
            </li>
            <li @click="closeMobileMenu">
              <RouterLink to="/tasks" class="nav-link">
                <span class="nav-icon">📋</span>
                <span class="nav-text">任务中心</span>
              </RouterLink>
            </li>
            <li @click="closeMobileMenu">
              <RouterLink to="/points" class="nav-link">
                <span class="nav-icon">💰</span>
                <span class="nav-text">积分中心</span>
              </RouterLink>
            </li>
            <li @click="closeMobileMenu">
              <RouterLink to="/mall" class="nav-link">
                <span class="nav-icon">🛒</span>
                <span class="nav-text">积分商城</span>
              </RouterLink>
            </li>
            <li v-if="isAdmin" @click="closeMobileMenu">
              <RouterLink to="/admin" class="nav-link admin-link">
                <span class="nav-icon">⚙️</span>
                <span class="nav-text">管理后台</span>
              </RouterLink>
            </li>
          </ul>
          
          <div class="mobile-footer" v-if="userStore.isLoggedIn">
            <div class="user-info">
              <div class="user-avatar">{{ userStore.user?.nickname?.charAt(0) }}</div>
              <div class="user-details">
                <span class="user-name">{{ userStore.user?.nickname }}</span>
                <span class="user-role" :class="{ admin: isAdmin }">{{ isAdmin ? '管理员' : '普通用户' }}</span>
              </div>
            </div>
            <button class="logout-btn" @click="handleLogout">
              <span class="btn-icon">🚪</span>
              <span class="btn-text">退出登录</span>
            </button>
          </div>
          
          <div class="mobile-footer" v-else>
            <div class="auth-buttons">
              <button class="login-btn" @click="handleLogin">
                <span class="btn-icon">🔑</span>
                <span class="btn-text">登录</span>
              </button>
              <button class="register-btn" @click="handleRegister">
                <span class="btn-icon">📝</span>
                <span class="btn-text">注册</span>
              </button>
            </div>
          </div>
        </div>
        
        <div class="mobile-overlay" v-if="isMobileMenuOpen" @click="closeMobileMenu"></div>
      </div>
    </nav>
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.navbar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 12px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.navbar-brand {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  z-index: 1001;
}

.navbar-brand a {
  color: white;
  text-decoration: none;
}

/* 桌面端导航 */
.desktop-nav {
  display: flex;
  align-items: center;
  gap: 30px;
  flex: 1;
  justify-content: flex-end;
}

.navbar-menu {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  gap: 12px;
  align-items: center;
}

.navbar-menu li a {
  color: white;
  text-decoration: none;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.3s ease;
  padding: 8px 12px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.navbar-menu li a:hover,
.navbar-menu li a.router-link-active {
  background: rgba(255, 255, 255, 0.2);
}

.navbar-menu li a.admin-link {
  background: rgba(255, 215, 0, 0.2);
  color: #ffd700;
}

.navbar-menu li a.admin-link:hover {
  background: rgba(255, 215, 0, 0.3);
}

.nav-icon {
  font-size: 16px;
}

.auth-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
}

.user-details {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

.user-role {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
}

.user-role.admin {
  color: #ffd700;
  font-weight: bold;
}

.auth-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.logout-btn,
.login-btn,
.register-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.logout-btn:hover,
.login-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.register-btn {
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
}

.register-btn:hover {
  background: white;
}

.btn-icon {
  font-size: 14px;
}

/* 移动端菜单按钮 */
.mobile-menu-btn {
  display: none;
  flex-direction: column;
  justify-content: space-between;
  width: 28px;
  height: 20px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  z-index: 1001;
}

.mobile-menu-btn span {
  display: block;
  width: 100%;
  height: 2px;
  background: white;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.mobile-menu-btn.active span:nth-child(1) {
  transform: rotate(45deg) translate(5px, 5px);
}

.mobile-menu-btn.active span:nth-child(2) {
  opacity: 0;
}

.mobile-menu-btn.active span:nth-child(3) {
  transform: rotate(-45deg) translate(5px, -5px);
}

/* 移动端侧边栏 */
.mobile-sidebar {
  position: fixed;
  top: 0;
  right: -280px;
  width: 280px;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  flex-direction: column;
  align-items: stretch;
  padding: 0;
  gap: 0;
  transition: right 0.3s ease;
  z-index: 1002;
  box-shadow: -4px 0 20px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
}

.mobile-sidebar.open {
  right: 0;
}

.mobile-overlay {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1001;
}

.mobile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.mobile-title {
  font-size: 18px;
  font-weight: bold;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 28px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.mobile-menu {
  list-style: none;
  margin: 0;
  padding: 15px 0;
  gap: 5px;
  flex: 1;
}

.mobile-menu li {
  width: 100%;
}

.mobile-menu li a {
  width: 100%;
  padding: 14px 20px;
  font-size: 16px;
  border-radius: 0;
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
}

.mobile-menu li a:hover {
  background: rgba(255, 255, 255, 0.2);
}

.mobile-menu li a.admin-link {
  background: rgba(255, 215, 0, 0.15);
  color: #ffd700;
}

.mobile-footer {
  margin-top: auto;
  padding: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.mobile-footer .user-info {
  margin-bottom: 15px;
  justify-content: flex-start;
}

.mobile-footer .user-avatar {
  width: 50px;
  height: 50px;
  font-size: 20px;
}

.mobile-footer .user-details {
  align-items: flex-start;
}

.mobile-footer .user-name {
  font-size: 16px;
}

.mobile-footer .auth-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mobile-footer .logout-btn,
.mobile-footer .login-btn,
.mobile-footer .register-btn {
  width: 100%;
  padding: 14px 16px;
  font-size: 16px;
  justify-content: center;
  border-radius: 8px;
}

.mobile-footer .register-btn {
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
}

.mobile-footer .register-btn:hover {
  background: white;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .navbar-menu {
    gap: 8px;
  }
  
  .navbar-menu li a {
    font-size: 14px;
    padding: 6px 10px;
  }
  
  .user-details {
    display: none;
  }
  
  .logout-btn .btn-text,
  .login-btn .btn-text,
  .register-btn .btn-text {
    display: none;
  }
  
  .logout-btn,
  .login-btn,
  .register-btn {
    padding: 8px;
  }
}

@media (max-width: 768px) {
  .desktop-nav {
    display: none;
  }
  
  .mobile-menu-btn {
    display: flex;
  }
  
  .mobile-overlay {
    display: block;
  }
  
  .navbar-brand {
    font-size: 18px;
  }
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f5f5;
}

@media (max-width: 768px) {
  .main-content {
    padding: 10px;
  }
}
</style>