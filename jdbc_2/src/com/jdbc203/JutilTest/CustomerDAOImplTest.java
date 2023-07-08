package com.jdbc203.JutilTest;

import com.jdbc201.util.JDBCUtils;
import com.jdbc202.bean.Customer;
import com.jdbc203.dao.CustomerDAOImpl;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * ClassName: CustomerDAOImplTest
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/7 20:37
 * @Version 1.0
 */
public class CustomerDAOImplTest {
    CustomerDAOImpl customerDAO = new CustomerDAOImpl();



    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Long time = sdf.parse("1975-1-17").getTime();
            Customer customer = new Customer(7, "李玟", "liwen@123.com", new Date(time));

            customerDAO.insert(connection, customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }


    }

    @Test
    public void testDeleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            customerDAO.deleteById(connection, 20);

            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testUpdate() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long time = sdf.parse("1975-01-17").getTime();
            Customer customer = new Customer(22, "李玟", "liwen@123.com", new Date(time));

            customerDAO.update(connection, customer);

            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,null);
        }


    }

    @Test
    public void testGetCustomerById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Customer customer = customerDAO.getCustomerById(connection, 22);
            System.out.println(customer);
            System.out.println("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,null);
        }

    }

    @Test
    public void testGetAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            List<Customer> list = customerDAO.getAll(connection);

            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);


        }

    }

    @Test
    public void testGetCount(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Long count = customerDAO.getCount(connection);

            System.out.println("表中的记录数为：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testGetMaxBirth() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Date maxBirth = customerDAO.getMaxBirth(connection);

            System.out.println("最大的生日为：" + maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }
}
