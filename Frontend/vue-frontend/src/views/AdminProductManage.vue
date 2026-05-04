<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">商品管理</h1>
      <button class="btn btn-primary" @click="showAddModal = true">
        添加商品
      </button>
    </div>

    <div class="tabs">
      <button
        :class="['tab-btn', { active: activeTab === 'products' }]"
        @click="activeTab = 'products'"
      >
        商品列表
      </button>
      <button
        :class="['tab-btn', { active: activeTab === 'all' }]"
        @click="activeTab = 'all'"
      >
        全部商品
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <template v-else>
      <div v-if="activeTab === 'products'">
        <div v-if="onlineProducts.length === 0" class="empty-state">
          <p>暂无上架商品</p>
        </div>
        <div v-else class="product-grid">
          <div v-for="product in onlineProducts" :key="product.id" class="product-card">
            <div v-if="product.imageUrl" class="product-image">
              <img :src="product.imageUrl" :alt="product.name" />
            </div>
            <div class="product-content">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-desc">{{ product.description }}</p>
              <div class="product-meta">
                <span class="product-price">⭐ {{ product.price }} 积分</span>
                <span class="product-stock">库存: {{ product.stock }}</span>
              </div>
              <div class="product-actions">
                <button class="btn btn-warning btn-sm" @click="offlineProduct(product.id)">
                  下架
                </button>
                <button class="btn btn-danger btn-sm" @click="removeProduct(product.id)">
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <div v-if="allProducts.length === 0" class="empty-state">
          <p>暂无商品</p>
        </div>
        <div v-else class="product-grid">
          <div v-for="product in allProducts" :key="product.id" class="product-card">
            <div v-if="product.imageUrl" class="product-image">
              <img :src="product.imageUrl" :alt="product.name" />
            </div>
            <div class="product-content">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-desc">{{ product.description }}</p>
              <div class="product-meta">
                <span class="product-price">⭐ {{ product.price }} 积分</span>
                <span :class="['product-status', product.status === 1 ? 'online' : 'offline']">
                  {{ product.status === 1 ? '上架中' : '已下架' }}
                </span>
              </div>
              <div class="product-actions">
                <button
                  v-if="product.status !== 1"
                  class="btn btn-success btn-sm"
                  @click="onlineProduct(product.id)"
                >
                  上架
                </button>
                <button
                  v-else
                  class="btn btn-warning btn-sm"
                  @click="offlineProduct(product.id)"
                >
                  下架
                </button>
                <button class="btn btn-danger btn-sm" @click="removeProduct(product.id)">
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-if="showAddModal" class="modal-overlay" @click.self="showAddModal = false">
      <div class="modal">
        <h2 class="modal-title">添加商品</h2>
        <form @submit.prevent="addProduct">
          <div class="form-group">
            <label>商品名称</label>
            <input v-model="newProduct.name" type="text" placeholder="请输入商品名称" required />
          </div>
          <div class="form-group">
            <label>商品描述</label>
            <textarea v-model="newProduct.description" placeholder="请输入商品描述" required></textarea>
          </div>
          <div class="form-group">
            <label>所需积分</label>
            <input v-model.number="newProduct.price" type="number" min="1" required />
          </div>
          <div class="form-group">
            <label>库存数量</label>
            <input v-model.number="newProduct.stock" type="number" min="0" required />
          </div>
          <div class="form-group">
            <label>图片URL（可选）</label>
            <input v-model="newProduct.imageUrl" type="url" placeholder="请输入图片地址" />
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="showAddModal = false">
              取消
            </button>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              {{ submitting ? '添加中...' : '添加并上架' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { mallAPI } from '../api';

export default {
  name: 'AdminProductManage',
  data() {
    return {
      allProducts: [],
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
    onlineProducts() {
      return this.allProducts.filter(p => p.status === 1 || p.status === '1');
    }
  },
  methods: {
    async loadProducts() {
      try {
        const res = await mallAPI.getProducts().catch(() => ({ data: [] }));
        this.allProducts = res.data || [];
      } catch (error) {
        console.error('加载商品失败:', error);
      }
    },
    async addProduct() {
      this.submitting = true;
      try {
        await mallAPI.addProduct(this.newProduct);
        await this.loadProducts();
        this.showAddModal = false;
        this.newProduct = {
          name: '',
          description: '',
          price: 0,
          stock: 0,
          imageUrl: ''
        };
      } catch (error) {
        alert(error.message || '添加失败');
      } finally {
        this.submitting = false;
      }
    },
    async onlineProduct(productId) {
      try {
        await mallAPI.onlineProduct(productId);
        await this.loadProducts();
      } catch (error) {
        alert(error.message || '操作失败');
      }
    },
    async offlineProduct(productId) {
      try {
        await mallAPI.offlineProduct(productId);
        await this.loadProducts();
      } catch (error) {
        alert(error.message || '操作失败');
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
    }
  },
  mounted() {
    this.loadProducts();
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

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.product-image {
  width: 100%;
  height: 180px;
  overflow: hidden;
  background: #f5f5f5;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-content {
  padding: 20px;
}

.product-name {
  font-size: 18px;
  color: #2c3e50;
  margin-bottom: 8px;
}

.product-desc {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 16px;
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
  color: #f39c12;
  font-weight: 600;
}

.product-status {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.product-status.online {
  background: #e8f8f0;
  color: #2ecc71;
}

.product-status.offline {
  background: #f5f5f5;
  color: #999;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 13px;
}

.btn-warning {
  background-color: #f39c12;
  color: white;
}

.btn-warning:hover {
  background-color: #d68910;
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

  .tabs {
    flex-direction: column;
  }

  .product-grid {
    grid-template-columns: 1fr;
  }
}
</style>
