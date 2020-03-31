package com.demo.winery.model;

import com.google.gson.annotations.SerializedName;


public class ResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("msg")
    private String msg;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
