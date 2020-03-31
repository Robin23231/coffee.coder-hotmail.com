package com.demo.winery.model;

import com.google.gson.annotations.SerializedName;

public class ReviewModel {
    @SerializedName("username")
    private String username;
    @SerializedName("email_review")
    private String email_review;
    @SerializedName("date")
    private String date;
    @SerializedName("review_text")
    private String review_text;
    @SerializedName("starts")
    private float starts;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail_review() {
        return email_review;
    }

    public void setEmail_review(String email_review) {
        this.email_review = email_review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public float getStarts() {
        return starts;
    }

    public void setStarts(float starts) {
        this.starts = starts;
    }
}
