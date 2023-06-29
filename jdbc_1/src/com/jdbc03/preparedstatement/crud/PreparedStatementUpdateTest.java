package com.jdbc03.preparedstatement.crud;


import com.jdbc02.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;

import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;


/**
 * ClassName: PreparedStatementUpdateTest
 * Description: 使用PreparedStatement来替换Statement,实现对数据表的增删改操作
 * 增删改；查
 *
 * @Author wggglggg
 * @Create 2023/6/29 7:32
 * @Version 1.0
 */
public class PreparedStatementUpdateTest {

    @Test
    public void testCommonUpdate(){
        String sql = "update `order` set order_name = ? where order_id = ?;";
        update(sql, "CC", "4");
    }

    //通用的增删改操作
    public void update(String sql, Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();

            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);

            //3.填充占位符
            for (int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);
            }

            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }

    //修改customers表的一条记录， 使用JDBCJunit包后的方法，获取配置信息与创建Connection在JDBCJunit包完成
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;


        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();

            //2.预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers SET email = ? WHERE id = ?;";
            ps = conn.prepareStatement(sql);

            //3.填充占位符
            ps.setObject(1, "caixingjuan@163.com");
            ps.setObject(2, 19);

            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }

    // 向customers表中添加一条记录, 没有写JDBCJunit包时最初方法
    @Test
    public void testInsert() {


        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 1.读取配置文件中的4个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            // 2.加载驱动
            Class.forName(driverClass);

            // 3.获取连接
            conn = DriverManager.getConnection(url, user, password);

//		System.out.println(conn);

            //4.预编译sql语句，返回PreparedStatement的实例
            String sql = "INSERT INTO customers(NAME, email, birth) VALUES(?,?,?);";//?:占位符
            ps = conn.prepareStatement(sql);
            //5.填充占位符
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");
            ps.setDate(3, new Date(date.getTime()));

            //6.执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //7.资源的关闭
            try {
                if(ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
