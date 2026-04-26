import { defineStore } from 'pinia';
import { pointService } from '../services/pointService';

export const usePointStore = defineStore('point', {
  state: () => ({
    userPoints: 0,
    pointHistory: [],
    pointRecords: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchUserPoints() {
      this.loading = true;
      try {
        const response = await pointService.getUserPoints();
        this.userPoints = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取用户积分失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },
    
    async fetchPointHistory() {
      this.loading = true;
      try {
        const response = await pointService.getPointHistory();
        this.pointHistory = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取积分历史失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },
    
    async fetchPointRecords() {
      this.loading = true;
      try {
        const response = await pointService.getPointRecords();
        this.pointRecords = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取积分记录失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    }
  }
});