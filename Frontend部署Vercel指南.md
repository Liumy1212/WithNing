# 前端部署到 Vercel 说明

## 前提条件
已完成后端部署，并获得公网可访问的后端地址

## 部署步骤

### 1. 配置后端地址

编辑 `Frontend/vue-frontend/src/api/index.js`，将 `YOUR_SERVER_IP` 替换为您的实际服务器地址：

```javascript
const api = axios.create({
  baseURL: process.env.NODE_ENV === 'production'
    ? (process.env.VUE_APP_API_BASE_URL || 'http://YOUR_NGROK_FORWARDING_URL/api')
    : '/api',
  timeout: 10000,
  withCredentials: true
});
```

### 2. 重新构建前端

```bash
cd Frontend/vue-frontend
npm run build
```

### 3. 部署到 Vercel

**方式一：使用 Vercel CLI**

```bash
npm i -g vercel
cd Frontend/vue-frontend
vercel
```

**方式二：使用 GitHub（推荐）**

1. 将代码推送到 GitHub 仓库
2. 访问 https://vercel.com/new
3. 导入您的 GitHub 仓库
4. 配置构建命令和输出目录
5. 点击 Deploy

### 4. 配置环境变量

在 Vercel 项目设置中添加：
- `VUE_APP_API_BASE_URL` = 您的后端API地址（例如：`http://xxx.ngrok.io/api`）

## 注意事项

- 确保后端服务保持运行
- 如果后端地址变化，需要重新部署前端
- 建议配置自定义域名以获得 HTTPS 支持
