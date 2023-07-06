package com.jdbc05.blob;

import com.jdbc05.blob.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * ClassName: InsertTest
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/6 16:15
 * @Version 1.0
 */

/*
        * 使用PreparedStatement实现批量数据的操作
        *
        * update、delete本身就具有批量操作的效果。
        * 此时的批量操作，主要指的是批量插入。使用PreparedStatement如何实现更高效的批量插入？
        *
        * 题目：向goods表中插入20000条数据
        * CREATE TABLE goods(
        id INT PRIMARY KEY AUTO_INCREMENT,
        NAME VARCHAR(25)
        );
        * 方式一：使用Statement
        * Connection conn = JDBCUtils.getConnection();
        * Statement st = conn.createStatement();
        * for(int i = 0; i < 20000; i++){
        *   Stirng sql = "insert into goods(name) values('name_ " + i + "')"
        *   st.execute(sql)
        * }
*/
public class InsertTest {
    //批量插入的方式二：使用PreparedStatement
    @Test
    public void testInsert1() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();

            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);

                ps.executeUpdate();
            }

            long end = System.currentTimeMillis();

            System.out.println("花费的时间为： " + (end - start));  // 18027
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, ps);

        }
    }

    /*
     * 批量插入的方式三：
     * 1.addBatch()、executeBatch()、clearBatch()
     * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
     * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
     * 3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     */

    @Test
    public void testInsert2() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();

            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);

            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1,"name_" + i);
                ps.executeBatch();

                if (i % 500 == 0){
                    ps.addBatch();
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费时间为：" + (end - start));  // 50550
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }
    }

    //批量插入的方式四：设置连接不允许自动提交数据
    @Test
    public void testInsert4() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();

            connection.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);

            for (int i = 1; i <= 1000000; i++) {

                ps.setObject(1, "name_" + i);

                ps.addBatch();

                if (i % 500 == 0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            connection.commit();
            connection.setAutoCommit(true);

            long end = System.currentTimeMillis();
            System.out.println("花费时间为: " + (end - start));  // 花费时间为: 4742
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }

    }
}
