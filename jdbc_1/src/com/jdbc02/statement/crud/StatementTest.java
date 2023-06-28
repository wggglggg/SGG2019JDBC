package com.jdbc02.statement.crud;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * ClassName: StatementTest
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/6/28 19:36
 * @Version 1.0
 */

public class StatementTest {

    // 使用Statement的弊端：需要拼写sql语句，并且存在SQL注入的问题
    // 如何避免出现sql注入：只要用 PreparedStatement(从Statement扩展而来) 取代 Statement
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名:");
        String user = scanner.next();
        System.out.print("请输入密码:");
        String password = scanner.next();

        //SELECT user,password FROM user_table WHERE user = '1' or ' AND password = '=1 or '1' = '1'
        String sql = "SELECT user, password FROM user_table WHERE user = '" + user +
                "' AND password = '" + password + "'";

        User returnUser = get(sql, User.class);

        if (returnUser != null) {
            System.out.println("登录成功");
        }else{
            System.out.println("用户名不存在或密码错误");
        }

    }


    public static <T> T get(String sql, Class<T> clazz ) throws Exception {

        Connection conn = null;

        Statement statement = null;

        ResultSet resultSet = null;

        try {
            // 1.加载配置文件
            InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);

            // 2.读取配置信息
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            // 3.加载驱动
            Class.forName(driverClass);

            // 4.获取连接
            conn = DriverManager.getConnection(url, user, password);

            statement = conn.createStatement();

            resultSet = statement.executeQuery(sql);

            // 获取结果集的元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();

            // 获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            if (resultSet.next()){  // Driver对象
                T t = clazz.getDeclaredConstructor().newInstance();

                // 从元数据中获取表格里第一列值
                for (int i = 0; i < columnCount; i++){

                    // //1. 获取列的名称
    //                String columnName = rsmd.getColumnName(i + 1);

                    // 1. 获取列的别名, 能过第二种方式获取
                    String columnName = rsmd.getColumnLabel(i + 1);

                    // 2. 根据列名获取对应数据表中的数据
                    Object columnVal = resultSet.getObject(columnName);

                    // 3. 将数据表中得到的数据，封装进对象, 通过反射获取user每个字段 ,并将字段权限设置为true
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnVal);

                }

                return t;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (statement != null)

                    statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }


}
