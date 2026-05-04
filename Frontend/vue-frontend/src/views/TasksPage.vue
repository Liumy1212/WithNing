<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">任务中心</h1>
      <button v-if="isAdmin" class="btn btn-primary" @click="showPublishModal = true">
        <span class="btn-icon">+</span>
        发布任务
      </button>
    </div>

    <div class="tabs">
      <button
        :class="['tab-btn', { active: activeTab === 'daily' }]"
        @click="activeTab = 'daily'"
      >
        <span class="tab-icon">📅</span>
        每日任务
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'stage' }]"
        @click="activeTab = 'stage'"
      >
        <span class="tab-icon">🎯</span>
        阶段任务
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'my-applies' }]"
        @click="activeTab = 'my-applies'"
      >
        <span class="tab-icon">📋</span>
        我的申请
      </button>
    </div>

    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
    <template v-else>
      <div v-if="activeTab !== 'my-applies'">
        <div v-if="currentTasks.length === 0" class="empty-state">
          <div class="empty-icon">📭</div>
          <p>暂无{{ activeTab === 'daily' ? '每日任务' : '阶段任务' }}</p>
        </div>
        <div v-else class="task-grid">
          <div
            v-for="task in currentTasks"
            :key="task.id"
            class="task-card"
            @click="showTaskDetail(task)"
          >
            <div v-if="task.imageUrl" class="task-image">
              <img :src="task.imageUrl" :alt="task.title" />
            </div>
            <div v-else class="task-image task-image-placeholder">
              <div class="task-icon">{{ task.type === 1 ? '📅' : '🎯' }}</div>
            </div>
            <div class="task-content">
              <div class="task-header">
                <h3 class="task-title">{{ task.title }}</h3>
                <div class="task-reward-badge">
                  <span class="reward-icon">⭐</span>
                  <span class="reward-points">{{ task.rewardPoints }}</span>
                </div>
              </div>
              <p class="task-desc">{{ task.description }}</p>
              <div class="task-meta">
                <span v-if="task.deadline" class="task-deadline">
                  <span class="meta-icon">⏰</span>
                  截止：{{ formatDate(task.deadline) }}
                </span>
                <span v-else class="task-time">
                  <span class="meta-icon">📅</span>
                  发布于：{{ formatDate(task.createTime) }}
                </span>
              </div>
              <div class="task-actions" @click.stop>
                <button
                  v-if="isAdmin"
                  class="btn btn-success btn-sm"
                  @click="completeTaskDirectly(task.id)"
                  :disabled="task.completed"
                >
                  {{ task.completed ? '✅ 已完成' : '✅ 完成' }}
                </button>
                <button
                  v-else
                  class="btn btn-primary"
                  @click="applyTask(task)"
                  :disabled="task.hasApplied"
                >
                  {{ task.hasApplied ? '⏳ 已申请' : '📝 申请完成' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <div v-if="myApplies.length === 0" class="empty-state">
          <div class="empty-icon">📋</div>
          <p>暂无申请记录</p>
        </div>
        <div v-else class="apply-list">
          <div v-for="apply in myApplies" :key="apply.id" class="apply-card">
            <div class="apply-header">
              <div class="apply-title">{{ apply.taskTitle }}</div>
              <span :class="['status-badge', 'status-' + apply.status]">
                {{ getStatusText(apply.status) }}
              </span>
            </div>
            <div class="apply-meta">
              <span class="apply-reward">
                <span class="meta-icon">⭐</span>
                +{{ apply.rewardPoints }} 积分
              </span>
              <span class="apply-time">
                <span class="meta-icon">📅</span>
                {{ formatDate(apply.createTime) }}
              </span>
            </div>
            <div v-if="apply.reviewComment" class="apply-comment">
              <span class="comment-label">审核备注：</span>
              {{ apply.reviewComment }}
            </div>
            <div v-if="apply.reviewTime" class="apply-review-time">
              <span class="meta-icon">🕐</span>
              审核时间：{{ formatDate(apply.reviewTime) }}
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal detail-modal">
        <div class="detail-header">
          <div class="detail-title-container">
            <h2 class="modal-title">{{ selectedTask?.title }}</h2>
            <div class="detail-reward">
              <span class="reward-icon">⭐</span>
              <span class="reward-points">{{ selectedTask?.rewardPoints }}</span>
              <span class="reward-label">积分</span>
            </div>
          </div>
          <button class="modal-close" @click="showDetailModal = false">×</button>
        </div>
        <div class="detail-content">
          <div v-if="selectedTask?.imageUrl" class="detail-image">
            <img :src="selectedTask.imageUrl" :alt="selectedTask.title" />
          </div>
          <div class="detail-body">
            <div class="detail-section">
              <div class="detail-label">任务描述</div>
              <p class="detail-desc">{{ selectedTask?.description }}</p>
            </div>
            <div class="detail-section">
              <div class="detail-label">任务类型</div>
              <span class="type-badge">
                {{ selectedTask?.type === 1 ? '每日任务' : '阶段任务' }}
              </span>
            </div>
            <div class="detail-section">
              <div class="detail-label">奖励积分</div>
              <div class="points-display">
                <span class="points-icon">⭐</span>
                <span class="points-value">{{ selectedTask?.rewardPoints }}</span>
              </div>
            </div>
            <div v-if="selectedTask?.deadline" class="detail-section">
              <div class="detail-label">截止时间</div>
              <span class="deadline-display">
                <span class="deadline-icon">⏰</span>
                {{ formatDate(selectedTask.deadline) }}
              </span>
            </div>
            <div class="detail-section">
              <div class="detail-label">发布时间</div>
              <span class="time-display">
                <span class="time-icon">📅</span>
                {{ formatDate(selectedTask?.createTime) }}
              </span>
            </div>
          </div>
        </div>
        <div class="modal-actions detail-actions">
          <button class="btn btn-secondary" @click="showDetailModal = false">
            关闭
          </button>
          <button
            v-if="isAdmin"
            class="btn btn-success"
            @click="completeTaskDirectly(selectedTask.id)"
            :disabled="selectedTask.completed"
          >
            {{ selectedTask.completed ? '已完成' : '完成任务' }}
          </button>
          <button
            v-else
            class="btn btn-primary"
            @click="applyTask(selectedTask)"
            :disabled="selectedTask.hasApplied"
          >
            {{ selectedTask.hasApplied ? '已申请' : '申请完成' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="showPublishModal" class="modal-overlay" @click.self="showPublishModal = false">
      <div class="modal">
        <h2 class="modal-title">
          <span class="modal-title-icon">+</span>
          发布{{ newTask.type === 1 ? '每日' : '阶段' }}任务
        </h2>
        <form @submit.prevent="publishTask">
          <div class="form-group">
            <label>任务类型</label>
            <select v-model.number="newTask.type" required class="form-select">
              <option :value="1">📅 每日任务</option>
              <option :value="2">🎯 阶段任务</option>
            </select>
          </div>
          <div class="form-group">
            <label>任务标题</label>
            <input
              v-model="newTask.title"
              type="text"
              placeholder="请输入任务标题"
              required
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>任务描述</label>
            <textarea
              v-model="newTask.description"
              placeholder="请输入任务描述"
              required
              class="form-textarea"
              rows="4"
            ></textarea>
          </div>
          <div class="form-group">
            <label>奖励积分</label>
            <input
              v-model.number="newTask.rewardPoints"
              type="number"
              min="1"
              required
              class="form-input"
              placeholder="请输入奖励积分"
            />
          </div>
          <div class="form-group" v-if="newTask.type === 2">
            <label>截止时间</label>
            <input
              v-model="newTask.deadline"
              type="date"
              required
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>图片URL（可选）</label>
            <input
              v-model="newTask.imageUrl"
              type="url"
              placeholder="请输入图片地址"
              class="form-input"
            />
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="showPublishModal = false">
              取消
            </button>
            <button type="submit" class="btn btn-primary" :disabled="publishing">
              {{ publishing ? '发布中...' : '发布任务' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { taskAPI } from '../api';
import store from '../store';
import { formatDate } from '../utils';

export default {
  name: 'TasksPage',
  data() {
    return {
      dailyTasks: [],
      stageTasks: [],
      myApplies: [],
      loading: true,
      activeTab: 'daily',
      showPublishModal: false,
      showDetailModal: false,
      publishing: false,
      selectedTask: null,
      newTask: {
        type: 1,
        title: '',
        description: '',
        rewardPoints: 10,
        deadline: '',
        imageUrl: ''
      }
    };
  },
  computed: {
    isAdmin() {
      return store.isAdmin();
    },
    currentTasks() {
      return this.activeTab === 'daily' ? this.dailyTasks : this.stageTasks;
    }
  },
  methods: {
    formatDate,
    getStatusText(status) {
      const numStatus = Number(status);
      const statusMap = {
        1: '✅ 已通过',
        2: '⏳ 待审核',
        3: '❌ 已拒绝'
      };
      return statusMap[numStatus] || status;
    },
    async loadTasks() {
      this.loading = true;
      try {
        const [dailyRes, stageRes] = await Promise.all([
          taskAPI.getDailyTasks().catch(() => ({ data: [] })),
          taskAPI.getStageTasks().catch(() => ({ data: [] }))
        ]);
        this.dailyTasks = dailyRes.data || [];
        this.stageTasks = stageRes.data || [];
      } catch (error) {
        console.error('加载任务失败:', error);
      } finally {
        this.loading = false;
      }
    },
    async loadMyApplies() {
      try {
        const res = await taskAPI.getMyApplies().catch(() => ({ data: [] }));
        this.myApplies = res.data || [];
      } catch (error) {
        console.error('加载申请记录失败:', error);
      }
    },
    showTaskDetail(task) {
      this.selectedTask = { ...task };
      this.showDetailModal = true;
    },
    async applyTask(task) {
      try {
        await taskAPI.applyTask(task.id);
        alert('申请已提交，等待管理员审核');
        await this.loadTasks();
        await this.loadMyApplies();
        this.showDetailModal = false;
      } catch (error) {
        alert(error.message || '申请失败');
      }
    },
    async completeTaskDirectly(taskId) {
      if (!confirm('确定要完成这个任务吗？完成后积分将直接发放。')) return;
      try {
        await taskAPI.completeTask(taskId);
        alert('任务已完成，积分已发放！');
        await this.loadTasks();
        if (this.selectedTask?.id === taskId) {
          this.selectedTask.completed = true;
        }
      } catch (error) {
        alert(error.message || '操作失败');
      }
    },
    async publishTask() {
      this.publishing = true;
      try {
        const taskData = { ...this.newTask };
        if (taskData.type === 2 && taskData.deadline) {
          taskData.deadline = new Date(taskData.deadline).toISOString();
        }
        await taskAPI.publishTask(taskData);
        this.showPublishModal = false;
        this.newTask = {
          type: 1,
          title: '',
          description: '',
          rewardPoints: 10,
          deadline: '',
          imageUrl: ''
        };
        await this.loadTasks();
      } catch (error) {
        alert(error.message || '发布任务失败');
      } finally {
        this.publishing = false;
      }
    }
  },
  mounted() {
    this.loadTasks();
    this.loadMyApplies();
  }
};
</script>

<style scoped>
.page-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
}

.btn {
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn-icon {
  font-size: 18px;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn-success {
  background: linear-gradient(135deg, #2ecc71 0%, #27ae60 100%);
  color: white;
}

.btn-success:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(46, 204, 113, 0.4);
}

.btn-success:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
}

.btn-sm {
  padding: 8px 16px;
  font-size: 13px;
}

.tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  background: white;
  padding: 8px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.tab-btn {
  flex: 1;
  padding: 14px 20px;
  border: none;
  background: transparent;
  border-radius: 8px;
  cursor: pointer;
  font-size: 15px;
  font-weight: 500;
  color: #666;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.tab-btn:hover {
  background: #f8f9fa;
  color: #495057;
}

.tab-btn.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.tab-icon {
  font-size: 18px;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  gap: 16px;
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading p {
  color: #999;
  font-size: 14px;
  margin: 0;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state p {
  color: #999;
  font-size: 16px;
  margin: 0;
}

.task-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.task-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
  cursor: pointer;
}

.task-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.task-image {
  width: 100%;
  height: 180px;
  overflow: hidden;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.task-image-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}

.task-icon {
  font-size: 64px;
  opacity: 0.5;
}

.task-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.task-content {
  padding: 20px;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  gap: 12px;
}

.task-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
  flex: 1;
}

.task-reward-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-weight: 600;
  flex-shrink: 0;
}

.reward-icon {
  font-size: 14px;
}

.reward-points {
  font-size: 16px;
  font-weight: 700;
}

.task-desc {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.task-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #999;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 8px;
}

.meta-icon {
  font-size: 12px;
  margin-right: 4px;
}

.task-deadline {
  color: #e74c3c;
  font-weight: 500;
}

.task-actions {
  display: flex;
  gap: 10px;
}

.apply-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.apply-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.apply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.apply-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.status-badge {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.status-1 {
  background: #e8f8f0;
  color: #2ecc71;
}

.status-2 {
  background: #fef5e7;
  color: #f39c12;
}

.status-3 {
  background: #fdeaea;
  color: #e74c3c;
}

.apply-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.apply-reward {
  color: #f39c12;
  font-weight: 600;
}

.apply-time {
  color: #999;
}

.apply-comment {
  font-size: 14px;
  color: #666;
  padding: 12px 16px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-top: 12px;
  line-height: 1.6;
}

.comment-label {
  font-weight: 600;
  color: #495057;
}

.apply-review-time {
  font-size: 13px;
  color: #999;
  margin-top: 8px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal {
  background: white;
  border-radius: 16px;
  padding: 32px;
  width: 100%;
  max-width: 540px;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-title {
  font-size: 22px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 24px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.modal-title-icon {
  font-size: 24px;
}

.modal-close {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 36px;
  height: 36px;
  border: none;
  background: #f8f9fa;
  color: #666;
  font-size: 24px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-close:hover {
  background: #e9ecef;
  color: #333;
}

.detail-modal {
  position: relative;
  padding: 0;
  overflow: hidden;
  max-width: 600px;
}

.detail-header {
  padding: 28px 32px 20px;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
}

.detail-title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.detail-header .modal-title {
  margin: 0;
}

.detail-reward {
  display: flex;
  align-items: center;
  gap: 8px;
  background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
  color: white;
  padding: 8px 16px;
  border-radius: 24px;
  font-weight: 600;
  flex-shrink: 0;
}

.detail-reward .reward-icon {
  font-size: 16px;
}

.detail-reward .reward-label {
  font-size: 13px;
  opacity: 0.9;
}

.detail-content {
  padding: 24px 32px;
}

.detail-image {
  width: 100%;
  height: 240px;
  overflow: hidden;
  border-radius: 12px;
  margin-bottom: 24px;
  background: #f8f9fa;
}

.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-body {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.detail-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-label {
  font-size: 13px;
  font-weight: 600;
  color: #999;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-desc {
  font-size: 15px;
  color: #495057;
  line-height: 1.6;
  margin: 0;
}

.type-badge {
  display: inline-flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  width: fit-content;
}

.points-display {
  display: flex;
  align-items: center;
  gap: 10px;
}

.points-icon {
  font-size: 28px;
}

.points-value {
  font-size: 32px;
  font-weight: 700;
  color: #f39c12;
}

.deadline-display,
.time-display {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
}

.deadline-display {
  color: #e74c3c;
  font-weight: 500;
}

.deadline-icon,
.time-icon {
  font-size: 16px;
}

.detail-actions {
  padding: 20px 32px 28px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #495057;
  margin-bottom: 8px;
}

.form-input,
.form-textarea,
.form-select {
  width: 100%;
  padding: 12px 14px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
  background: white;
  box-sizing: border-box;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

@media (max-width: 768px) {
  .page-container {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .page-title {
    font-size: 24px;
  }

  .tabs {
    flex-direction: column;
  }

  .task-grid {
    grid-template-columns: 1fr;
  }

  .modal {
    padding: 24px;
  }

  .detail-modal {
    padding: 0;
  }

  .detail-header,
  .detail-content,
  .detail-actions {
    padding-left: 20px;
    padding-right: 20px;
  }

  .detail-title-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
