<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">每日任务</h1>
      <button v-if="isAdmin" class="btn btn-primary" @click="showPublishModal = true">
        发布任务
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <div v-else-if="tasks.length === 0" class="empty-state">
      <p>暂无每日任务</p>
    </div>
    <div v-else class="task-grid">
      <div v-for="task in tasks" :key="task.id" class="task-card">
        <div v-if="task.imageUrl" class="task-image">
          <img :src="task.imageUrl" :alt="task.title" />
        </div>
        <div class="task-content">
          <div class="task-header">
            <h3 class="task-title">{{ task.title }}</h3>
            <span :class="['status-badge', getStatusClass(task.status)]">
              {{ formatStatus(task.status) }}
            </span>
          </div>
          <p class="task-desc">{{ task.description }}</p>
          <div class="task-meta">
            <span class="task-reward">⭐ {{ task.rewardPoints }} 积分</span>
            <span class="task-time">{{ formatDate(task.createTime) }}</span>
          </div>
          <div class="task-actions">
            <button
              v-if="!task.completed && task.status === 'approved'"
              class="btn btn-success"
              @click="completeTask(task.id)"
            >
              完成
            </button>
            <span v-else-if="task.completed" class="completed-text">已完成</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showPublishModal" class="modal-overlay" @click.self="showPublishModal = false">
      <div class="modal">
        <h2 class="modal-title">发布每日任务</h2>
        <form @submit.prevent="publishTask">
          <div class="form-group">
            <label>任务标题</label>
            <input v-model="newTask.title" type="text" placeholder="请输入任务标题" required />
          </div>
          <div class="form-group">
            <label>任务描述</label>
            <textarea v-model="newTask.description" placeholder="请输入任务描述" required></textarea>
          </div>
          <div class="form-group">
            <label>奖励积分</label>
            <input v-model.number="newTask.rewardPoints" type="number" min="1" required />
          </div>
          <div class="form-group">
            <label>图片URL（可选）</label>
            <input v-model="newTask.imageUrl" type="url" placeholder="请输入图片地址" />
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="showPublishModal = false">
              取消
            </button>
            <button type="submit" class="btn btn-primary" :disabled="publishing">
              {{ publishing ? '发布中...' : '发布' }}
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
import { formatDate, formatStatus, getStatusClass } from '../utils';

export default {
  name: 'DailyTasks',
  data() {
    return {
      tasks: [],
      loading: true,
      errorMessage: '',
      showPublishModal: false,
      publishing: false,
      newTask: {
        type: 'daily',
        title: '',
        description: '',
        rewardPoints: 10,
        imageUrl: ''
      }
    };
  },
  computed: {
    isAdmin() {
      return store.isAdmin();
    }
  },
  async mounted() {
    await this.loadTasks();
  },
  methods: {
    formatDate,
    formatStatus,
    getStatusClass,
    async loadTasks() {
      this.loading = true;
      this.errorMessage = '';
      try {
        const response = await taskAPI.getDailyTasks();
        this.tasks = response.data || [];
      } catch (error) {
        this.errorMessage = '加载任务失败';
      } finally {
        this.loading = false;
      }
    },
    async completeTask(taskId) {
      try {
        await taskAPI.completeTask(taskId);
        await this.loadTasks();
      } catch (error) {
        alert('完成任务失败');
      }
    },
    async publishTask() {
      this.publishing = true;
      try {
        await taskAPI.publishTask(this.newTask);
        this.showPublishModal = false;
        this.newTask = {
          type: 'daily',
          title: '',
          description: '',
          rewardPoints: 10,
          imageUrl: ''
        };
        await this.loadTasks();
      } catch (error) {
        alert('发布任务失败');
      } finally {
        this.publishing = false;
      }
    }
  }
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.task-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.task-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
}

.task-card:hover {
  transform: translateY(-4px);
}

.task-image {
  width: 100%;
  height: 160px;
  overflow: hidden;
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
}

.task-title {
  font-size: 18px;
  color: #2c3e50;
  flex: 1;
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.status-approved {
  background: #e8f8f0;
  color: #2ecc71;
}

.status-badge.status-pending {
  background: #fef5e7;
  color: #f39c12;
}

.status-badge.status-rejected {
  background: #fdeaea;
  color: #e74c3c;
}

.task-desc {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
}

.task-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #999;
  margin-bottom: 16px;
}

.task-reward {
  color: #f39c12;
  font-weight: 500;
}

.task-actions {
  display: flex;
  gap: 10px;
}

.completed-text {
  color: #2ecc71;
  font-weight: 500;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal {
  background: white;
  border-radius: 12px;
  padding: 30px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-title {
  font-size: 20px;
  margin-bottom: 24px;
  color: #2c3e50;
}

.modal textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  min-height: 100px;
  resize: vertical;
}

.modal textarea:focus {
  outline: none;
  border-color: #3498db;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .task-grid {
    grid-template-columns: 1fr;
  }
}
</style>
