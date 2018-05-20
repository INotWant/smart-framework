package top.inotwant.helper;

import top.inotwant.annocation.Controller;
import top.inotwant.annocation.Service;
import top.inotwant.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Class 帮助类
 */
public final class ClassHelper {

    private static Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.loadClassSet(basePackage);
    }

    /**
     * 加载 base package 下的所有类
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取加载的由 service 标注的所有类
     */
    public static Set<Class<?>> getServiceClassSet() {
        return getAnnotationClassSet(Service.class);
    }

    /**
     * 获取加载的由 controller 标注的所有类
     */
    public static Set<Class<?>> getControllerClassSet() {
        return getAnnotationClassSet(Controller.class);
    }

    /**
     * 获取框架管理的 bean （包含， service controller）
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> result = new HashSet<>();
        result.addAll(getServiceClassSet());
        result.addAll(getControllerClassSet());
        return result;
    }

    /**
     * 获取继承自指定类的所有类（不包括 “指定类” 本身）
     */
    public static Set<Class<?>> getSubClassSet(Class<?> clazz) {
        Set<Class<?>> result = new HashSet<>();
        for (Class<?> tClass : CLASS_SET) {
            if (clazz.isAssignableFrom(tClass) && !clazz.equals(tClass))
                result.add(tClass);
        }
        return result;
    }

    /**
     * 获取指定注解下的所有类
     */
    public static Set<Class<?>> getAnnotationClassSet(Class<? extends Annotation> clazz) {
        Set<Class<?>> result = new HashSet<>();
        for (Class<?> tClass : CLASS_SET) {
            if (tClass.isAnnotationPresent(clazz))
                result.add(tClass);
        }
        return result;
    }
}
