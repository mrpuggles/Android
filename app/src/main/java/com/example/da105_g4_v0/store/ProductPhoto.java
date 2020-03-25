package com.example.da105_g4_v0.store;

import java.io.Serializable;

public class ProductPhoto implements Serializable {
    private String photo_no;
    private String prod_no;
    private String photo_name;
//    private String photo_content;

    public ProductPhoto() {}

    public String getPhoto_no() {
        return photo_no;
    }

    public void setPhoto_no(String photo_no) {
        this.photo_no = photo_no;
    }

    public String getProd_no() {
        return prod_no;
    }

    public void setProd_no(String prod_no) {
        this.prod_no = prod_no;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

//    public String getPhoto_content() {
//        return photo_content;
//    }
//
//    public void setPhoto_content(String photo_content) {
//        this.photo_content = photo_content;
//    }
}