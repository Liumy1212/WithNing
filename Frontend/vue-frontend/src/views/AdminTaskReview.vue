<template>
  <div class="page-container">
    <h1 class="page-title">任务审批</h1>

    <div class="tabs">
      <button
        :class="['tab-btn', { active: activeTab === 'submits' }]"
        @click="activeTab = 'submits'"
      >
        任务申请
        <span v-if="pendingCount > 0" class="badge">{{ pendingCount }}</span>
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'products' }]"
        @click="activeTab = 'products'"
      >
        商品兑换
        <span v-if="exchangePendingCount > 0" class="badge">{{ exchangePendingCount }}</span>
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <template v-else>
      <div v-if="activeTab === 'submits'">
        <div v-if="submits.length === 0" class="empty-state">
          <p>暂无待处理的申请</p>
        </div>
        <div v-else class="apply-list">
          <div v-for="submit in submits" :key="submit.id" class="apply-card">
            <div class="apply-header">
              <div class="apply-user">{{ submit.userNickname || '用户' + submit.userId }}</div>
              <span :class="['status-badge', 'status-' + submit.status]">
                {{ getStatusText(submit.status) }}
              </span>
            </div>
            <div class="apply-content">
              <div class="apply-title">{{ submit.taskTitle }}</div>
              <div class="apply-meta">
                <span class="apply-reward">+{{ submit.rewardPoints }} 积分</span>
                <span class="apply-time">{{ formatDate(submit.createTime) }}</span>
              </div>
            </div>
            <div v-if="submit.status === 2 || submit.status === '2'" class="apply-actions">
              <input
                v-model="reviewComment"
                type="text"
                placeholder="审核备注（可选）"
                class="review-input"
              />
              <button class="btn btn-success" @click="reviewSubmit(submit.id, 1)">
                通过
              </button>
              <button class="btn btn-danger" @click="reviewSubmit(submit.id, 3)">
                拒绝
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <div v-if="exchangeApplies.length === 0" class="empty-state">
          <p>暂无待处理的兑换申请</p>
        </div>
        <div v-else class="apply-list">
          <div v-for="apply in exchangeApplies" :key="apply.id" class="apply-card">
            <div class="apply-header">
              <div class="apply-user">{{ apply.userNickname || '用户' + apply.userId }}</div>
              <span :class="['status-badge', 'status-' + apply.status]">
                {{ getStatusText(apply.status) }}
              </span>
            </div>
            <div class="apply-content">
              <div class="apply-title">{{ apply.productName }}</div>
              <div class="apply-meta">
                <span class="apply-price">-{{ apply.price }} 积分</span>
                <span class="apply-time">{{ formatDate(apply.createTime) }}</span>
              </div>
            </div>
            <div v-if="apply.status === 2 || apply.status === '2'" class="apply-actions">
              <input
                v-model="reviewComment"
                type="text"
                placeholder="审核备注（可选）"
                class="review-input"
              />
              <button class="btn btn-success" @click="reviewExchange(apply.id, 1)">
                通过
              </button>
              <button class="btn btn-danger" @click="reviewExchange(apply.id, 3)">
                拒绝
              </button>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { taskAPI, mallAPI } from '../api';
import { formatDate } from '../utils';

export default {
  name: 'AdminTaskReview',
  data() {
    return {
      submits: [],
      exchangeApplies: [],
      loading: true,
      activeTab: 'submits',
      reviewComment: ''
    };
  },
  computed: {
    pendingCount() {
      return this.submits.filter(s => s.status === 2 || s.status === '2').length;
    },
    exchangePendingCount() {
      return this.exchangeApplies.filter(a => a.status === 2 || a.status === '2').length;
    }
  },
  methods: {
    formatDate,
    getStatusText(status) {
      const numStatus = Number(status);
      const statusMap = {
        1: '已通过',
        2: '待审核',
        3: '已拒绝'
      };
      return statusMap[numStatus] || status;
    },
    async loadSubmits() {
      try {
        const res = await taskAPI.getSubmitList().catch(() => ({ data: [] }));
        this.submits = res.data || [];
      } catch (error) {
        console.error('加载申请列表失败:', error);
      }
    },
    async loadExchangeApplies() {
      try {
        const res = await mallAPI.getApplyList().catch(() => ({ data: [] }));
        this.exchangeApplies = res.data || [];
      } catch (error) {
        console.error('加载兑换申请列表失败:', error);
      }
    },
    async reviewSubmit(id, status) {
      try {
        await taskAPI.reviewSubmit(id, {
          status,
          reviewComment: this.reviewComment
        });
        this.reviewComment = '';
        await this.loadSubmits();
      } catch (error) {
        alert(error.message || '审核失败');
      }
    },
    async reviewExchange(id, status) {
      try {
        await mallAPI.reviewApply(id, {
          status,
          reviewComment: this.reviewComment
        });
        this.reviewComment = '';
        await this.loadExchangeApplies();
      } catch (error) {
        alert(error.message || '审核失败');
      }
    }
  },
  async mounted() {
    this.loading = true;
    await Promise.all([
      this.loadSubmits(),
      this.loadExchangeApplies()
    ]);
    this.loading = false;
  }
}
</script>

<style scoped>
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
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.tab-btn:hover {
  background: #f5f5f5;
}

.tab-btn.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.badge {
  background: #e74c3c;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.apply-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.apply-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.apply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.apply-user {
  font-size: 14px;
  color: #666;
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
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

.apply-title {
  font-size: 16px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 8px;
}

.apply-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #999;
  margin-bottom: 16px;
}

.apply-reward {
  color: #2ecc71;
  font-weight: 500;
}

.apply-price {
  color: #e74c3c;
  font-weight: 500;
}

.apply-actions {
  display: flex;
  gap: 10px;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #eee;
}

.review-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.review-input:focus {
  outline: none;
  border-color: #667eea;
}

@media (max-width: 768px) {
  .tabs {
    flex-direction: column;
  }

  .apply-actions {
    flex-direction: column;
  }

  .review-input {
    width: 100%;
  }

  .apply-actions .btn {
    width: 100%;
  }
}
</style>
