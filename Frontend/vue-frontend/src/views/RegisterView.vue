<template>
  <div class="register-view">
    <div class="register-container">
      <div class="register-header">
        <h1>用户注册</h1>
        <p>创建您的账号，开始使用打卡监督系统</p>
      </div>
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="registerForm.username" required placeholder="请输入用户名">
        </div>
        <div class="form-group">
          <label>昵称</label>
          <input type="text" v-model="registerForm.nickname" required placeholder="请输入昵称">
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input type="email" v-model="registerForm.email" required placeholder="请输入邮箱">
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input type="tel" v-model="registerForm.phone" placeholder="请输入手机号（选填）">
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="registerForm.password" required placeholder="请输入密码" minlength="6">
        </div>
        <div class="form-group">
          <label>确认密码</label>
          <input type="password" v-model="confirmPassword" required placeholder="请再次输入密码">
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>
        <div v-if="success" class="success-message">{{ success }}</div>
        <button type="submit" class="register-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>
      <div class="register-footer">
        <p>已有账号？<router-link to="/login">立即登录</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/userStore';

const router = useRouter();
const userStore = useUserStore();

const registerForm = ref({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  password: ''
});

const confirmPassword = ref('');
const loading = ref(false);
const error = ref(null);
const success = ref(null);

const handleRegister = async () => {
  error.value = null;
  success.value = null;
  
  if (registerForm.value.password !== confirmPassword.value) {
    error.value = '两次输入的密码不一致';
    return;
  }
  
  if (registerForm.value.password.length < 6) {
    error.value = '密码长度至少为6位';
    return;
  }
  
  loading.value = true;
  try {
    await userStore.register(registerForm.value);
    success.value = '注册成功！正在跳转至登录页面...';
    setTimeout(() => {
      router.push('/login');
    }, 1500);
  } catch (err) {
    error.value = '注册失败，用户名或邮箱可能已被使用';
    console.error('注册失败', err);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-view {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-container {
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.register-header p {
  color: #666;
  font-size: 14px;
}

.register-form {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 18px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-group input::placeholder {
  color: #aaa;
}

.error-message {
  color: #f44336;
  font-size: 14px;
  margin-bottom: 15px;
  padding: 10px;
  background: #ffebee;
  border-radius: 6px;
  text-align: center;
}

.success-message {
  color: #4CAF50;
  font-size: 14px;
  margin-bottom: 15px;
  padding: 10px;
  background: #e8f5e9;
  border-radius: 6px;
  text-align: center;
}

.register-btn {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 14px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.register-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.register-footer {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.register-footer p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.register-footer a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.register-footer a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .register-container {
    padding: 30px 20px;
  }
  
  .register-header h1 {
    font-size: 24px;
  }
}
</style>