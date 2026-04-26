import { defineStore } from 'pinia';
import { authService } from '../services/authService';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    isLoggedIn: false,
    loading: false,
    error: null
  }),
  
  getters: {
    isAdmin: (state) => state.user?.role === 'admin',
    isNormal: (state) => state.user?.role === 'normal',
    userPoints: (state) => state.user?.points || 0
  },
  
  actions: {
    async login(credentials) {
      this.loading = true;
      try {
        const response = await authService.login(credentials);
        if (response.data && response.data.token) {
          this.token = response.data.token;
          this.user = response.data.user;
          this.isLoggedIn = true;
          localStorage.setItem('token', response.data.token);
          this.error = null;
        }
        return response.data;
      } catch (err) {
        this.error = '登录失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    async fetchCurrentUser() {
      this.loading = true;
      try {
        const response = await authService.getCurrentUser();
        this.user = response.data;
        this.isLoggedIn = true;
        this.error = null;
        return response.data;
      } catch (err) {
        this.error = '获取用户信息失败';
        this.user = null;
        this.isLoggedIn = false;
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    async register(userData) {
      this.loading = true;
      try {
        const response = await authService.register(userData);
        this.error = null;
        return response.data;
      } catch (err) {
        this.error = '注册失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    logout() {
      this.user = null;
      this.token = null;
      this.isLoggedIn = false;
      localStorage.removeItem('token');
    }
  }
});