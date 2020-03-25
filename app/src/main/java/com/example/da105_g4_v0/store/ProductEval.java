package com.example.da105_g4_v0.store;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProductEval implements Serializable {

    private String order_no;
    private String prod_no;
    private String mem_no;
    private Timestamp eval_time;
    private Integer eval_star;
    private String eval_content;

    public ProductEval() {}

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

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public Timestamp getEval_time() {
        return eval_time;
    }

    public void setEval_time(Timestamp eval_time) {
        this.eval_time = eval_time;
    }

    public Integer getEval_star() {
        return eval_star;
    }

    public void setEval_star(Integer eval_star) {
        this.eval_star = eval_star;
    }

    public String getEval_content() {
        return eval_content;
    }

    public void setEval_content(String eval_content) {
        this.eval_content = eval_content;
    }
}
