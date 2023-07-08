package com.jdbc202.dao;

import com.jdbc201.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ClassName: BaseDAO
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/7 15:38
 * @Version 1.0
 */
public abstract class BaseDAO {

    // 通用的增删改操作---version 2.0, 使用增删改时,传入连接,将连接固定到调用update前,
    // 这样一扣除金额与二增加金额用的是同一个连接（考虑上事务）
    public int update(Connection connection, String sql, Object...args) {

        PreparedStatement ps = null;
        try {
            // 1.预编译sql语句，返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);

            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }


            // 3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.资源的关闭
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    // 通用的查询操作，用于返回数据表中的一条记录（version 2.0：考虑上事务）
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            while (rs.next()){

                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnValue = rs.getObject(i + 1);
                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field filed = clazz.getDeclaredField(columnLabel);
                    filed.setAccessible(true);
                    filed.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);

        }

        return null;
    }

    // 通用的查询操作，用于返回数据表中的多条记录构成的集合（version 2.0：考虑上事务）
    public <T> List<T> getList(Connection connection, Class<T> clazz, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            // 创建集合对象
            List<T> list = new ArrayList<>();

            while (rs.next()){

                T t = clazz.getDeclaredConstructor().newInstance();
                // 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnValue = rs.getObject(i + 1);
                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field filed = clazz.getDeclaredField(columnLabel);
                    filed.setAccessible(true);
                    filed.set(t, columnValue);
                }
                list.add(t);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);

        }

        return null;
    }

    //用于查询特殊值的通用的方法
    public <E> E getValue(Connection connection, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();

            if (rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }


        return null;

    }
}
