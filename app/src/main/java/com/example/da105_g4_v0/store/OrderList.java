package com.example.da105_g4_v0.store;

import java.io.Serializable;

public class OrderList implements Serializable {

    private String order_no;
    private String prod_no;
    private String ven_no;
    private Integer quantity;
    private String order_status;
    private Integer subtotal;

    public OrderList() {}

    public OrderList(String order_no, String prod_no, String ven_no, Integer quantity, String order_status, Integer subtotal) {
        super();
        this.order_no = order_no;
        this.prod_no = prod_no;
        this.ven_no = ven_no;
        this.quantity = quantity;
        this.order_status = order_status;
        this.subtotal = subtotal;
    }



    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getProd_no() {
        return prod_no;
    }

    public void setProd_no(String prod_no) {
        this.prod_no = prod_no;
    }

    public String getVen_no() {
        return ven_no;
    }

    public void setVen_no(String ven_no) {
        this.ven_no = ven_no;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubTotal(Integer subtotal) {
        this.subtotal = subtotal;
    }
}
