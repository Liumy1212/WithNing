# ngrok 下载说明

## 方法一：手动下载

1. 访问 https://ngrok.com/download
2. 下载 Windows AMD64 版本
3. 解压后将 `ngrok.exe` 复制到 `c:\Users\Liumy\Desktop\WithNing - 副本\ngrok\` 目录

## 方法二：使用更新命令

如果您的 ngrok 可以更新，运行：
```bash
ngrok update
```

## 配置

下载新版本后，authtoken 已经配置好了，直接运行：

```bash
cd "c:\Users\Liumy\Desktop\WithNing - 副本\ngrok"
ngrok tcp 3306      # 穿透数据库
ngrok http 8080     # 穿透后端API
```
