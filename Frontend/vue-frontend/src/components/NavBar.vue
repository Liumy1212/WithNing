<template>
  <nav class="navbar">
    <div class="navbar-container">
      <div class="navbar-brand">
        <router-link to="/home">WithNing</router-link>
      </div>

      <div class="navbar-scroll-wrapper">
        <div class="navbar-links">
          <router-link to="/home" class="nav-item">
            <span class="icon">🏠</span>
            <span class="text">首页</span>
          </router-link>

          <router-link to="/tasks" class="nav-item">
            <span class="icon">📋</span>
            <span class="text">任务</span>
          </router-link>

          <router-link to="/points" class="nav-item">
            <span class="icon">⭐</span>
            <span class="text">积分</span>
          </router-link>

          <router-link to="/mall" class="nav-item">
            <span class="icon">🛒</span>
            <span class="text">商城</span>
          </router-link>

          <router-link v-if="isAdmin" to="/admin/task-review" class="nav-item">
            <span class="icon">⚙️</span>
            <span class="text">管理</span>
          </router-link>
        </div>
      </div>

      <div class="navbar-actions">
        <button class="btn-logout" @click="showLogoutConfirm = true" title="退出登录">
          <span>🚪</span>
        </button>
        <button class="navbar-toggle" @click="toggleMobileMenu" :class="{ active: isMobileMenuOpen }">
          <span></span>
          <span></span>
          <span></span>
        </button>
      </div>
    </div>

    <div class="mobile-menu" :class="{ active: isMobileMenuOpen }">
      <div class="mobile-links">
        <router-link to="/home" class="mobile-nav-item" @click="closeMobileMenu">
          <span class="icon">🏠</span>
          <span class="text">首页</span>
        </router-link>
        <router-link to="/tasks" class="mobile-nav-item" @click="closeMobileMenu">
          <span class="icon">📋</span>
          <span class="text">任务</span>
        </router-link>
        <router-link to="/points" class="mobile-nav-item" @click="closeMobileMenu">
          <span class="icon">⭐</span>
          <span class="text">积分</span>
        </router-link>
        <router-link to="/mall" class="mobile-nav-item" @click="closeMobileMenu">
          <span class="icon">🛒</span>
          <span class="text">商城</span>
        </router-link>
        <router-link v-if="isAdmin" to="/admin/task-review" class="mobile-nav-item" @click="closeMobileMenu">
          <span class="icon">⚙️</span>
          <span class="text">管理</span>
        </router-link>
      </div>
      <div class="mobile-user">
        <button class="btn-mobile-logout" @click="showLogoutConfirm = true">退出登录</button>
      </div>
    </div>

    <div class="logout-modal" v-if="showLogoutConfirm" @click.self="showLogoutConfirm = false">
      <div class="modal-content">
        <h3>确认退出</h3>
        <p>确定要退出登录吗？</p>
        <div class="modal-actions">
          <button class="btn btn-cancel" @click="showLogoutConfirm = false">取消</button>
          <button class="btn btn-confirm" @click="handleLogout">确认退出</button>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import store from '../store';
import { authAPI } from '../api';

export default {
  name: 'NavBar',
  data() {
    return {
      isMobileMenuOpen: false,
      showLogoutConfirm: false
    };
  },
  computed: {
    userInfo() {
      return store.getUserInfo();
    },
    isAdmin() {
      return store.isAdmin();
    }
  },
  methods: {
    toggleMobileMenu() {
      this.isMobileMenuOpen = !this.isMobileMenuOpen;
    },
    closeMobileMenu() {
      this.isMobileMenuOpen = false;
    },
    async handleLogout() {
      try {
        await authAPI.logout();
      } catch (error) {
        console.log('Logout request failed:', error);
      }
      store.clearUserInfo();
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 20px;
}

.navbar-brand {
  flex-shrink: 0;
}

.navbar-brand a {
  font-size: 24px;
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.navbar-scroll-wrapper {
  flex: 1;
  overflow-x: auto;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
}

.navbar-scroll-wrapper::-webkit-scrollbar {
  display: none;
}

.navbar-links {
  display: inline-flex;
  gap: 4px;
  padding: 8px 0;
  white-space: nowrap;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s;
  font-size: 14px;
  cursor: pointer;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.nav-item.router-link-active {
  background: rgba(255, 255, 255, 0.3);
  color: white;
}

.navbar-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.btn-logout {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s;
}

.btn-logout:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.navbar-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 5px;
}

.navbar-toggle span {
  width: 25px;
  height: 3px;
  background: white;
  border-radius: 2px;
  transition: all 0.3s;
}

.navbar-toggle.active span:nth-child(1) {
  transform: rotate(45deg) translate(5px, 5px);
}

.navbar-toggle.active span:nth-child(2) {
  opacity: 0;
}

.navbar-toggle.active span:nth-child(3) {
  transform: rotate(-45deg) translate(5px, -5px);
}

.mobile-menu {
  position: fixed;
  top: 60px;
  left: 0;
  right: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s;
  z-index: 999;
}

.mobile-menu.active {
  max-height: calc(100vh - 60px);
  overflow-y: auto;
}

.mobile-links {
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s;
  font-size: 16px;
}

.mobile-nav-item:hover {
  background: rgba(255, 255, 255, 0.2);
}

.mobile-nav-item.router-link-active {
  background: rgba(255, 255, 255, 0.3);
}

.mobile-user {
  padding: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.btn-mobile-logout {
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s;
}

.btn-mobile-logout:hover {
  background: rgba(255, 255, 255, 0.3);
}

.logout-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  width: 90%;
  max-width: 320px;
  text-align: center;
}

.modal-content h3 {
  margin-bottom: 12px;
  color: #333;
}

.modal-content p {
  margin-bottom: 24px;
  color: #666;
}

.modal-actions {
  display: flex;
  gap: 12px;
}

.modal-actions .btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-cancel:hover {
  background: #e0e0e0;
}

.btn-confirm {
  background: #e74c3c;
  color: white;
}

.btn-confirm:hover {
  background: #c0392b;
}

@media (max-width: 768px) {
  .navbar {
    height: 56px;
  }

  .navbar-scroll-wrapper {
    display: none;
  }

  .navbar-toggle {
    display: flex;
  }

  .mobile-menu {
    top: 56px;
  }
}

@media (max-width: 480px) {
  .navbar-brand a {
    font-size: 20px;
  }
}
</style>
