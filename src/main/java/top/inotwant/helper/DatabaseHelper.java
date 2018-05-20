package top.inotwant.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.inotwant.utils.CollectionUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final ThreadLocal<Connection> CONN_THREAD_LOCAL = new ThreadLocal<>();

    // DB util
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    // 数据库连接池 (DBPC)
    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();

    static {
        DRIVER = ConfigHelper.getJdbcDriver();
        URL = ConfigHelper.getJdbcUrl();
        USERNAME = ConfigHelper.getJdbcUsername();
        PASSWORD = ConfigHelper.getJdbcPassword();

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can't load jdbc driver", e);
        }

        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }

    /**
     * 从数据库连接池中获取连接
     */
    public static Connection getConnection() {
        Connection conn = CONN_THREAD_LOCAL.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                // 要在终端查看原因
                throw new RuntimeException(e);
            } finally {
                CONN_THREAD_LOCAL.set(conn);
            }
        }
        return conn;
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            // 这里重新抛出 运行时异常 ，可保证 `return` 不在执行。
            throw new RuntimeException(e);
        }
        return entity;
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> resultList;
        try {
            Connection conn = getConnection();
            resultList = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return resultList;
    }


    public static int execUpdate(String sql, Object... params) {
        int rows;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can't insert entity: fieldMap is empty");
            return false;
        }
        StringBuilder sbValues = new StringBuilder();
        sbValues.append("(");
        StringBuilder sbColumns = new StringBuilder("insert into `").append(getTableName(entityClass)).append("` (");
        for (String column : fieldMap.keySet()) {
            sbColumns.append(column).append(", ");
            sbValues.append("?, ");
        }
        sbColumns.replace(sbColumns.lastIndexOf(", "), sbColumns.length(), " )");
        sbValues.replace(sbValues.lastIndexOf(", "), sbValues.length(), " )");
        String sql = sbColumns + " values " + sbValues;

        Object[] params = fieldMap.values().toArray();

        return 1 == execUpdate(sql, params);
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can't update entity: fieldMap is empty");
            return false;
        }
        String sql = "update `" + getTableName(entityClass) + "` set ";
        StringBuilder sbColumn = new StringBuilder();
        for (String column : fieldMap.keySet()) {
            sbColumn.append(column).append("=?, ");
        }
        sql += sbColumn.substring(0, sbColumn.lastIndexOf(", ")) + " where id=?";
        List<Object> paramList = new ArrayList<>(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return 1 == execUpdate(sql, params);
    }

    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "delete from `" + getTableName(entityClass) + "` where id=?";
        return 1 == execUpdate(sql, id);
    }

    /**
     * 获取表名
     */
    private static String getTableName(Class entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }

    /**
     * 执行 sql 脚本
     */
    public static void executeSqlFile(String sFileName) {
        try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(sFileName)))) {
            String line;
            while ((line = bfReader.readLine()) != null) {
                execUpdate(line);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(sFileName + " file can't find", e);
        } catch (IOException e) {
            LOGGER.error("load sql file failed", e);
        }

    }

    /**
     * 开启事务
     */
    public static void startTransaction() {
        Connection conn = DatabaseHelper.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.error("start transaction fail", e);
            throw new RuntimeException(e);
        } finally {
            CONN_THREAD_LOCAL.set(conn);
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = DatabaseHelper.getConnection();
        try {
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("commit transaction fail", e);
            throw new RuntimeException(e);
        } finally {
            CONN_THREAD_LOCAL.remove();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = DatabaseHelper.getConnection();
        try {
            conn.rollback();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("rollback transaction fail", e);
            throw new RuntimeException(e);
        } finally {
            CONN_THREAD_LOCAL.remove();
        }
    }


}

