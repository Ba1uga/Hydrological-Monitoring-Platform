package com.baluga.config;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class EncodingConfigurationGuardTest {

    private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    private static final Path APPLICATION_YML = Path.of("src/main/resources/application.yml");
    private static final Path POM = Path.of("pom.xml");
    private static final Path CONFIG_CONTROLLER = Path.of("src/main/java/com/baluga/module/prediction/controller/ConfigController.java");
    private static final List<Path> HTML_PAGES = List.of(
        Path.of("src/main/resources/static/index/index.html"),
        Path.of("src/main/resources/static/prediction/prediction.html"),
        Path.of("src/main/resources/static/waterlevel/waterlevel.html"),
        Path.of("src/main/resources/static/waterproject/waterproject.html"),
        Path.of("src/main/resources/static/floodcontrol/Flood_Control_And_Drought_Relief.html")
    );

    @Test
    void applicationYaml_forcesUtf8ServletEncoding() throws IOException {
        String yaml = Files.readString(APPLICATION_YML, StandardCharsets.UTF_8);

        assertTrue(yaml.contains("server:"));
        assertTrue(yaml.contains("servlet:"));
        assertTrue(yaml.contains("encoding:"));
        assertTrue(yaml.contains("enabled: true"));
        assertTrue(yaml.contains("charset: UTF-8"));
        assertTrue(yaml.contains("force: true"));
        assertTrue(yaml.contains("force-response: true"));
    }

    @Test
    void pom_declaresUtf8BuildAndReportingEncodings() throws IOException {
        String pom = Files.readString(POM, StandardCharsets.UTF_8);

        assertTrue(pom.contains("<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>"));
        assertTrue(pom.contains("<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>"));
    }

    @Test
    void configController_usesReadableUtf8Messages() throws IOException {
        String source = Files.readString(CONFIG_CONTROLLER, StandardCharsets.UTF_8);

        assertTrue(source.contains("获取配置失败"));
        assertTrue(source.contains("获取分类配置失败"));
        assertTrue(source.contains("配置不存在"));
        assertFalse(source.contains("閼惧嘲"));
        assertFalse(source.contains("闁板秶"));
    }

    @Test
    void primaryStaticHtmlPages_useUtf8Bom() throws IOException {
        for (Path page : HTML_PAGES) {
            byte[] bytes = Files.readAllBytes(page);
            assertTrue(bytes.length >= UTF8_BOM.length, page + " should not be empty");
            assertArrayEquals(
                UTF8_BOM,
                new byte[] {bytes[0], bytes[1], bytes[2]},
                page + " should start with an UTF-8 BOM"
            );
        }
    }

    @Test
    void primaryStaticHtmlPages_retainReadableChineseCopy() throws IOException {
        assertTrue(Files.readString(HTML_PAGES.get(0), StandardCharsets.UTF_8).contains("中国水文检测可视化平台"));
        assertTrue(Files.readString(HTML_PAGES.get(1), StandardCharsets.UTF_8).contains("水质水位预测中心"));
        assertTrue(Files.readString(HTML_PAGES.get(2), StandardCharsets.UTF_8).contains("动态水位监测中心"));
        assertTrue(Files.readString(HTML_PAGES.get(3), StandardCharsets.UTF_8).contains("重点水利工程监测中心"));
        assertTrue(Files.readString(HTML_PAGES.get(4), StandardCharsets.UTF_8).contains("防汛抗旱指挥调度中心"));
    }
}
