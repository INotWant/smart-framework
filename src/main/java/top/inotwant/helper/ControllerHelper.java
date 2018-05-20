package top.inotwant.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.annocation.Action;
import top.inotwant.bean.Handler;
import top.inotwant.bean.Request;
import top.inotwant.utils.ArrayUtil;
import top.inotwant.utils.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * controller 帮助类
 */
public final class ControllerHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(ControllerHelper.class);
    private final static Map<Request, Handler> CONTROLLER_MAP = new HashMap<>();

    static {
        LOGGER.warn("===================CONTROLLER HELPER======================");
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            for (Class<?> clazz : controllerClassSet) {
                Method[] methods = clazz.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {
                            Action actionAnn = method.getAnnotation(Action.class);
                            String mapping = actionAnn.value();
                            if (mapping.matches("\\w+:/\\w+")) {
                                String[] split = mapping.split(":");
                                Request request = new Request(split[0], split[1]);
                                Handler handler = new Handler(clazz, method);
                                CONTROLLER_MAP.put(request, handler);
                            } else {
                                LOGGER.warn("action mapping is a fault format: " + mapping);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 controller_map
     */
    public static Map<Request, Handler> getControllerMap() {
        return CONTROLLER_MAP;
    }

    /**
     * 获取 handler
     */
    public static Handler getHandler(Request request) {
        Handler handler = CONTROLLER_MAP.get(request);
        if (handler == null) {
            LOGGER.error("request method or path error.");
            return null;
        } else
            return handler;
    }
}
