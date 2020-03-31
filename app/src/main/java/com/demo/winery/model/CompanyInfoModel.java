package com.demo.winery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CompanyInfoModel {
    @SerializedName("status")
    private String status;
    @SerializedName("msg")
    private List<Company_content> company_contents = new ArrayList<Company_content>();

    public String getStatus() {
        return status;
    }

    public List<Company_content> getCompany_contents() {
        return company_contents;
    }

}
