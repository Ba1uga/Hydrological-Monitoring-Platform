package com.baluga.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "app.scheduling.enabled=false",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:hydrology-test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.data.redis.repositories.enabled=false",
                "amap.key=test-key",
                "amap.security-js-code=test-security-code"
        }
)
class StaticHtmlEncodingIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void predictionPage_isServedAsUtf8Html() throws Exception {
        assertUtf8Html("/prediction/prediction.html", "水质水位预测中心");
    }

    @Test
    void otherDashboardPages_areServedAsUtf8Html() throws Exception {
        assertUtf8Html("/waterlevel/waterlevel.html", "动态水位监测中心");
        assertUtf8Html("/waterproject/waterproject.html", "中国水利工程可视化平台");
        assertUtf8Html("/floodcontrol/Flood_Control_And_Drought_Relief.html", "防汛抗旱指挥调度中心");
    }

    private void assertUtf8Html(String path, String marker) throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);
        String contentType = response.getHeaders().getContentType() != null
                ? response.getHeaders().getContentType().toString()
                : null;

        assertNotNull(contentType, path + " should declare a Content-Type");
        assertTrue(response.getStatusCode().is2xxSuccessful(), path + " should respond successfully");
        assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.TEXT_HTML));
        assertTrue(contentType.contains("charset=UTF-8"), path + " should be served as UTF-8");
        assertEquals(StandardCharsets.UTF_8.name(), response.getHeaders().getContentType().getCharset().name());
        assertNotNull(response.getBody(), path + " should return an HTML body");
        assertTrue(response.getBody().contains(marker));
    }
}
