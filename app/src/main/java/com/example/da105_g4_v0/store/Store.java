package com.example.da105_g4_v0.store;

import java.io.Serializable;

public class Store implements Serializable {
    private String description;
    private Integer image;

    public Store(){
    }

    public Store(String description, Integer image){
        this.description = description;
        this.image=image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }



}
