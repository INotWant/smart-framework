package top.inotwant.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyManager {

    /**
     * 获取代理对象
     * @param targetClass
     * @param proxyList
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxyInstance(final Class<T> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain<>(targetClass, (T) targetObject, method, methodProxy, params, proxyList).doProxyChain();
            }
        });
    }

}
