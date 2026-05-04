export const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

export const formatStatus = (status) => {
  const statusMap = {
    'approved': '已通过',
    'pending': '待审核',
    'rejected': '已拒绝',
    'active': '上架',
    'inactive': '下架'
  };
  return statusMap[status] || status;
};

export const formatTaskType = (type) => {
  return type === 'daily' ? '每日任务' : '阶段任务';
};

export const getStatusClass = (status) => {
  const classMap = {
    'approved': 'status-approved',
    'pending': 'status-pending',
    'rejected': 'status-rejected',
    'active': 'status-approved',
    'inactive': 'status-pending'
  };
  return classMap[status] || '';
};

export const isAdmin = () => {
  const userInfo = localStorage.getItem('userInfo');
  if (!userInfo) return false;
  return JSON.parse(userInfo).role === 'admin';
};
