<template>
  <div class="page-container">
    <h1 class="page-title">积分中心</h1>

    <div class="points-overview">
      <div class="points-card">
        <div class="points-icon">⭐</div>
        <div class="points-info">
          <div class="points-value">{{ currentPoints }}</div>
          <div class="points-label">当前积分</div>
        </div>
      </div>
    </div>

    <div class="tabs">
      <button
        :class="['tab-btn', { active: activeTab === 'records' }]"
        @click="activeTab = 'records'"
      >
        积分记录
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'history' }]"
        @click="activeTab = 'history'"
      >
        积分历史
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <template v-else>
      <div v-if="activeTab === 'records'">
        <div v-if="records.length === 0" class="empty-state">
          <p>暂无积分记录</p>
        </div>
        <div v-else class="record-list">
          <div v-for="record in records" :key="record.id" class="record-card">
            <div class="record-info">
              <div class="record-icon">
                {{ record.type === 'earn' ? '📈' : '📉' }}
              </div>
              <div class="record-details">
                <div class="record-desc">{{ record.description }}</div>
                <div class="record-time">{{ formatDate(record.createTime) }}</div>
              </div>
              <div :class="['record-points', record.type]">
                {{ record.type === 'earn' ? '+' : '-' }}{{ record.points }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <div v-if="history.length === 0" class="empty-state">
          <p>暂无积分历史</p>
        </div>
        <div v-else class="history-list">
          <div v-for="item in history" :key="item.date" class="history-card">
            <div class="history-date">{{ item.date }}</div>
            <div class="history-content">
              <span class="history-points" :class="{ positive: item.points > 0 }">
                {{ item.points > 0 ? '+' : '' }}{{ item.points }}
              </span>
              <span class="history-desc">{{ item.description }}</span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { pointsAPI } from '../api';
import store from '../store';
import { formatDate } from '../utils';

export default {
  name: 'PointsPage',
  data() {
    return {
      currentPoints: 0,
      records: [],
      history: [],
      loading: true,
      activeTab: 'records'
    };
  },
  methods: {
    formatDate,
    async loadData() {
      this.loading = true;
      try {
        const [pointsRes, recordsRes, historyRes] = await Promise.all([
          pointsAPI.getPoints().catch(() => ({ data: 0 })),
          pointsAPI.getRecords().catch(() => ({ data: [] })),
          pointsAPI.getHistory().catch(() => ({ data: [] }))
        ]);
        
        this.currentPoints = pointsRes.data || 0;
        this.records = recordsRes.data || [];
        this.history = historyRes.data || [];
        
        const userInfo = store.getUserInfo();
        if (userInfo && userInfo.points !== undefined) {
          this.currentPoints = userInfo.points;
        }
      } catch (error) {
        console.error('加载积分数据失败:', error);
      } finally {
        this.loading = false;
      }
    }
  },
  mounted() {
    this.loadData();
  }
}
</script>

<style scoped>
.points-overview {
  margin-bottom: 24px;
}

.points-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 32px;
  display: flex;
  align-items: center;
  gap: 24px;
  color: white;
}

.points-icon {
  font-size: 56px;
}

.points-info {
  flex: 1;
}

.points-value {
  font-size: 48px;
  font-weight: bold;
  line-height: 1.2;
}

.points-label {
  font-size: 16px;
  opacity: 0.9;
}

.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: white;
  padding: 8px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.tab-btn {
  flex: 1;
  padding: 12px 20px;
  border: none;
  background: transparent;
  border-radius: 8px;
  cursor: pointer;
  font-size: 15px;
  color: #666;
  transition: all 0.3s;
}

.tab-btn:hover {
  background: #f5f5f5;
}

.tab-btn.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-card {
  background: white;
  border-radius: 12px;
  padding: 16px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.record-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.record-icon {
  font-size: 32px;
}

.record-details {
  flex: 1;
}

.record-desc {
  font-size: 15px;
  color: #333;
  margin-bottom: 4px;
}

.record-time {
  font-size: 13px;
  color: #999;
}

.record-points {
  font-size: 18px;
  font-weight: 600;
}

.record-points.earn {
  color: #2ecc71;
}

.record-points.spend {
  color: #e74c3c;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-card {
  background: white;
  border-radius: 12px;
  padding: 16px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.history-date {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.history-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.history-points {
  font-size: 16px;
  font-weight: 600;
  color: #e74c3c;
}

.history-points.positive {
  color: #2ecc71;
}

.history-desc {
  font-size: 14px;
  color: #999;
}

@media (max-width: 768px) {
  .tabs {
    flex-direction: column;
  }

  .points-card {
    padding: 24px;
  }

  .points-value {
    font-size: 36px;
  }

  .record-info {
    flex-wrap: wrap;
  }
}
</style>
