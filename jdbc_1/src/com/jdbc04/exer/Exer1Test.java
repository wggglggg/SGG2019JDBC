package com.jdbc04.exer;

import com.jdbc03.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * ClassName: Exer1Test
 * Description:
 *
 * @Author wggglggg
 * @Create 2023/7/3 8:51
 * @Version 1.0
 */
public class Exer1Test {

    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入邮箱：");
        String email = scanner.next();
        System.out.print("请输入生日：");
        String birthday = scanner.next();

        String sql = "insert into customers(name, email, birth) values(?,?,?)";
        int insertCount = update(sql, name, email, birthday);

        if (insertCount > 0){
            System.out.println("添加成功");

        }else{
            System.out.println("添加失败");
        }
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
