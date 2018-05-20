package top.inotwant.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.annocation.InJect;
import top.inotwant.utils.ArrayUtil;
import top.inotwant.utils.CollectionUtil;
import top.inotwant.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * ioc 实现类
 */
public final class IocHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(IocHelper.class);

    static {
        LOGGER.warn("======================IOC HELPER=========================");
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                Class<?> clazz = entry.getKey();
                Object obj = entry.getValue();
                // TODO 切勿调成 getFields() ，它不能获取私有属性
                Field[] fields = clazz.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(fields)) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(InJect.class)) {
                            Object newValue = BeanHelper.getBean(field.getType());
                            ReflectUtil.modifyField(obj, field, newValue);
                        }
                    }
                }
            }
        }
    }

}
