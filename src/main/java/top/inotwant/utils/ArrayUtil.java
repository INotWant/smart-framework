package top.inotwant.utils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类（封装 Arrayutils）
 */
public final class ArrayUtil {

    /**
     * 判断数组是否为空
     */
    public static boolean isEmpty(Object[] objects) {
        return ArrayUtils.isEmpty(objects);
    }

    /**
     * 判断数组是否不为空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }


}
