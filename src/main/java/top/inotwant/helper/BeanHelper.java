package top.inotwant.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.utils.ReflectUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean 帮助类
 */
public final class BeanHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);
    private final static Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : beanClassSet) {
            BEAN_MAP.put(clazz, ReflectUtil.getInstance(clazz));
        }
    }

    /**
     * 获取 bean 映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }


    /**
     * 获取 bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        Object obj = BEAN_MAP.get(clazz);
        if (obj != null)
            return (T) obj;
        else {
            LOGGER.error("can't get bean by class: " + clazz.getSimpleName());
            throw new RuntimeException("can't get bean by class: " + clazz.getSimpleName());
        }
    }

    /**
     * 更改 bean
     */
    public static <T> void putBean(Class<T> clazz, Object obj){
        BEAN_MAP.put(clazz, obj);
    }
}
