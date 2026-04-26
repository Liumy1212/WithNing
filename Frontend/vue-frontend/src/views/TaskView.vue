<template>
  <div class="task-view">
    <div class="task-header">
      <h1>任务中心</h1>
      <button class="add-task-btn" @click="handleAddClick">
        {{ userStore.isLoggedIn ? (isAdmin ? '+ 发布任务' : '+ 提交任务申请') : '+ 提交任务申请' }}
      </button>
    </div>
    
    <!-- 未登录提示 -->
    <div v-if="!userStore.isLoggedIn" class="login-tip">
      <p>您还未登录，登录后可提交任务申请和完成任务</p>
      <button @click="$router.push('/login')" class="login-tip-btn">去登录</button>
    </div>
    
    <!-- 每日任务列表 -->
    <div class="task-section">
      <h2>每日任务</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="task-list">
        <div v-for="task in taskStore.dailyTasks" :key="task.id" class="task-item">
          <div class="task-info">
            <h3>{{ task.title }}</h3>
            <p>{{ task.description }}</p>
            <div class="task-meta">
              <span>奖励积分: {{ task.rewardPoints }}</span>
              <span v-if="task.completed" class="completed-tag">已完成</span>
              <span v-else-if="task.status === 'pending'" class="pending-tag">待审核</span>
            </div>
          </div>
          <div class="task-actions">
            <button 
              v-if="!task.completed && task.status === 'approved' && userStore.isLoggedIn" 
              @click="completeTask(task.id)" 
              :disabled="loading"
            >
              完成任务
            </button>
            <button 
              v-else-if="!task.completed && task.status === 'approved' && !userStore.isLoggedIn"
              disabled
              class="disabled-btn"
            >
              登录后完成
            </button>
          </div>
        </div>
        <div v-if="taskStore.dailyTasks.length === 0" class="empty">暂无每日任务</div>
      </div>
    </div>
    
    <!-- 阶段任务列表 -->
    <div class="task-section">
      <h2>阶段任务</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="task-list">
        <div v-for="task in taskStore.stageTasks" :key="task.id" class="task-item">
          <div class="task-info">
            <h3>{{ task.title }}</h3>
            <p>{{ task.description }}</p>
            <div class="task-meta">
              <span>奖励积分: {{ task.rewardPoints }}</span>
              <span>期限: {{ task.deadline }}</span>
              <span v-if="task.completed" class="completed-tag">已完成</span>
              <span v-else-if="task.status === 'pending'" class="pending-tag">待审核</span>
            </div>
          </div>
          <div class="task-actions">
            <button 
              v-if="!task.completed && task.status === 'approved' && userStore.isLoggedIn" 
              @click="completeTask(task.id)" 
              :disabled="loading"
            >
              完成任务
            </button>
            <button 
              v-else-if="!task.completed && task.status === 'approved' && !userStore.isLoggedIn"
              disabled
              class="disabled-btn"
            >
              登录后完成
            </button>
          </div>
        </div>
        <div v-if="taskStore.stageTasks.length === 0" class="empty">暂无阶段任务</div>
      </div>
    </div>
    
    <!-- 任务表单弹窗 -->
    <TaskFormModal
      :visible="showTaskModal"
      :isAdmin="false"
      @close="closeTaskModal"
      @submit="handleTaskSubmit"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useTaskStore } from '../stores/taskStore';
import { useUserStore } from '../stores/userStore';
import TaskFormModal from '../components/TaskFormModal.vue';

const router = useRouter();
const taskStore = useTaskStore();
const userStore = useUserStore();

const showTaskModal = ref(false);
const loading = ref(false);
const error = ref(null);

const isAdmin = computed(() => userStore.isAdmin);

onMounted(() => {
  loadData();
});

const loadData = async () => {
  loading.value = true;
  error.value = null;
  try {
    await taskStore.fetchDailyTasks();
    await taskStore.fetchStageTasks();
  } catch (err) {
    error.value = '加载数据失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const handleAddClick = () => {
  if (!userStore.isLoggedIn) {
    if (confirm('您还未登录，是否去登录？')) {
      router.push('/login');
    }
    return;
  }
  showTaskModal.value = true;
};

const closeTaskModal = () => {
  showTaskModal.value = false;
};

const handleTaskSubmit = async (formData) => {
  try {
    await taskStore.submitTask(formData);
    closeTaskModal();
    await loadData();
  } catch (err) {
    console.error('提交失败', err);
    error.value = '提交失败';
  }
};

const completeTask = async (taskId) => {
  try {
    await taskStore.completeTask(taskId);
    await loadData();
  } catch (err) {
    console.error('完成任务失败', err);
    error.value = '完成任务失败';
  }
};
</script>

<style scoped>
.task-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 15px;
}

h1 {
  margin: 0;
  color: #333;
}

.add-task-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(102, 126, 234, 0.3);
}

.add-task-btn:hover {
  background: #764ba2;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(102, 126, 234, 0.4);
}

.login-tip {
  background: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.login-tip p {
  margin: 0;
  color: #856404;
  font-size: 14px;
}

.login-tip-btn {
  background: #ffc107;
  color: #333;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.login-tip-btn:hover {
  background: #e0a800;
}

.task-section {
  margin-bottom: 30px;
}

.task-section h2 {
  margin-bottom: 15px;
  color: #333;
  border-bottom: 2px solid #667eea;
  padding-bottom: 10px;
}

.task-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.task-item {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.task-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.task-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 18px;
}

.task-info p {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.5;
}

.task-meta {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 14px;
  color: #888;
}

.completed-tag {
  color: #4CAF50;
  font-weight: bold;
}

.pending-tag {
  color: #f59e0b;
  font-weight: bold;
}

.task-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.task-actions button {
  flex: 1;
  background: #4CAF50;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.task-actions button:hover {
  background: #45a049;
}

.task-actions button:disabled,
.task-actions button.disabled-btn {
  background: #ccc;
  cursor: not-allowed;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}

.error {
  text-align: center;
  padding: 20px;
  color: #f44336;
  background: #ffebee;
  border-radius: 8px;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #666;
  background: #f5f5f5;
  border-radius: 8px;
  grid-column: 1 / -1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .task-view {
    padding: 10px;
  }
  
  .task-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  h1 {
    font-size: 24px;
    text-align: center;
  }
  
  .add-task-btn {
    width: 100%;
  }
  
  .login-tip {
    flex-direction: column;
    text-align: center;
  }
  
  .task-list {
    grid-template-columns: 1fr;
  }
  
  .task-item {
    padding: 15px;
  }
  
  .task-info h3 {
    font-size: 16px;
  }
  
  .task-actions {
    flex-direction: column;
  }
}
</style>