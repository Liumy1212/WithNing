<template>
  <div class="modal-overlay" v-if="visible" @click="handleOverlayClick">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>上架商品</h2>
        <button class="close-btn" @click="handleClose">&times;</button>
      </div>
      <form @submit.prevent="handleSubmit" class="modal-form">
        <div class="form-group">
          <label>商品名称</label>
          <input type="text" v-model="formData.name" required>
        </div>
        <div class="form-group">
          <label>商品描述</label>
          <textarea v-model="formData.description" required></textarea>
        </div>
        <div class="form-group">
          <label>商品价格（积分）</label>
          <input type="number" v-model="formData.price" required min="1">
        </div>
        <div class="form-group">
          <label>商品图片URL</label>
          <input type="url" v-model="formData.imageUrl" required>
        </div>
        <div class="form-group">
          <label>商品库存</label>
          <input type="number" v-model="formData.stock" required min="0">
        </div>
        <div class="form-actions">
          <button type="button" class="cancel-btn" @click="handleClose">取消</button>
          <button type="submit" class="submit-btn" :disabled="loading">
            {{ loading ? '上架中...' : '上架商品' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['close', 'submit']);

const loading = ref(false);
const formData = ref({
  name: '',
  description: '',
  price: 10,
  imageUrl: '',
  stock: 10
});

watch(() => props.visible, (newVal) => {
  if (newVal) {
    resetForm();
  }
});

const resetForm = () => {
  formData.value = {
    name: '',
    description: '',
    price: 10,
    imageUrl: '',
    stock: 10
  };
};

const handleClose = () => {
  emit('close');
};

const handleOverlayClick = () => {
  handleClose();
};

const handleSubmit = async () => {
  loading.value = true;
  try {
    emit('submit', { ...formData.value });
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
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
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
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
  font-size: 20px;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  color: #999;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.close-btn:hover {
  color: #333;
}

.modal-form {
  padding: 20px;
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
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 30px;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.cancel-btn:hover {
  background: #e0e0e0;
}

.submit-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.submit-btn:hover {
  background: #764ba2;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

@media (max-width: 480px) {
  .modal-content {
    max-width: 100%;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .cancel-btn,
  .submit-btn {
    width: 100%;
  }
}
</style>