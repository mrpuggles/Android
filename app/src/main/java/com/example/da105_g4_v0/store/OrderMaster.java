package com.example.da105_g4_v0.store;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class OrderMaster implements Serializable {

    private String order_no;
    private String mem_no;
    private Timestamp order_time;
    private Integer order_total;
    private String order_status;
    private List<OrderProduct> orderProductList;

    public OrderMaster() {}

    public OrderMaster(String order_no, String mem_no, Integer order_total, Timestamp order_time, List<OrderProduct> orderProductList) {
        this.order_no = order_no;
        this.mem_no = mem_no;
        this.order_total = order_total;
        this.order_time = order_time;
        this.orderProductList = orderProductList;
    }


    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public Integer getOrder_total() {
        return order_total;
    }

    public void setOrder_total(Integer order_total) {
        this.order_total = order_total;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
