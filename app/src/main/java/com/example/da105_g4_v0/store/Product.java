package com.example.da105_g4_v0.store;

import java.io.Serializable;

public class Product implements Serializable {
    private String prod_no;
    private String ven_no;
    private String prod_name;
    private String prod_intro;
    private String prod_var;
    private String prod_regi;
    private Integer prod_year;
    private Integer prod_ml;
    private Double prod_perc;
    private Integer prod_price;
    private String prod_status;
    private Integer val_count;
    private Integer val_total;

    @Override
    // 要比對欲加入商品與購物車內商品的isbn是否相同，true則值相同
    public boolean equals(Object obj){
        if(this == obj){

            return true;
        }

        if(obj ==null || !(obj instanceof Product)){
            return false;
        }

        return this.getProd_no().equals(((Product) obj).getProd_no());
    }

    public Product() {}

    public Product(String prod_no, String ven_no, String prod_name, Integer prod_price) {
        this.prod_no = prod_no;
        this.ven_no = ven_no;
        this.prod_name = prod_name;
        this.prod_price = prod_price;
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

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_intro() {
        return prod_intro;
    }

    public void setProd_intro(String prod_intro) {
        this.prod_intro = prod_intro;
    }

    public String getProd_var() {
        return prod_var;
    }

    public void setProd_var(String prod_var) {
        this.prod_var = prod_var;
    }

    public String getProd_regi() {
        return prod_regi;
    }

    public void setProd_regi(String prod_regi) {
        this.prod_regi = prod_regi;
    }

    public Integer getProd_year() {
        return prod_year;
    }

    public void setProd_year(Integer prod_year) {
        this.prod_year = prod_year;
    }

    public Integer getProd_ml() {
        return prod_ml;
    }

    public void setProd_ml(Integer prod_ml) {
        this.prod_ml = prod_ml;
    }

    public Double getProd_perc() {
        return prod_perc;
    }

    public void setProd_perc(Double prod_perc) {
        this.prod_perc = prod_perc;
    }

    public Integer getProd_price() {
        return prod_price;
    }

    public void setProd_price(Integer prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_status() {
        return prod_status;
    }

    public void setProd_status(String prod_status) {
        this.prod_status = prod_status;
    }

    public Integer getVal_count() {
        return val_count;
    }

    public void setVal_count(Integer val_count) {
        this.val_count = val_count;
    }

    public Integer getVal_total() {
        return val_total;
    }

    public void setVal_total(Integer val_total) {
        this.val_total = val_total;
    }
}