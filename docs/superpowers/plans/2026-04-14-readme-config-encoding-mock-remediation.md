# Monitoring Truthfulness Remediation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Remove committed secrets, align repository documentation with actual runtime behavior, fix touched user-visible encoding issues, and eliminate silent mock-data fallback from `waterlevel` and `prediction`.

**Architecture:** Keep the remediation bounded. Use file-based regression tests for documentation/config and static frontend guards, plus focused controller unit tests for `waterlevel` backend behavior. For frontend pages, replace simulated fallback with explicit empty/error states and real API wiring where the page already has backend endpoints available.

**Tech Stack:** Spring Boot 3.4.13, Java 17, JUnit 5, Mockito, MyBatis-Plus, Spring Data JPA, static HTML/JavaScript dashboards

---

### Task 1: Sanitize tracked config and rewrite README

**Files:**
- Create: `src/test/java/com/baluga/config/RepositorySanitizationTest.java`
- Create: `src/main/resources/application-example.yml`
- Modify: `src/main/resources/application.yml`
- Modify: `README.md`

**Files and responsibilities:**
- `src/test/java/com/baluga/config/RepositorySanitizationTest.java`
  Guards against reintroducing committed secrets and README/runtime drift.
- `src/main/resources/application.yml`
  Uses environment-backed placeholders instead of tracked secrets.
- `src/main/resources/application-example.yml`
  Documents safe local configuration values without exposing secrets.
- `README.md`
  Becomes the accurate UTF-8 entry document for maintainers.

- [ ] **Step 1: Write the failing regression test**

```java
package com.baluga.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class RepositorySanitizationTest {

    private static final Path APPLICATION_YML = Path.of("src/main/resources/application.yml");
    private static final Path README = Path.of("README.md");

    @Test
    void applicationYaml_usesEnvironmentPlaceholders_andDoesNotContainCommittedSecrets() throws IOException {
        String yaml = Files.readString(APPLICATION_YML, StandardCharsets.UTF_8);

        assertTrue(yaml.contains("${DB_URL:jdbc:mysql://localhost:3306/china_hydrology_monitor}"));
        assertTrue(yaml.contains("${DB_USERNAME:change_me}"));
        assertTrue(yaml.contains("${DB_PASSWORD:change_me}"));
        assertTrue(yaml.contains("${REDIS_HOST:localhost}"));
        assertTrue(yaml.contains("${REDIS_PORT:6379}"));
        assertTrue(yaml.contains("${AMAP_KEY:change_me}"));
        assertTrue(yaml.contains("${AMAP_SECURITY_JS_CODE:change_me}"));

        assertFalse(yaml.contains("Tvgai36183"));
        assertFalse(yaml.contains("a6d9aeded24e4453798fb33a0a736050"));
    }

    @Test
    void readme_matchesActualRuntimeFacts() throws IOException {
        String readme = Files.readString(README, StandardCharsets.UTF_8);

        assertTrue(readme.contains("Java 17"));
        assertTrue(readme.contains("Spring Boot 3.4.13"));
        assertTrue(readme.contains("8080"));
        assertTrue(readme.contains("/floodcontrol/Flood_Control_And_Drought_Relief.html"));
        assertTrue(readme.contains("DB_URL"));
        assertTrue(readme.contains("DB_USERNAME"));
        assertTrue(readme.contains("DB_PASSWORD"));
        assertTrue(readme.contains("AMAP_KEY"));
        assertTrue(readme.contains("AMAP_SECURITY_JS_CODE"));
    }
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```bash
mvn -Dtest=RepositorySanitizationTest test
```

Expected:

```text
FAILURE
Tests run: 2, Failures: 2
```

- [ ] **Step 3: Write the minimal implementation**

Replace `src/main/resources/application.yml` with:

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DB_URL:jdbc:mysql://localhost:3306/china_hydrology_monitor}
    username: ${DB_USERNAME:change_me}
    password: ${DB_PASSWORD:change_me}
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      database: ${REDIS_DATABASE:0}

  cache:
    type: redis
    redis:
      time-to-live: 4200000
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    serialization:
      write-dates-as-timestamps: false
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

amap:
  key: ${AMAP_KEY:change_me}
  security-js-code: ${AMAP_SECURITY_JS_CODE:change_me}

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.baluga.module.waterlevel.entity
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true

logging:
  level:
    com.baluga.module.waterlevel.mapper: DEBUG
```

Create `src/main/resources/application-example.yml` with:

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

amap:
  key: your_amap_key
  security-js-code: your_amap_security_js_code
```

Replace `README.md` with:

```md
# China Hydrological Monitoring Platform

中国水文监测可视化平台，基于 `Spring Boot 3.4.13` 和 `Java 17`。仓库同时包含后端接口和多个静态大屏页面。

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

Pages live under `src/main/resources/static/`:

- `/index/index.html`
- `/floodcontrol/Flood_Control_And_Drought_Relief.html`
- `/waterlevel/waterlevel.html`
- `/prediction/prediction.html`
- `/waterproject/waterproject.html`

## Configuration

Tracked source no longer stores real credentials. Set these environment variables before local startup:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `REDIS_HOST`
- `REDIS_PORT`
- `REDIS_DATABASE`
- `AMAP_KEY`
- `AMAP_SECURITY_JS_CODE`
- `SERVER_PORT`

See `src/main/resources/application-example.yml` for a safe local example.

## Local Start

```bash
mvn spring-boot:run
```

Or:

```bash
mvn -DskipTests clean package
java -jar target/ChinaHydrologicalMonitoringPlatform-0.0.1-SNAPSHOT.jar
```

## Notes

- `floodcontrol` is the most complete live-data workflow in the repository.
- `waterlevel` and `prediction` have contained demo-oriented frontend behavior; this remediation removes silent mock fallback from production-facing flows.
```

- [ ] **Step 4: Run the regression test again**

Run:

```bash
mvn -Dtest=RepositorySanitizationTest test
```

Expected:

```text
BUILD SUCCESS
Tests run: 2, Failures: 0, Errors: 0
```

- [ ] **Step 5: Commit**

```bash
git add README.md src/main/resources/application.yml src/main/resources/application-example.yml src/test/java/com/baluga/config/RepositorySanitizationTest.java
git commit -m "docs: sanitize config and align readme"
```

### Task 2: Remove backend mock-success fallback from `waterlevel` controllers

**Files:**
- Create: `src/test/java/com/baluga/module/waterlevel/controller/WaterLevelControllerFallbackTest.java`
- Modify: `src/main/java/com/baluga/module/waterlevel/controller/StationController.java`
- Modify: `src/main/java/com/baluga/module/waterlevel/controller/WaterLevelController.java`
- Modify: `src/main/java/com/baluga/module/waterlevel/controller/WarningController.java`

**Files and responsibilities:**
- `src/test/java/com/baluga/module/waterlevel/controller/WaterLevelControllerFallbackTest.java`
  Proves controller methods stop fabricating successful data on error.
- `StationController.java`
  Returns explicit error responses instead of simulated stations.
- `WaterLevelController.java`
  Returns explicit error or empty real results instead of simulated levels.
- `WarningController.java`
  Returns explicit error responses on failures instead of fake success.

- [ ] **Step 1: Write the failing controller regression test**

```java
package com.baluga.module.waterlevel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baluga.module.waterlevel.entity.Warning;
import com.baluga.module.waterlevel.service.IStationService;
import com.baluga.module.waterlevel.service.IWarningService;
import com.baluga.module.waterlevel.service.IWaterLevelService;
import com.baluga.module.waterlevel.util.Result;

@ExtendWith(MockitoExtension.class)
class WaterLevelControllerFallbackTest {

    @Mock
    private IStationService stationService;

    @Mock
    private IWaterLevelService waterLevelService;

    @Mock
    private IWarningService warningService;

    @InjectMocks
    private StationController stationController;

    @InjectMocks
    private WaterLevelController waterLevelController;

    @InjectMocks
    private WarningController warningController;

    @Test
    void getAllStations_whenServiceThrows_returnsErrorInsteadOfSimulatedStations() {
        when(stationService.list()).thenThrow(new RuntimeException("db down"));

        Result<?> result = stationController.getAllStations();

        assertEquals(500, result.getCode());
        assertNull(result.getData());
        assertTrue(result.getMsg().contains("站点"));
    }

    @Test
    void getLatestLevel_whenNoRecordExists_returnsErrorInsteadOfSimulatedLevel() {
        when(waterLevelService.getLatestLevel(1L)).thenReturn(null);

        Result<?> result = waterLevelController.getLatestLevel(1L);

        assertEquals(500, result.getCode());
        assertNull(result.getData());
        assertTrue(result.getMsg().contains("水位"));
    }

    @Test
    void getAllCurrentLevels_whenServiceThrows_returnsErrorInsteadOfSimulatedList() {
        when(waterLevelService.getAllCurrentLevels()).thenThrow(new RuntimeException("db down"));

        Result<?> result = waterLevelController.getAllCurrentLevels();

        assertEquals(500, result.getCode());
        assertNull(result.getData());
        assertTrue(result.getMsg().contains("水位"));
    }

    @Test
    void getAllWarnings_whenServiceThrows_returnsErrorInsteadOfEmptySuccess() {
        when(warningService.list()).thenThrow(new RuntimeException("db down"));

        Result<List<Warning>> result = warningController.getAllWarnings();

        assertEquals(500, result.getCode());
        assertNull(result.getData());
        assertTrue(result.getMsg().contains("预警"));
    }
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```bash
mvn -Dtest=WaterLevelControllerFallbackTest test
```

Expected:

```text
FAILURE
expected: <500> but was: <200>
```

- [ ] **Step 3: Write the minimal implementation**

Update `src/main/java/com/baluga/module/waterlevel/controller/StationController.java` methods to:

```java
@GetMapping("/all")
public Result<List<Station>> getAllStations() {
    try {
        return Result.success(stationService.list());
    } catch (Exception ex) {
        return Result.error("站点数据加载失败");
    }
}

@GetMapping("/search")
public Result<List<Station>> searchStations(String keyword) {
    try {
        if (keyword == null || keyword.isBlank()) {
            return Result.success(stationService.list());
        }
        return Result.success(stationService.getStationByName(keyword));
    } catch (Exception ex) {
        return Result.error("站点搜索失败");
    }
}
```

Update `src/main/java/com/baluga/module/waterlevel/controller/WaterLevelController.java` methods to:

```java
@GetMapping("/latest/{stationId}")
public Result<WaterLevel> getLatestLevel(@PathVariable Long stationId) {
    try {
        WaterLevel latest = waterLevelService.getLatestLevel(stationId);
        if (latest == null) {
            return Result.error("未找到该站点的最新水位数据");
        }
        return Result.success(latest);
    } catch (Exception ex) {
        return Result.error("水位数据加载失败");
    }
}

@GetMapping("/history/{stationId}")
public Result<List<WaterLevel>> getHistoryLevel(
        @PathVariable Long stationId,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
    try {
        return Result.success(waterLevelService.getHistoryLevel(stationId, startTime, endTime));
    } catch (Exception ex) {
        return Result.error("历史水位数据加载失败");
    }
}

@GetMapping("/current/all")
public Result<List<WaterLevel>> getAllCurrentLevels() {
    try {
        List<WaterLevel> levels = waterLevelService.getAllCurrentLevels();
        return Result.success(levels == null ? List.of() : levels);
    } catch (Exception ex) {
        return Result.error("实时水位数据加载失败");
    }
}
```

Remove these private helper methods entirely from `WaterLevelController.java`:

```java
private List<WaterLevel> generateSimulatedCurrentLevels()
private WaterLevel generateSimulatedLatestLevel(Long stationId)
private WaterLevel buildSimulatedLevel(...)
```

Update `src/main/java/com/baluga/module/waterlevel/controller/WarningController.java` methods to:

```java
@GetMapping("/list")
public Result<List<Warning>> getAllWarnings() {
    try {
        return Result.success(warningService.list());
    } catch (Exception ex) {
        return Result.error("预警数据加载失败");
    }
}

@GetMapping("/station/{stationId}")
public Result<List<Warning>> getWarningByStationId(@PathVariable Long stationId) {
    try {
        return Result.success(warningService.getWarningByStationId(stationId));
    } catch (Exception ex) {
        return Result.error("站点预警数据加载失败");
    }
}

@GetMapping("/unprocessed")
public Result<List<Warning>> getUnprocessedWarnings() {
    try {
        return Result.success(warningService.getUnprocessedWarnings());
    } catch (Exception ex) {
        return Result.error("未处理预警数据加载失败");
    }
}

@GetMapping("/level/{level}")
public Result<List<Warning>> getWarningByLevel(@PathVariable Integer level) {
    try {
        return Result.success(warningService.getWarningByLevel(level));
    } catch (Exception ex) {
        return Result.error("按等级加载预警失败");
    }
}
```

- [ ] **Step 4: Run the regression test again**

Run:

```bash
mvn -Dtest=WaterLevelControllerFallbackTest test
```

Expected:

```text
BUILD SUCCESS
Tests run: 4, Failures: 0, Errors: 0
```

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/baluga/module/waterlevel/controller/StationController.java src/main/java/com/baluga/module/waterlevel/controller/WaterLevelController.java src/main/java/com/baluga/module/waterlevel/controller/WarningController.java src/test/java/com/baluga/module/waterlevel/controller/WaterLevelControllerFallbackTest.java
git commit -m "fix: remove waterlevel mock fallback responses"
```

### Task 3: Remove silent fallback and garbled touched strings from `waterlevel.html`

**Files:**
- Create: `src/test/java/com/baluga/frontend/WaterlevelFrontendFallbackGuardTest.java`
- Modify: `src/main/resources/static/waterlevel/waterlevel.html`

**Files and responsibilities:**
- `src/test/java/com/baluga/frontend/WaterlevelFrontendFallbackGuardTest.java`
  Guards against reintroducing simulated real-time data in the page.
- `src/main/resources/static/waterlevel/waterlevel.html`
  Uses only real API responses, renders empty/error states explicitly, and fixes touched user-facing text.

- [ ] **Step 1: Write the failing static-page guard test**

```java
package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class WaterlevelFrontendFallbackGuardTest {

    private static final Path PAGE = Path.of("src/main/resources/static/waterlevel/waterlevel.html");

    @Test
    void waterlevelPage_doesNotRetainSimulationFallbackEntryPoints() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("simulateRealTimeData("));
        assertFalse(html.contains("updateWaterLevelDataWithSimulation("));
        assertFalse(html.contains("generateSimulatedWaterData("));
        assertTrue(html.contains("function renderNoDataState(message)"));
        assertTrue(html.contains("function fetchJson(url)"));
    }
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```bash
mvn -Dtest=WaterlevelFrontendFallbackGuardTest test
```

Expected:

```text
FAILURE
expected: <false> but was: <true>
```

- [ ] **Step 3: Write the minimal implementation**

Near the API utilities in `src/main/resources/static/waterlevel/waterlevel.html`, add:

```html
<script>
    const API_BASE_URL = '/api';

    async function fetchJson(url) {
        const response = await fetch(url);
        const payload = await response.json();
        if (!response.ok || !payload || payload.code !== 200) {
            throw new Error((payload && (payload.msg || payload.message)) || `请求失败: ${response.status}`);
        }
        return payload.data;
    }

    function showPageStatus(message, type = 'warning') {
        const color = type === 'error' ? '#ff4d4f' : '#faad14';
        console.warn(message);
        const alertList = document.getElementById('alertList');
        if (alertList) {
            alertList.innerHTML = `
                <div class="alert-item">
                    <div class="alert-details">
                        <div class="alert-title" style="color:${color};">${message}</div>
                        <div class="alert-desc">当前页面未使用模拟数据补位</div>
                    </div>
                    <div class="alert-time">--:--</div>
                </div>`;
        }
    }

    function renderNoDataState(message) {
        showPageStatus(message, 'warning');
        const alertCount = document.getElementById('alertCount');
        if (alertCount) {
            alertCount.textContent = '0 条';
        }
        updateWaterLevelData([]);
        updateWarningData([]);
    }
</script>
```

Replace `initAll()` boot logic so it no longer calls `simulateRealTimeData()`:

```js
async function initAll() {
    createTechParticles();
    createEnergyDots();
    initCharts();
    initMap();
    bindEvents();
    await fetchRealTimeData();
    realTimeUpdateInterval = setInterval(fetchRealTimeData, 10000);
}
```

Replace `fetchRealTimeData()` with:

```js
async function fetchRealTimeData() {
    const [waterResult, warningResult, stationResult] = await Promise.allSettled([
        fetchJson(`${API_BASE_URL}/waterlevel/data/current/all`),
        fetchJson(`${API_BASE_URL}/waterlevel/warning/unprocessed`),
        fetchJson(`${API_BASE_URL}/waterlevel/station/all`)
    ]);

    const waterLevels = waterResult.status === 'fulfilled' ? waterResult.value : [];
    const warnings = warningResult.status === 'fulfilled' ? warningResult.value : [];
    const stations = stationResult.status === 'fulfilled' ? stationResult.value : [];

    if (waterResult.status === 'rejected' && warningResult.status === 'rejected' && stationResult.status === 'rejected') {
        renderNoDataState('未加载到真实水位、站点和预警数据');
        return;
    }

    if (waterResult.status === 'rejected') {
        showPageStatus('未加载到真实水位数据');
    }
    if (warningResult.status === 'rejected') {
        showPageStatus('未加载到真实预警数据');
    }
    if (stationResult.status === 'rejected') {
        showPageStatus('未加载到真实站点数据');
    }

    updateWaterLevelData(Array.isArray(waterLevels) ? waterLevels : []);
    updateWarningData(Array.isArray(warnings) ? warnings : []);
    updateMapWithStations(Array.isArray(stations) ? stations : [], Array.isArray(waterLevels) ? waterLevels : []);
}
```

Update `updateWarningData` empty branch to:

```js
function updateWarningData(warnings) {
    const safeWarnings = Array.isArray(warnings) ? warnings : [];
    document.getElementById('alertCount').textContent = `${safeWarnings.length} 条`;

    const alertList = document.getElementById('alertList');
    alertList.innerHTML = '';

    if (safeWarnings.length === 0) {
        alertList.innerHTML = `
            <div class="alert-item">
                <div class="alert-details">
                    <div class="alert-title">暂无真实预警数据</div>
                    <div class="alert-desc">当前接口未返回可展示的预警记录</div>
                </div>
                <div class="alert-time">--:--</div>
            </div>`;
        return;
    }

    safeWarnings.slice(0, 5).forEach(warning => {
        // keep existing item rendering
    });
}
```

Update chart functions so empty arrays render text instead of fake series:

```js
function updateLevelChart(data) {
    if (!Array.isArray(data) || data.length === 0) {
        levelChart.setOption({
            title: {
                text: '暂无真实水位数据',
                left: 'center',
                top: 'center',
                textStyle: { color: '#80d4ff', fontSize: 12 }
            },
            xAxis: { data: [] },
            series: []
        });
        return;
    }

    // keep existing real-data rendering logic
}
```

Delete these functions entirely:

```js
function updateWaterLevelDataWithSimulation()
function generateSimulatedWaterData()
function simulateRealTimeData()
```

While touching the file, replace garbled visible strings in the edited regions with correct Chinese, including:

```text
动态水位监测平台
未加载到真实水位数据
未加载到真实预警数据
未加载到真实站点数据
暂无真实预警数据
当前接口未返回可展示的预警记录
```

- [ ] **Step 4: Run the static guard test again**

Run:

```bash
mvn -Dtest=WaterlevelFrontendFallbackGuardTest test
```

Expected:

```text
BUILD SUCCESS
Tests run: 1, Failures: 0, Errors: 0
```

- [ ] **Step 5: Commit**

```bash
git add src/main/resources/static/waterlevel/waterlevel.html src/test/java/com/baluga/frontend/WaterlevelFrontendFallbackGuardTest.java
git commit -m "fix: remove waterlevel frontend mock fallback"
```

### Task 4: Remove mock fallback and use real chart APIs in `prediction.html`

**Files:**
- Create: `src/test/java/com/baluga/frontend/PredictionFrontendFallbackGuardTest.java`
- Modify: `src/main/resources/static/prediction/prediction.html`

**Files and responsibilities:**
- `src/test/java/com/baluga/frontend/PredictionFrontendFallbackGuardTest.java`
  Guards against reintroducing mock-data bootstrapping in the prediction page.
- `src/main/resources/static/prediction/prediction.html`
  Uses real prediction data endpoints, explicit empty/error states, and no silent fallback.

- [ ] **Step 1: Write the failing static-page guard test**

```java
package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class PredictionFrontendFallbackGuardTest {

    private static final Path PAGE = Path.of("src/main/resources/static/prediction/prediction.html");

    @Test
    void predictionPage_doesNotRetainMockBootstrapFunctions() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("useMockData("));
        assertFalse(html.contains("generateMockWarnings("));
        assertFalse(html.contains("generatePredictionData("));
        assertTrue(html.contains("const DATA_API_BASE_URL = '/api/prediction/data';"));
        assertTrue(html.contains("async function loadChartData(stationId, days)"));
    }
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```bash
mvn -Dtest=PredictionFrontendFallbackGuardTest test
```

Expected:

```text
FAILURE
expected: <false> but was: <true>
```

- [ ] **Step 3: Write the minimal implementation**

Near existing API constants in `src/main/resources/static/prediction/prediction.html`, add:

```js
const API_BASE_URL = '/api/prediction/stations';
const WARNING_API_BASE_URL = '/api/prediction/warnings';
const DATA_API_BASE_URL = '/api/prediction/data';
```

Replace the data bootstrap error path so it does not call `useMockData()`:

```js
async function fetchData() {
    try {
        const [stationsResponse, warningsResponse] = await Promise.all([
            makeApiRequest(`${API_BASE_URL}/frontend/all`),
            makeApiRequest(`${WARNING_API_BASE_URL}/frontend/all`)
        ]);

        monitoringStations = Array.isArray(stationsResponse?.data) ? stationsResponse.data : [];
        warningData = Array.isArray(warningsResponse?.data) ? warningsResponse.data : [];

        if (!monitoringStations.length) {
            showNotification('未加载到真实站点数据', 'warning');
        }
        if (!warningData.length) {
            showNotification('未加载到真实预警数据', 'warning');
        }

        updateMapStatsFromStations();
        return true;
    } catch (error) {
        console.error('数据获取失败:', error);
        monitoringStations = [];
        warningData = [];
        showNotification('真实数据加载失败，请检查后端接口', 'error');
        updateMapStatsFromStations();
        return false;
    }
}
```

Add real chart loading helpers:

```js
async function loadChartData(stationId, days) {
    const [historicalResponse, predictionResponse] = await Promise.all([
        makeApiRequest(`${DATA_API_BASE_URL}/historical/${stationId}`, { days: 10 }),
        makeApiRequest(`${DATA_API_BASE_URL}/prediction/${stationId}`, { days })
    ]);

    return {
        historical: historicalResponse?.data || { dates: [], pH: [], DO: [], COD: [], level: [] },
        prediction: Array.isArray(predictionResponse?.data) ? predictionResponse.data : []
    };
}

function transformPredictionSeries(predictionRows) {
    const rows = Array.isArray(predictionRows) ? predictionRows : [];
    return {
        dates: rows.map(item => item.date || ''),
        pH: rows.map(item => Number(item.pH ?? null)),
        DO: rows.map(item => Number(item.dissolvedOxygen ?? null)),
        COD: rows.map(item => Number(item.cod ?? null)),
        level: rows.map(item => Number(item.waterLevel ?? null))
    };
}
```

Replace `updateChartData()` with:

```js
async function updateChartData() {
    const station = monitoringStations.find(s => s.id === selectedStationId);
    if (!station || !currentChart) return;

    const predictionDaysInput = document.getElementById('predictionDays');
    const days = predictionDaysInput ? parseInt(predictionDaysInput.value, 10) || 7 : 7;

    try {
        const chartData = await loadChartData(selectedStationId, days);
        renderChart(station, chartData.historical, transformPredictionSeries(chartData.prediction));
    } catch (error) {
        console.error('获取图表数据失败:', error);
        renderChart(station, { dates: [], pH: [], DO: [], COD: [], level: [] }, { dates: [], pH: [], DO: [], COD: [], level: [] });
        showNotification('未加载到真实历史/预测数据', 'warning');
    }
}
```

Replace `runPrediction()` with:

```js
async function runPrediction() {
    const station = monitoringStations.find(s => s.id === selectedStationId);
    if (!station) return;

    const predictionDaysInput = document.getElementById('predictionDays');
    const days = predictionDaysInput ? parseInt(predictionDaysInput.value, 10) || 7 : 7;
    const btn = document.getElementById('runPrediction');
    const originalText = btn.innerHTML;

    btn.innerHTML = '<span>...</span> 预测中';
    btn.disabled = true;

    try {
        await axios.post(`${DATA_API_BASE_URL}/prediction/run`, { stationId: selectedStationId, days });
        await updateChartData();
        showNotification(`已刷新未来 ${days} 天的真实预测数据`, 'success');
    } catch (error) {
        console.error('预测失败:', error);
        showNotification('预测接口执行失败', 'error');
    } finally {
        btn.innerHTML = originalText;
        btn.disabled = false;
    }
}
```

Delete these functions entirely:

```js
function generateMockWarnings(stations)
function getHistoricalData(station)
function useMockData()
function generateHistoricalData(station)
function generatePredictionData(station, days)
```

Update touched visible strings in edited regions to proper Chinese, including:

```text
中国水质水位预测可视化平台
未加载到真实站点数据
未加载到真实预警数据
真实数据加载失败，请检查后端接口
未加载到真实历史/预测数据
预测接口执行失败
```

- [ ] **Step 4: Run the static guard test again**

Run:

```bash
mvn -Dtest=PredictionFrontendFallbackGuardTest test
```

Expected:

```text
BUILD SUCCESS
Tests run: 1, Failures: 0, Errors: 0
```

- [ ] **Step 5: Commit**

```bash
git add src/main/resources/static/prediction/prediction.html src/test/java/com/baluga/frontend/PredictionFrontendFallbackGuardTest.java
git commit -m "fix: remove prediction frontend mock fallback"
```

### Task 5: Run full remediation verification

**Files:**
- Modify: `docs/superpowers/plans/2026-04-14-readme-config-encoding-mock-remediation.md`

**Files and responsibilities:**
- `docs/superpowers/plans/2026-04-14-readme-config-encoding-mock-remediation.md`
  Record any blockers discovered during verification before handoff.

- [ ] **Step 1: Run the targeted regression tests**

Run:

```bash
mvn -Dtest=RepositorySanitizationTest,WaterLevelControllerFallbackTest,WaterlevelFrontendFallbackGuardTest,PredictionFrontendFallbackGuardTest test
```

Expected:

```text
BUILD SUCCESS
Failures: 0, Errors: 0
```

- [ ] **Step 2: Run a compile/package verification**

Run:

```bash
mvn -DskipTests clean package
```

Expected:

```text
BUILD SUCCESS
```

- [ ] **Step 3: Run repository-level grep checks**

Run:

```bash
rg -n "Tvgai36183|a6d9aeded24e4453798fb33a0a736050" README.md src/main/resources/application.yml src/main/resources/application-example.yml src/main/resources/static
```

Expected:

```text
no matches
```

Run:

```bash
rg -n "simulateRealTimeData|generateSimulatedWaterData|updateWaterLevelDataWithSimulation|useMockData|generateMockWarnings|generatePredictionData" src/main/resources/static/waterlevel/waterlevel.html src/main/resources/static/prediction/prediction.html
```

Expected:

```text
no matches
```

- [ ] **Step 4: Perform manual browser checks**

Run:

```bash
mvn spring-boot:run
```

Manual checks:

```text
1. Open /waterlevel/waterlevel.html and confirm the page does not auto-fill fake data when the waterlevel APIs fail.
2. Open /prediction/prediction.html and confirm failed station/history/prediction calls show warnings or empty states instead of mock records.
3. Open / and confirm the root still redirects to /floodcontrol/Flood_Control_And_Drought_Relief.html.
4. Open /api/config/amap and confirm it returns placeholder or env-backed values rather than committed secrets.
```

Expected:

```text
Pages remain readable, no silent mock replacement appears, and edited Chinese text is readable in touched regions.
```

- [ ] **Step 5: Commit**

```bash
git add README.md src/main/resources/application.yml src/main/resources/application-example.yml src/main/resources/static/waterlevel/waterlevel.html src/main/resources/static/prediction/prediction.html src/test/java/com/baluga/config/RepositorySanitizationTest.java src/test/java/com/baluga/module/waterlevel/controller/WaterLevelControllerFallbackTest.java src/test/java/com/baluga/frontend/WaterlevelFrontendFallbackGuardTest.java src/test/java/com/baluga/frontend/PredictionFrontendFallbackGuardTest.java
git commit -m "chore: harden docs config and frontend data truthfulness"
```
