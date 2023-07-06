package com.jdbc05.blob;

import com.jdbc05.blob.bean.Customer;
import com.jdbc05.blob.util.JDBCUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * ClassName: BlobTest
 * Description: 测试使用PreparedStatement操作Blob类型的数据
 *
 * @Author wggglggg
 * @Create 2023/7/6 15:10
 * @Version 1.0
 */
public class BlobTest {
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        String sql = "insert into customers (name,email,birth,photo) values (?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, "李玟");
        ps.setObject(2, "liwen@126.com");
        ps.setObject(3, "1976-01-17");
        FileInputStream fis = new FileInputStream(new File("李玟-龙兄虎弟.jpg"));
        ps.setBlob(4, fis);     // Blob类型，无法用Object

        ps.execute();

        fis.close();
        JDBCUtils.closeResource(connection, ps);

    }

    @Test
    public void testQuery() throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, 22);

            rs = ps.executeQuery();

            if (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                Blob phone = rs.getBlob("photo");
                is = phone.getBinaryStream();
                fos = new FileOutputStream("李玟.jpg");

                byte[] buffer = new byte[1024];     // 一次1024字节
                int len;
                while(( len = is.read(buffer) )!= -1){
                    fos.write(buffer,0, len);

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            fos.close();
            is.close();
            JDBCUtils.closeResource(connection, ps, rs);
        }


    }

}
