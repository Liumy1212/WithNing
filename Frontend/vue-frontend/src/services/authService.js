import api from './api';

/**
 * 用户认证服务
 * 处理用户登录、注册等认证相关的API请求
 */
export const authService = {
  /**
   * 用户登录
   * @param {Object} credentials - 登录凭证
   * @param {string} credentials.username - 用户名
   * @param {string} credentials.password - 密码
   * @returns {Promise} 返回登录结果
   */
  login(credentials) {
    return api.post('/auth/login', credentials);
  },
  
  /**
   * 获取当前用户信息
   * @returns {Promise} 返回当前用户信息
   */
  getCurrentUser() {
    return api.get('/auth/currentUser');
  },
  
  /**
   * 用户注册
   * @param {Object} userData - 用户数据
   * @param {string} userData.username - 用户名
   * @param {string} userData.password - 密码
   * @param {string} userData.nickname - 昵称
   * @param {string} userData.email - 邮箱
   * @param {string} [userData.phone] - 手机号
   * @returns {Promise} 返回注册结果
   */
  register(userData) {
    return api.post('/auth/register', userData);
  }
};