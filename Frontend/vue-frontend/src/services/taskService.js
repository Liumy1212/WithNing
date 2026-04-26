import api from './api';

/**
 * 任务服务
 * 处理任务相关的API请求
 */
export const taskService = {
  /**
   * 获取每日任务列表
   * @returns {Promise} 返回每日任务列表
   */
  getDailyTasks() {
    return api.get('/tasks/daily');
  },
  
  /**
   * 获取阶段任务列表
   * @returns {Promise} 返回阶段任务列表
   */
  getStageTasks() {
    return api.get('/tasks/stage');
  },
  
  /**
   * 发布任务（管理员）
   * @param {Object} taskData - 任务数据
   * @param {string} taskData.type - 任务类型（daily: 每日任务, stage: 阶段任务）
   * @param {string} taskData.title - 任务标题
   * @param {string} taskData.description - 任务描述
   * @param {number} taskData.rewardPoints - 奖励积分
   * @param {string} [taskData.deadline] - 任务期限（阶段任务必填）
   * @param {string} [taskData.imageUrl] - 任务图片URL（可选）
   * @returns {Promise} 返回创建的任务
   */
  createTask(taskData) {
    return api.post('/tasks', taskData);
  },
  
  /**
   * 完成任务
   * @param {number} taskId - 任务ID
   * @returns {Promise} 返回操作结果
   */
  completeTask(taskId) {
    return api.put(`/tasks/${taskId}/complete`);
  },
  
  /**
   * 获取任务详情
   * @param {number} taskId - 任务ID
   * @returns {Promise} 返回任务详情
   */
  getTaskDetail(taskId) {
    return api.get(`/tasks/${taskId}`);
  },
  
  /**
   * 提交任务申请（普通用户）
   * @param {Object} submitData - 申请数据
   * @param {string} submitData.type - 任务类型
   * @param {string} submitData.title - 任务标题
   * @param {string} submitData.description - 任务描述
   * @param {number} submitData.rewardPoints - 奖励积分
   * @param {string} [submitData.deadline] - 任务期限
   * @param {string} [submitData.imageUrl] - 任务图片URL（可选）
   * @returns {Promise} 返回申请结果
   */
  submitTask(submitData) {
    return api.post('/tasks/submit', submitData);
  },
  
  /**
   * 获取任务申请列表（管理员）
   * @returns {Promise} 返回任务申请列表
   */
  getTaskSubmits() {
    return api.get('/tasks/submits');
  },
  
  /**
   * 审核任务申请（管理员）
   * @param {number} submitId - 申请ID
   * @param {Object} reviewData - 审核数据
   * @param {string} reviewData.status - 审核状态（approved/rejected）
   * @param {string} [reviewData.reviewComment] - 审核评论
   * @returns {Promise} 返回审核结果
   */
  reviewTask(submitId, reviewData) {
    return api.put(`/tasks/submit/${submitId}/review`, reviewData);
  }
};