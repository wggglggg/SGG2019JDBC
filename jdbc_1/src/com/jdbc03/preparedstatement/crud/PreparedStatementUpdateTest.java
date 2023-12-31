package com.jdbc03.preparedstatement.crud;

import com.jdbc03.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * ClassName: PreparedStatemUpdateTest
 * Description: 使用PreparedStatement来替换Statement,实现对数据表的增删改操作
 *
 * @Author wggglggg
 * @Create 2023/7/3 8:41
 * @Version 1.0
 */
public class PreparedStatementUpdateTest {

    @Test
    public void testCommonUpdate(){
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD", 2);
    }

    //通用的增删改操作
    public int update(String sql, Object...args){ //sql中占位符的个数与可变形参的长度相同！
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            connection = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]); //小心参数声明错误！！
            }
            //4.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }

}
