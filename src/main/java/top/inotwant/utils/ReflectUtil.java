package top.inotwant.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射 帮助类
 */
public final class ReflectUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 获取类对应的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("get a instance about " + clazz.getSimpleName());
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... params) {
        method.setAccessible(true);
        try {
            return method.invoke(obj, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("invoke '" + method.getName() + "' fail");
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改对象的属性
     */
    public static void modifyField(Object obj, Field field, Object newValue) {
        field.setAccessible(true);
        try {
            field.set(obj, newValue);
        } catch (IllegalAccessException e) {
            LOGGER.error("modify '" + field.getName() + "' fail");
            throw new RuntimeException(e);
        }

    }

}
