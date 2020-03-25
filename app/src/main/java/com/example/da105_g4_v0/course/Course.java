package com.example.da105_g4_v0.course;

import java.io.Serializable;
import java.sql.Timestamp;

public class Course implements Serializable {
    private String cour_no;
    private String ven_no;
    private String cour_name;
    private String cour_info;
    private Double cour_hours;
    private Timestamp cour_starttime;
    private Timestamp cour_endtime;
    private Timestamp cour_time;
    private Integer cour_price;
    private Integer cour_minjoin;
    private Integer cour_maxjoin;
    private String cour_status;
    private Integer val_count;
    private Integer val_total;
    private String cour_address;
    private Integer cour_count;

    public Course() {

    }

    public String getCour_no() {
        return cour_no;
    }

    public void setCour_no(String cour_no) {
        this.cour_no = cour_no;
    }

    public String getVen_no() {
        return ven_no;
    }

    public void setVen_no(String ven_no) {
        this.ven_no = ven_no;
    }

    public String getCour_name() {
        return cour_name;
    }

    public void setCour_name(String cour_name) {
        this.cour_name = cour_name;
    }

    public String getCour_info() {
        return cour_info;
    }

    public void setCour_info(String cour_info) {
        this.cour_info = cour_info;
    }

    public Double getCour_hours() {
        return cour_hours;
    }

    public void setCour_hours(Double cour_hours) {
        this.cour_hours = cour_hours;
    }

    public Timestamp getCour_starttime() {
        return cour_starttime;
    }

    public void setCour_starttime(Timestamp cour_starttime) {
        this.cour_starttime = cour_starttime;
    }

    public Timestamp getCour_endtime() {
        return cour_endtime;
    }

    public void setCour_endtime(Timestamp cour_endtime) {
        this.cour_endtime = cour_endtime;
    }

    public Timestamp getCour_time() {
        return cour_time;
    }

    public void setCour_time(Timestamp cour_time) {
        this.cour_time = cour_time;
    }

    public Integer getCour_price() {
        return cour_price;
    }

    public void setCour_price(Integer cour_price) {
        this.cour_price = cour_price;
    }

    public Integer getCour_minjoin() {
        return cour_minjoin;
    }

    public void setCour_minjoin(Integer cour_minjoin) {
        this.cour_minjoin = cour_minjoin;
    }

    public Integer getCour_maxjoin() {
        return cour_maxjoin;
    }

    public void setCour_maxjoin(Integer cour_maxjoin) {
        this.cour_maxjoin = cour_maxjoin;
    }

    public String getCour_status() {
        return cour_status;
    }

    public void setCour_status(String cour_status) {
        this.cour_status = cour_status;
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

    public String getCour_address() {
        return cour_address;
    }

    public void setCour_address(String cour_address) {
        this.cour_address = cour_address;
    }

    public Integer getCour_count() {
        return cour_count;
    }

    public void setCour_count(Integer cour_count) {
        this.cour_count = cour_count;
    }
}
