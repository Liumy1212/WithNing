import { reactive } from 'vue';

const STORAGE_KEY = 'userInfo';

const state = reactive({
  userInfo: null,
  initialized: false
});

const initStore = () => {
  if (!state.initialized) {
    const info = localStorage.getItem(STORAGE_KEY);
    state.userInfo = info ? JSON.parse(info) : null;
    state.initialized = true;
  }
};

const store = {
  saveUserInfo(userInfo) {
    state.userInfo = userInfo;
    localStorage.setItem(STORAGE_KEY, JSON.stringify(userInfo));
  },
  getUserInfo() {
    initStore();
    return state.userInfo;
  },
  clearUserInfo() {
    state.userInfo = null;
    localStorage.removeItem(STORAGE_KEY);
  },
  isLoggedIn() {
    initStore();
    return !!state.userInfo;
  },
  isAdmin() {
    initStore();
    return state.userInfo && (state.userInfo.role === 'admin' || state.userInfo.role === 1 || state.userInfo.role === '1');
  },
  get state() {
    return state;
  }
};

initStore();

export default store;
