package com.demo.winery.model;

public class MyListData{
    private String description;
    private int imgId;
    public MyListData(String description, int imgId) {
        this.description = description;
        this.imgId = imgId;
    }
    public String getDescription() {
        return description;
    }

    public int getImgId() {
        return imgId;
    }
}