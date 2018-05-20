package top.inotwant.proxy;

/**
 * 代理接口
 */
public interface Proxy {

    /**
     * 链式处理操作
     *
     * @param proxyChain 描述 被代理者 对应的代理链
     */
     <T> Object doProxy(ProxyChain<T> proxyChain);

}
