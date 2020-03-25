package com.example.da105_g4_v0.course;

import java.io.Serializable;

public class CoursePhoto implements Serializable {

    private String photo_no;
    private String cour_no;
    private String photo_name;
    private String photo_content;
    public CoursePhoto(){

    }
    public String getPhoto_no() {
        return photo_no;
    }
    public void setPhoto_no(String photo_no) {
        this.photo_no = photo_no;
    }
    public String getCour_no() {
        return cour_no;
    }
    public void setCour_no(String cour_no) {
        this.cour_no = cour_no;
    }
    public String getPhoto_name() {
        return photo_name;
    }
    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    public String getPhoto_content() {
        return photo_content;
    }

    public void setPhoto_content(String photo_content) {
        this.photo_content = photo_content;
    }
}
