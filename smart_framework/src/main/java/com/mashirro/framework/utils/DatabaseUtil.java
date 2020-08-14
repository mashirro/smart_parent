package com.mashirro.framework.utils;


import com.mashirro.framework.pojo.ConfigConstants;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作工具类
 */
public class DatabaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();
    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();


    static {
        Properties myProp = PropsUtil.loadProps(ConfigConstants.SMART_FRAMEWORK_CONFIG_FILE);
        //获取当前配置的环境
        String propName = myProp.getProperty(ConfigConstants.SMART_FRAMEWORK_MULTI_PROFILE_CONFIG);
        Properties activeProp = PropsUtil.loadProps(propName.concat(ConfigConstants.PROPERTIES_SUFFIX));
        DATA_SOURCE.setDriverClassName(activeProp.getProperty(ConfigConstants.JDBC_DRIVER));
        DATA_SOURCE.setUrl(activeProp.getProperty(ConfigConstants.JDBC_URL));
        DATA_SOURCE.setUsername(activeProp.getProperty(ConfigConstants.JDBC_USERNAME));
        DATA_SOURCE.setPassword(activeProp.getProperty(ConfigConstants.JDBC_PASSWORD));
    }


    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
//        Connection coon = null;
//        try {
//            coon = DriverManager.getConnection(url, username, password);
//        } catch (SQLException e) {
//            logger.error("获取数据库连接出现异常!", e);
//            e.printStackTrace();
//        }
//        return coon;
        Connection coon = CONNECTION_HOLDER.get();
        if (coon == null) {
            try {
                //coon = DriverManager.getConnection(url, username, password);
                coon = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                logger.error("获取数据库连接出现异常!", e);
                e.printStackTrace();
            } finally {
                CONNECTION_HOLDER.set(coon);
            }
        }
        return coon;
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     */
//    public static void closeConnection(Connection connection) {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                logger.error("关闭数据库连接出现异常!", e);
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 关闭数据库连接
     */
//    public static void closeConnection() {
//        Connection coon = CONNECTION_HOLDER.get();
//        if (coon != null) {
//            try {
//                coon.close();
//            } catch (SQLException e) {
//                logger.error("关闭数据库连接出现异常!", e);
//                e.printStackTrace();
//            } finally {
//                CONNECTION_HOLDER.remove();
//            }
//        }
//    }


    /**
     * 查询实体列表
     *
     * @param coon        数据库连接
     * @param entityClass 实体Class对象
     * @param sql         sql语句
     * @param params      参数
     * @param <T>         泛型
     * @return
     */
//    public static <T> List<T> queryEntityList(Connection coon, Class<T> entityClass, String sql, Object... params) {
//        List<T> entityList = new ArrayList<>();
//        try {
//            entityList = QUERY_RUNNER.query(coon, sql, new BeanListHandler<T>(entityClass), params);
//        } catch (SQLException e) {
//            logger.error("查询实体列表出错!", e);
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        } finally {
//            closeConnection(coon);
//        }
//        return entityList;
//    }

    /**
     * 查询实体列表
     *
     * @param entityClass 实体Class对象
     * @param sql         sql语句
     * @param params      参数
     * @param <T>         泛型
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = new ArrayList<>();
        try {
            Connection coon = getConnection();
            entityList = QUERY_RUNNER.query(coon, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("查询实体列表出错!", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return entityList;
    }


    /**
     * 查询实体
     *
     * @param entityClass 实体Class对象
     * @param sql         sql语句
     * @param params      参数
     * @param <T>         泛型
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        try {
            Connection coon = getConnection();
            //BeanHandler:返回bean对象
            entity = QUERY_RUNNER.query(coon, sql, new BeanHandler<T>(entityClass), params);
        } catch (Exception e) {
            logger.error("查询实体失败!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return entity;
    }


    /**
     * 执行查询语句(查询不一定是基于单表的,有可能多表进行查询,需要提供一个更强大的查询方法)
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Connection coon = getConnection();
            result = QUERY_RUNNER.query(coon, sql, new MapListHandler(), params);
        } catch (Exception e) {
            logger.error("executeQuery执行异常!");
            e.printStackTrace();
        }
        return result;
    }
}
