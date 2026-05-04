<template>
  <div class="page-container">
    <div class="announcement-card">
      <div class="announcement-header">
        <span class="announcement-icon">📢</span>
        <h2>公告栏</h2>
      </div>
      <div class="announcement-content">
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="announcements.length === 0" class="empty-state">
          暂无公告
        </div>
        <div v-else class="announcement-list">
          <div v-for="item in announcements" :key="item.id" class="announcement-item">
            <div class="announcement-title">{{ item.title }}</div>
            <div class="announcement-text">{{ item.content }}</div>
            <div class="announcement-time">{{ formatDate(item.createTime) }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { formatDate } from '../utils';

export default {
  name: 'HomePage',
  data() {
    return {
      announcements: [],
      loading: true
    };
  },
  methods: {
    formatDate,
    async loadAnnouncements() {
      this.loading = false;
    }
  },
  mounted() {
    this.loadAnnouncements();
  }
}
</script>

<style scoped>
.announcement-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.announcement-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.announcement-icon {
  font-size: 28px;
}

.announcement-header h2 {
  font-size: 20px;
  color: #2c3e50;
  margin: 0;
}

.announcement-content {
  min-height: 200px;
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.announcement-item {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #667eea;
}

.announcement-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.announcement-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 8px;
}

.announcement-time {
  font-size: 12px;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}
</style>
