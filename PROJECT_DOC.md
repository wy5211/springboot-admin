# Spring Boot Admin 系统项目文档

## 一、项目概述

基于 **Spring Boot 3.2.5 + Java 17** 构建的后台管理系统，采用经典的 **RBAC（基于角色的访问控制）** 架构。提供用户管理、角色管理、菜单/权限管理、部门管理、操作日志、文件存储等功能，支持通过 Docker Compose 一键部署。

| 项目信息 | 说明 |
|---------|------|
| 构建工具 | Maven |
| JDK 版本 | 17 |
| 核心框架 | Spring Boot 3.2.5 |
| ORM 框架 | MyBatis-Plus 3.5.6 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis 7 |
| 文件存储 | MinIO |
| 接口文档 | SpringDoc OpenAPI (Swagger) |
| 认证方式 | JWT (无状态) |

---

## 二、目录结构总览

```
springboot-admin/
├── pom.xml                              # Maven 构建配置与依赖管理
├── Dockerfile                           # 多阶段 Docker 镜像构建
├── docker-compose.yml                   # 容器编排（5 个服务）
├── nginx/
│   └── nginx.conf                       # Nginx 反向代理配置
├── sql/
│   └── init.sql                         # 数据库初始化脚本（建表 + 种子数据）
└── src/main/
    ├── java/com/example/demo/
    │   ├── Application.java             # 启动类
    │   ├── aspect/                      # AOP 切面
    │   ├── common/                      # 公共模块
    │   │   ├── annotation/              # 自定义注解
    │   │   ├── exception/               # 异常处理
    │   │   ├── page/                    # 分页封装
    │   │   └── result/                  # 统一响应封装
    │   ├── config/                      # 配置类
    │   ├── controller/                  # 控制器层（REST API）
    │   ├── convert/                     # 对象转换器
    │   ├── dto/                         # 数据传输对象（请求入参）
    │   ├── entity/                      # 实体类（对应数据库表）
    │   ├── mapper/                      # 数据访问层
    │   ├── security/                    # 安全认证模块
    │   ├── service/                     # 业务逻辑层
    │   │   └── impl/                    # 业务逻辑实现
    │   ├── task/                        # 定时任务
    │   ├── util/                        # 工具类
    │   └── vo/                          # 视图对象（响应出参）
    └── resources/
        ├── application.yml              # 主配置文件
        ├── application-dev.yml          # 开发环境配置
        ├── application-prod.yml         # 生产环境配置
        ├── logback-spring.xml           # 日志配置
        └── mapper/                      # MyBatis XML 映射文件
```

---

## 三、各模块详细说明

### 3.1 启动类 — `Application.java`

Spring Boot 应用入口，注解 `@SpringBootApplication` 自动扫描组件，`@EnableScheduling` 启用定时任务。

---

### 3.2 `config/` — 基础设施配置

| 配置类 | 功能说明 |
|--------|---------|
| **`SecurityConfig`** | Spring Security 核心配置。关闭 Session、禁用 CSRF；白名单放行登录、验证码、Swagger 等接口；注册 BCrypt 密码编码器；启用方法级权限控制 `@PreAuthorize` |
| **`RedisConfig`** | 配置 `RedisTemplate<String, Object>`，Key 使用 String 序列化，Value 使用 JSON 序列化，支持 `LocalDateTime` 类型 |
| **`MinioConfig`** | 读取 `minio.*` 配置项（endpoint、accessKey、secretKey、bucketName），注册 `MinioClient` Bean |
| **`MybatisPlusConfig`** | 配置 MySQL 分页插件 `PaginationInnerInterceptor`；配置自动填充处理器（插入时填充 `createTime`，插入/更新时填充 `updateTime`） |
| **`SwaggerConfig`** | 配置 SpringDoc OpenAPI，设置 API 标题、Bearer Token 安全方案，方便在 Swagger UI 中测试接口 |

---

### 3.3 `controller/` — REST API 层

| 控制器 | 路径前缀 | 功能 |
|--------|---------|------|
| **`AuthController`** | `/api/auth` | 用户认证：登录（验证用户名密码，返回 JWT + 用户信息 + 权限列表）、登出 |
| **`SysUserController`** | `/api/system/user` | 用户管理：分页查询、详情、新增、编辑、删除、重置密码、分配角色 |
| **`SysRoleController`** | `/api/system/role` | 角色管理：列表查询、新增、编辑、删除、分配菜单权限 |
| **`SysMenuController`** | `/api/system/menu` | 菜单管理：菜单树查询、新增、编辑、删除、获取当前用户菜单 |
| **`SysDeptController`** | `/api/system/dept` | 部门管理：部门树查询 |
| **`FileController`** | `/api/file` | 文件管理：上传文件至 MinIO、删除文件 |
| **`DemoController`** | `/api` | 示例接口：健康检查，无需认证 |

> 所有业务接口均使用 `@PreAuthorize` 进行权限控制，写操作使用 `@Log` 注解记录操作日志。

---

### 3.4 `entity/` — 实体类

对应数据库表，使用 MyBatis-Plus 注解 + Lombok。

| 实体类 | 数据库表 | 说明 |
|--------|---------|------|
| **`SysUser`** | `sys_user` | 用户：用户名、密码、昵称、邮箱、手机、头像、部门ID、状态；支持逻辑删除 |
| **`SysRole`** | `sys_role` | 角色：角色名、角色编码、排序、状态；支持逻辑删除 |
| **`SysMenu`** | `sys_menu` | 菜单：父级ID、菜单名、类型（目录/菜单/按钮）、路径、组件、权限标识、图标；含 `children` 字段用于构建树 |
| **`SysDept`** | `sys_dept` | 部门：父级ID、部门名、排序、负责人；含 `children` 字段用于构建树；支持逻辑删除 |
| **`SysUserRole`** | `sys_user_role` | 用户-角色关联表，唯一约束 (userId, roleId) |
| **`SysRoleMenu`** | `sys_role_menu` | 角色-菜单关联表，唯一约束 (roleId, menuId) |
| **`SysOperLog`** | `sys_oper_log` | 操作日志：模块名、操作类型、请求方法、请求参数、返回结果、操作人、操作时间 |

---

### 3.5 `dto/` — 数据传输对象（请求参数）

| DTO | 用途 |
|-----|------|
| **`LoginDTO`** | 登录请求：用户名、密码、验证码 Key 和验证码值 |
| **`UserCreateDTO`** | 新增用户：用户名（3-50字符）、密码（6-50字符）、邮箱（格式校验）等 |
| **`UserUpdateDTO`** | 编辑用户：所有字段可选，支持部分更新 |
| **`UserQueryDTO`** | 用户分页查询：支持用户名、手机号、状态筛选，默认每页10条 |
| **`RoleCreateDTO`** | 新增角色：角色名和角色编码不能为空 |
| **`RoleUpdateDTO`** | 编辑角色：所有字段可选 |
| **`MenuCreateDTO`** | 新增/编辑菜单：菜单名、菜单类型必填，默认父级ID为0 |
| **`AssignRoleDTO`** | 为用户分配角色：用户ID + 角色ID列表 |
| **`AssignMenuDTO`** | 为角色分配菜单：角色ID + 菜单ID列表 |

---

### 3.6 `vo/` — 视图对象（响应参数）

| VO | 用途 |
|----|------|
| **`LoginVO`** | 登录响应：token、用户名、昵称、权限列表 |
| **`UserVO`** | 用户详情响应：基本信息 + 关联的角色ID列表和角色名列表 |

---

### 3.7 `mapper/` — 数据访问层

所有 Mapper 继承 MyBatis-Plus 的 `BaseMapper<T>`，提供标准 CRUD。部分自定义 SQL 定义在 XML 映射文件中。

| Mapper | 自定义方法 | 说明 |
|--------|-----------|------|
| **`SysUserMapper`** | `selectRoleIdsByUserId` | 根据用户ID查询关联的角色ID列表 |
| **`SysRoleMapper`** | `selectRolesByUserId` | 根据用户ID查询完整的角色对象列表 |
| **`SysMenuMapper`** | `selectMenusByRoleIds` | 根据角色ID集合查询菜单列表 |
| | `selectPermissionsByUserId` | 根据用户ID查询所有权限标识（用于 Spring Security） |
| 其他 Mapper | 无 | 仅使用 BaseMapper 内置方法 |

XML 映射文件位于 `src/main/resources/mapper/` 目录。

---

### 3.8 `service/` — 业务逻辑层

| 服务接口 | 实现类 | 核心逻辑 |
|---------|--------|---------|
| **`SysUserService`** | `SysUserServiceImpl` | 用户分页查询（支持关键字和状态筛选）、新增（BCrypt 加密密码 + 用户名唯一校验）、编辑、逻辑删除（禁止删除 admin）、密码重置为 "123456"、角色分配 |
| **`SysRoleService`** | `SysRoleServiceImpl` | 角色列表查询、新增（角色编码唯一校验）、编辑、删除（禁止删除 SUPER_ADMIN）、按用户查询角色、菜单权限分配 |
| **`SysMenuService`** | `SysMenuServiceImpl` | 递归构建菜单树、CRUD、删除校验（有子菜单时禁止删除）、根据用户ID获取权限标识、根据用户ID获取菜单树 |
| **`SysDeptService`** | `SysDeptServiceImpl` | 递归构建部门树 |
| **`SysOperLogService`** | `SysOperLogServiceImpl` | 异步保存操作日志（`@Async`），分页查询日志 |
| **`CaptchaService`** | 无接口，直接 `@Service` | 生成图形验证码（Hutool CaptchaUtil），存入 Redis（5分钟过期），验证并删除 |
| **`MinioService`** | 无接口，直接 `@Service` | 文件上传（支持白名单校验、自动创建 Bucket）、文件删除 |

---

### 3.9 `security/` — 认证与授权

| 类 | 功能 |
|----|------|
| **`JwtTokenProvider`** | JWT 令牌生成（HMAC256）、解析用户名、验证令牌有效性和过期时间 |
| **`JwtAuthenticationFilter`** | 请求过滤器：从 `Authorization: Bearer xxx` 提取令牌，验证后加载用户权限信息并设置 `SecurityContext` |
| **`CustomUserDetailsService`** | 实现 `UserDetailsService`：从数据库加载用户，校验状态，查询用户权限列表，构建 `UserDetails` 对象 |

**认证流程：**

```
请求 → JwtAuthenticationFilter → 提取 Token → JwtTokenProvider 验证
→ CustomUserDetailsService 加载用户权限 → 设置 SecurityContext → 放行到 Controller
```

---

### 3.10 `common/` — 公共模块

#### `annotation/Log` — 自定义日志注解

方法级注解，属性：
- `title`：模块名称（如 "用户管理"）
- `businessType`：操作类型（0=其他, 1=新增, 2=修改, 3=删除）

#### `aspect/LogAspect` — 操作日志切面

拦截 `@Log` 注解的方法，记录操作人、请求方法、请求URL、请求参数、返回结果、异常信息、耗时，异步保存到 `sys_oper_log` 表。

#### `exception/` — 异常处理

| 类 | 说明 |
|----|------|
| **`BusinessException`** | 自定义业务异常，包含错误码和消息 |
| **`GlobalExceptionHandler`** | 全局异常处理器（`@RestControllerAdvice`）：统一捕获业务异常（返回自定义码）、参数校验异常（400）、认证失败（401）、权限不足（403）、系统异常（500） |

#### `result/Result` — 统一响应封装

响应格式：`{ code, message, data }`，提供 `success()`、`error()` 等静态工厂方法。

#### `page/PageResult` — 分页封装

分页响应格式：`{ records, total, pageNum, pageSize }`。

---

### 3.11 `convert/` — 对象转换器

**`RoleConvert`**：MapStruct 转换器接口，定义了 DTO 与 Entity 之间的转换方法。

---

### 3.12 `task/` — 定时任务

| 任务 | 执行时间 | 功能 |
|------|---------|------|
| **`CleanTokenTask`** | 每日凌晨 2:00 | 清理过期 JWT 令牌（预留，待实现） |
| **`DataStatisticsTask`** | 每日凌晨 3:00 | 数据统计（新增用户、活跃用户等，预留，待实现） |

---

### 3.13 `util/` — 工具类

**`RedisUtil`**：Redis 操作工具类，封装了 String、Hash、List、Set 等常用操作。

---

## 四、数据库设计

数据库 `springboot_admin` 包含 7 张表：

```
sys_user (用户表)
    ├── sys_user_role (用户角色关联表)
    │       └── sys_role (角色表)
    │               └── sys_role_menu (角色菜单关联表)
    │                       └── sys_menu (菜单表)
    └── sys_dept (部门表)

sys_oper_log (操作日志表)
```

**种子数据：**
- 超级管理员：用户名 `admin`，密码 `admin123`
- 角色：`SUPER_ADMIN`（超级管理员）、`COMMON_USER`（普通用户）
- 菜单：系统管理目录下包含用户、角色、菜单、部门管理和操作日志等子菜单及按钮权限

---

## 五、部署架构

```
                    ┌──────────────┐
                    │  Nginx :80   │
                    │  (反向代理)   │
                    └──────┬───────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
      /api/* 转发    Swagger UI      Gzip 压缩
              │
    ┌─────────┴──────────┐
    │  Spring Boot :8080 │
    └──┬───────┬─────────┘
       │       │
  ┌────┴──┐ ┌──┴───┐
  │ MySQL │ │ Redis │
  │ :3306 │ │ :6379 │
  └───────┘ └──────┘
```

**Docker Compose 编排的 5 个服务：**

| 服务 | 镜像 | 端口 | 说明 |
|------|------|------|------|
| mysql | mysql:8.0 | 3306 | 数据库，自动执行 `init.sql` 初始化 |
| redis | redis:7-alpine | 6379 | 缓存，存储验证码等 |
| minio | minio/minio | 9000/9001 | 对象存储，文件上传/下载 |
| app | 自构建 | 8080 | Spring Boot 应用 |
| nginx | nginx:alpine | 80 | 反向代理，API 转发 + Swagger + Gzip |

---

## 六、配置文件说明

| 文件 | 说明 |
|------|------|
| **`application.yml`** | 主配置：服务端口 8088（dev）/ 8080（prod）、文件上传限制 10MB、MyBatis-Plus 配置、Swagger 路径 |
| **`application-dev.yml`** | 开发环境：本地 MySQL、Redis、MinIO 连接，JWT 密钥，连接池大小 5-20 |
| **`application-prod.yml`** | 生产环境：所有连接通过环境变量注入，连接池大小 10-50，降低日志级别 |
| **`logback-spring.xml`** | 日志配置：开发环境仅控制台输出；生产环境控制台 + 滚动文件（按天切割，保留30天，单文件最大50MB） |

---

## 七、主要依赖

| 类别 | 依赖 | 版本 |
|------|------|------|
| Web | spring-boot-starter-web | 3.2.5 |
| 安全 | spring-boot-starter-security | 3.2.5 |
| 校验 | spring-boot-starter-validation | 3.2.5 |
| Redis | spring-boot-starter-data-redis | 3.2.5 |
| AOP | spring-boot-starter-aop | 3.2.5 |
| ORM | mybatis-plus-spring-boot3-starter | 3.5.6 |
| 数据库 | mysql-connector-j | (managed) |
| JWT | java-jwt (Auth0) | 4.4.0 |
| 文件存储 | minio | 8.5.9 |
| 工具库 | hutool-all | 5.8.26 |
| JSON | fastjson2 | 2.0.47 |
| 对象映射 | mapstruct | 1.5.5.Final |
| 接口文档 | springdoc-openapi-starter-webmvc-ui | 2.5.0 |
| 简化代码 | lombok | (managed) |
