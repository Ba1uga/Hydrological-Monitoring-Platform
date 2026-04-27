# 中国水文监测可视化平台

基于 `Spring Boot 3.4.13` 和 `Java 17` 的水文监测可视化平台。仓库同时包含后端接口、静态大屏页面，以及用于本地初始化演示数据的 SQL 脚本。

项目启动后，根路径默认会跳转到：

```text
http://localhost:8080/index/index.html
```

## 项目概述

本项目主要包含以下几部分能力：

- 省级看板聚合展示
- 防汛抗旱监测与历史查询
- 水位、天气、预警数据展示
- 水质水位预测与预警展示
- 水利工程与主要河流可视化展示

## 技术栈

- `Java 17`
- `Spring Boot 3.4.13`
- `Spring Web`
- `Spring Data JPA`
- `Spring Validation`
- `Spring WebSocket`
- `MyBatis-Plus 3.5.7`
- `MySQL`
- `Redis`
- `Maven`

## 模块说明

| 模块 | 说明 |
| --- | --- |
| `index` | 省级首页大屏与聚合接口 |
| `floodcontrol` | 防汛抗旱监测、历史查询、资源调度、WebSocket 推送 |
| `waterlevel` | 水位、站点、天气、预警接口 |
| `prediction` | 水质水位预测、监测数据、预警接口 |
| `waterproject` | 水利工程与主要河流展示 |

## 目录结构

```text
src/main/java/                  后端 Java 代码
src/main/resources/static/      前端静态页面
src/main/resources/sql/         手工建表与初始化数据脚本
src/main/resources/application.yml
src/main/resources/application-example.yml
```

## 页面入口

静态页面位于 `src/main/resources/static/`，常用访问地址如下：

- `http://localhost:8080/index/index.html`
- `http://localhost:8080/floodcontrol/Flood_Control_And_Drought_Relief.html`
- `http://localhost:8080/waterlevel/waterlevel.html`
- `http://localhost:8080/prediction/prediction.html`
- `http://localhost:8080/waterproject/waterproject.html`

## 环境要求

启动前请先准备以下环境：

- `JDK 17`
- `Maven`
- `MySQL`
- `Redis`

## 快速开始

### 1. 克隆项目

```bash
git clone <你的仓库地址>
cd ChinaHydrologicalMonitoringPlatform
```

### 2. 创建数据库

项目默认数据库名为 `china_hydrology_monitor`。请先在本地 MySQL 中手工创建数据库：

```sql
CREATE DATABASE china_hydrology_monitor
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### 3. 手工创建表并导入初始化数据

当前仓库没有接入 `Flyway` 或 `Liquibase`，数据库、表结构和初始化数据需要用户自己导入。

SQL 脚本位于 `src/main/resources/sql/`。建议在空库中执行，或先做好数据备份。

建议按以下顺序导入：

#### floodcontrol

1. `src/main/resources/sql/floodcontrol/monitoring_station.sql`
2. `src/main/resources/sql/floodcontrol/monitoring_station_history.sql`
3. `src/main/resources/sql/floodcontrol/rescue_material_category.sql`
4. `src/main/resources/sql/floodcontrol/rescue_material.sql`
5. `src/main/resources/sql/floodcontrol/rescue_material_usage.sql`

#### prediction

1. `src/main/resources/sql/prediction/stations.sql`
2. `src/main/resources/sql/prediction/monitoring_data.sql`
3. `src/main/resources/sql/prediction/warnings.sql`
4. `src/main/resources/sql/prediction/configs.sql`

#### waterlevel

1. `src/main/resources/sql/waterlevel/station.sql`
2. `src/main/resources/sql/waterlevel/water_level.sql`
3. `src/main/resources/sql/waterlevel/warning.sql`
4. `src/main/resources/sql/waterlevel/warnings.sql`
5. `src/main/resources/sql/waterlevel/weather.sql`

#### waterproject

1. `src/main/resources/sql/waterproject/water_project.sql`
2. `src/main/resources/sql/waterproject/major_river.sql`

### 4. 配置本地运行参数

请不要直接把真实凭据写入 `src/main/resources/application.yml`。

推荐做法：

1. 参考 `src/main/resources/application-example.yml`
2. 在 `src/main/resources/` 下新建 `application-local.yml`
3. 把本地数据库、Redis 和地图配置写入 `application-local.yml`

`application.yml` 会在启动时自动导入 `application-local.yml`。

示例：

```yaml
server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/china_hydrology_monitor
    username: your_mysql_user
    password: your_mysql_password
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
  jpa:
    hibernate:
      ddl-auto: none

amap:
  key: your_amap_key
  security-js-code: your_amap_security_js_code
```

说明：

- `application-local.yml` 不应提交到仓库
- 如果缺少高德地图配置，相关地图页面可能无法正常显示

### 5. 启动 Redis 和 MySQL

启动项目之前，请确认：

- MySQL 已启动
- 数据库 `china_hydrology_monitor` 已存在
- 所有 SQL 脚本已成功执行
- Redis 已启动

### 6. 启动项目

开发模式启动：

```bash
mvn spring-boot:run
```

或先打包再运行：

```bash
mvn -DskipTests clean package
java -jar target/ChinaHydrologicalMonitoringPlatform-0.0.1-SNAPSHOT.jar
```

### 7. 访问项目

默认访问地址：

```text
http://localhost:8080
```

项目根路径会自动跳转到首页大屏。

## 配置项说明

项目支持通过环境变量覆盖部分配置：

| 环境变量 | 说明 |
| --- | --- |
| `SERVER_PORT` | 服务端口，默认 `8080` |
| `DB_URL` | MySQL 连接串 |
| `DB_USERNAME` | MySQL 用户名 |
| `DB_PASSWORD` | MySQL 密码 |
| `REDIS_HOST` | Redis 主机 |
| `REDIS_PORT` | Redis 端口 |
| `REDIS_DATABASE` | Redis 库编号 |
| `AMAP_KEY` | 高德地图 Key |
| `AMAP_SECURITY_JS_CODE` | 高德地图安全码 |

## 运行说明

- 项目主启动类为 `com.baluga.ChinaHydrologicalMonitoringPlatformApplication`
- `waterproject` 模块带有少量代码级初始化逻辑
- 其他模块主要依赖 `src/main/resources/sql/` 中的初始化脚本
- 如果没有导入 SQL，部分页面会出现空数据或接口无结果

## 常见问题

### 1. 启动时报数据库连接失败

请检查：

- `application-local.yml` 中的数据库地址、用户名、密码是否正确
- MySQL 服务是否已启动
- 数据库 `china_hydrology_monitor` 是否已创建

### 2. 启动后接口报表不存在

通常是因为没有手工导入 SQL 脚本，或者导入的库不是当前连接的数据库。

### 3. 页面打开后没有地图或地图空白

通常是高德地图配置缺失，请检查：

- `AMAP_KEY`
- `AMAP_SECURITY_JS_CODE`

### 4. 启动时报 Redis 连接失败

请确认本地 Redis 已启动，并且配置的主机、端口、库编号正确。

## 开发建议

- 本地开发时，把真实配置放到 `application-local.yml`
- 不要提交本地数据库账号、密码或第三方地图密钥
- 提交前先确认没有把本地运行文件或缓存目录带入版本库
