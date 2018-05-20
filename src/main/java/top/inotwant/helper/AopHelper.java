package top.inotwant.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.annocation.Aspect;
import top.inotwant.proxy.AspectProxy;
import top.inotwant.proxy.Proxy;
import top.inotwant.proxy.ProxyManager;
import top.inotwant.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * AOP 实现类
 */
public final class AopHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        LOGGER.warn("======================AOP HELPER=========================");

        Map<Class<?>, List<Proxy>> proxyMap;
        try {
            proxyMap = getProxyMap();
            addTransactionProxyMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> entry : proxyMap.entrySet()) {
                Class<?> sourceClass = entry.getKey();
                List<Proxy> proxyList = entry.getValue();
                Object proxyInstance = ProxyManager.getProxyInstance(sourceClass, proxyList);
                // 使用 “代理链” 替换 “被代理对象”
                BeanHelper.putBean(sourceClass, proxyInstance);
            }
        } catch (Exception e) {
            LOGGER.error("aop helper fail", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成 (被代理类,代理类集) 映射
     */
    public static Map<Class<?>, List<Proxy>> getProxyMap() throws Exception {
        Map<Class<?>, List<Proxy>> result = new HashMap<>();
        Set<Class<?>> proxySet = ClassHelper.getSubClassSet(AspectProxy.class);
        for (Class<?> proxyClass : proxySet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Class<? extends Annotation> value = aspect.value();
                if (!value.equals(Aspect.class)) {
                    Set<Class<?>> annotationClassSet = ClassHelper.getAnnotationClassSet(value);
                    for (Class<?> sourceClass : annotationClassSet) {
                        if (result.get(sourceClass) == null) {
                            List<Proxy> proxyList = new ArrayList<>();
                            proxyList.add((Proxy) proxyClass.newInstance());
                            result.put(sourceClass, proxyList);
                        } else {
                            result.get(sourceClass).add((Proxy) proxyClass.newInstance());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 添加 事务控制代理 映射
     */
    private static void addTransactionProxyMap(Map<Class<?>, List<Proxy>> proxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getServiceClassSet();
        for (Class<?> clazz : serviceClassSet) {
            List<Proxy> proxyList = proxyMap.get(clazz);
            if (proxyList == null) {
                proxyList = new ArrayList<>();
            }
            proxyList.add(new TransactionProxy());
            proxyMap.put(clazz, proxyList);
        }
    }


}
