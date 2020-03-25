package com.example.da105_g4_v0.bid;

import java.sql.Timestamp;

public class MemberBidInfo {

    private static final long serialVersionUID = 1L;

    private String bid_no;
    private String auc_no;
    private String mem_no;
    private Integer bid_price;
    private Timestamp  bid_time;

    public MemberBidInfo(){}

    public String getBid_no() {
        return bid_no;
    }
    public void setBid_no(String bid_no) {
        this.bid_no = bid_no;
    }
    public String getAuc_no() {
        return auc_no;
    }
    public void setAuc_no(String auc_no) {
        this.auc_no = auc_no;
    }
    public String getMem_no() {
        return mem_no;
    }
    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }
    public Integer getBid_price() {
        return bid_price;
    }
    public void setBid_price(Integer bid_price) {
        this.bid_price = bid_price;
    }
    public Timestamp getBid_time() {
        return bid_time;
    }
    public void setBid_time(Timestamp bid_time) {
        this.bid_time = bid_time;
    }
}