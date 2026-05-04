<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">积分商城</h1>
      <button v-if="isAdmin" class="btn btn-primary" @click="showAddModal = true">
        <span class="btn-icon">+</span>
        添加商品
      </button>
    </div>

    <div class="tabs">
      <button
        :class="['tab-btn', { active: activeTab === 'products' }]"
        @click="activeTab = 'products'"
      >
        <span class="tab-icon">🛍️</span>
        商品列表
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'my-applies' }]"
        @click="activeTab = 'my-applies'"
      >
        <span class="tab-icon">📋</span>
        我的兑换
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'records' }]"
        @click="activeTab = 'records'"
      >
        <span class="tab-icon">📜</span>
        兑换记录
      </button>
    </div>

    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
    <template v-else>
      <div v-if="activeTab === 'products'">
        <div v-if="products.length === 0" class="empty-state">
          <div class="empty-icon">🛍️</div>
          <p>暂无商品</p>
        </div>
        <div v-else class="product-grid">
          <div v-for="product in products" :key="product.id" class="product-card">
            <div v-if="product.imageUrl" class="product-image">
              <img :src="product.imageUrl" :alt="product.name" />
            </div>
            <div v-else class="product-image product-image-placeholder">
              <div class="product-icon">🎁</div>
            </div>
            <div class="product-content">
              <div class="product-header">
                <h3 class="product-name">{{ product.name }}</h3>
                <span v-if="product.status === 2" class="offline-badge">已下架</span>
              </div>
              <p class="product-desc">{{ product.description }}</p>
              <div class="product-meta">
                <span class="product-price">
                  <span class="price-icon">⭐</span>
                  {{ product.price }} 积分
                </span>
                <span class="product-stock">
                  库存: <span class="stock-num">{{ product.stock }}</span>
                </span>
              </div>
              <div class="product-actions">
                <button
                  v-if="isAdmin"
                  class="btn btn-danger btn-sm"
                  @click="removeProduct(product.id)"
                >
                  删除
                </button>
                <button
                  v-if="product.status === 1"
                  class="btn btn-primary"
                  @click="applyExchange(product)"
                  :disabled="product.stock <= 0"
                >
                  {{ product.stock <= 0 ? '已售罄' : '立即兑换' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="activeTab === 'my-applies'">
        <div v-if="myApplies.length === 0" class="empty-state">
          <div class="empty-icon">📋</div>
          <p>暂无兑换申请</p>
        </div>
        <div v-else class="apply-list">
          <div v-for="apply in myApplies" :key="apply.id" class="apply-card">
            <div class="apply-header">
              <div class="apply-title">{{ apply.productName }}</div>
              <span :class="['status-badge', 'status-' + apply.status]">
                {{ getStatusText(apply.status) }}
              </span>
            </div>
            <div class="apply-meta">
              <span class="apply-price">
                <span class="price-icon">⭐</span>
                -{{ apply.price }} 积分
              </span>
              <span class="apply-time">
                <span class="time-icon">📅</span>
                {{ formatDate(apply.createTime) }}
              </span>
            </div>
            <div v-if="apply.reviewComment" class="apply-comment">
              <span class="comment-label">审核备注：</span>
              {{ apply.reviewComment }}
            </div>
            <div v-if="apply.reviewTime" class="apply-review-time">
              <span class="time-icon">🕐</span>
              审核时间：{{ formatDate(apply.reviewTime) }}
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <div v-if="records.length === 0" class="empty-state">
          <div class="empty-icon">📜</div>
          <p>暂无兑换记录</p>
        </div>
        <div v-else class="record-list">
          <div v-for="record in records" :key="record.id" class="record-card">
            <div class="record-header">
              <div class="record-title">{{ record.productName }}</div>
            </div>
            <div class="record-meta">
              <span class="record-price">
                <span class="price-icon">⭐</span>
                -{{ record.price }} 积分
              </span>
              <span class="record-time">
                <span class="time-icon">📅</span>
                {{ formatDate(record.exchangeTime) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-if="showAddModal" class="modal-overlay" @click.self="showAddModal = false">
      <div class="modal">
        <h2 class="modal-title">
          <span class="modal-title-icon">+</span>
          添加商品
        </h2>
        <form @submit.prevent="addProduct">
          <div class="form-group">
            <label>商品名称</label>
            <input
              v-model="newProduct.name"
              type="text"
              placeholder="请输入商品名称"
              required
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>商品描述</label>
            <textarea
              v-model="newProduct.description"
              placeholder="请输入商品描述"
              required
              class="form-textarea"
              rows="4"
            ></textarea>
          </div>
          <div class="form-group">
            <label>所需积分</label>
            <input
              v-model.number="newProduct.price"
              type="number"
              min="1"
              required
              class="form-input"
              placeholder="请输入所需积分"
            />
          </div>
          <div class="form-group">
            <label>库存数量</label>
            <input
              v-model.number="newProduct.stock"
              type="number"
              min="0"
              required
              class="form-input"
              placeholder="请输入库存数量"
            />
          </div>
          <div class="form-group">
            <label>图片URL（可选）</label>
            <input
              v-model="newProduct.imageUrl"
              type="url"
              placeholder="请输入图片地址"
              class="form-input"
            />
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="showAddModal = false">
              取消
            </button>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              {{ submitting ? '添加中...' : '添加商品' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { mallAPI } from '../api';
import store from '../store';
import { formatDate } from '../utils';

export default {
  name: 'MallPage',
  data() {
    return {
      products: [],
      myApplies: [],
      records: [],
      loading: true,
      activeTab: 'products',
      showAddModal: false,
      submitting: false,
      newProduct: {
        name: '',
        description: '',
        price: 0,
        stock: 0,
        imageUrl: ''
      }
    };
  },
  computed: {
    isAdmin() {
      return store.isAdmin();
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
    async loadProducts() {
      try {
        const res = await mallAPI.getProducts().catch(() => ({ data: [] }));
        this.products = res.data || [];
      } catch (error) {
        console.error('加载商品失败:', error);
      }
    },
    async loadMyApplies() {
      try {
        const res = await mallAPI.getMyApplies().catch(() => ({ data: [] }));
        this.myApplies = res.data || [];
      } catch (error) {
        console.error('加载兑换申请失败:', error);
      }
    },
    async loadRecords() {
      try {
        const res = await mallAPI.getExchangeRecords().catch(() => ({ data: [] }));
        this.records = res.data || [];
      } catch (error) {
        console.error('加载兑换记录失败:', error);
      }
    },
    async applyExchange(product) {
      if (!confirm(`确定要兑换「${product.name}」吗？需要消耗 ${product.price} 积分。`)) return;
      try {
        await mallAPI.applyExchange(product.id);
        alert('兑换申请已提交，等待管理员审核');
        await this.loadMyApplies();
      } catch (error) {
        alert(error.message || '兑换失败');
      }
    },
    async removeProduct(productId) {
      if (!confirm('确定要删除这个商品吗？')) return;
      try {
        await mallAPI.removeProduct(productId);
        await this.loadProducts();
      } catch (error) {
        alert(error.message || '删除失败');
      }
    },
    async addProduct() {
      this.submitting = true;
      try {
        await mallAPI.addProduct(this.newProduct);
        this.showAddModal = false;
        this.newProduct = {
          name: '',
          description: '',
          price: 0,
          stock: 0,
          imageUrl: ''
        };
        await this.loadProducts();
      } catch (error) {
        alert(error.message || '添加失败');
      } finally {
        this.submitting = false;
      }
    }
  },
  async mounted() {
    this.loading = true;
    await Promise.all([
      this.loadProducts(),
      this.loadMyApplies(),
      this.loadRecords()
    ]);
    this.loading = false;
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

.btn-danger {
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
}

.btn-danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.4);
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

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.product-image {
  width: 100%;
  height: 180px;
  overflow: hidden;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.product-image-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-icon {
  font-size: 64px;
  opacity: 0.5;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-content {
  padding: 20px;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.product-name {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.offline-badge {
  background: #e9ecef;
  color: #6c757d;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.product-desc {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 42px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  margin-bottom: 16px;
}

.product-price {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #f39c12;
  font-weight: 600;
  font-size: 16px;
}

.price-icon {
  font-size: 16px;
}

.product-stock {
  color: #999;
}

.stock-num {
  font-weight: 500;
  color: #666;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.apply-list,
.record-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.apply-card,
.record-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.apply-header,
.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.apply-title,
.record-title {
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

.apply-meta,
.record-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.apply-price,
.record-price {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #e74c3c;
  font-weight: 600;
}

.apply-time,
.record-time {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #999;
}

.time-icon {
  font-size: 14px;
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
  display: flex;
  align-items: center;
  gap: 6px;
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
.form-textarea {
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
.form-textarea:focus {
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

  .product-grid {
    grid-template-columns: 1fr;
  }

  .modal {
    padding: 24px;
  }
}
</style>
