<template>
  <div class="point-view">
    <h1>积分中心</h1>
    
    <!-- 用户当前积分 -->
    <div class="current-points">
      <h2>当前积分</h2>
      <div class="points-display">
        <div v-if="pointStore.loading" class="loading">加载中...</div>
        <div v-else-if="pointStore.error" class="error">{{ pointStore.error }}</div>
        <div v-else class="points-value">{{ pointStore.userPoints }}</div>
      </div>
    </div>
    
    <!-- 积分历史记录 -->
    <div class="point-history">
      <h2>积分历史</h2>
      <div v-if="pointStore.loading" class="loading">加载中...</div>
      <div v-else-if="pointStore.error" class="error">{{ pointStore.error }}</div>
      <div v-else class="history-list">
        <div v-for="record in pointStore.pointRecords" :key="record.id" class="history-item">
          <div class="history-info">
            <h3>{{ record.description }}</h3>
            <p>{{ record.createTime }}</p>
          </div>
          <div class="history-points" :class="record.points > 0 ? 'positive' : 'negative'">
            {{ record.points > 0 ? '+' : '' }}{{ record.points }}
          </div>
        </div>
        <div v-if="pointStore.pointRecords.length === 0" class="empty">暂无积分记录</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { usePointStore } from '../stores/pointStore';

const pointStore = usePointStore();

onMounted(() => {
  pointStore.fetchUserPoints();
  pointStore.fetchPointRecords();
});
</script>

<style scoped>
.point-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.current-points {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px;
  border-radius: 12px;
  margin-bottom: 30px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.current-points h2 {
  margin-bottom: 20px;
  font-size: 24px;
}

.points-display {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100px;
}

.points-value {
  font-size: 72px;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.point-history {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.point-history h2 {
  margin-bottom: 20px;
  color: #333;
  border-bottom: 2px solid #667eea;
  padding-bottom: 10px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.history-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.history-info h3 {
  margin-bottom: 5px;
  color: #333;
  font-size: 16px;
}

.history-info p {
  color: #888;
  font-size: 14px;
}

.history-points {
  font-size: 20px;
  font-weight: bold;
  min-width: 80px;
  text-align: right;
}

.history-points.positive {
  color: #4CAF50;
}

.history-points.negative {
  color: #f44336;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.error {
  text-align: center;
  padding: 20px;
  color: #f44336;
  background: #ffebee;
  border-radius: 4px;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #666;
  background: #f5f5f5;
  border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .point-view {
    padding: 10px;
  }
  
  h1 {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .current-points {
    padding: 30px 20px;
  }
  
  .current-points h2 {
    font-size: 20px;
    margin-bottom: 15px;
  }
  
  .points-value {
    font-size: 48px;
  }
  
  .point-history {
    padding: 20px;
  }
  
  .point-history h2 {
    font-size: 18px;
  }
  
  .history-item {
    padding: 12px;
  }
  
  .history-info h3 {
    font-size: 14px;
  }
  
  .history-info p {
    font-size: 12px;
  }
  
  .history-points {
    font-size: 16px;
    min-width: 60px;
  }
  
  .empty {
    padding: 30px;
  }
}
</style>