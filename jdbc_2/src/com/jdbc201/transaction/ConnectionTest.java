package com.jdbc201.transaction;

import com.jdbc201.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * ClassName: ConnectionTest
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/7 7:49
 * @Version 1.0
 */
public class ConnectionTest {
    @Test
    public void testGetConnection() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        System.out.println("connection = " + connection);
    }
}
