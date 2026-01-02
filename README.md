# ChinaHydrologicalMonitoringPlatform

中国水文监测平台（Spring Boot 3 + Java 21）。后端提供多模块接口（首页省份看板 / 防汛抗旱 / 水位监测 / 预测预警 / 水利工程），前端页面以静态大屏为主（位于 `src/main/resources/static`）。

## 技术栈

- Java 21
- Spring Boot 3.4.13
- Web：spring-boot-starter-web
- 数据：Spring Data JPA、MyBatis-Plus（Boot3）、MySQL
- Redis：spring-boot-starter-data-redis
- WebSocket：STOMP + SockJS（`/ws`）

## 本地运行

### 1) 环境要求

- JDK 21
- Maven（项目未包含 `mvnw`/`mvnw.cmd`）
- MySQL（用于 MyBatis/JPA 模块的数据读写）
- Redis（如启用相关功能）

### 2) 配置

编辑 [application.yml](/ChinaHydrologicalMonitoringPlatform/src/main/resources/application.yml)：

- `server.port`：默认 `8083`
- `spring.datasource.*`：填写你的 MySQL 连接信息
- `spring.data.redis.*`：本地 Redis 默认 `localhost:6379`
- `amap.*`：高德地图 Key（前端地图相关会使用）

建议不要把真实账号口令提交到仓库；本地开发可用环境变量/私有配置覆盖。

### 3) 启动

```bash
mvn spring-boot:run
```

或打包运行：

```bash
mvn -DskipTests=true clean package
java -jar target/ChinaHydrologicalMonitoringPlatform-0.0.1-SNAPSHOT.jar
```

启动后默认访问：`http://localhost:8083`

## 前端页面（静态资源）

静态页面目录：`src/main/resources/static/`

- 首页大屏：`/index/index.html`
- 防汛抗旱指挥调度：`/floodcontrol/Flood_Control_And_Drought_Relief.html`
- 水位监测：`/waterlevel/waterlevel.html`
- 水利工程：`/waterproject/waterproject.html`
- 预测预警：`/prediction/prediction.html`

## 后端接口

### 首页省份看板（index 模块）

- `GET /api/province/dashboard?adcode=xxxx&name=可选`
  - 返回：站点类型分布、流量（按年序列）、水质雷达、重点工程等聚合数据
  - 代码入口：[ProvinceDashboardController](file:/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/module/index/controller/ProvinceDashboardController.java)

### 防汛抗旱大屏（floodcontrol 模块）

基础前缀：`/currentOverview`

- `GET /currentOverview/warningStations?mode=all|flood|drought`
- `GET /currentOverview/trend?mode=all|flood|drought`
- `GET /currentOverview/affectedArea?mode=all|flood|drought`
- `GET /currentOverview/stations?mode=all|flood|drought`
- `GET /currentOverview/currentHourStations?mode=all|flood|drought`
- `GET /currentOverview/dashboard-card?mode=all|flood|drought`
- `GET /currentOverview/realtime-card?mode=all|flood|drought`
- `GET /currentOverview/sevenDaysHistory?stationName=xxx&mode=all|flood|drought`

其他接口：

- 历史查询：`GET /history/list?startDate=yyyy-MM-dd HH:mm:ss&endDate=...&stationName=...`
- 物资：
  - `GET /resource/flood`
  - `GET /resource/drought`
  - `GET /resource/{mode}/details?category=xxx`（mode：`flood`/`drought`）

代码入口：[OverviewController](file:///d:/AAA_Mine/workSpace/%E9%A1%B9%E7%9B%AE/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/module/floodcontrol/controller/OverviewController.java)、[HistoryController](file:///d:/AAA_Mine/workSpace/%E9%A1%B9%E7%9B%AE/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/module/floodcontrol/controller/HistoryController.java)、[ResourceController](file:///d:/AAA_Mine/workSpace/%E9%A1%B9%E7%9B%AE/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/module/floodcontrol/controller/ResourceController.java)

### 水位监测（waterlevel 模块）

站点：

- `GET /api/waterlevel/station/all`
- `GET /api/waterlevel/station/search?keyword=xxx`

水位数据：

- `GET /api/waterlevel/data/latest/{stationId}`
- `GET /api/waterlevel/data/history/{stationId}?startTime=yyyy-MM-dd HH:mm:ss&endTime=yyyy-MM-dd HH:mm:ss`
- `GET /api/waterlevel/data/current/all`
- `POST /api/waterlevel/data/predict`
- `POST /api/waterlevel/data/add`
- `DELETE /api/waterlevel/data/{id}`

预警：

- `GET /api/waterlevel/warning/list`
- `GET /api/waterlevel/warning/station/{stationId}`
- `GET /api/waterlevel/warning/unprocessed`
- `GET /api/waterlevel/warning/level/{level}`
- `PUT /api/waterlevel/warning/process/{id}`
- `POST /api/waterlevel/warning/add`
- `DELETE /api/waterlevel/warning/{id}`

天气：

- `GET /api/waterlevel/weather/latest/all`
- `GET /api/waterlevel/weather/list`
- `GET /api/waterlevel/weather/location/{location}`
- `GET /api/waterlevel/weather/location/time?location=...&startTime=...&endTime=...`
- `GET /api/waterlevel/weather/latest/{location}`
- `POST /api/waterlevel/weather/add`
- `PUT /api/waterlevel/weather/update`
- `DELETE /api/waterlevel/weather/{id}`

### 预测预警（prediction 模块）

- 站点：
  - `GET /api/prediction/stations/frontend/all`
  - `GET /api/prediction/stations/{id}`
  - `GET /api/prediction/stations/code/{code}`
  - `GET /api/prediction/stations/frontend/stats`
  - `GET /api/prediction/stations/frontend/stats/map`
  - `GET /api/prediction/stations/frontend/stats/realtime`
  - `GET /api/prediction/stations/summary`
  - `GET /api/prediction/stations/{id}/history?days=可选`
  - `GET /api/prediction/stations/{id}/predict?days=可选`
  - `GET /api/prediction/stations/status/{status}`
  - `GET /api/prediction/stations/quality/{qualityClass}`
- 数据：
  - `GET /api/prediction/data/realtime/all`
  - `GET /api/prediction/data/historical/{stationId}?days=可选`
  - `GET /api/prediction/data/prediction/{stationId}?days=可选`
  - `POST /api/prediction/data/prediction/run`
- 预警：
  - `GET /api/prediction/warnings/frontend/all`
  - `GET /api/prediction/warnings/frontend/active`
  - `GET /api/prediction/warnings/frontend/stats`
  - `GET /api/prediction/warnings/station/{stationId}`
- 配置：
  - `GET /api/prediction/config/all`
  - `GET /api/prediction/config/category/{category}`
  - `GET /api/prediction/config/{key}`

### 水利工程（waterproject 模块）

- 工程：
  - `GET /api/projects`
  - `GET /api/projects/{id}`
  - `GET /api/projects/filter?tag=xxx`
- 河流：
  - `GET /api/rivers`

说明：该模块包含启动时的数据初始化逻辑（当库表为空时写入示例数据），入口：[DataInitializer](file:///d:/AAA_Mine/workSpace/%E9%A1%B9%E7%9B%AE/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/module/waterproject/config/DataInitializer.java)

## WebSocket（STOMP）

- 端点：`/ws`（SockJS）
- 应用前缀：`/app`
- 订阅前缀：`/topic`

示例主题（服务端推送）：

- `/topic/currentHourStations/{mode}`
- `/topic/dashboardCard/{mode}`
- `/topic/realTimeCard/{mode}`

消息示例：

- 客户端发送：`/app/message`
- 服务端广播：`/topic/response`

配置入口：[WebSocketConfig](file:///d:/AAA_Mine/workSpace/%E9%A1%B9%E7%9B%AE/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/config/WebSocketConfig.java)、处理器：[WebSocketHandler](file:///d:/AAA_Mine/workSpace/%E9%A1%B9%E7%9B%AE/ChinaHydrologicalMonitoringPlatform/src/main/java/com/baluga/module/floodcontrol/controller/WebSocketHandler.java)

## 数据库初始化（SQL）

仓库内提供了按模块划分的 SQL 脚本（位于 `src/main/resources/sql/`），可用于初始化表结构/示例数据：

- `sql/floodcontrol/*`
- `sql/prediction/*`
- `sql/waterlevel/*`
- `sql/waterproject/*`
