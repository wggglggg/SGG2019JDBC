package com.jdbc201.transaction;

import com.jdbc201.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: TransactionTest
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/7 7:52
 * @Version 1.0
 */
public class TransactionTest {

    //******************未考虑数据库事务情况下的转账操作**************************
    /*
     * 针对于数据表user_table来说：
     * AA用户给BB用户转账100
     *
     * update user_table set balance = balance - 100 where user = 'AA';
     * update user_table set balance = balance + 100 where user = 'BB';
     */

    @Test
    public void testUpdate(){


        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        //模拟网络异常, 要用到事务transaction来解决转账途中出现错误时，要么同时执行，要么全部回滚
        System.out.println(0 / 0);

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");



    }

    //********************考虑数据库事务后的转账操作*********************
    @Test
    public void testUpdateTx(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            //1.取消数据的自动提交
            connection.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(connection,sql1, "AA");

            //模拟网络异常, 要用到事务transaction来解决转账途中出现错误时，要么同时执行，要么全部回滚
            System.out.println(0 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(connection,sql2, "BB");

            System.out.println("转账成功");

            //2.手动提交数据
            connection.commit();
            // 恢复自动提交数据
        } catch (Exception e) {
            e.printStackTrace();

            //3.回滚数据
            try {
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(connection,null );
        }
    }

    // 通用的增删改操作---version 2.0, 使用增删改时,传入连接,将连接固定到调用update前,
    // 这样一扣除金额与二增加金额用的是同一个连接（考虑上事务）
    public int update(Connection connection, String sql, Object...args) {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }


            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    // 通用的增删改操作---version 1.0
    public int update(String sql, Object...args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();


            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }


            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }

    @Test
    public void testGetInstance() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        String sql = "select * from user_table;";
        User user = getInstance(connection, User.class, sql);
        System.out.println(user);

    }
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            while (rs.next()){

                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

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

    @Test
    public void testGetList() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from user_table";
        List<User> list = getList(connection, User.class, sql);
        for (User u :
                list) {
            System.out.println(u);
        }
    }
    public <T> List<T> getList(Connection connection, Class<T> clazz, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            List<T> list = new ArrayList<>();

            while (rs.next()){

                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

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


}
