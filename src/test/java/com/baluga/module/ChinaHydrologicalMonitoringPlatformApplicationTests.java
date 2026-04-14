package com.baluga.module;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
class ChinaHydrologicalMonitoringPlatformApplicationTests {

    @Test
    void contextLoads() {
    }

}
