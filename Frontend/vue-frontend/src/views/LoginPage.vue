<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <h1 class="auth-title">WithNing</h1>
        <p class="auth-subtitle">打卡监督系统</p>

        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

        <form @submit.prevent="handleLogin" class="auth-form">
          <div class="form-group">
            <label>用户名</label>
            <input
              v-model="formData.username"
              type="text"
              placeholder="请输入用户名"
              required
            />
          </div>
          <div class="form-group">
            <label>密码</label>
            <input
              v-model="formData.password"
              type="password"
              placeholder="请输入密码"
              required
            />
          </div>
          <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <div class="auth-footer">
          <span>还没有账号？</span>
          <router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import store from '../store';

export default {
  name: 'LoginPage',
  data() {
    return {
      formData: {
        username: '',
        password: ''
      },
      loading: false,
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';

      try {
        const response = await axios.post('/api/auth/login', this.formData, {
          withCredentials: true
        });
        
        const res = response.data;
        
        if (res.code === 1 || res.code === '1') {
          const userData = res.data || res;
          store.saveUserInfo(userData);
          this.successMessage = '登录成功，正在跳转...';
          
          setTimeout(() => {
            this.$router.push('/home');
          }, 100);
        } else {
          throw new Error(res.msg || '登录失败');
        }
      } catch (error) {
        this.errorMessage = error.message || '登录失败，请检查用户名和密码';
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 400px;
}

.auth-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.auth-title {
  font-size: 32px;
  text-align: center;
  color: #667eea;
  margin-bottom: 8px;
}

.auth-subtitle {
  text-align: center;
  color: #999;
  margin-bottom: 30px;
}

.auth-form {
  margin-bottom: 24px;
}

.btn-block {
  width: 100%;
  padding: 12px;
  font-size: 16px;
}

.auth-footer {
  text-align: center;
  color: #666;
}

.auth-footer a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style>
