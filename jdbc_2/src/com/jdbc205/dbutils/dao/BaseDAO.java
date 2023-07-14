package com.jdbc205.dbutils.dao;

import com.jdbc204.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BaseDAO
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/7 15:38
 * @Version 1.0
 */
public abstract class BaseDAO<T> {
    // DBUtils.jar包中的QueryRunner类，操作数据库，下面都是基于这个类
    private QueryRunner queryRunner = new QueryRunner();

    private Class<T> clazz = null;
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        clazz = (Class) actualTypeArguments[0];

    }


    /**
     * 通用的增删改操作
     *
     * @param sql
     * @param params
     * @return
     */
    public int update(Connection connection, String sql, Object...params){
        int rows = 0;
        try {
            rows = queryRunner.update(connection, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 获取一个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public T getBean(Connection connection, String sql, Object...params){
        T t = null;
        try {
            t = queryRunner.query(connection, sql, new BeanHandler<>(clazz), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取所有对象
     *
     * @param sql
     * @param params
     * @return
     */
    public List<T> getBeanList(Connection connection, String sql, Object...params){
        List<T> list = null;
        try {
            list = queryRunner.query(connection, sql, new BeanListHandler<>(clazz), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取一个但一值得方法，专门用来执行像 select count(*)...这样的sql语句
     *
     * @param sql
     * @param params
     * @return
     */
    public Object getValue(Connection connection, String sql, Object...params){
        // 调用queryRunner的query方法获取一个单一的值
        Object value = null;
        try {
            value = queryRunner.query(connection, sql, new ScalarHandler<>(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return value;
    }
}
