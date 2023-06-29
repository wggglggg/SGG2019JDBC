package com.jdbc03.preparedstatement.crud;

import com.jdbc02.util.JDBCUtils;
import com.jdbc03.bean.Customer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * ClassName: CustomerForQuery
 * Description:     针对于Customers表的查询操作
 *
 * @Author wggglggg
 * @Create 2023/6/29 18:03
 * @Version 1.0
 */
public class CustomerForQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "SELECT `name`, birth FROM customers WHERE `name` = ?;";
        Customer customer = queryForCustomers(sql, "林志玲");
        System.out.println(customer);
    }

    public Customer queryForCustomers(String sql, Object...args)  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);

            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if (rs.next()){
                Customer customer = new Customer();

                for (int i = 0; i < columnCount; i++) {
                    Object columValue = rs.getObject(1 + i);

                    String columnName = rsmd.getColumnName(1 + i);

                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }
}
