package top.inotwant.utils;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null)
            str = str.trim();
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 分隔符
     */
    public static final String SEPARATOR = String.valueOf((char) 29);

}
