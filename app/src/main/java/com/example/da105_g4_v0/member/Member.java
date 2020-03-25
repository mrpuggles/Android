package com.example.da105_g4_v0.member;

import java.io.Serializable;
import java.sql.Date;

public class Member implements Serializable {
    private String mem_no;
    private String mem_name;
    private String mem_birth;
    private String mem_phone;
    private String mem_id;
    private String mem_psw;
    private String mem_email;
    private java.sql.Date mem_regtime;
    private String mem_address;
    private Integer mem_photo;
    private Integer mem_unpaid;
    private String mem_auc_auth;
    private String mem_status;

    public Member(){

    }
    public Member(String mem_no, String mem_name, String mem_birth, String mem_phone, String mem_id, String mem_psw, String mem_email, Date mem_regtime, String mem_address, Integer mem_photo, Integer mem_unpaid, String mem_auc_auth, String mem_status) {
        this.mem_no = mem_no;
        this.mem_name = mem_name;
        this.mem_birth = mem_birth;
        this.mem_phone = mem_phone;
        this.mem_id = mem_id;
        this.mem_psw = mem_psw;
        this.mem_email = mem_email;
        this.mem_regtime = mem_regtime;
        this.mem_address = mem_address;
        this.mem_photo = mem_photo;
        this.mem_unpaid = mem_unpaid;
        this.mem_auc_auth = mem_auc_auth;
        this.mem_status = mem_status;
    }

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_birth() {
        return mem_birth;
    }

    public void setMem_birth(String mem_birth) {
        this.mem_birth = mem_birth;
    }

    public String getMem_phone() {
        return mem_phone;
    }

    public void setMem_phone(String mem_phone) {
        this.mem_phone = mem_phone;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_psw() {
        return mem_psw;
    }

    public void setMem_psw(String mem_psw) {
        this.mem_psw = mem_psw;
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public Date getMem_regtime() {
        return mem_regtime;
    }

    public void setMem_regtime(Date mem_regtime) {
        this.mem_regtime = mem_regtime;
    }

    public String getMem_address() {
        return mem_address;
    }

    public void setMem_address(String mem_address) {
        this.mem_address = mem_address;
    }

    public Integer getMem_photo() {
        return mem_photo;
    }

    public void setMem_photo(Integer mem_photo) {
        this.mem_photo = mem_photo;
    }

    public Integer getMem_unpaid() {
        return mem_unpaid;
    }

    public void setMem_unpaid(Integer mem_unpaid) {
        this.mem_unpaid = mem_unpaid;
    }

    public String getMem_auc_auth() {
        return mem_auc_auth;
    }

    public void setMem_auc_auth(String mem_auc_auth) {
        this.mem_auc_auth = mem_auc_auth;
    }

    public String getMem_status() {
        return mem_status;
    }

    public void setMem_status(String mem_status) {
        this.mem_status = mem_status;
    }
}
