# Vercel 部署指南

## 前提条件
- ✅ ngrok 已运行，后端API可访问：https://awry-regulate-unfitting.ngrok-free.dev
- ✅ 前端已构建完成

## 部署步骤

### 1. 推送代码到 GitHub

在 GitHub 上创建新仓库，然后：

```bash
cd "c:\Users\Liumy\Desktop\WithNing - 副本"
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/withning.git
git push -u origin main
```

### 2. 部署到 Vercel

1. 访问 https://vercel.com/new
2. 点击 "Import Git Repository"
3. 选择您刚创建的 GitHub 仓库
4. 配置项目：
   - **Framework Preset**: Vue.js
   - **Root Directory**: ./
   - **Build Command**: npm run build
   - **Output Directory**: dist
5. 点击 "Deploy"

### 3. 环境变量配置（重要！）

在 Vercel 项目设置中添加环境变量：
- **Name**: `VUE_APP_API_BASE_URL`
- **Value**: `https://awry-regulate-unfitting.ngrok-free.dev/api`

或者直接在部署页面配置。

## 部署后访问

部署成功后，Vercel 会提供 URL，例如：`https://your-project.vercel.app`

## 注意事项

⚠️ **ngrok 限制**：
- ngrok 免费版每次重启后 URL 会变化
- 如果 ngrok 重启，需要重新配置前端环境变量并重新部署

💡 **保持服务运行**：
- 关闭电脑后服务会断开
- 如需长期运行，需要保持电脑开机或升级 ngrok 付费版

## 测试账号

- 管理员：admin / 123456
- 用户1：user1 / 123456
- 用户2：user2 / 123456
