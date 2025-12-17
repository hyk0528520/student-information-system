# 学生信息管理系统 (Student Information Management System)

> 一个基于 Spring Boot 的 B/S 架构学生信息管理后台系统。

## 技术栈
- **后端**: Spring Boot 2.7, MyBatis-Plus, Sa-Token
- **数据库**: MySQL 8.0, Redis 7.0
- **工具**: Maven, Git, IDEA
- **前端模板**: Thymeleaf

## 核心功能
- 学籍管理（学生增删改查）
- 成绩与课程管理
- 在线请假流程
- 公告发布
- 数据统计与缓存优化（Redis）

## 项目特点
1. **高效开发**：使用 MyBatis-Plus 减少 70% 的简单 SQL 编写。
2. **性能优化**：集成 Redis 缓存高频数据，页面响应提升至毫秒级。
3. **权限控制**：通过 Sa-Token 实现简洁的登录鉴权。

## 快速启动
1. 克隆项目：`git clone https://github.com/你的用户名/项目名.git`
2. 导入数据库：执行 `/sql/student_system.sql` 到 MySQL。
3. 修改 `src/main/resources/application.yml` 中的数据库和 Redis 配置。
4. 运行主类 `Application.java`，访问 `http://localhost:8080`，管理员账号：admin / 123456 

## 后续计划
- 前端界面美化（考虑 Vue/React）
- 添加 Docker 部署脚本
- 接入邮件通知模块

