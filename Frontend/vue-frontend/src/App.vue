<template>
  <div id="app" class="app-container">
    <NavBar v-if="showNav" />
    <main :class="{ 'with-nav': showNav }">
      <router-view />
    </main>
  </div>
</template>

<script>
import { computed, watch, ref } from 'vue';
import { useRoute } from 'vue-router';
import NavBar from './components/NavBar.vue';
import store from './store';

export default {
  name: 'App',
  components: {
    NavBar
  },
  setup() {
    const route = useRoute();
    const forceUpdate = ref(0);
    
    watch(
      () => route.path,
      () => {
        forceUpdate.value++;
      }
    );
    
    const showNav = computed(() => {
      forceUpdate.value;
      const isLoggedIn = !!store.state.userInfo;
      const isAuthPage = route.name === 'Login' || route.name === 'Register';
      return isLoggedIn && !isAuthPage;
    });
    
    return { showNav };
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f5f7fa;
  color: #333;
}

.app-container {
  min-height: 100vh;
}

main {
  padding: 20px;
}

main.with-nav {
  padding-top: 80px;
}

@media (max-width: 768px) {
  main.with-nav {
    padding-top: 70px;
  }
}

.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #2c3e50;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-success {
  background-color: #2ecc71;
  color: white;
}

.btn-success:hover {
  background-color: #27ae60;
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background-color: #c0392b;
}

.btn-warning {
  background-color: #f39c12;
  color: white;
}

.btn-warning:hover {
  background-color: #d68910;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.status-approved {
  color: #2ecc71;
}

.status-pending {
  color: #f39c12;
}

.status-rejected {
  color: #e74c3c;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #999;
}

.error-message {
  color: #e74c3c;
  background: #fdeaea;
  padding: 12px;
  border-radius: 6px;
  margin-bottom: 16px;
}

.success-message {
  color: #2ecc71;
  background: #e8f8f0;
  padding: 12px;
  border-radius: 6px;
  margin-bottom: 16px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #3498db;
}

.grid {
  display: grid;
  gap: 20px;
}

.grid-2 {
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
}

.grid-3 {
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
}

.grid-4 {
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
}
</style>
