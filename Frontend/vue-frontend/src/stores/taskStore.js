import { defineStore } from 'pinia';
import { taskService } from '../services/taskService';

export const useTaskStore = defineStore('task', {
  state: () => ({
    dailyTasks: [],
    stageTasks: [],
    currentTask: null,
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchDailyTasks() {
      this.loading = true;
      try {
        const response = await taskService.getDailyTasks();
        this.dailyTasks = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取每日任务失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },
    
    async fetchStageTasks() {
      this.loading = true;
      try {
        const response = await taskService.getStageTasks();
        this.stageTasks = response.data;
        this.error = null;
      } catch (err) {
        this.error = '获取阶段任务失败';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },
    
    async createTask(taskData) {
      this.loading = true;
      try {
        const response = await taskService.createTask(taskData);
        if (taskData.type === 'daily') {
          this.dailyTasks.push(response.data);
        } else {
          this.stageTasks.push(response.data);
        }
        this.error = null;
        return response.data;
      } catch (err) {
        this.error = '创建任务失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    async completeTask(taskId) {
      this.loading = true;
      try {
        await taskService.completeTask(taskId);
        // 更新任务状态
        const dailyTaskIndex = this.dailyTasks.findIndex(task => task.id === taskId);
        if (dailyTaskIndex !== -1) {
          this.dailyTasks[dailyTaskIndex].completed = true;
        }
        const stageTaskIndex = this.stageTasks.findIndex(task => task.id === taskId);
        if (stageTaskIndex !== -1) {
          this.stageTasks[stageTaskIndex].completed = true;
        }
        this.error = null;
      } catch (err) {
        this.error = '完成任务失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    async fetchTaskDetail(taskId) {
      this.loading = true;
      try {
        const response = await taskService.getTaskDetail(taskId);
        this.currentTask = response.data;
        this.error = null;
        return response.data;
      } catch (err) {
        this.error = '获取任务详情失败';
        console.error(err);
        throw err;
      } finally {
        this.loading = false;
      }
    }
  }
});