# 智慧图书馆借阅系统

基于 Spring Boot + Vue3 的校园图书馆借阅管理系统，集成了条码 OCR 识别和大模型图书推荐功能。

## 功能特性

### 用户端
- 用户登录/注册
- 图书列表查询
- 扫码借阅 / 扫码归还
- 借阅记录查看
- AI 图书推荐

### 管理员端
- 用户管理
- 图书库存管理
- 借阅记录管理
- 出入库管理
- 借阅统计与数据看板

## 技术栈

| 模块 | 技术 |
|------|------|
| 后端 | Spring Boot 2.6 + MyBatis + MySQL 8 |
| 前端 | Vue 3 + Vite + Element Plus + ECharts |
| AI 模块 | Python 3.9 + OpenCV + pyzbar + 百度OCR + 通义千问 |

## 项目结构

```
library_barcode_borrow/
├── ai-model/              # AI 模块（条码识别 + 图书推荐）
│   ├── main.py            # 入口脚本
│   ├── ocr_api.py         # 条码识别（百度OCR + 本地pyzbar双引擎）
│   ├── ai_recommend.py    # 通义千问图书推荐
│   ├── image_preprocess.py# 图片灰度预处理
│   ├── config.py          # API密钥配置（需自行填入）
│   └── requirements.txt   # Python 依赖
├── backend/               # Spring Boot 后端
│   └── src/main/
│       ├── java/com/campus/libraryborrowbackend/
│       └── resources/
│           ├── mapper/
│           └── application.example.properties  # 配置模板
├── frontend/              # Vue3 前端
├── database/              # 数据库脚本
│   ├── library_db.sql
│   └── 数据库设计说明.md
└── readme.md
```

## 环境要求

- JDK 8+
- Maven 3.8+
- Node.js 16+
- MySQL 8.0+
- Python 3.9+

## 快速开始

### 1. 数据库初始化

```sql
CREATE DATABASE IF NOT EXISTS library_db DEFAULT CHARACTER SET utf8mb4;
USE library_db;
SOURCE database/library_db.sql;
```

### 2. 后端配置与启动

1. 复制配置模板：
   ```bash
   cp backend/src/main/resources/application.example.properties backend/src/main/resources/application.properties
   ```
2. 修改 `application.properties` 中的数据库密码和 Python 路径。
3. 启动后端：
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   后端运行在 `http://localhost:8080`

### 3. AI 模块配置与依赖

1. 安装 Python 依赖：
   ```bash
   cd ai-model
   pip install -r requirements.txt
   ```
2. 修改 `ai-model/config.py`，填入百度智能云和百炼（通义千问）的 API 密钥。
3. 后端通过 `PythonUtil` 以子进程方式调用 `main.py`，无需单独启动 AI 服务。

### 4. 前端启动

```bash
cd frontend
npm install
npm run dev
```
前端运行在 `http://localhost:5173`

## 演示账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 用户 | zhangsan | 123456 |

## 条码识别说明

条码识别采用双引擎兜底策略：
1. 优先调用百度云 OCR API；
2. 若调用失败，降级到本地 `pyzbar` 识别；
3. 本地识别会尝试多种图像预处理（高斯模糊、二值化、OTSU、自适应阈值等）和多种条码格式。

## 注意事项

1. `config.py` 中的 API 密钥需自行申请填入，仓库中为占位符。
2. `application.properties` 已被 `.gitignore` 忽略，不会提交到仓库。
3. 图片上传路径默认为 `backend/upload/`，可根据需要调整。
