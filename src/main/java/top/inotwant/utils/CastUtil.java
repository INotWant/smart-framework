package top.inotwant.utils;

/**
 * 强转工具类
 */

public final class CastUtil {

    /**
     * 转为 String
     */
    public static String castString(Object obj, String defaultValue) {
        String value = defaultValue;
        if (obj != null) {
            value = String.valueOf(obj);
        }
        return value;
    }

    public static String castString(Object obj) {
        return castString(obj, "");
    }

    /**
     * 转为 double
     */
    public static double castDouble(Object obj, double defaultValue) {
        double value = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }

    /**
     * 转为 long
     */
    public static long castLong(Object obj, long defaultValue) {
        long value = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static long castLong(Object obj) {
        return castLong(obj, 0);
    }

    /**
     * 转为 int
     */
    public static int castInt(Object obj, int defaultValue) {
        int value = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }

    /**
     * 转为 boolean
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean value = defaultValue;
        if (obj != null) {
            value = Boolean.parseBoolean(castString(obj));
        }
        return value;
    }

    public static boolean castBoolean(Object obj) {
        return castBoolean(obj, false);
    }
}
