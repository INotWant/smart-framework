package top.inotwant.helper;

import top.inotwant.ConfigConstant;
import top.inotwant.utils.PropsUtil;

import java.util.Properties;

/**
 * 获取配置文件帮助类
 */

public final class ConfigHelper {

    private static final Properties configProperties = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取 jdbc 驱动
     */
    public static String getJdbcDriver() {
        return PropsUtil.getString(configProperties, ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取 jdbc url
     */
    public static String getJdbcUrl() {
        return PropsUtil.getString(configProperties, ConfigConstant.JDBC_URL);
    }

    /**
     * 获取 jdbc username （默认为 root）
     */
    public static String getJdbcUsername() {
        return PropsUtil.getString(configProperties, ConfigConstant.JDBC_USERNAME, "root");
    }

    /**
     * 获取 jdbc password
     */
    public static String getJdbcPassword() {
        return PropsUtil.getString(configProperties, ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取 web 应用的包
     */
    public static String getAppBasePackage() {
        return PropsUtil.getString(configProperties, ConfigConstant.BASE_PACKAGE);
    }

    /**
     * 获取 jsp 的存放路径 （默认为 jsp）
     */
    public static String getAppJspPath() {
        return PropsUtil.getString(configProperties, ConfigConstant.JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取 资源文件 的存放路径（默认为 asset）
     */
    public static String getAppAssetPath() {
        return PropsUtil.getString(configProperties, ConfigConstant.ASSET_PATH, "/asset/");
    }

    /**
     * 获取 上传文件的大小限制
     */
    public static int getAppUploadLimit() {
        return PropsUtil.getInteger(configProperties, ConfigConstant.APP_UPLOAD_LIMIT, 10);
    }

}
