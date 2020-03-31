package com.demo.winery.model;

import com.google.gson.annotations.SerializedName;

public class CostModel {
    @SerializedName("status")
    private String status;
    @SerializedName("winery_coin")
    private int winery_coin;
    @SerializedName("festival_coin")
    private int festival_coin;
    @SerializedName("tour_coin")
    private int tour_coin;

    public CostModel() {
    }

    public int getWinery_coin() {
        return winery_coin;
    }

    public void setWinery_coin(int winery_coin) {
        this.winery_coin = winery_coin;
    }

    public int getFestival_coin() {
        return festival_coin;
    }

    public void setFestival_coin(int festival_coin) {
        this.festival_coin = festival_coin;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTour_coin() {
        return tour_coin;
    }

    public void setTour_coin(int tour_coin) {
        this.tour_coin = tour_coin;
    }
}
