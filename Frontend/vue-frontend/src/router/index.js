import { createRouter, createWebHistory } from 'vue-router';
import store from '../store';
import LoginPage from '../views/LoginPage.vue';
import RegisterPage from '../views/RegisterPage.vue';
import HomePage from '../views/HomePage.vue';
import TasksPage from '../views/TasksPage.vue';
import DailyTasks from '../views/DailyTasks.vue';
import StageTasks from '../views/StageTasks.vue';
import PointsPage from '../views/PointsPage.vue';
import MallPage from '../views/MallPage.vue';
import AdminTaskReview from '../views/AdminTaskReview.vue';
import AdminProductManage from '../views/AdminProductManage.vue';
import { adminAPI } from '../api';

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
    meta: { requiresAuth: false }
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage,
    meta: { requiresAuth: true }
  },
  {
    path: '/tasks',
    name: 'Tasks',
    component: TasksPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/tasks/daily',
    name: 'DailyTasks',
    component: DailyTasks,
    meta: { requiresAuth: true }
  },
  {
    path: '/tasks/stage',
    name: 'StageTasks',
    component: StageTasks,
    meta: { requiresAuth: true }
  },
  {
    path: '/points',
    name: 'Points',
    component: PointsPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/mall',
    name: 'Mall',
    component: MallPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/task-review',
    name: 'AdminTaskReview',
    component: AdminTaskReview,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/product-manage',
    name: 'AdminProductManage',
    component: AdminProductManage,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(async (to, from, next) => {
  const isLoggedIn = store.isLoggedIn();

  if (to.meta.requiresAuth && !isLoggedIn) {
    next({ name: 'Login' });
    return;
  }

  if ((to.name === 'Login' || to.name === 'Register') && isLoggedIn) {
    next({ name: 'Home' });
    return;
  }

  if (to.meta.requiresAdmin) {
    try {
      await adminAPI.checkAdmin();
      next();
    } catch (error) {
      alert('您没有管理员权限，无法访问该页面');
      next({ name: 'Home' });
    }
    return;
  }

  next();
});

export default router;
