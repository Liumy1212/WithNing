import axios from 'axios';

const api = axios.create({
  // 使用环境变量，在生产环境中需要设置 VUE_APP_API_BASE_URL
  baseURL: process.env.NODE_ENV === 'production'
    ? (process.env.VUE_APP_API_BASE_URL || 'https://awry-regulate-unfitting.ngrok-free.dev/api')
    : '/api',
  timeout: 10000,
  withCredentials: true
});

api.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  response => {
    const res = response.data;
    if (res && typeof res === 'object') {
      const successCodes = [200, '200', 1, '1'];
      if (res.code !== undefined && !successCodes.includes(res.code)) {
        return Promise.reject(new Error(res.msg || '请求失败'));
      }
      if (res.success !== undefined && !res.success) {
        return Promise.reject(new Error(res.msg || '请求失败'));
      }
    }
    return res;
  },
  error => {
    if (error.response) {
      const status = error.response.status;
      if (status === 401) {
        return Promise.reject(new Error('未登录或登录已过期'));
      } else if (status === 403) {
        return Promise.reject(new Error('权限不足'));
      } else if (status === 404) {
        return Promise.reject(new Error('资源不存在'));
      } else if (status === 500) {
        return Promise.reject(new Error('服务器内部错误'));
      }
    }
    return Promise.reject(new Error('网络请求失败'));
  }
);

export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  logout: () => api.post('/auth/logout'),
  getUserInfo: () => api.get('/auth/userinfo')
};

export const taskAPI = {
  getDailyTasks: () => api.get('/tasks/daily'),
  getStageTasks: () => api.get('/tasks/stage'),
  getTaskDetail: (id) => api.get(`/tasks/${id}`),
  publishTask: (data) => api.post('/tasks', data),
  deleteTask: (id) => api.delete(`/tasks/${id}`),
  applyTask: (taskId) => api.post(`/tasks/apply/${taskId}`),
  getMyApplies: () => api.get('/tasks/my-applies'),
  getSubmitList: () => api.get('/tasks/submits'),
  reviewSubmit: (id, data) => api.put(`/tasks/submit/${id}/review`, data),
  completeTask: (taskId) => api.put(`/tasks/complete/${taskId}`)
};

export const pointsAPI = {
  getPoints: () => api.get('/points'),
  getHistory: () => api.get('/points/history'),
  getRecords: () => api.get('/points/records')
};

export const mallAPI = {
  getProducts: () => api.get('/mall/products'),
  getProductDetail: (id) => api.get(`/mall/products/${id}`),
  addProduct: (data) => api.post('/mall/products', data),
  removeProduct: (id) => api.delete(`/mall/products/${id}`),
  onlineProduct: (id) => api.put(`/mall/products/${id}/online`),
  offlineProduct: (id) => api.put(`/mall/products/${id}/offline`),
  applyExchange: (productId) => api.post(`/mall/exchange/apply/${productId}`),
  getMyApplies: () => api.get('/mall/exchange/my-applies'),
  getApplyList: () => api.get('/mall/exchange/applies'),
  reviewApply: (id, reviewData) => api.put(`/mall/exchange/${id}/review`, reviewData),
  getExchangeRecords: () => api.get('/mall/exchange/records')
};

export const adminAPI = {
  checkAdmin: () => api.get('/admin/check'),
  getUsers: () => api.get('/admin/users')
};

export default api;
