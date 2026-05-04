# 打卡监督系统 API 接口文档

## 目录
1. [用户认证模块](#1-用户认证模块)
2. [任务模块](#2-任务模块)
3. [积分模块](#3-积分模块)
4. [商城模块](#4-商城模块)
5. [管理员模块](#5-管理员模块)
6. [通用说明](#6-通用说明)

---

## 1. 用户认证模块

### 1.1 用户登录
- **接口路径**: `/api/auth/login`
- **请求方法**: `POST`
- **作用**: 验证用户账号密码，登录成功后服务器会创建session，返回用户基本信息
- **涉及表**: `user`
- **后端实现**:
  - 根据 `username` 查询 `user` 表
  - 验证密码是否匹配
  - 创建 session 存储用户信息
  - 返回用户基本信息（id, username, nickname, role, points）
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
    "code": 1,
    "msg": "登录成功",
    "data": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "role": 1,
      "points": 1000
    }
  }
  ```

### 1.2 用户注册
- **接口路径**: `/api/auth/register`
- **请求方法**: `POST`
- **作用**: 创建新用户账号，自动设置为普通用户角色
- **涉及表**: `user`
- **后端实现**:
  - 检查 `username` 是否已存在
  - 插入新用户记录，`role` 默认为 2（普通用户），`points` 默认为 0
- **请求数据**:
  ```json
  {
    "username": "newuser",
    "password": "123456",
    "nickname": "新用户"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "注册成功",
    "data": {
      "id": 2,
      "username": "newuser",
      "nickname": "新用户",
      "role": 2,
      "points": 0
    }
  }
  ```

### 1.3 用户登出
- **接口路径**: `/api/auth/logout`
- **请求方法**: `POST`
- **作用**: 清除当前用户的session信息，退出登录状态
- **涉及表**: 无
- **后端实现**: 清除 session 中的用户信息
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "退出成功",
    "data": null
  }
  ```

### 1.4 获取用户信息
- **接口路径**: `/api/auth/userinfo`
- **请求方法**: `GET`
- **作用**: 获取当前登录用户的详细信息（基于session）
- **涉及表**: `user`
- **后端实现**: 从 session 获取用户ID，查询 `user` 表返回用户信息
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "role": 1,
      "points": 1000
    }
  }
  ```

---

## 2. 任务模块

### 2.1 获取已发布任务列表（每日任务）
- **接口路径**: `/api/tasks/daily`
- **请求方法**: `GET`
- **作用**: 获取所有已审核通过（task.status=1）且当前用户未提交完成申请的每日任务列表，用户提交完成申请后的任务将不再显示
- **涉及表**: `task` + `task_submit`（用于排除已申请的任务）
- **后端实现**:
  - 查询 `task` 表，条件：`type = 1` AND `status = 1`
  - 排除当前用户已提交完成申请的任务（关联 `task_submit` 表过滤）
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "title": "每日打卡",
        "description": "完成每日打卡任务",
        "rewardPoints": 10,
        "completed": false,
        "createTime": "2026-04-26T00:00:00",
        "type": 1,
        "status": 1,
        "publisherId": 1,
        "imageUrl": null
      }
    ]
  }
  ```

### 2.2 获取已发布任务列表（阶段任务）
- **接口路径**: `/api/tasks/stage`
- **请求方法**: `GET`
- **作用**: 获取所有已审核通过（task.status=1）且当前用户未提交完成申请的阶段任务列表，包含截止时间，用户提交完成申请后的任务将不再显示
- **涉及表**: `task` + `task_submit`（用于排除已申请的任务）
- **后端实现**:
  - 查询 `task` 表，条件：`type = 2` AND `status = 1`
  - 排除当前用户已提交完成申请的任务（关联 `task_submit` 表过滤）
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 2,
        "title": "周打卡挑战",
        "description": "完成一周的打卡任务",
        "rewardPoints": 50,
        "deadline": "2026-05-02T23:59:59",
        "completed": false,
        "createTime": "2026-04-26T00:00:00",
        "type": 2,
        "status": 1,
        "publisherId": 1,
        "imageUrl": "https://example.com/task.jpg"
      }
    ]
  }
  ```

### 2.3 获取任务详情
- **接口路径**: `/api/tasks/{taskId}`
- **请求方法**: `GET`
- **作用**: 根据任务ID获取单个任务的详细信息
- **涉及表**: `task`
- **后端实现**: 根据 taskId 查询 `task` 表
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "id": 1,
      "title": "每日打卡",
      "description": "完成每日打卡任务",
      "rewardPoints": 10,
      "completed": false,
      "createTime": "2026-04-26T00:00:00",
      "type": 1,
      "status": 1,
      "publisherId": 1,
      "imageUrl": null
    }
  }
  ```

### 2.4 发布任务（管理员）
- **接口路径**: `/api/tasks`
- **请求方法**: `POST`
- **作用**: 管理员直接发布新任务，任务状态自动设置为"已通过"（task.status=1），立即对所有用户可见
- **涉及表**: `task`
- **后端实现**:
  - 从 session 获取当前用户ID作为 `publisher_id`
  - 插入 `task` 表，`status` 设为 1（已通过）
- **请求数据**:
  ```json
  {
    "type": 1,
    "title": "每日学习",
    "description": "每天学习1小时",
    "rewardPoints": 15,
    "deadline": null,
    "imageUrl": "https://example.com/study.jpg"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "发布成功",
    "data": null
  }
  ```

### 2.5 删除任务（管理员）
- **接口路径**: `/api/tasks/{taskId}`
- **请求方法**: `DELETE`
- **作用**: 管理员删除指定任务，删除后任务将从所有用户页面消失
- **涉及表**: `task`
- **后端实现**: 根据 taskId 删除 `task` 表中的记录
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "删除成功",
    "data": null
  }
  ```

### 2.6 申请发布任务（普通用户）
- **接口路径**: `/api/tasks/publish/apply`
- **请求方法**: `POST`
- **作用**: 普通用户提交新任务发布申请，等待管理员审批通过后任务才会正式发布显示给所有用户
- **涉及表**: `task`
- **后端实现**:
  - 从 session 获取当前用户ID作为 `publisher_id`
  - 插入 `task` 表，`status` 设为 2（待审核）
- **请求数据**:
  ```json
  {
    "type": 1,
    "title": "每日阅读",
    "description": "每天阅读30分钟",
    "rewardPoints": 20,
    "deadline": null,
    "imageUrl": "https://example.com/read.jpg"
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "申请成功，等待管理员审核",
    "data": {
      "id": 5,
      "title": "每日阅读",
      "description": "每天阅读30分钟",
      "rewardPoints": 20,
      "type": 1,
      "status": 2,
      "publisherId": 2,
      "imageUrl": "https://example.com/read.jpg",
      "createTime": "2026-04-26T12:00:00"
    }
  }
  ```

### 2.7 获取我的发布申请记录（普通用户）
- **接口路径**: `/api/tasks/publish/my-applies`
- **请求方法**: `GET`
- **作用**: 查看当前用户提交的所有任务发布申请记录及其审核状态
- **涉及表**: `task`
- **后端实现**: 查询 `task` 表，条件：`publisher_id = 当前用户ID` AND `status IN (2, 3)`
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 5,
        "title": "每日阅读",
        "description": "每天阅读30分钟",
        "rewardPoints": 20,
        "type": 1,
        "status": 2,
        "createTime": "2026-04-26T12:00:00"
      }
    ]
  }
  ```

### 2.8 提交任务完成申请（普通用户）
- **接口路径**: `/api/tasks/apply/{taskId}`
- **请求方法**: `POST`
- **作用**: 普通用户完成任务后点击"申请完成"按钮，提交完成申请，等待管理员审批后才能获得积分奖励
- **涉及表**: `task_submit`
- **后端实现**:
  - 从 session 获取当前用户ID
  - 查询 `task` 表获取任务信息（title, description, type, reward_points）
  - 插入 `task_submit` 表，`status` 设为 2（待审核）
  - **注意**：此接口不会修改 `task.status`，只创建 `task_submit` 申请记录
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "申请成功，等待管理员审核",
    "data": null
  }
  ```

### 2.9 获取我的任务申请记录（普通用户）
- **接口路径**: `/api/tasks/my-applies`
- **请求方法**: `GET`
- **作用**: 查看当前用户提交的所有任务完成申请记录
- **涉及表**: `task_submit`
- **后端实现**: 查询 `task_submit` 表，条件：`user_id = 当前用户ID`
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "userId": 2,
        "taskId": 1,
        "taskTitle": "每日打卡",
        "rewardPoints": 10,
        "status": 2,
        "reviewComment": null,
        "createTime": "2026-04-26T12:00:00",
        "reviewTime": null
      }
    ]
  }
  ```

### 2.10 获取任务发布申请列表（管理员）
- **接口路径**: `/api/admin/tasks/publish/applies`
- **请求方法**: `GET`
- **作用**: 管理员查看所有用户提交的任务发布申请，进行审批操作
- **涉及表**: `task` + `user`
- **后端实现**:
  - 查询 `task` 表，条件：`status = 2`（待审核）
  - 关联 `user` 表获取申请人昵称（nickname）
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 5,
        "userId": 2,
        "userNickname": "用户1",
        "title": "每日阅读",
        "description": "每天阅读30分钟",
        "rewardPoints": 20,
        "type": 1,
        "status": 2,
        "createTime": "2026-04-26T12:00:00"
      }
    ]
  }
  ```

### 2.11 审核任务发布申请（管理员）
- **接口路径**: `/api/admin/tasks/publish/{applyId}/review`
- **请求方法**: `PUT`
- **作用**: 管理员审批用户提交的任务发布申请，通过后任务正式发布（task.status=1），拒绝则 task.status=3
- **涉及表**: `task` + `user`
- **后端实现**:
  1. 更新 `task` 表：设置 `status`
  2. 如果通过（status=1）：任务对所有用户可见
  3. 如果拒绝（status=3）：任务不对普通用户显示
- **请求数据**:
  ```json
  {
    "status": 1
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "审核完成",
    "data": null
  }
  ```

### 2.12 获取任务完成申请列表（管理员）
- **接口路径**: `/api/admin/tasks/applies`
- **请求方法**: `GET`
- **作用**: 管理员查看所有用户提交的任务完成申请，进行审批操作
- **涉及表**: `task_submit` + `user`
- **后端实现**:
  - 查询 `task_submit` 表所有记录
  - 关联 `user` 表获取申请人昵称（nickname）
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "userId": 2,
        "userNickname": "用户1",
        "taskId": 1,
        "taskTitle": "每日打卡",
        "rewardPoints": 10,
        "status": 2,
        "imageUrl": null,
        "createTime": "2026-04-26T12:00:00"
      }
    ]
  }
  ```

### 2.13 审核任务完成申请（管理员）
- **接口路径**: `/api/admin/tasks/apply/{applyId}/review`
- **请求方法**: `PUT`
- **作用**: 管理员审批用户提交的任务完成申请，通过后用户获得积分，拒绝则不获得
- **涉及表**: `task_submit` + `user` + `point_record` + `user_task`
- **后端实现**:
  1. 更新 `task_submit` 表：设置 `status`
  2. **注意**：此接口修改的是 `task_submit.status`，不是 `task.status`
  3. 如果通过（task_submit.status=1）：
     - 更新 `user` 表：增加用户积分
     - 插入 `point_record` 表：记录积分变动（type=1, 任务奖励）
     - 插入/更新 `user_task` 表：标记任务已完成
- **请求数据**:
  ```json
  {
    "status": 1
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "审核完成",
    "data": null
  }
  ```

### 2.14 管理员直接完成任务
- **接口路径**: `/api/admin/tasks/{taskId}/complete`
- **请求方法**: `PUT`
- **作用**: 管理员直接完成任务（无需审批），积分直接发放给管理员账号
- **涉及表**: `task` + `user` + `point_record` + `user_task`
- **后端实现**:
  1. 查询 `task` 表获取任务奖励积分
  2. 更新 `user` 表：增加管理员积分
  3. 插入 `point_record` 表：记录积分变动
  4. 插入/更新 `user_task` 表：标记任务已完成
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "任务完成",
    "data": null
  }
  ```

---

## 3. 积分模块

### 3.1 获取用户积分
- **接口路径**: `/api/points`
- **请求方法**: `GET`
- **作用**: 获取当前登录用户的积分余额
- **涉及表**: `user`
- **后端实现**: 从 session 获取用户ID，查询 `user` 表的 `points` 字段
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": 100
  }
  ```

### 3.2 获取积分记录
- **接口路径**: `/api/points/records`
- **请求方法**: `GET`
- **作用**: 获取当前用户的所有积分变动明细记录
- **涉及表**: `point_record`
- **后端实现**: 查询 `point_record` 表，条件：`user_id = 当前用户ID`，按时间倒序
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "points": 10,
        "type": 1,
        "description": "完成任务：每日打卡",
        "createTime": "2026-04-26T08:00:00"
      }
    ]
  }
  ```

### 3.3 获取积分历史
- **接口路径**: `/api/points/history`
- **请求方法**: `GET`
- **作用**: 按日期汇总显示用户的积分变动历史
- **涉及表**: `point_record`
- **后端实现**: 查询 `point_record` 表，按日期分组汇总
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "date": "2026-04-26",
        "points": 10,
        "description": "完成每日打卡"
      }
    ]
  }
  ```

---

## 4. 商城模块

### 4.1 获取商品列表
- **接口路径**: `/api/mall/products`
- **请求方法**: `GET`
- **作用**: 获取所有已上架（status=1）的商品列表
- **涉及表**: `product`
- **后端实现**: 查询 `product` 表，条件：`status = 1`
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "积分兑换券",
        "description": "可兑换100积分",
        "price": 90,
        "imageUrl": "https://example.com/product1.jpg",
        "stock": 100,
        "status": 1,
        "publisherId": 1
      }
    ]
  }
  ```

### 4.2 获取商品详情
- **接口路径**: `/api/mall/products/{productId}`
- **请求方法**: `GET`
- **作用**: 根据商品ID获取单个商品的详细信息
- **涉及表**: `product`
- **后端实现**: 根据 productId 查询 `product` 表
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "id": 1,
      "name": "积分兑换券",
      "description": "可兑换100积分",
      "price": 90,
      "imageUrl": "https://example.com/product1.jpg",
      "stock": 100,
      "status": 1,
      "publisherId": 1
    }
  }
  ```

### 4.3 添加商品（管理员）
- **接口路径**: `/api/mall/products`
- **请求方法**: `POST`
- **作用**: 管理员添加新商品，新商品默认为上架状态（status=1）
- **涉及表**: `product`
- **后端实现**:
  - 从 session 获取当前用户ID作为 `publisher_id`
  - 插入 `product` 表，`status` 默认为 1（上架）
- **请求数据**:
  ```json
  {
    "name": "精美笔记本",
    "description": "高质量笔记本",
    "price": 50,
    "imageUrl": "https://example.com/notebook.jpg",
    "stock": 50
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "添加成功",
    "data": null
  }
  ```

### 4.4 删除商品（管理员）
- **接口路径**: `/api/mall/products/{productId}`
- **请求方法**: `DELETE`
- **作用**: 管理员删除指定商品
- **涉及表**: `product`
- **后端实现**: 根据 productId 删除 `product` 表中的记录
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "删除成功",
    "data": null
  }
  ```

### 4.5 上架商品（管理员）
- **接口路径**: `/api/mall/products/{productId}/online`
- **请求方法**: `PUT`
- **作用**: 将下架的商品上架，使其对所有用户可见并可兑换
- **涉及表**: `product`
- **后端实现**: 更新 `product` 表，设置 `status = 1`
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "上架成功",
    "data": null
  }
  ```

### 4.6 下架商品（管理员）
- **接口路径**: `/api/mall/products/{productId}/offline`
- **请求方法**: `PUT`
- **作用**: 将上架的商品下架
- **涉及表**: `product`
- **后端实现**: 更新 `product` 表，设置 `status = 2`
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "下架成功",
    "data": null
  }
  ```

### 4.7 申请兑换商品（普通用户）
- **接口路径**: `/api/mall/exchange/apply/{productId}`
- **请求方法**: `POST`
- **作用**: 普通用户点击"立即兑换"按钮，提交商品兑换申请，扣除积分前需管理员审批确认
- **涉及表**: `exchange_apply`
- **后端实现**:
  - 查询 `product` 表获取商品信息（name, price）
  - 检查用户积分是否足够（可选，也可在审核时检查）
  - 插入兑换申请记录（exchange_apply 表）
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "申请成功，等待管理员审核",
    "data": null
  }
  ```

### 4.8 获取我的兑换申请记录（普通用户）
- **接口路径**: `/api/mall/exchange/my-applies`
- **请求方法**: `GET`
- **作用**: 查看当前用户提交的所有商品兑换申请记录
- **涉及表**: `exchange_apply`
- **后端实现**: 查询兑换申请表，条件：`user_id = 当前用户ID`
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "userId": 2,
        "productId": 1,
        "productName": "积分兑换券",
        "price": 90,
        "status": 2,
        "reviewComment": null,
        "createTime": "2026-04-26T12:00:00",
        "reviewTime": null
      }
    ]
  }
  ```

### 4.9 获取兑换申请列表（管理员）
- **接口路径**: `/api/mall/exchange/applies`
- **请求方法**: `GET`
- **作用**: 管理员查看所有用户提交的商品兑换申请
- **涉及表**: `exchange_apply` + `user` + `product`
- **后端实现**:
  - 查询兑换申请表所有记录
  - 关联 `user` 表获取申请人昵称
  - 关联 `product` 表获取商品名称
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "userId": 2,
        "userNickname": "用户1",
        "productId": 1,
        "productName": "积分兑换券",
        "price": 90,
        "status": 2,
        "createTime": "2026-04-26T12:00:00"
      }
    ]
  }
  ```

### 4.10 审核兑换申请（管理员）
- **接口路径**: `/api/mall/exchange/{applyId}/review`
- **请求方法**: `PUT`
- **作用**: 管理员审批用户提交的商品兑换申请，通过后用户积分扣除并获得商品
- **涉及表**: `exchange_apply` + `user` + `product` + `point_record` + `exchange_record`
- **后端实现**:
  1. 更新兑换申请表：设置 `status`
  2. 如果通过（status=1）：
     - 查询 `product` 表获取商品价格
     - 检查用户积分是否足够
     - 更新 `user` 表：扣除用户积分
     - 更新 `product` 表：减少库存
     - 插入 `point_record` 表：记录积分变动（type=3, 兑换）
     - 插入 `exchange_record` 表：记录兑换成功
- **请求数据**:
  ```json
  {
    "status": 1
  }
  ```
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "审核完成",
    "data": null
  }
  ```

### 4.11 获取兑换记录
- **接口路径**: `/api/mall/exchange/records`
- **请求方法**: `GET`
- **作用**: 查看所有已完成的商品兑换记录
- **涉及表**: `exchange_record` + `product`
- **后端实现**:
  - 查询 `exchange_record` 表，条件：`user_id = 当前用户ID`
  - 关联 `product` 表获取商品名称
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "productId": 1,
        "productName": "积分兑换券",
        "price": 90,
        "exchangeTime": "2026-04-26T12:00:00"
      }
    ]
  }
  ```

---

## 5. 管理员模块

### 5.1 检测管理员权限
- **接口路径**: `/api/admin/check`
- **请求方法**: `GET`
- **作用**: 验证当前登录用户是否为管理员
- **涉及表**: `user`
- **后端实现**: 从 session 获取用户ID，查询 `user` 表的 `role` 字段，判断是否为 1
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "是管理员",
    "data": true
  }
  ```

### 5.2 获取用户列表（管理员）
- **接口路径**: `/api/admin/users`
- **请求方法**: `GET`
- **作用**: 管理员查看所有注册用户的信息列表
- **涉及表**: `user`
- **后端实现**: 查询 `user` 表所有记录
- **返回数据**:
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "管理员",
        "role": 1,
        "points": 1000
      }
    ]
  }
  ```

---

## 6. 通用说明

### 6.1 通用响应格式

所有接口统一使用以下响应格式：

**成功响应**
```json
{
  "code": 1,
  "msg": "操作成功",
  "data": { ... }
}
```

**失败响应**
```json
{
  "code": 0,
  "msg": "操作失败",
  "data": null
}
```

### 6.2 数据库表关系总结

| 接口 | 涉及表 | 操作类型 |
|------|--------|----------|
| 登录 | `user` | SELECT |
| 注册 | `user` | INSERT |
| 获取任务列表 | `task` + `user_task` | SELECT |
| 发布任务（管理员） | `task` | INSERT |
| 删除任务 | `task` | DELETE |
| 申请发布任务 | `task` | INSERT |
| 审核发布申请 | `task` | UPDATE |
| 申请完成任务 | `task_submit` | INSERT |
| 审核完成申请 | `task_submit` + `user` + `point_record` + `user_task` | UPDATE + INSERT |
| 管理员完成任务 | `task` + `user` + `point_record` + `user_task` | SELECT + UPDATE + INSERT |
| 获取积分记录 | `point_record` | SELECT |
| 获取商品列表 | `product` | SELECT |
| 添加商品 | `product` | INSERT |
| 上架/下架商品 | `product` | UPDATE |
| 申请兑换商品 | `exchange_apply` | SELECT + INSERT |
| 审核兑换申请 | `exchange_apply` + `user` + `product` + `point_record` + `exchange_record` | UPDATE + INSERT |

### 6.3 需要新增的数据库表

**exchange_apply（兑换申请表）** - 建议新增

| 字段名 | 数据类型 | 描述 |
|--------|----------|------|
| `id` | BIGINT | 主键 |
| `user_id` | BIGINT | 申请人ID |
| `product_id` | BIGINT | 商品ID |
| `product_name` | VARCHAR(100) | 商品名称（冗余） |
| `price` | INT | 兑换价格 |
| `status` | TINYINT | 状态：1=已通过, 2=待审核, 3=已拒绝 |
| `reviewer_id` | BIGINT | 审核人ID |
| `review_comment` | VARCHAR(200) | 审核备注 |
| `create_time` | DATETIME | 申请时间 |
| `review_time` | DATETIME | 审核时间 |

### 6.4 状态值说明

**重要区分：存在两个不同的 status 字段，分别用于不同的业务场景**

#### task.status（任务发布状态）
表示任务本身是否被管理员批准发布

| 值 | 说明 | 可见性 |
|-----|------|--------|
| 1 | 已发布（管理员批准） | 所有用户可见 |
| 2 | 待审核（等待管理员审批） | 仅申请者和管理员可见 |
| 3 | 已拒绝 | 仅申请者和管理员可见 |

#### task_submit.status（任务完成申请审核状态）
表示用户提交的任务完成申请的审核状态

| 值 | 说明 | 影响 |
|-----|------|------|
| 1 | 已通过（审核通过） | 用户获得积分 |
| 2 | 待审核（等待管理员审批） | 等待审核中 |
| 3 | 已拒绝（审核被拒绝） | 用户不获得积分 |

#### 其他状态字段

| 状态类型 | 值 | 说明 |
|----------|-----|------|
| 用户角色 (role) | 1 | 管理员 |
| 用户角色 (role) | 2 | 普通用户 |
| 任务类型 (type) | 1 | 每日任务 |
| 任务类型 (type) | 2 | 阶段任务 |
| 商品状态 (status) | 1 | 上架中 |
| 商品状态 (status) | 2 | 已下架 |
| 兑换申请状态 (exchange_apply.status) | 1 | 已通过 |
| 兑换申请状态 (exchange_apply.status) | 2 | 待审核 |
| 兑换申请状态 (exchange_apply.status) | 3 | 已拒绝 |
| 积分变动类型 (type) | 1 | 任务奖励 |
| 积分变动类型 (type) | 2 | 惩罚 |
| 积分变动类型 (type) | 3 | 兑换 |

### 6.5 user_task 表作用说明

`user_task` 表是一个**用户-任务关联表**，用于记录用户与任务之间的关系：

1. **任务完成状态追踪**: 记录用户是否完成了某个任务（`completed` 字段）
2. **完成时间记录**: 记录用户完成任务的具体时间（`complete_time` 字段）
3. **每日任务重置支持**: 支持每日任务的自动重置逻辑
4. **任务进度管理**: 为用户提供个人任务完成情况的统计和展示

### 6.6 业务流程图

#### 任务发布流程
```
普通用户：申请发布任务 → task.status=2 → 等待审核 → 通过后 task.status=1（所有用户可见）
管理员：查看发布申请 → 审批通过/拒绝 → 更新 task.status
```

#### 任务完成流程
```
普通用户：看到已发布任务（task.status=1）→ 申请完成 → task_submit.status=2 → 等待审核
管理员：查看完成申请 → 审批通过/拒绝 → task_submit.status=1/3 → 通过后用户获得积分
```

#### 商品兑换流程
```
普通用户：商品列表 → 申请兑换 → exchange_apply.status=2 → 等待审核 → 通过后扣除积分
管理员：添加商品 → 上架商品 → 查看兑换申请 → 审批通过/拒绝 → 通过后用户获得商品
```

### 6.7 角色权限说明

| 功能 | 普通用户 | 管理员 |
|------|---------|--------|
| 查看任务列表 | ✅ | ✅ |
| 申请发布任务 | ✅ | ✅（直接发布） |
| 申请完成任务 | ✅ | ✅（直接完成） |
| 审核任务发布 | ❌ | ✅ |
| 审核任务完成 | ❌ | ✅ |
| 添加商品 | ❌ | ✅ |
| 上架/下架商品 | ❌ | ✅ |
| 审核兑换申请 | ❌ | ✅ |
