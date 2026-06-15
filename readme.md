# 智慧图书馆借阅系统

基于 Spring Boot + Vue3 的智慧图书馆借阅管理系统，支持扫码借阅、库存管理、逾期提醒等功能。

## 🏗️ 技术栈

### 后端技术
- **框架**: Spring Boot 2.6.13
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis Plus
- **语言**: Java 8

### 前端技术
- **框架**: Vue 3 + TypeScript
- **UI 组件**: Element Plus
- **路由**: Vue Router
- **构建工具**: Vite
- **图标库**: Element Plus Icons

### AI 模块
- **语言**: Python 3.9+
- **OCR 识别**: 基于深度学习的条形码识别

## 📦 依赖列表

### 前端依赖

**生产依赖：**
| 依赖名称 | 版本 | 用途 |
|---------|------|------|
| `vue` | ^3.3.8 | Vue 3 核心框架 |
| `vue-router` | ^4.6.4 | Vue 路由管理 |
| `element-plus` | ^2.4.4 | Element Plus UI 组件库 |
| `@element-plus/icons-vue` | ^2.3.2 | Element Plus 图标库 |
| `axios` | ^1.6.0 | HTTP 请求库 |
| `echarts` | ^5.4.3 | 图表可视化库 |

**开发依赖：**
| 依赖名称 | 版本 | 用途 |
|---------|------|------|
| `vite` | ^4.4.9 | Vite 构建工具 |
| `@vitejs/plugin-vue` | ^4.4.0 | Vite Vue 插件 |

### 后端依赖

| 依赖名称 | 版本 | 用途 |
|---------|------|------|
| `spring-boot-starter-web` | 2.6.13 | Spring Boot Web 支持 |
| `mybatis-spring-boot-starter` | 2.2.2 | MyBatis ORM 整合 |
| `mysql-connector-j` | 8.0.31 | MySQL 数据库驱动 |
| `lombok` | - | 简化 Java 代码 |
| `fastjson2` | 2.0.52 | JSON 序列化/反序列化 |
| `spring-boot-starter-validation` | - | 参数校验 |

## ✨ 功能特性

### 用户端功能
- [x] 用户登录/注册
- [x] 图书列表查询
- [x] 扫码借阅图书
- [x] 借阅记录查看（倒序显示）
- [x] 扫码归还图书

### 管理员端功能
- [x] 用户管理（增删改查）
- [x] 图书库存管理（分类查询、库存调整）
- [x] 借阅管理（全馆借阅记录、管理员代还书）
- [x] 出入库管理
- [x] 统计分析

## 📁 项目结构

```
library_barcode_borrow/
├── ai-model/              # AI 模块（OCR识别）
│   ├── __pycache__/
│   ├── img/
│   ├── ai_recommend.py
│   ├── ocr_api.py
│   └── main.py
├── backend/               # Spring Boot后端
│   └── src/main/
│       ├── java/com/campus/libraryborrowbackend/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── mapper/
│       │   ├── entity/
│       │   └── config/
│       └── resources/
│           ├── mapper/
│           └── application.properties
├── frontend/              # Vue前端
│   ├── src/
│   │   ├── views/
│   │   ├── router/
│   │   ├── styles/
│   │   └── utils/
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── database/              # 数据库脚本
│   ├── library_db.sql
│   └── 数据库设计说明.md
└── readme.md
```

## 🛠️ 环境要求

- JDK 8+
- Maven 3.8+
- Node.js 16+
- MySQL 8.0+

## 🚀 快速开始

### 1. 数据库配置

创建数据库并执行初始化脚本：

```sql
CREATE DATABASE IF NOT EXISTS library_db DEFAULT CHARACTER SET utf8mb4;
USE library_db;
SOURCE database/library_db.sql;
```

### 2. 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务默认运行在 `http://localhost:5173`

## 📱 演示账号

### 管理员账号
- 用户名：`admin`
- 密码：`admin123`

### 用户账号
- 用户名：`zhangsan`
- 密码：`123456`

## ⚠️ 注意事项

1. **数据库配置**: 启动前请确保 `application.properties` 中的数据库连接信息正确
2. **文件上传路径**: 图片上传路径配置为 `D:/library_barcode_borrow/backend/upload/`
3. **OCR服务**: AI模块为可选组件，如需使用OCR功能，请启动Python服务


