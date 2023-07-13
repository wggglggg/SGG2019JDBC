package com.jdbc204.connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.jdbc204.util.JDBCUtils;
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
 * ClassName: DruidTest
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/13 16:23
 * @Version 1.0
 */
public class DruidTest {

    @Test
    public void testConnection() throws Exception {
        FileInputStream fis = new FileInputStream(new File("src/druid.properties"));

        Properties properties = new Properties();
        properties.load(fis);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testDruidConnectionWithUtils() throws SQLException {
        Connection druidConnection = JDBCUtils.getDruidConnection();
        System.out.println(druidConnection);
    }
}
