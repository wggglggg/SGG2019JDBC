package com.jdbc205.dbutils.dao;

import com.jdbc202.bean.Customer;

import javax.xml.namespace.QName;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * ClassName: CustomerDAOImpl
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/7 20:25
 * @Version 1.0
 */
public class  CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO {
    @Override
    public void insert(Connection connection, Customer customer) {
        String sql = "insert into customers(name, email, birth) values(?,?,?)";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from customers where id = ?";
        update(connection, sql, id);
    }

    @Override
    public void update(Connection connection, Customer customer) {
        String sql = "update customers set name = ?, email = ?, birth = ? where id = ?";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());
    }

    //升级代码.去除Customer.class,让BaseDAO基类去做这件事,而且使用什么bean类清楚直接,不用每次都写
    @Override
    public Customer getCustomerById(Connection connection, int id) {
        String sql = "select id, name, email, birth from customers where id = ?";
        Customer customer = getBean(connection, sql, id);
        return customer;
    }

    //升级代码.去除Customer.class,让BaseDAO基类去做这件事,而且使用什么bean类清楚直接,不用每次都写
    @Override
    public List<Customer> getAll(Connection connection) {
        String sql = "select id, name, email, birth from customers";
        List<Customer> list = getBeanList(connection, sql); //
        return list;
    }


    @Override
    public Long getCount(Connection connection, Object...params) {
        String sql = "select count(*) from customers where name like ?";
        return (Long) getValue(connection, sql, params);

    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customers";
        return (Date) getValue(connection, sql);
    }


}
