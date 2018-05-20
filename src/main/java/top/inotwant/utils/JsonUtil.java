package top.inotwant.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class JsonUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String toJson(T t) {
        try {
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            LOGGER.error("to json failure", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("from json failure", e);
            throw new RuntimeException(e);
        }
    }


}
