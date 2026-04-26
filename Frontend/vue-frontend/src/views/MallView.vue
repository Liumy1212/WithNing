<template>
  <div class="mall-view">
    <div class="mall-header">
      <h1>积分商城</h1>
      <button v-if="isAdmin" class="add-product-btn" @click="openProductModal">
        + 上架商品
      </button>
    </div>
    
    <!-- 商品列表 -->
    <div class="products-section">
      <h2>商品列表</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="products-grid">
        <div v-for="product in mallStore.products" :key="product.id" class="product-card">
          <div class="product-image">
            <img :src="product.imageUrl" :alt="product.name" />
            <span v-if="product.status === 'inactive'" class="offline-tag">已下架</span>
          </div>
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p>{{ product.description }}</p>
            <div class="product-meta">
              <span class="price">{{ product.price }} 积分</span>
              <span class="stock">库存: {{ product.stock }}</span>
            </div>
            <div class="product-actions">
              <button v-if="isAdmin && product.status === 'active'" @click="handleOffline(product.id)" class="offline-btn">
                下架
              </button>
              <button v-else-if="!isAdmin && product.status === 'active'" @click="exchangeProduct(product.id)" class="exchange-btn" :disabled="loading">
                兑换商品
              </button>
            </div>
          </div>
        </div>
        <div v-if="mallStore.products.length === 0" class="empty">暂无商品</div>
      </div>
    </div>
    
    <!-- 兑换记录 -->
    <div class="exchange-records">
      <h2>兑换记录</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="records-list">
        <div v-for="record in mallStore.exchangeRecords" :key="record.id" class="record-item">
          <div class="record-info">
            <h3>{{ record.productName }}</h3>
            <p>{{ record.exchangeTime }}</p>
          </div>
          <div class="record-price">{{ record.price }} 积分</div>
        </div>
        <div v-if="mallStore.exchangeRecords.length === 0" class="empty">暂无兑换记录</div>
      </div>
    </div>
    
    <!-- 商品表单弹窗 -->
    <ProductFormModal
      :visible="showProductModal"
      @close="closeProductModal"
      @submit="handleProductSubmit"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useMallStore } from '../stores/mallStore';
import { useUserStore } from '../stores/userStore';
import ProductFormModal from '../components/ProductFormModal.vue';

const mallStore = useMallStore();
const userStore = useUserStore();

const showProductModal = ref(false);
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
    await mallStore.fetchProducts();
    await mallStore.fetchExchangeRecords();
  } catch (err) {
    error.value = '加载数据失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const openProductModal = () => {
  showProductModal.value = true;
};

const closeProductModal = () => {
  showProductModal.value = false;
};

const handleProductSubmit = async (formData) => {
  try {
    await mallStore.addProduct(formData);
    closeProductModal();
    await loadData();
  } catch (err) {
    console.error('上架商品失败', err);
    error.value = '上架商品失败';
  }
};

const handleOffline = async (productId) => {
  try {
    await mallStore.offlineProduct(productId);
    await loadData();
  } catch (err) {
    console.error('下架商品失败', err);
    error.value = '下架商品失败';
  }
};

const exchangeProduct = async (productId) => {
  try {
    await mallStore.exchangeProduct(productId);
    await loadData();
  } catch (err) {
    console.error('兑换商品失败', err);
    error.value = '兑换商品失败';
  }
};
</script>

<style scoped>
.mall-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.mall-header {
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

.add-product-btn {
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

.add-product-btn:hover {
  background: #764ba2;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(102, 126, 234, 0.4);
}

.products-section {
  margin-bottom: 40px;
}

.products-section h2 {
  margin-bottom: 20px;
  color: #333;
  border-bottom: 2px solid #667eea;
  padding-bottom: 10px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.product-image {
  height: 200px;
  overflow: hidden;
  position: relative;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.1);
}

.offline-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 12px;
}

.product-info {
  padding: 20px;
}

.product-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 18px;
}

.product-info p {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.5;
  font-size: 14px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: #667eea;
}

.stock {
  font-size: 14px;
  color: #888;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.product-actions button {
  flex: 1;
  border: none;
  padding: 10px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.exchange-btn {
  background: #667eea;
  color: white;
}

.exchange-btn:hover {
  background: #764ba2;
}

.offline-btn {
  background: #f59e0b;
  color: white;
}

.offline-btn:hover {
  background: #d97706;
}

.product-actions button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.exchange-records {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.exchange-records h2 {
  margin-bottom: 20px;
  color: #333;
  border-bottom: 2px solid #667eea;
  padding-bottom: 10px;
}

.records-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.record-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.record-info h3 {
  margin: 0 0 5px 0;
  color: #333;
  font-size: 16px;
}

.record-info p {
  margin: 0;
  color: #888;
  font-size: 14px;
}

.record-price {
  font-size: 18px;
  font-weight: bold;
  color: #667eea;
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
  .mall-view {
    padding: 10px;
  }
  
  .mall-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  h1 {
    font-size: 24px;
    text-align: center;
  }
  
  .add-product-btn {
    width: 100%;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .product-image {
    height: 180px;
  }
  
  .product-info {
    padding: 15px;
  }
  
  .product-info h3 {
    font-size: 16px;
  }
  
  .product-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .product-actions {
    flex-direction: column;
  }
  
  .exchange-records {
    padding: 20px;
  }
  
  .record-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .record-price {
    font-size: 16px;
  }
}
</style>