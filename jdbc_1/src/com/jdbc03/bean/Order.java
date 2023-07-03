package com.jdbc03.bean;

import java.sql.Date;

/**
 * ClassName: Order
 * Description: 使用PreparedStatement实现针对于不同表的通用的查询操作
 *
 * @Author wggglggg
 * @Create 2023/7/3 7:53
 * @Version 1.0
 */
public class Order {
    private int orderId;
    private String orderName;
    private Date orderDate;
    private Long num;


    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Order(int orderId, String orderName, Date orderDate, Long num) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
        this.num = num;
    }

    public Order() {
    }

    public Order(int orderId, String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderDate=" + orderDate +
                ", num='" + num + '\'' +
                '}';
    }
}
