# 不止乎 · buzhihu

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8-007396.svg)](buzhihu_project/springboot)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.9-6DB33F.svg)](buzhihu_project/springboot)
[![Vue](https://img.shields.io/badge/Vue-2.6-4FC08D.svg)](buzhihu_project/vue)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%2F8.0-4479A1.svg)](manager.sql)

> 不止乎——一个面向校园的内容社区：写博客、看活动、交朋友。
> 「知道的越多，越觉得不止于此。」

基于 **Spring Boot 2 + Vue 2** 前后端分离实现的轻量内容社区，覆盖 **内容创作（博客）→ 互动（点赞/收藏/评论/关注）→ 运营（活动/报名/公告）→ 治理（举报/封禁）** 的完整闭环，并配有数据看板的后台管理系统。

## ✨ 功能特性

**前台（游客可读，登录后参与）**

- 📝 博客：分类浏览、富文本发布、标签、搜索、阅读量
- 🎯 活动中心：活动列表/详情、在线报名、**名额上限**、报名状态
- ❤️ 互动：点赞、收藏、评论（树形回复）、@ 通知
- 👥 关注流：关注作者、首页聚合关注动态
- 🔔 站内通知：点赞/收藏/评论/回复/报名/系统 六类消息，未读角标
- 🧑 个人中心：我的博客 / 报名 / 点赞 / 收藏 / 评论；公开个人主页（`/front/user`）
- 🏠 公告栏、全文搜索

**后台（管理员）**

- 用户管理（含封禁/解封）、博客/评论/分类管理、活动管理（含名额设置）
- **举报处理**：忽略 / 删除内容 / 封禁账号，处理结果自动通知内容作者
- **数据看板**：总量统计、7 日趋势、分类分布、互动榜单（报名最多的活动 / 点赞最多的文章）

**安全设计**

- JWT 无状态鉴权（角色 ADMIN/USER），写操作归属校验（普通用户只能改自己的内容）
- 前台只读接口公开放行，管理接口严格鉴权
- 富文本入库前 **jsoup 消毒**，防存储型 XSS
- 文件上传**扩展名白名单 + 20MB 上限**

## 🧱 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Spring Boot 2.5.9 · MyBatis · PageHelper · JWT (java-jwt) · Hutool · jsoup · MySQL |
| 前端 | Vue 2.6 · Vue Router · Element UI · Axios · wangeditor · highlight.js |
| 环境 | JDK 8 · Maven 3.6+ · Node 14+ |

## 📁 目录结构

```
buzhihu_project/
├── buzhihu_project/          # 工程代码（两层目录，注意）
│   ├── springboot/           # 后端（端口 9091）
│   │   └── src/main/java/com/example/
│   │       ├── controller/   # 16 个模块的 REST 接口
│   │       ├── service/      # 业务逻辑（含归属校验、XSS 消毒）
│   │       ├── mapper/       # MyBatis（XML + 注解）
│   │       └── common/       # JWT 拦截器、CORS、统一返回
│   └── vue/                  # 前端（端口 8080）
│       └── src/views/
│           ├── front/        # 前台页面（Home/Activity/BlogDetail/Person/...）
│           └── manager/      # 后台页面（User/Blog/Report/Dashboard/...）
├── manager.sql               # 数据库脚本（13 张表 + 种子数据）
└── 项目体检报告.md            # 架构体检与修复记录
```

**数据模型（13 表）**：`user` `admin` `category` `blog` `activity` `activity_sign` `comment` `likes` `collect` `follow` `notice` `notification` `report`

## 🚀 快速开始

**环境要求**：JDK 8 · Maven 3.6+ · MySQL 5.7+/8.0 · Node 14+

**1. 初始化数据库**

```bash
mysql -uroot -p < manager.sql      # 会创建 xm-blog 库（13 张表 + 种子数据）
```

> 后端默认连接 `localhost:3306/xm-blog`，账号 `root/123456`，可在 `buzhihu_project/springboot/src/main/resources/application.yml` 修改。

**2. 启动后端**（端口 9091）

```bash
cd buzhihu_project/springboot
mvn spring-boot:run
```

**3. 启动前端**（端口 8080，访问 http://localhost:8080）

```bash
cd buzhihu_project/vue
npm install
npm run serve
```

**种子账号**

| 角色 | 账号 | 密码 |
|---|---|---|
| 管理员 | `admin` | `admin` |
| 普通用户 | `zhangsan` / `lisi` | `123456` |

## ✅ 当前状态

- [x] 前后端可编译、可启动（后端经 8 项接口冒烟测试：登录/鉴权/看板/榜单/评论树/阅读数等）
- [x] 匿名浏览、权限拦截、XSS 消毒、上传白名单均已落地并验证
- [ ] 单元测试（尚未覆盖）
- [ ] 部署脚本 / Docker 化（规划中）

详细的问题排查与架构决策过程见 [`项目体检报告.md`](./项目体检报告.md)。

## 📌 规划中的方向

- 标签独立建模（标签云 / 点击筛选）
- 全文搜索（MySQL FULLTEXT → Elasticsearch）
- 找回密码（邮箱验证码）
- 草稿箱与定时发布
- 产品文案去脚手架化（更名 / 视觉统一）

## 📄 License

[MIT License](LICENSE) · Copyright (c) 2026 Lefan Ma
