package top.inotwant.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 描述 被代理者 对应的代理链
 */
public class ProxyChain<T> {

    private final Class<T> targetClass;
    private final T targetObject;
    private final Method method;
    private final MethodProxy methodProxy;
    private final Object[] params;

    private List<Proxy> proxyList;
    private int index = 0;

    public ProxyChain(Class<T> targetClass, T targetObject, Method method, MethodProxy methodProxy, Object[] params, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.method = method;
        this.methodProxy = methodProxy;
        this.params = params;

        this.proxyList = proxyList;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public Method getMethod() {
        return method;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object[] getParams() {
        return params;
    }

    public T getTargetObject() {
        return targetObject;
    }

    /**
     * 配合 doProxy() 以及利用 index 实现 “链式代理”
     */
    public Object doProxyChain() throws Throwable {
        Object result;
        if (this.index >= proxyList.size()) {
            result = methodProxy.invokeSuper(targetObject, params);
            this.index = 0; // TODO 自己添加，为了实现 链式代理 的多次调用
        } else {
            result = proxyList.get(this.index++).doProxy(this);
        }
        return result;
    }
}
