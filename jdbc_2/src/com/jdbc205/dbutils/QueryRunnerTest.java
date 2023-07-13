package com.jdbc205.dbutils;

import com.jdbc202.bean.Customer;
import com.jdbc204.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * ClassName: QueryRunnerTest
 * Description:             * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 *
 * @Author wggglggg
 * @Create 2023/7/13 21:47
 * @Version 1.0
 */
public class QueryRunnerTest {

    //测试插入
    @Test
    public void testInsert() {
        Connection druidConnection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            druidConnection = JDBCUtils.getDruidConnection();
            String sql = "insert into customers(name,email, birth) values(?, ?, ?)";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long birth = sdf.parse("1975-5-3").getTime();
            int rows = queryRunner.update(druidConnection, sql, "蔡灿得", "caicande@163.com", new Date(birth));
            System.out.println(rows>0 ? "正常" : "失败");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(druidConnection, null);
        }
    }

    //测试查询
    /*
     * BeanHander:是ResultSetHandler接口的实现类，用于封装表中的一条记录。
     */
    @Test
    public void testBeanQuery(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getDruidConnection();
            String sql = "select * from customers where id = ?";

            BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);
            QueryRunner queryRunner = new QueryRunner();
            Customer customer = queryRunner.query(connection,sql, beanHandler, 24);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    /*
     * BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合。
     */

    @Test
    public void testBeanListHandler() {
        Connection druidConnection = null;
        try {
            druidConnection = JDBCUtils.getDruidConnection();
            String sql = "select * from customers where id < ?";

            QueryRunner queryRunner = new QueryRunner();
            BeanListHandler<Customer> beanListHandler = new BeanListHandler<>(Customer.class);

            List<Customer> list = queryRunner.query(druidConnection, sql, beanListHandler, 24);
            list.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(druidConnection,null);
        }
    }

    /*
     * MapHander:是ResultSetHandler接口的实现类，对应表中的一条记录。
     * 将字段及相应字段的值作为map中的key和value
     */
    @Test
    public void testMapHanderQuery(){
        Connection druidConnection = null;
        try {
            druidConnection = JDBCUtils.getDruidConnection();
            String sql = "select * from customers where id = ?";

            MapHandler mapHandler = new MapHandler();
            QueryRunner queryRunner = new QueryRunner();
            Map<String, Object> map = queryRunner.query(druidConnection, sql, mapHandler, 24);
            System.out.println("map = " + map);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(druidConnection, null);
        }
    }

    /*
     * MapListHander:是ResultSetHandler接口的实现类，对应表中的多条记录。
     * 将字段及相应字段的值作为map中的key和value。将这些map添加到List中
     */
    @Test
    public void testMapListHandlerQuery(){
        Connection druidConnection = null;
        try {
            druidConnection = JDBCUtils.getDruidConnection();
            String sql = "select * from customers where id < ?";

            MapListHandler mapListHandler = new MapListHandler();
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> list = queryRunner.query(druidConnection, sql, mapListHandler, 24);
            list.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(druidConnection, null);
        }
    }

    /*
     * ScalarHandler:用于查询特殊值
     */

    @Test
    public void testScalarHandlerQuery(){
        Connection druidConnection = null;
        try {
            druidConnection = JDBCUtils.getDruidConnection();
            String sql = "select count(*) from customers where name like ?";

            ScalarHandler scalarHandler = new ScalarHandler();
            QueryRunner queryRunner = new QueryRunner();
            Long count = (Long) queryRunner.query(druidConnection, sql, scalarHandler, "%李%");
            System.out.println("count = " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(druidConnection, null);
        }
    }
    /*
     * 自定义ResultSetHandler的实现类
     */
    @Test
    public void testQuery7(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidConnection();

            String sql = "select id,name,email,birth from customers where id = ?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<>(){

                @Override
                public Customer handle(ResultSet rs) throws SQLException {
//					System.out.println("handle");
//					return null;

//					return new Customer(12, "成龙", "Jacky@126.com", new Date(234324234324L));

                    if(rs.next()){
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        Date birth = rs.getDate("birth");
                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }
                    return null;

                }

            };
            Customer customer = runner.query(conn, sql, handler,23);
            System.out.println(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn, null);

        }

    }
}
