package com.example.da105_g4_v0.bid;

import java.io.Serializable;
import java.sql.Timestamp;

public class Auction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String auc_no;
    private String ven_no;
    private String auc_name;
    private Integer auc_startprice;
    private Timestamp auc_starttime;
    private Timestamp auc_endtime;
    private Integer auc_bidincr;
    private Integer auc_endprice;
    private byte[] auc_photo;
    private String auc_status;
    private Integer auc_count;
    private String auc_intro;
    private String auc_ven_intro;
    private String showTime;

    public Auction(){}

    public String getAuc_intro() {
        return auc_intro;
    }
    public void setAuc_intro(String auc_intro) {
        this.auc_intro = auc_intro;
    }
    public String getAuc_ven_intro() {
        return auc_ven_intro;
    }
    public void setAuc_ven_intro(String auc_ven_intro) {
        this.auc_ven_intro = auc_ven_intro;
    }

    public String getAuc_no() {
        return auc_no;
    }
    public void setAuc_no(String auc_no) {
        this.auc_no = auc_no;
    }
    public String getVen_no() {
        return ven_no;
    }
    public void setVen_no(String ven_no) {
        this.ven_no = ven_no;
    }
    public String getAuc_name() {
        return auc_name;
    }
    public void setAuc_name(String auc_name) {
        this.auc_name = auc_name;
    }
    public Integer getAuc_startprice() {
        return auc_startprice;
    }
    public void setAuc_startprice(Integer auc_startprice) {
        this.auc_startprice = auc_startprice;
    }
    public Timestamp getAuc_starttime() {
        return auc_starttime;
    }
    public void setAuc_starttime(Timestamp auc_starttime) {
        this.auc_starttime = auc_starttime;
    }
    public Timestamp getAuc_endtime() {
        return auc_endtime;
    }
    public void setAuc_endtime(Timestamp auc_endtime) {
        this.auc_endtime = auc_endtime;
    }
    public Integer getAuc_bidincr() {
        return auc_bidincr;
    }
    public void setAuc_bidincr(Integer auc_bidincr) {
        this.auc_bidincr = auc_bidincr;
    }
    public Integer getAuc_endprice() {
        return auc_endprice;
    }
    public void setAuc_endprice(Integer auc_endprice) {
        this.auc_endprice = auc_endprice;
    }
    public byte[] getAuc_photo() {
        return auc_photo;
    }
    public void setAuc_photo(byte[] auc_photo) {
        this.auc_photo = auc_photo;
    }
    public String getAuc_status() {
        return auc_status;
    }
    public void setAuc_status(String auc_status) {
        this.auc_status = auc_status;
    }
    public Integer getAuc_count() {
        return auc_count;
    }
    public void setAuc_count(Integer auc_count) {
        this.auc_count = auc_count;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
