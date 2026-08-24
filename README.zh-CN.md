<h1 align="center">图书管理系统</h1>

<p align="center">
  基于 <strong>Spring Boot + Vue（Element UI）</strong> 的 Web 端 <strong>图书管理系统</strong>。
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-8-f89820?labelColor=555555&style=flat-square&logo=openjdk&logoColor=white" alt="Java 8">
  <img src="https://img.shields.io/badge/Backend-Spring%20Boot%202.5.6-6DB33F?labelColor=555555&style=flat-square&logo=spring&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Frontend-Vue%202.6-42b883?labelColor=555555&style=flat-square&logo=vue.js&logoColor=white" alt="Vue">
  <img src="https://img.shields.io/badge/MySQL-8.0-00758f?labelColor=555555&style=flat-square&logo=mysql&logoColor=white" alt="MySQL 8.0">
  <img src="https://img.shields.io/badge/Redis-cache-red?labelColor=555555&style=flat-square&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/ORM-MyBatis-000000?labelColor=555555&style=flat-square" alt="MyBatis">
  <img src="https://img.shields.io/badge/UI-Element%20UI-409EFF?labelColor=555555&style=flat-square" alt="Element UI">
  <img src="https://img.shields.io/badge/IDE-IntelliJ%20IDEA-946beb?labelColor=555555&style=flat-square&logo=intellijidea&logoColor=white" alt="IntelliJ IDEA">
  <img src="https://img.shields.io/badge/License-MIT-lightgrey?labelColor=555555&style=flat-square" alt="License MIT">
  <img src="https://img.shields.io/badge/release-v1.0-blue?labelColor=555555&style=flat-square" alt="Release v1.0">
</p>

<p align="center">
  <a href="#安装运行">安装运行</a> ·
  <a href="#项目简介">项目简介</a> ·
  <a href="#功能特性">功能特性</a> ·
  <a href="#技术栈">技术栈</a> ·
  <a href="#项目结构">项目结构</a> ·
  <a href="#截图">截图</a> ·
  <a href="#数据库">数据库</a> ·
  <a href="#默认账号">默认账号</a> ·
  <a href="#注意事项">注意事项</a> ·
  <a href="#许可证">许可证</a>
</p>

<p align="center">
  <a href="README.md">English</a> · <a href="README.zh-CN.md"><strong>简体中文</strong></a>
</p>

---

<a id="安装运行"></a>
## 🚀 安装运行

### 前置条件

- **JDK 8**
- **MySQL 8.0**（脚本可在 MySQL 8.0+ 运行）
- **Redis**（任意版本，如 Redis 3.2，用于缓存）
- **Node.js 14**（前端工具链基于 Node 14）
- **Maven 3.5**（使用 apache-maven-3.5.0）
- **IntelliJ IDEA**（推荐用于后端开发）

### 步骤

**1. 获取代码**

```bash
git clone https://github.com/Je-taimais/book-management-system.git
cd book-management-system
```

**2. 准备数据库**

```sql
CREATE DATABASE `db_book` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `db_book`;
SOURCE database/db_book.sql;
-- 可选：为 book_info 增加 stock（可借库存）字段
SOURCE database/upgrade_stock.sql;
```

**3. 启动 Redis**

```
redis-server
```

> 默认连接：`localhost:6379`，无密码 —— 与 `application.yml` 一致。

**4. 运行后端（`book-backend`）**

- 用 IntelliJ IDEA 打开 `book-backend`。
- 设置 **Project SDK = Java 8**，**Maven home = `D:\apache-maven-3.5.0`**。
- 运行主类 `com.shanzhu.book.BackendApplication`。
- 或在终端执行（需 Maven 3.5 已加入 `PATH`）：

```bash
cd book-backend
mvn spring-boot:run
```

> 后端访问地址：`http://localhost:9111/BookManager`。
> 数据库账号密码在 `src/main/resources/application.yml` 中（默认 `root` / `root`），请按你的 MySQL 修改。

**5. 运行前端（`book-frontend`）**

```bash
cd book-frontend
npm install      # 使用 Node.js 14
npm run dev      # 开发服务器 http://localhost:9112
```

打开 **http://localhost:9112**。前端会将接口请求代理到后端 `/BookManager`。

生产构建：

```bash
npm run build:prod   # 输出 dist/（部署在 /BookManager 下）
```

---

<a id="项目简介"></a>
## 📚 项目简介

图书管理系统是一套基于 B/S 架构、采用 **Spring Boot** 后端与 **Vue + Element UI** 前端开发的 Web 端图书管理平台，包含两类角色：

- **管理员**可在后台维护图书、图书类型、借阅记录、用户、逾期与罚款，并通过带图表的仪表盘监控系统运行状况；
- **读者**可浏览与关键词检索图书、借阅 / 归还图书、查看个人借阅记录、查看逾期、缴纳罚款并修改密码。

项目采用清晰的前后端分离架构：后端通过 MyBatis + MySQL + Redis 提供 REST API，前端为基于 Vue CLI 的单页应用。

---

<a id="解决了什么问题"></a>
## 🎯 解决了什么问题

传统的图书流通依赖人工、纸质或零散的记录方式，容易出错，也难以掌握“借了什么、是否逾期、库存多少”。本项目将整套业务流程整合进同一个 Web 应用：

- **对管理员**：提供可视化的图书编目、分类与用户管理、借还办理、罚款收缴后台，告别电子表格；
- **对读者**：通过友好的界面自助浏览、检索、借阅、归还与缴费；
- **对管理者**：仪表盘提供实时统计（图书总数、借阅总数、用户总数、图书类型分布、借阅状态分布、高价图书排行）辅助决策；
- **对学习者**：是一份结构清晰、易于阅读的 Spring Boot + Vue 全栈教学 / 课程设计示例。

---

<a id="功能特性"></a>
## ✨ 功能特性

### 管理端

- **仪表盘** —— 统计卡片与 ECharts 图表（图书 / 借阅 / 用户总数、图书类型；图书类型分布、借阅状态分布、高价图书排行）
- **图书管理** —— 图书增删改查、封面上传
- **图书类型管理** —— 分类维护
- **借阅管理** —— 借还记录查看与管理
- **用户管理** —— 读者账号管理
- **逾期管理** —— 跟踪逾期借阅
- **罚款 / 缴费** —— 罚款记录与收缴
- **修改密码**

### 读者端

- 注册 / 登录
- 浏览与关键词检索图书
- 借阅 / 归还图书
- 查看个人借阅记录
- 查看逾期
- 缴纳罚款
- 修改密码

---

<a id="技术栈"></a>
## 🧱 技术栈

| 层级 | 技术 |
| --- | --- |
| 后端语言 | Java 8 |
| 后端框架 | Spring Boot 2.5.6 |
| ORM | MyBatis 2.0 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis |
| 前端 | Vue 2.6.10 + Vue CLI 4.4 |
| 界面 | Element UI 2.13.2 |
| 图表 | ECharts 5.6 |
| 状态 / 路由 | Vuex 3.1 / Vue Router 3.0 |
| HTTP 客户端 | Axios |
| 构建与开发工具 | Maven 3.5、IntelliJ IDEA |
| 架构 | B/S，前后端分离 |

---

<a id="项目结构"></a>
## 📂 项目结构

```
book-management-system/
├── book-backend/                # Spring Boot 后端（端口 9111，上下文 /BookManager）
│   └── src/main/java/com/shanzhu/book/
│       ├── config/              # Spring 配置（MyBatis、Redis、拦截器等）
│       ├── exception/           # 全局异常处理
│       ├── interceptor/         # 登录 / 权限拦截器
│       ├── mapper/              # MyBatis Mapper 接口
│       ├── model/               # 实体类与 DTO
│       ├── service/ & impl/     # 业务逻辑
│       ├── utils/               # 工具类
│       └── web/                 # 控制器（REST API）
│       └── src/main/resources/
│           ├── application.yml  # 端口、数据源、Redis 配置
│           ├── mapper/           # MyBatis XML
│           └── static/files/    # 上传的图书封面图片
├── book-frontend/               # Vue + Element UI 前端（开发端口 9112）
│   └── src/
│       ├── api/                 # axios 接口模块
│       ├── views/               # 页面（dashboard、bookinfo、booktype、borrow、
│       │                        #        user、overdue、payment、login、register、password…）
│       ├── router/  store/      # 路由与 Vuex
│       └── components/  layout/ # 公共组件与布局
├── database/
│   ├── db_book.sql              # 表结构与示例数据
│   └── upgrade_stock.sql        # 可选：新增 stock 字段
├── screenshots/                 # 项目截图
├── docs/
│   └── 图书馆系统.docx           # 项目文档（中文）
├── README.md                    # 英文说明（本文件的对应版本）
├── README.zh-CN.md              # 简体中文（本文件）
├── LICENSE
└── .gitignore
```

---

<a id="截图"></a>
## 🖼️ 截图

### 系统总览
![系统总览](screenshots/基于vue图书管理系统.png)

### 登录
![登录](screenshots/登录.png)

### 首页 / 仪表盘
![仪表盘](screenshots/首页.png)

### 图书类型管理
![图书类型管理](screenshots/图书管理-图书类型管理.png)

### 借阅信息管理
![借阅管理](screenshots/图书管理-借阅信息管理.png)

### 用户管理
![用户管理](screenshots/其他管理-用户管理.png)

### 密码更改
![密码更改](screenshots/其他管理-密码更改.png)

---

<a id="数据库"></a>
## 🗄️ 数据库

数据库 `db_book` 包含 5 张表：

| 表 | 说明 |
| --- | --- |
| `user` | 用户账号 —— `userType = 1` 管理员，`= 0` 读者 |
| `book_info` | 图书（书名、作者、价格、类型、简介、借阅状态、封面图片） |
| `book_type` | 图书类型（分类） |
| `borrow` | 借阅 / 归还记录 |
| `payment` | 罚款 / 缴费记录 |

> `upgrade_stock.sql` 可为 `book_info` 可选地增加 `stock` 字段，用于记录可借库存。

---

<a id="默认账号"></a>
## 🔑 默认账号

> 密码在数据库中以明文存储（教学 / 课程设计用途，正式使用前请务必修改）。

| 角色 | 用户名 | 密码 | 说明 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `123456` | `userType = 1` |
| 管理员 | `admin2` | `123456` | `userType = 1` |
| 读者 | `李明` | `123456` | `userType = 0` |

---

<a id="注意事项"></a>
## ⚠️ 注意事项

- **端口与上下文**：后端运行于 `http://localhost:9111/BookManager`；前端开发服务器运行于 `http://localhost:9112`，并将接口请求代理到后端 `/BookManager`。
- **数据库账号密码**写死在 `application.yml` 中（`root` / `root`），正式部署请修改。
- **Redis 必须启动**（默认 `localhost:6379`，无密码），否则缓存层会在启动 / 请求时出错。
- **图书封面**位于 `book-backend/src/main/resources/static/files`，已随仓库提交，保证界面开箱完整。
- **需使用 Node.js 14**：工具链（尤其是 `node-sass`）基于 Node 14；使用更高版本可能导致 `npm install` 失败。

---

<a id="许可证"></a>
## 📄 许可证

本项目以 [MIT 许可证](LICENSE) 开源发布。你可以依据该许可证自由地使用、复制、修改与再分发，但须保留版权声明与许可声明。

---

<div align="center">Made with ❤️ by <a href="https://github.com/Je-taimais">Je-taimais</a> · <a href="README.md">English</a></div>
