package top.inotwant.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Proxy 的 “模板类”
 */
public abstract class AspectProxy implements Proxy {

    private final static Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    // 这里处理了 doProxyChain() 抛出的异常
    @Override
    public <T> Object doProxy(ProxyChain<T> proxyChain) {
        Class<T> targetClass = proxyChain.getTargetClass();
        Method method = proxyChain.getMethod();
        Object[] params = proxyChain.getParams();
        Object result;
        try {
            // 过滤
            if (intercept(targetClass, method, params)) {
                before(targetClass, method, params);
                result = proxyChain.doProxyChain();
                after(targetClass, method, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable e) {
            error(targetClass, method, params);
            LOGGER.error("aspect proxy fail", e);
            throw new RuntimeException(e);
        } finally {
            end();
        }

        return result;
    }

    protected <T> boolean intercept(Class<T> targetClass, Method method, Object[] params) {
        return true;
    }

    protected <T> void before(Class<T> targetClass, Method method, Object[] params) {

    }

    protected <T> void after(Class<T> targetClass, Method method, Object[] params) {

    }

    protected <T> void error(Class<T> targetClass, Method method, Object[] params) {

    }

    protected void end() {

    }


}
