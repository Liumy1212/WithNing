import { defineStore } from 'pinia';
import { mallService } from '../services/mallService';

export const useMallStore = defineStore('mall', {
  state: () => ({
    products: [],
    currentProduct: null,
    exchangeRecords: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchProducts() {
      this.loading = true;
      try {
        const response = await mallService.getProducts();
        this.products = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取商品列表失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },
    
    async fetchProductDetail(productId) {
      this.loading = true;
      try {
        const response = await mallService.getProductDetail(productId);
        this.currentProduct = response.data;
        this.error = null;
        return response.data;
      } catch (err) {
        this.error = '获取商品详情失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    async exchangeProduct(productId) {
      this.loading = true;
      try {
        const response = await mallService.exchangeProduct(productId);
        this.exchangeRecords.push(response.data);
        this.error = null;
        return response.data;
      } catch (err) {
        this.error = '兑换商品失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    async fetchExchangeRecords() {
      this.loading = true;
      try {
        const response = await mallService.getExchangeRecords();
        this.exchangeRecords = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取兑换记录失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    }
  }
});