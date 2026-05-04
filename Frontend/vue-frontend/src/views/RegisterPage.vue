<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <h1 class="auth-title">注册账号</h1>
        <p class="auth-subtitle">加入 WithNing 打卡监督系统</p>

        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

        <form @submit.prevent="handleRegister" class="auth-form">
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
            <label>昵称</label>
            <input
              v-model="formData.nickname"
              type="text"
              placeholder="请输入昵称"
              required
            />
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input
              v-model="formData.email"
              type="email"
              placeholder="请输入邮箱"
              required
            />
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input
              v-model="formData.phone"
              type="tel"
              placeholder="请输入手机号（可选）"
            />
          </div>
          <div class="form-group">
            <label>密码</label>
            <input
              v-model="formData.password"
              type="password"
              placeholder="请输入密码"
              required
              minlength="6"
            />
          </div>
          <div class="form-group">
            <label>确认密码</label>
            <input
              v-model="confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              required
            />
          </div>
          <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>

        <div class="auth-footer">
          <span>已有账号？</span>
          <router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { authAPI } from '../api';
import store from '../store';

export default {
  name: 'RegisterPage',
  data() {
    return {
      formData: {
        username: '',
        nickname: '',
        email: '',
        phone: '',
        password: ''
      },
      confirmPassword: '',
      loading: false,
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async handleRegister() {
      this.errorMessage = '';
      this.successMessage = '';

      if (this.formData.password !== this.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致';
        return;
      }

      if (this.formData.password.length < 6) {
        this.errorMessage = '密码长度至少为6位';
        return;
      }

      this.loading = true;

      try {
        const response = await authAPI.register(this.formData);
        this.successMessage = '注册成功！正在跳转到登录页面...';
        store.saveUserInfo(response.data);
        setTimeout(() => {
          this.$router.push('/login');
        }, 1500);
      } catch (error) {
        this.errorMessage = error.message || '注册失败，请重试';
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
  font-size: 28px;
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
  margin-top: 8px;
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
