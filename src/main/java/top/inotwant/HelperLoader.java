package top.inotwant;

import top.inotwant.helper.*;
import top.inotwant.utils.ClassUtil;

/**
 * 加载相关的 helper 类
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classes = {
                ConfigHelper.class,
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> clazz : classes) {
            ClassUtil.loadClass(clazz.getName());
        }
    }

}
