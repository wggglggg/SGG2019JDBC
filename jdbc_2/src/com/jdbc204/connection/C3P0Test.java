package com.jdbc204.connection;

import com.jdbc204.util.JDBCUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ClassName: C3P0Test
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/13 15:29
 * @Version 1.0
 */
public class C3P0Test {

    //方式一：
    @Test
    public void testGetConnection() throws PropertyVetoException, SQLException {
        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql:///test");
        cpds.setUser("root");
        cpds.setPassword("abc123");

        //通过设置相关的参数，对数据库连接池进行管理：
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection connection = cpds.getConnection();
        System.out.println(connection);

        //销毁c3p0数据库连接池
        DataSources.destroy(cpds);
    }

    //方式二：使用配置文件
    @Test
    public void testGetConnectionByXml() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0-config");
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testGetConnectionWithUtils() throws SQLException {
        Connection c3P0Connection = JDBCUtils.getC3P0Connection();
        System.out.println(c3P0Connection);
    }
}
