import api from './api';

/**
 * 商城服务
 * 处理商城相关的API请求
 */
export const mallService = {
  /**
   * 获取商品列表
   * @returns {Promise} 返回商品列表
   */
  getProducts() {
    return api.get('/mall/products');
  },
  
  /**
   * 获取商品详情
   * @param {number} productId - 商品ID
   * @returns {Promise} 返回商品详情
   */
  getProductDetail(productId) {
    return api.get(`/mall/products/${productId}`);
  },
  
  /**
   * 上架商品（管理员）
   * @param {Object} productData - 商品数据
   * @param {string} productData.name - 商品名称
   * @param {string} productData.description - 商品描述
   * @param {number} productData.price - 商品价格
   * @param {string} [productData.imageUrl] - 商品图片URL（可选）
   * @param {number} productData.stock - 商品库存
   * @returns {Promise} 返回上架结果
   */
  addProduct(productData) {
    return api.post('/mall/products', productData);
  },
  
  /**
   * 下架商品（管理员）
   * @param {number} productId - 商品ID
   * @returns {Promise} 返回下架结果
   */
  offlineProduct(productId) {
    return api.put(`/mall/products/${productId}/offline`);
  },
  
  /**
   * 上架商品（管理员）
   * @param {number} productId - 商品ID
   * @returns {Promise} 返回上架结果
   */
  onlineProduct(productId) {
    return api.put(`/mall/products/${productId}/online`);
  },
  
  /**
   * 删除商品（管理员）
   * @param {number} productId - 商品ID
   * @returns {Promise} 返回删除结果
   */
  deleteProduct(productId) {
    return api.delete(`/mall/products/${productId}`);
  },
  
  /**
   * 兑换商品
   * @param {number} productId - 商品ID
   * @returns {Promise} 返回兑换结果
   */
  exchangeProduct(productId) {
    return api.post(`/mall/exchange/${productId}`);
  },
  
  /**
   * 获取兑换记录
   * @returns {Promise} 返回兑换记录列表
   */
  getExchangeRecords() {
    return api.get('/mall/exchange/records');
  }
};