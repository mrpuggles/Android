package com.example.da105_g4_v0.vendor;

import java.io.Serializable;
import java.sql.Date;

public class Vendor implements Serializable {

    private String ven_no;
    private String ven_id;
    private String ven_psw;
    private String ven_name;
    private String ven_address;
    private String ven_phone;
    private String ven_email;
    private String ven_intro;
    private String ven_account;
    private String ven_gui;
    private Date ven_regtime;
    private byte[] ven_photo;
    private String ven_status;

    public Vendor() {

    }

    public Vendor(String ven_no, String ven_id, String ven_psw, String ven_name, String ven_address, String ven_phone,
                    String ven_email, String ven_intro, String ven_account, String ven_gui, Date ven_regtime, byte[] ven_photo, String ven_status ) {
        this.ven_no=ven_no;
        this.ven_id = ven_id;
        this.ven_psw=ven_psw;
        this.ven_name=ven_name;
        this.ven_address = ven_address;
        this.ven_phone = ven_phone;
        this.ven_email = ven_email;
        this.ven_intro = ven_intro;
        this.ven_account=ven_account;
        this.ven_gui=ven_gui;
        this.ven_regtime=ven_regtime;
        this.ven_photo=ven_photo;
        this.ven_status=ven_status;
    }

    public String getVen_no() {
        return ven_no;
    }

    public void setVen_no(String ven_no) {
        this.ven_no = ven_no;
    }

    public String getVen_id() {
        return ven_id;
    }

    public void setVen_id(String ven_id) {
        this.ven_id = ven_id;
    }

    public String getVen_psw() {
        return ven_psw;
    }

    public void setVen_psw(String ven_psw) {
        this.ven_psw = ven_psw;
    }

    public String getVen_name() {
        return ven_name;
    }

    public void setVen_name(String ven_name) {
        this.ven_name = ven_name;
    }

    public String getVen_address() {
        return ven_address;
    }

    public void setVen_address(String ven_address) {
        this.ven_address = ven_address;
    }

    public String getVen_phone() {
        return ven_phone;
    }

    public void setVen_phone(String ven_phone) {
        this.ven_phone = ven_phone;
    }

    public String getVen_email() {
        return ven_email;
    }

    public void setVen_email(String ven_email) {
        this.ven_email = ven_email;
    }

    public String getVen_intro() {
        return ven_intro;
    }

    public void setVen_intro(String ven_intro) {
        this.ven_intro = ven_intro;
    }

    public String getVen_account() {
        return ven_account;
    }

    public void setVen_account(String ven_account) {
        this.ven_account = ven_account;
    }

    public String getVen_gui() {
        return ven_gui;
    }

    public void setVen_gui(String ven_gui) {
        this.ven_gui = ven_gui;
    }

    public Date getVen_regtime() {
        return ven_regtime;
    }

    public void setVen_regtime(Date ven_regtime) {
        this.ven_regtime = ven_regtime;
    }

    public byte[] getVen_photo() {
        return ven_photo;
    }

    public void setVen_photo(byte[] ven_photo) {
        this.ven_photo = ven_photo;
    }

    public String getVen_status() {
        return ven_status;
    }

    public void setVen_status(String ven_status) {
        this.ven_status = ven_status;
    }
}
