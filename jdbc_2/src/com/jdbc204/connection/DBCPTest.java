package com.jdbc204.connection;

import com.jdbc204.util.JDBCUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName: DBCPTest
 * Description:     测试DBCP的数据库连接池技术
 *
 * @Author wggglggg
 * @Create 2023/7/13 16:03
 * @Version 1.0
 */
public class DBCPTest {

    //方式一：不推荐
    @Test
    public void testConnection() throws SQLException {
        //创建了DBCP的数据库连接池
        BasicDataSource dataSource = new BasicDataSource();

        //设置基本信息
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///test");
        dataSource.setUsername("root");
        dataSource.setPassword("abc123");

        //还可以设置其他涉及数据库连接池管理的相关属性：
        dataSource.setInitialSize(10);
        dataSource.setMaxActive(10);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    //方式二：推荐：使用配置文件
    @Test
    public void testConnectionByProperties() throws Exception {
        FileInputStream fis = new FileInputStream(new File("src/dbcp.properties"));

        Properties properties = new Properties();
        properties.load(fis);

        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testConnectionByPropertiesWithUtils() throws SQLException {
        Connection dbcpConnection = JDBCUtils.getDBCPConnection();
        System.out.println(dbcpConnection);
    }
}
