<template>
  <div class="admin-view">
    <h1>管理员控制台</h1>
    
    <div class="admin-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.id" 
        :class="{ active: activeTab === tab.id }"
        @click="activeTab = tab.id"
      >
        {{ tab.name }}
      </button>
    </div>
    
    <!-- 任务发布 -->
    <div v-if="activeTab === 'tasks'" class="tab-content">
      <div class="section-header">
        <h2>发布新任务</h2>
      </div>
      <form @submit.prevent="handleCreateTask" class="task-form">
        <div class="form-row">
          <div class="form-group">
            <label>任务类型</label>
            <select v-model="taskForm.type" required>
              <option value="daily">每日任务</option>
              <option value="stage">阶段任务</option>
            </select>
          </div>
          <div class="form-group">
            <label>奖励积分</label>
            <input type="number" v-model="taskForm.rewardPoints" required min="1">
          </div>
        </div>
        <div class="form-group">
          <label>任务标题</label>
          <input type="text" v-model="taskForm.title" required>
        </div>
        <div class="form-group">
          <label>任务描述</label>
          <textarea v-model="taskForm.description" required></textarea>
        </div>
        <div class="form-group" v-if="taskForm.type === 'stage'">
          <label>任务期限</label>
          <input type="date" v-model="taskForm.deadline" required>
        </div>
        <div class="form-group">
          <label>任务图片（可选）</label>
          <input type="url" v-model="taskForm.imageUrl" placeholder="请输入图片URL">
        </div>
        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '发布中...' : '发布任务' }}
        </button>
      </form>
    </div>
    
    <!-- 审核申请 -->
    <div v-if="activeTab === 'reviews'" class="tab-content">
      <div class="section-header">
        <h2>任务申请审核</h2>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="review-list">
        <div v-for="submit in taskSubmits" :key="submit.id" class="review-item">
          <div class="review-image" v-if="submit.imageUrl">
            <img :src="submit.imageUrl" :alt="submit.title">
          </div>
          <div class="review-info">
            <h3>{{ submit.title }}</h3>
            <p>{{ submit.description }}</p>
            <div class="review-meta">
              <span>申请人: {{ submit.userNickname }}</span>
              <span>奖励积分: {{ submit.rewardPoints }}</span>
              <span>类型: {{ submit.type === 'daily' ? '每日任务' : '阶段任务' }}</span>
              <span>期限: {{ submit.deadline || '无' }}</span>
              <span>申请时间: {{ submit.createTime }}</span>
            </div>
          </div>
          <div class="review-actions">
            <button class="approve-btn" @click="handleReview(submit.id, 'approved')">通过</button>
            <button class="reject-btn" @click="handleReview(submit.id, 'rejected')">拒绝</button>
          </div>
        </div>
        <div v-if="taskSubmits.length === 0" class="empty">暂无待审核申请</div>
      </div>
    </div>
    
    <!-- 商品管理 -->
    <div v-if="activeTab === 'products'" class="tab-content">
      <div class="section-header">
        <h2>商品管理</h2>
        <button class="add-btn" @click="showProductModal = true">+ 添加商品</button>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="products-grid">
        <div v-for="product in products" :key="product.id" class="product-card">
          <div class="product-image">
            <img v-if="product.imageUrl" :src="product.imageUrl" :alt="product.name" />
            <div v-else class="no-image">暂无图片</div>
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
              <button v-if="product.status === 'active'" class="offline-btn" @click="handleOffline(product.id)">下架</button>
              <button v-else class="online-btn" @click="handleOnline(product.id)">上架</button>
              <button class="delete-btn" @click="handleDelete(product.id)">删除</button>
            </div>
          </div>
        </div>
        <div v-if="products.length === 0" class="empty">暂无商品</div>
      </div>
    </div>
    
    <!-- 商品表单弹窗 -->
    <div v-if="showProductModal" class="modal-overlay" @click="showProductModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>添加商品</h2>
          <button class="close-btn" @click="showProductModal = false">&times;</button>
        </div>
        <form @submit.prevent="handleAddProduct" class="modal-form">
          <div class="form-group">
            <label>商品名称</label>
            <input type="text" v-model="productForm.name" required>
          </div>
          <div class="form-group">
            <label>商品描述</label>
            <textarea v-model="productForm.description" required></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>价格（积分）</label>
              <input type="number" v-model="productForm.price" required min="1">
            </div>
            <div class="form-group">
              <label>库存</label>
              <input type="number" v-model="productForm.stock" required min="0">
            </div>
          </div>
          <div class="form-group">
            <label>图片URL（可选）</label>
            <input type="url" v-model="productForm.imageUrl" placeholder="请输入图片URL">
          </div>
          <div class="form-actions">
            <button type="button" class="cancel-btn" @click="showProductModal = false">取消</button>
            <button type="submit" class="submit-btn" :disabled="loading">添加</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useTaskStore } from '../stores/taskStore';
import { useMallStore } from '../stores/mallStore';

const taskStore = useTaskStore();
const mallStore = useMallStore();

const tabs = [
  { id: 'tasks', name: '发布任务' },
  { id: 'reviews', name: '审核申请' },
  { id: 'products', name: '商品管理' }
];

const activeTab = ref('tasks');
const loading = ref(false);
const error = ref(null);
const taskSubmits = ref([]);
const products = ref([]);

const showProductModal = ref(false);

const taskForm = ref({
  type: 'daily',
  title: '',
  description: '',
  rewardPoints: 10,
  deadline: '',
  imageUrl: ''
});

const productForm = ref({
  name: '',
  description: '',
  price: 10,
  stock: 10,
  imageUrl: ''
});

onMounted(() => {
  loadData();
});

const loadData = async () => {
  await Promise.all([
    fetchTaskSubmits(),
    fetchProducts()
  ]);
};

const fetchTaskSubmits = async () => {
  try {
    const response = await taskStore.getTaskSubmits();
    taskSubmits.value = response.data || [];
  } catch (err) {
    console.error('获取任务申请失败', err);
  }
};

const fetchProducts = async () => {
  try {
    await mallStore.fetchProducts();
    products.value = mallStore.products;
  } catch (err) {
    console.error('获取商品列表失败', err);
  }
};

const handleCreateTask = async () => {
  loading.value = true;
  error.value = null;
  try {
    const data = { ...taskForm.value };
    if (!data.imageUrl) {
      delete data.imageUrl;
    }
    await taskStore.createTask(data);
    taskForm.value = {
      type: 'daily',
      title: '',
      description: '',
      rewardPoints: 10,
      deadline: '',
      imageUrl: ''
    };
    alert('任务发布成功');
  } catch (err) {
    error.value = '发布失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const handleReview = async (submitId, status) => {
  loading.value = true;
  try {
    await taskStore.reviewTask(submitId, { status });
    await fetchTaskSubmits();
  } catch (err) {
    error.value = '审核失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const handleAddProduct = async () => {
  loading.value = true;
  try {
    const data = { ...productForm.value };
    if (!data.imageUrl) {
      delete data.imageUrl;
    }
    await mallStore.addProduct(data);
    showProductModal.value = false;
    productForm.value = {
      name: '',
      description: '',
      price: 10,
      stock: 10,
      imageUrl: ''
    };
    await fetchProducts();
  } catch (err) {
    error.value = '添加失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const handleOffline = async (productId) => {
  try {
    await mallStore.offlineProduct(productId);
    await fetchProducts();
  } catch (err) {
    error.value = '下架失败';
    console.error(err);
  }
};

const handleOnline = async (productId) => {
  try {
    await mallStore.onlineProduct(productId);
    await fetchProducts();
  } catch (err) {
    error.value = '上架失败';
    console.error(err);
  }
};

const handleDelete = async (productId) => {
  if (!confirm('确定要删除这个商品吗？')) return;
  try {
    await mallStore.deleteProduct(productId);
    await fetchProducts();
  } catch (err) {
    error.value = '删除失败';
    console.error(err);
  }
};
</script>

<style scoped>
.admin-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.admin-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
}

.admin-tabs button {
  background: none;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.admin-tabs button:hover {
  background: #f5f5f5;
}

.admin-tabs button.active {
  background: #667eea;
  color: white;
}

.tab-content {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  margin: 0;
  color: #333;
}

.add-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-btn:hover {
  background: #764ba2;
}

.task-form {
  max-width: 600px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.submit-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background: #764ba2;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.review-list,
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.review-item,
.product-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s ease;
}

.review-item:hover,
.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.review-image {
  width: 100%;
  height: 120px;
  overflow: hidden;
  border-radius: 6px;
  margin-bottom: 15px;
}

.review-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.review-info h3,
.product-info h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.review-info p,
.product-info p {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.5;
}

.review-meta {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 14px;
  color: #888;
  margin-bottom: 15px;
}

.review-actions {
  display: flex;
  gap: 10px;
}

.approve-btn,
.reject-btn,
.offline-btn,
.online-btn,
.delete-btn {
  flex: 1;
  border: none;
  padding: 10px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.approve-btn {
  background: #4CAF50;
  color: white;
}

.approve-btn:hover {
  background: #45a049;
}

.reject-btn {
  background: #f44336;
  color: white;
}

.reject-btn:hover {
  background: #d32f2f;
}

.offline-btn {
  background: #f59e0b;
  color: white;
}

.offline-btn:hover {
  background: #d97706;
}

.online-btn {
  background: #4CAF50;
  color: white;
}

.online-btn:hover {
  background: #45a049;
}

.delete-btn {
  background: #f44336;
  color: white;
}

.delete-btn:hover {
  background: #d32f2f;
}

.product-image {
  height: 150px;
  overflow: hidden;
  border-radius: 6px;
  margin-bottom: 15px;
  position: relative;
  background: #f5f5f5;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #aaa;
  font-size: 14px;
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

.product-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.price {
  font-size: 18px;
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

.loading,
.error,
.empty {
  text-align: center;
  padding: 40px;
  color: #666;
  background: #f5f5f5;
  border-radius: 8px;
}

.error {
  color: #f44336;
  background: #ffebee;
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
  z-index: 2000;
  padding: 20px;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.modal-header h2 {
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  color: #999;
  cursor: pointer;
}

.modal-form {
  padding: 20px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 20px;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
}

.cancel-btn:hover {
  background: #e0e0e0;
}

@media (max-width: 768px) {
  .admin-view {
    padding: 10px;
  }
  
  .admin-tabs {
    flex-direction: column;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .review-list,
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .review-actions,
  .product-actions {
    flex-direction: column;
  }
}
</style>