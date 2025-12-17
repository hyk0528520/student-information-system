-- 创建数据库
CREATE DATABASE IF NOT EXISTS student_db DEFAULT CHARACTER SET utf8mb4;
USE student_db;

-- 用户表（管理员/学生）
CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `username` varchar(50) NOT NULL COMMENT '用户名',
                            `password` varchar(100) NOT NULL COMMENT '密码',
                            `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
                            `gender` tinyint DEFAULT '0' COMMENT '性别：0-男，1-女',
                            `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                            `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                            `role` varchar(20) DEFAULT NULL COMMENT '角色：admin-管理员，student-学生',
                            `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `deleted` tinyint DEFAULT '0' COMMENT '删除标志：0-未删除，1-已删除',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入管理员用户
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `gender`, `phone`, `email`, `role`, `status`) VALUES
                                                                                                               ('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 0, '13800000000', 'admin@example.com', 'admin', 1),
                                                                                                               ('student001', 'e10adc3949ba59abbe56e057f20f883e', '张三', 0, '13800000001', 'student001@example.com', 'student', 1);

-- 学生信息表
CREATE TABLE `student_info` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学生ID',
                                `student_no` varchar(20) NOT NULL COMMENT '学号',
                                `student_name` varchar(50) NOT NULL COMMENT '姓名',
                                `gender` tinyint DEFAULT '0' COMMENT '性别：0-男，1-女',
                                `birthday` date DEFAULT NULL COMMENT '出生日期',
                                `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
                                `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                                `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                                `college` varchar(100) DEFAULT NULL COMMENT '学院',
                                `major` varchar(100) DEFAULT NULL COMMENT '专业',
                                `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
                                `enrollment_year` varchar(4) DEFAULT NULL COMMENT '入学年份',
                                `status` tinyint DEFAULT '1' COMMENT '状态：1-在读，2-休学，3-毕业',
                                `address` varchar(200) DEFAULT NULL COMMENT '家庭住址',
                                `emergency_contact` varchar(50) DEFAULT NULL COMMENT '紧急联系人',
                                `emergency_phone` varchar(20) DEFAULT NULL COMMENT '紧急联系电话',
                                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `deleted` tinyint DEFAULT '0' COMMENT '删除标志',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_student_no` (`student_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 插入示例学生数据
INSERT INTO `student_info` (`student_no`, `student_name`, `gender`, `birthday`, `college`, `major`, `class_name`, `enrollment_year`) VALUES
                                                                                                                                         ('2022001', '张三', 0, '2003-05-15', '计算机学院', '软件工程', '软件2201班', '2022'),
                                                                                                                                         ('2022002', '李四', 1, '2003-08-20', '计算机学院', '软件工程', '软件2201班', '2022'),
                                                                                                                                         ('2022003', '王五', 0, '2002-12-10', '信息学院', '计算机科学', '计科2201班', '2022');

-- 课程表
CREATE TABLE `course` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程ID',
                          `course_no` varchar(20) NOT NULL COMMENT '课程编号',
                          `course_name` varchar(100) NOT NULL COMMENT '课程名称',
                          `teacher` varchar(50) DEFAULT NULL COMMENT '授课教师',
                          `credit` decimal(3,1) DEFAULT '0.0' COMMENT '学分',
                          `hours` int DEFAULT '0' COMMENT '学时',
                          `semester` varchar(20) DEFAULT NULL COMMENT '学期',
                          `description` text COMMENT '课程描述',
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `deleted` tinyint DEFAULT '0' COMMENT '删除标志',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_course_no` (`course_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 插入示例课程数据
INSERT INTO `course` (`course_no`, `course_name`, `teacher`, `credit`, `hours`, `semester`) VALUES
                                                                                                ('C001', 'Java程序设计', '王老师', 3.0, 48, '2023-2024-1'),
                                                                                                ('C002', '数据库原理', '李老师', 3.0, 48, '2023-2024-1'),
                                                                                                ('C003', 'Web前端开发', '张老师', 2.5, 40, '2023-2024-2');

-- 成绩表
CREATE TABLE `score` (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '成绩ID',
                         `student_id` bigint NOT NULL COMMENT '学生ID',
                         `student_no` varchar(20) NOT NULL COMMENT '学号',
                         `student_name` varchar(50) NOT NULL COMMENT '学生姓名',
                         `course_id` bigint NOT NULL COMMENT '课程ID',
                         `course_no` varchar(20) NOT NULL COMMENT '课程编号',
                         `course_name` varchar(100) NOT NULL COMMENT '课程名称',
                         `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
                         `semester` varchar(20) DEFAULT NULL COMMENT '学期',
                         `exam_time` date DEFAULT NULL COMMENT '考试时间',
                         `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `deleted` tinyint DEFAULT '0' COMMENT '删除标志',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `uk_student_course` (`student_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 插入示例成绩数据
INSERT INTO `score` (`student_id`, `student_no`, `student_name`, `course_id`, `course_no`, `course_name`, `score`, `semester`) VALUES
                                                                                                                                   (1, '2022001', '张三', 1, 'C001', 'Java程序设计', 85.5, '2023-2024-1'),
                                                                                                                                   (1, '2022001', '张三', 2, 'C002', '数据库原理', 92.0, '2023-2024-1'),
                                                                                                                                   (2, '2022002', '李四', 1, 'C001', 'Java程序设计', 78.0, '2023-2024-1');

-- 请假表
CREATE TABLE `leave_application` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '请假ID',
                                     `student_id` bigint NOT NULL COMMENT '学生ID',
                                     `student_no` varchar(20) NOT NULL COMMENT '学号',
                                     `student_name` varchar(50) NOT NULL COMMENT '学生姓名',
                                     `start_time` datetime NOT NULL COMMENT '开始时间',
                                     `end_time` datetime NOT NULL COMMENT '结束时间',
                                     `days` decimal(5,1) DEFAULT '0.0' COMMENT '请假天数',
                                     `type` tinyint DEFAULT '1' COMMENT '请假类型：1-事假，2-病假，3-其他',
                                     `reason` varchar(500) NOT NULL COMMENT '请假原因',
                                     `status` tinyint DEFAULT '0' COMMENT '状态：0-待审批，1-已批准，2-已拒绝',
                                     `approver` varchar(50) DEFAULT NULL COMMENT '审批人',
                                     `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
                                     `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `deleted` tinyint DEFAULT '0' COMMENT '删除标志',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假表';

-- 公告表
CREATE TABLE `notice` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
                          `title` varchar(200) NOT NULL COMMENT '公告标题',
                          `content` text NOT NULL COMMENT '公告内容',
                          `type` tinyint DEFAULT '1' COMMENT '公告类型：1-系统公告，2-课程通知，3-活动通知',
                          `publisher` varchar(50) DEFAULT NULL COMMENT '发布人',
                          `publish_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                          `status` tinyint DEFAULT '1' COMMENT '状态：0-草稿，1-已发布，2-已撤回',
                          `view_count` int DEFAULT '0' COMMENT '查看次数',
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `deleted` tinyint DEFAULT '0' COMMENT '删除标志',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 插入示例公告
INSERT INTO `notice` (`title`, `content`, `type`, `publisher`, `status`, `view_count`) VALUES
                                                                                           ('欢迎使用学生信息管理系统', '欢迎各位老师和同学使用本系统，系统将持续优化和改进！', 1, '管理员', 1, 150),
                                                                                           ('期末考试安排通知', '本学期期末考试将于下个月进行，请同学们认真复习准备。', 2, '教务处', 1, 300);