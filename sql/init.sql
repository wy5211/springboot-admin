-- ========================================
-- Spring Boot 后台管理系统 - 数据库初始化
-- ========================================

CREATE DATABASE IF NOT EXISTS springboot_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE springboot_admin;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(200) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT '' COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
    `avatar` VARCHAR(500) DEFAULT '' COMMENT '头像',
    `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态（1正常 0禁用）',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志（0未删除 1已删除）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB COMMENT='用户表';

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（1正常 0禁用）',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    `remark` VARCHAR(200) DEFAULT '' COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB COMMENT='角色表';

-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_type` TINYINT NOT NULL COMMENT '类型（1目录 2菜单 3按钮）',
    `path` VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    `component` VARCHAR(200) DEFAULT '' COMMENT '组件路径',
    `permission` VARCHAR(100) DEFAULT '' COMMENT '权限标识',
    `icon` VARCHAR(100) DEFAULT '' COMMENT '图标',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `visible` TINYINT DEFAULT 1 COMMENT '是否可见',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='菜单表';

-- ----------------------------
-- 部门表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
    `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `leader` VARCHAR(50) DEFAULT '' COMMENT '负责人',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '联系电话',
    `email` VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='部门表';

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB COMMENT='用户角色关联表';

-- ----------------------------
-- 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB COMMENT='角色菜单关联表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(50) DEFAULT '' COMMENT '操作模块',
    `business_type` TINYINT DEFAULT 0 COMMENT '业务类型（0其他 1新增 2修改 3删除）',
    `method` VARCHAR(200) DEFAULT '' COMMENT '方法名称',
    `request_method` VARCHAR(10) DEFAULT '' COMMENT '请求方式',
    `oper_name` VARCHAR(50) DEFAULT '' COMMENT '操作人',
    `oper_url` VARCHAR(500) DEFAULT '' COMMENT '请求URL',
    `oper_param` TEXT COMMENT '请求参数',
    `oper_result` TEXT COMMENT '返回结果',
    `status` TINYINT DEFAULT 1 COMMENT '状态（1正常 0异常）',
    `error_msg` TEXT COMMENT '错误信息',
    `oper_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB COMMENT='操作日志表';

-- ========================================
-- 初始数据
-- ========================================

-- 初始管理员用户（密码: admin123，BCrypt加密）
INSERT INTO `sys_user` (`username`, `password`, `nickname`, `email`, `status`) VALUES
('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 'admin@example.com', 1);

-- 初始角色
INSERT INTO `sys_role` (`role_name`, `role_code`, `sort_order`, `remark`) VALUES
('超级管理员', 'SUPER_ADMIN', 1, '拥有所有权限'),
('普通用户', 'COMMON_USER', 2, '普通用户权限');

-- 初始菜单
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `path`, `component`, `permission`, `sort_order`) VALUES
(0, '系统管理', 1, '/system', NULL, '', 1),
(1, '用户管理', 2, '/system/user', 'system/user/index', 'system:user:list', 1),
(1, '角色管理', 2, '/system/role', 'system/role/index', 'system:role:list', 2),
(1, '菜单管理', 2, '/system/menu', 'system/menu/index', 'system:menu:list', 3),
(1, '部门管理', 2, '/system/dept', 'system/dept/index', 'system:dept:list', 4),
(1, '操作日志', 2, '/system/log', 'system/log/index', 'system:log:list', 5),
(2, '用户新增', 3, '', '', 'system:user:add', 1),
(2, '用户修改', 3, '', '', 'system:user:edit', 2),
(2, '用户删除', 3, '', '', 'system:user:delete', 3),
(3, '角色新增', 3, '', '', 'system:role:add', 1),
(3, '角色修改', 3, '', '', 'system:role:edit', 2),
(3, '角色删除', 3, '', '', 'system:role:delete', 3),
(4, '菜单新增', 3, '', '', 'system:menu:add', 1),
(4, '菜单修改', 3, '', '', 'system:menu:edit', 2),
(4, '菜单删除', 3, '', '', 'system:menu:delete', 3);

-- 管理员关联超级管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 超级管理员关联所有菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, `id` FROM `sys_menu`;
