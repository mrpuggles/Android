package com.example.da105_g4_v0.course;

import java.io.Serializable;
import java.sql.Timestamp;

public class CourseDetail implements Serializable {

    private String cour_no;
    private String mem_no;
    private String cour_status;
    private Timestamp join_time;

    public CourseDetail(){

    }

    public String getCour_no() {
        return cour_no;
    }
    public void setCour_no(String cour_no) {
        this.cour_no = cour_no;
    }
    public String getMem_no() {
        return mem_no;
    }
    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }
    public String getCour_status() {
        return cour_status;
    }
    public void setCour_status(String cour_status) {
        this.cour_status = cour_status;
    }
    public Timestamp getJoin_time() {
        return join_time;
    }
    public void setJoin_time(Timestamp join_time) {
        this.join_time = join_time;
    }

}
