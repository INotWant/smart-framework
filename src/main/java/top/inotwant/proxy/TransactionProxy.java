package top.inotwant.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.annocation.Aspect;
import top.inotwant.annocation.Service;
import top.inotwant.annocation.Transaction;
import top.inotwant.helper.DatabaseHelper;

import java.lang.reflect.Method;

/**
 * 事务代理
 */

@Aspect(Service.class)
public class TransactionProxy extends AspectProxy {

    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    @Override
    protected <T> boolean intercept(Class<T> targetClass, Method method, Object[] params) {
        // 判读是否需要 事务控制
        return method.isAnnotationPresent(Transaction.class);
    }

    @Override
    protected <T> void before(Class<T> targetClass, Method method, Object[] params) {
        // 开启事务
        LOGGER.info("[info] start transaction.");
        DatabaseHelper.startTransaction();
    }

    @Override
    protected <T> void after(Class<T> targetClass, Method method, Object[] params) {
        // 提交事务
        LOGGER.info("[info] commit transaction.");
        DatabaseHelper.commitTransaction();
    }

    @Override
    protected <T> void error(Class<T> targetClass, Method method, Object[] params) {
        // 回滚事务
        LOGGER.info("[info] rollback transaction.");
        DatabaseHelper.rollbackTransaction();
    }
}
