package com.example.da105_g4_v0.store;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class OrderProduct extends Product {

    private Integer quantity;

    public OrderProduct(String prod_no, String ven_no, String prod_name,  Integer prod_price , Integer quantity) {
        super(prod_no, ven_no, prod_name, prod_price);
        this.quantity = quantity;
    }

    public OrderProduct(Product product, Integer quantity) {
        this(product.getProd_no(), product.getVen_no(), product.getProd_name(), product.getProd_price(), quantity);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
