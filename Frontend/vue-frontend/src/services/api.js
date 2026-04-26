import axios from 'axios';

// API基础配置
// 后端服务地址，根据实际部署情况修改
const BASE_URL = 'http://localhost:8080/api';

// 创建axios实例
const api = axios.create({
  baseURL: BASE_URL, // API基础地址
  timeout: 10000, // 请求超时时间（毫秒）
  headers: {
    'Content-Type': 'application/json' // 默认请求头
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加认证token等
    // const token = localStorage.getItem('token');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 统一处理响应数据
    return response;
  },
  error => {
    // 统一处理错误
    console.error('API请求错误:', error);
    return Promise.reject(error);
  }
);

export default api;