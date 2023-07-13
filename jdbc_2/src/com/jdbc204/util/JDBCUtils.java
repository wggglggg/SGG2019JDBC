package com.jdbc204.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * ClassName: JDBCUtils
 * Description:             获取数据库的连接
 *
 * @Author wggglggg
 * @Create 2023/7/13 15:50
 * @Version 1.0
 */
public class JDBCUtils {
    public static Connection getConnection() throws Exception {
        // 1.读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(is);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        // 2.加载驱动
        Class.forName(driverClass);

        // 3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     *
     * @Description 使用C3P0的数据库连接池技术
     * @author wggglggg
     * @return connection
     * @throws SQLException
     */
    //数据库连接池只需提供一个new 对象即可。
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0-config");
    public static Connection getC3P0Connection() throws SQLException {
        Connection connection = cpds.getConnection();
        return connection;
    }


    /**
     *
     * @Description 使用DBCP数据库连接池技术获取数据库连接
     * @author
     * @return
     * @throws Exception
     */
    //创建一个DBCP数据库连接池

    private static DataSource dataSource;
    static {
        FileInputStream fis = null;
        try {
            Properties properties = new Properties();
            fis = new FileInputStream(new File("src/dbcp.properties"));
            properties.load(fis);


            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public static Connection getDBCPConnection() throws SQLException {

        Connection connection = dataSource.getConnection();
        return connection;
    }

    /**
     * 使用Druid数据库连接池技术
     */

    private static  DataSource druidDataSource;
    static {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("src/druid.properties"));
            Properties properties = new Properties();
            properties.load(fis);

            druidDataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getDruidConnection() throws SQLException {
        Connection connection = druidDataSource.getConnection();
        return connection;
    }


    /**
     *
     * @Description 关闭连接和Statement的操作
     * @author
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn,Statement ps){
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
    /**
     *
     * @Description 关闭资源操作
     * @author
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
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
        try {
            if(rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
