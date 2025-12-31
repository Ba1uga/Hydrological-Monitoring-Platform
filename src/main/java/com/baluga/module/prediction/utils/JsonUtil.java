package com.baluga.module.prediction.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JsonUtil {

    private static ObjectMapper staticObjectMapper;
    private final ObjectMapper objectMapper;

    public JsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        staticObjectMapper = this.objectMapper;
    }

    public static String toJson(Object obj) {
        try {
            return staticObjectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败", e);
            return "{}";
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return staticObjectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON转对象失败", e);
            return null;
        }
    }

    public static Map<String, Object> jsonToMap(String json) {
        try {
            return staticObjectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON转Map失败", e);
            return null;
        }
    }

    public static List<Map<String, Object>> jsonToListMap(String json) {
        try {
            return staticObjectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON转List<Map>失败", e);
            return null;
        }
    }

    public static List<String> jsonToStringList(String json) {
        try {
            return staticObjectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON转List<String>失败", e);
            return null;
        }
    }
}
