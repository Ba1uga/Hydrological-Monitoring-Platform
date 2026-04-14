# China Hydrological Monitoring Platform

中国水文监测可视化平台，基于 `Spring Boot 3.4.13` 和 `Java 17`。仓库同时包含后端接口与多个静态大屏页面。

## Runtime Facts

- Java: `17`
- Spring Boot: `3.4.13`
- Default port: `8080`
- Root path redirects to: `/floodcontrol/Flood_Control_And_Drought_Relief.html`

## Modules

- `index`: 省级看板聚合接口与首页大屏
- `floodcontrol`: 防汛抗旱监测、历史查询、资源调度、WebSocket 推送
- `waterlevel`: 水位、站点、天气、预警接口
- `prediction`: 站点统计、监测数据、预测预警接口
- `waterproject`: 水利工程与主要河流展示

## Static Pages

静态页面位于 `src/main/resources/static/`：

- `/index/index.html`
- `/floodcontrol/Flood_Control_And_Drought_Relief.html`
- `/waterlevel/waterlevel.html`
- `/prediction/prediction.html`
- `/waterproject/waterproject.html`

## Tech Stack

- Java 17
- Spring Boot 3.4.13
- Spring Web
- Spring Data JPA
- MyBatis-Plus
- MySQL
- Redis
- WebSocket + STOMP + SockJS

## Configuration

仓库不再跟踪真实凭据。启动前请通过环境变量提供配置：

- `SERVER_PORT`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `REDIS_HOST`
- `REDIS_PORT`
- `REDIS_DATABASE`
- `AMAP_KEY`
- `AMAP_SECURITY_JS_CODE`

可参考 `src/main/resources/application-example.yml` 作为本地示例配置。

## Local Start

环境要求：

- JDK 17
- Maven
- MySQL
- Redis

启动：

```bash
mvn spring-boot:run
```

或打包运行：

```bash
mvn -DskipTests clean package
java -jar target/ChinaHydrologicalMonitoringPlatform-0.0.1-SNAPSHOT.jar
```

默认访问地址：

```text
http://localhost:8080
```

## Notes

- `floodcontrol` 是当前仓库里最完整的实时数据链路。
- `waterlevel` 和 `prediction` 历史上包含演示性质较强的前端逻辑；本轮整改会优先移除静默 mock 回退，避免用伪造数据冒充真实运行状态。
