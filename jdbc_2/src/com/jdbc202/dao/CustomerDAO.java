package com.jdbc202.dao;

import com.jdbc202.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * ClassName: CustomerDAO
 * Description:
 *          此接口用于规范针对于customers表的常用操作
 * @Author wggglggg
 * @Create 2023/7/7 20:16
 * @Version 1.0
 */
public interface CustomerDAO {

    /**
     * 将cust对象添加到数据库中
     * @param connection
     * @param customer
     */
    void insert(Connection connection, Customer customer);

    /**
     * 针对指定的id，删除表中的一条记录
     * @param connection
     * @param id
     */
    void deleteById(Connection connection, int id);

    /**
     * 针对内存中的cust对象，去修改数据表中指定的记录
     * @param connection
     * @param customer
     */
    void update(Connection connection, Customer customer);

    /**
     * 针对指定的id查询得到对应的Customer对象
     * @param connection
     * @param id
     * @return
     */
    Customer getCustomerById(Connection connection, int id);

    /**
     * 查询表中的所有记录构成的集合
     * @param connection
     * @return
     */
    List<Customer> getAll(Connection connection);

    /**
     * 返回数据表中的数据的条目数
     * @param connection
     * @return
     */
    Long getCount(Connection connection);

    /**
     * 返回数据表中最大的生日
     * @param connection
     * @return
     */
    Date getMaxBirth(Connection connection);
}
