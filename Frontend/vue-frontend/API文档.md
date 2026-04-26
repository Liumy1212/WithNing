# 打卡监督系统 API 接口文档

## 1. 用户认证模块

### 1.1 用户登录
- **接口路径**: `/api/auth/login`
- **请求方法**: `POST`
- **请求数据**:
  ```json
  {
    "username": "admin",
    "password": "123456"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "user": {
        "id": 1,
        "username": "admin",
        "nickname": "管理员",
        "role": "admin",
        "points": 1000
      }
    }
  }
  ```

### 1.2 获取当前用户信息
- **接口路径**: `/api/auth/currentUser`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "role": "admin",
      "points": 1000
    }
  }
  ```

### 1.3 用户注册
- **接口路径**: `/api/auth/register`
- **请求方法**: `POST`
- **请求数据**:
  ```json
  {
    "username": "newuser",
    "password": "123456",
    "nickname": "新用户",
    "email": "newuser@example.com",
    "phone": "13800138001"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "id": 2,
      "username": "newuser",
      "nickname": "新用户",
      "role": "normal",
      "points": 0
    }
  }
  ```

## 2. 任务模块

### 2.1 获取每日任务列表
- **接口路径**: `/api/tasks/daily`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "id": 1,
      "title": "每日打卡",
      "description": "完成每日打卡任务",
      "rewardPoints": 10,
      "completed": false,
      "createTime": "2026-04-26T00:00:00",
      "type": "daily",
      "status": "approved",
      "publisherId": 1
    }
  ]
  ```

### 2.2 获取阶段任务列表
- **接口路径**: `/api/tasks/stage`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "id": 2,
      "title": "周打卡挑战",
      "description": "完成一周的打卡任务",
      "rewardPoints": 50,
      "deadline": "2026-05-02T23:59:59",
      "completed": false,
      "createTime": "2026-04-26T00:00:00",
      "type": "stage",
      "status": "approved",
      "publisherId": 1
    }
  ]
  ```

### 2.3 发布任务（管理员）
- **接口路径**: `/api/tasks`
- **请求方法**: `POST`
- **请求数据**:
  ```json
  {
    "type": "daily",
    "title": "每日学习",
    "description": "每天学习1小时",
    "rewardPoints": 15,
    "deadline": null
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "发布成功",
    "data": {
      "id": 4,
      "title": "每日学习",
      "description": "每天学习1小时",
      "rewardPoints": 15,
      "completed": false,
      "createTime": "2026-04-26T12:00:00",
      "type": "daily",
      "status": "approved",
      "publisherId": 1
    }
  }
  ```

### 2.4 完成任务
- **接口路径**: `/api/tasks/{taskId}/complete`
- **请求方法**: `PUT`
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "任务完成成功"
  }
  ```

### 2.5 获取任务详情
- **接口路径**: `/api/tasks/{taskId}`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  {
    "id": 1,
    "title": "每日打卡",
    "description": "完成每日打卡任务",
    "rewardPoints": 10,
    "completed": false,
    "createTime": "2026-04-26T00:00:00",
    "type": "daily",
    "status": "approved",
    "publisherId": 1
  }
  ```

### 2.6 提交任务申请（普通用户）
- **接口路径**: `/api/tasks/submit`
- **请求方法**: `POST`
- **请求数据**:
  ```json
  {
    "type": "daily",
    "title": "每日运动",
    "description": "每天运动30分钟",
    "rewardPoints": 10,
    "deadline": null
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "提交成功，等待管理员审核",
    "data": {
      "id": 1,
      "userId": 2,
      "title": "每日运动",
      "description": "每天运动30分钟",
      "rewardPoints": 10,
      "type": "daily",
      "status": "pending",
      "createTime": "2026-04-26T12:00:00"
    }
  }
  ```

### 2.7 获取任务申请列表（管理员）
- **接口路径**: `/api/tasks/submits`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "id": 1,
      "userId": 2,
      "userNickname": "用户1",
      "title": "每日运动",
      "description": "每天运动30分钟",
      "rewardPoints": 10,
      "type": "daily",
      "status": "pending",
      "createTime": "2026-04-26T12:00:00"
    }
  ]
  ```

### 2.8 审核任务申请（管理员）
- **接口路径**: `/api/tasks/submit/{submitId}/review`
- **请求方法**: `PUT`
- **请求数据**:
  ```json
  {
    "status": "approved",
    "reviewComment": "审核通过"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "审核完成"
  }
  ```

## 3. 积分模块

### 3.1 获取用户积分
- **接口路径**: `/api/points`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  100
  ```

### 3.2 获取积分历史
- **接口路径**: `/api/points/history`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "date": "2026-04-26",
      "points": 10,
      "description": "完成每日打卡"
    }
  ]
  ```

### 3.3 获取积分记录
- **接口路径**: `/api/points/records`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "id": 1,
      "points": 10,
      "description": "完成每日打卡任务",
      "createTime": "2026-04-26T08:00:00"
    },
    {
      "id": 2,
      "points": -5,
      "description": "任务失败惩罚",
      "createTime": "2026-04-25T23:59:59"
    }
  ]
  ```

## 4. 商城模块

### 4.1 获取商品列表
- **接口路径**: `/api/mall/products`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "id": 1,
      "name": "积分兑换券",
      "description": "可兑换100积分",
      "price": 90,
      "imageUrl": "https://example.com/product1.jpg",
      "stock": 100,
      "status": "active",
      "publisherId": 1
    }
  ]
  ```

### 4.2 获取商品详情
- **接口路径**: `/api/mall/products/{productId}`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  {
    "id": 1,
    "name": "积分兑换券",
    "description": "可兑换100积分",
    "price": 90,
    "imageUrl": "https://example.com/product1.jpg",
    "stock": 100,
    "status": "active",
    "publisherId": 1
  }
  ```

### 4.3 上架商品（管理员）
- **接口路径**: `/api/mall/products`
- **请求方法**: `POST`
- **请求数据**:
  ```json
  {
    "name": "精美笔记本",
    "description": "高质量笔记本",
    "price": 50,
    "imageUrl": "https://example.com/product2.jpg",
    "stock": 50
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "上架成功",
    "data": {
      "id": 2,
      "name": "精美笔记本",
      "description": "高质量笔记本",
      "price": 50,
      "imageUrl": "https://example.com/product2.jpg",
      "stock": 50,
      "status": "active",
      "publisherId": 1
    }
  }
  ```

### 4.4 下架商品（管理员）
- **接口路径**: `/api/mall/products/{productId}/offline`
- **请求方法**: `PUT`
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "下架成功"
  }
  ```

### 4.5 上架商品（管理员）
- **接口路径**: `/api/mall/products/{productId}/online`
- **请求方法**: `PUT`
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "上架成功"
  }
  ```

### 4.6 删除商品（管理员）
- **接口路径**: `/api/mall/products/{productId}`
- **请求方法**: `DELETE`
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "删除成功"
  }
  ```

### 4.7 兑换商品
- **接口路径**: `/api/mall/exchange/{productId}`
- **请求方法**: `POST`
- **返回数据**:
  ```json
  {
    "code": 200,
    "message": "兑换成功",
    "data": {
      "id": 1,
      "productId": 1,
      "productName": "积分兑换券",
      "price": 90,
      "exchangeTime": "2026-04-26T12:00:00"
    }
  }
  ```

### 4.8 获取兑换记录
- **接口路径**: `/api/mall/exchange/records`
- **请求方法**: `GET`
- **返回数据**:
  ```json
  [
    {
      "id": 1,
      "productId": 1,
      "productName": "积分兑换券",
      "price": 90,
      "exchangeTime": "2026-04-26T12:00:00"
    }
  ]
  ```

## 5. 通用响应格式

### 5.1 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {...}
}
```

### 5.2 失败响应
```json
{
  "code": 400,
  "message": "操作失败",
  "data": null
}
```

## 6. 错误码

| 错误码 | 描述 |
|--------|------|
| 400 | 请求参数错误 |
| 401 | 未授权（未登录或token过期） |
| 403 | 禁止访问（权限不足） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 7. 角色权限说明

| 角色 | 权限 |
|------|------|
| admin（管理员） | 发布任务、审核任务申请、上架/下架/删除商品、查看所有用户任务 |
| normal（普通用户） | 提交任务申请、完成分配的任务、查看个人任务、兑换商品 |

## 8. 接口调用说明

### 8.1 认证机制
- 所有需要登录的接口都需要在请求头中携带 `Authorization` 字段
- 格式：`Authorization: Bearer {token}`
- token 由登录接口返回

### 8.2 接口调用示例
```javascript
// 登录示例
const response = await fetch('/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    username: 'admin',
    password: '123456'
  })
});

const data = await response.json();
const token = data.data.token;

// 调用需要认证的接口
const taskResponse = await fetch('/api/tasks/daily', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```