package top.inotwant.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String pFileName) {
        Properties properties = null;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(pFileName)) {
            properties = new Properties();
            properties.load(is);
        } catch (FileNotFoundException e) {
            LOGGER.error(pFileName + " file isn't found! ");
        } catch (IOException e) {
            LOGGER.error("load properties file failure", e);
        }
        return properties;
    }

    /**
     * 获取字符型属性（默认值为 ‘空字符串’）
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    /**
     * 获取字符型属性（可指定默认值）
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.containsKey(key))
            value = properties.getProperty(key);
        return value;
    }

    /**
     * 获取整数类型属性（默认值为 0）
     */
    public static Integer getInteger(Properties properties, String key) {
        return getInteger(properties, key, 0);
    }

    /**
     * 获取整数类型属性（可指定默认值）
     */
    public static Integer getInteger(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if (properties.containsKey(key))
            value = CastUtil.castInt(properties.getProperty(key));
        return value;
    }


    /**
     * 获取字符型属性（默认值为 false）
     */
    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, false);
    }

    /**
     * 获取布尔型属性（可指定默认值）
     */
    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (properties.containsKey(key))
            value = CastUtil.castBoolean(properties.getProperty(key));
        return value;
    }

}
