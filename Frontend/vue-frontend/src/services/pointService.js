import api from './api';

/**
 * 积分服务
 * 处理积分相关的API请求
 */
export const pointService = {
  /**
   * 获取用户积分
   * @returns {Promise} 返回用户当前积分
   */
  getUserPoints() {
    return api.get('/points');
  },
  
  /**
   * 获取积分历史
   * @returns {Promise} 返回积分历史记录
   */
  getPointHistory() {
    return api.get('/points/history');
  },
  
  /**
   * 获取积分变动记录
   * @returns {Promise} 返回积分变动详细记录
   */
  getPointRecords() {
    return api.get('/points/records');
  }
};