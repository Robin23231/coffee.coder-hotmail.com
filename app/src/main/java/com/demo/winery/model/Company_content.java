package com.demo.winery.model;

import com.google.gson.annotations.SerializedName;

public class Company_content extends BaseCompanyInfo {
    @SerializedName("id")
    private String id;
    @SerializedName("Company")
    private String Company;
    @SerializedName("Address")
    private String Address;
    @SerializedName("City")
    private String City;
    @SerializedName("State")
    private String State;
    @SerializedName("Zip")
    private String Zip;
    @SerializedName("County")
    private String County;
    @SerializedName("Phone")
    private String Phone;
    @SerializedName("Website")
    private String Website;
    @SerializedName("Contact")
    private String Contact;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Email")
    private String Email;
    @SerializedName("SIC_Code")
    private String SIC_Code;
    @SerializedName("Industry")
    private String Industry;
    @SerializedName("Description")
    private String Description;
    @SerializedName("Date_Signup")
    private String Date_Signup;
    @SerializedName("status")
    private String Status;
    @SerializedName("wine_tasting")
    private String wine_tasting;
    @SerializedName("setting_Zipcode")
    private String setting_Zipcode;
    @SerializedName("setting_xmiles")
    private String setting_xmiles;



    @SerializedName("winery_id")
    private String winery_id;

    //distance

    @SerializedName("distance")
    private String distance;

    //tour

    @SerializedName("tour_id")
    private String tour_id;
    @SerializedName("tour_review_content")
    private String tour_review_content;
    @SerializedName("tour_review_stars")
    private float tour_review_stars;


    @SerializedName("tour_stars")
    private float tour_stars;

    //review
    @SerializedName("username")
    private String username;
    @SerializedName("email_review")
    private String email_review;
    @SerializedName("date")
    private String date;
    @SerializedName("stars")
    private float stars;

    //ads

    @SerializedName("category")
    private String category;
    @SerializedName("contact_name")
    private String contact_name;
    @SerializedName("ads_logo")
    private String ads_logo;
//event

    @SerializedName("id_event")
    private String id_event;
    @SerializedName("event_name")
    private String event_name;
    @SerializedName("date_event")
    private String date_event;
    @SerializedName("time_event")
    private String time_event;
    @SerializedName("location_event")
    private String location_event;
    @SerializedName("description_event")
    private String description_event;
    @SerializedName("start_time")
    private String start_time;
    @SerializedName("end_time")
    private String end_time;
    @SerializedName("event_logo")
    private String event_logo;
    @SerializedName("event_winery_name")
    private String event_winery_name;
    @SerializedName("event_zip")
    private String event_zip;
    @SerializedName("event_Address")
    private String event_Address;


    @SerializedName("review_text")
    private String review_text;
    @SerializedName("logo")
    private String logo;

    //festival

    @SerializedName("festival_id")
    private String festival_id;
    @SerializedName("festival_name")
    private String festival_name;
    @SerializedName("festival_date")
    private String festival_date;
    @SerializedName("festival_venue")
    private String festival_venue;
    @SerializedName("festival_address")
    private String festival_address;
    @SerializedName("festival_city")
    private String festival_city;
    @SerializedName("festival_state")
    private String festival_state;
    @SerializedName("festival_zip_code")
    private String festival_zip_code;
    @SerializedName("festival_phone")
    private String festival_phone;
    @SerializedName("festival_website_url")
    private String festival_website_url;
    @SerializedName("festival_time")
    private String festival_time;
    @SerializedName("festival_cost")
    private String festival_cost;
    @SerializedName("festival_description")
    private String festival_description;
    @SerializedName("festival_logo")
    private String festival_logo;


    //paymod

    @SerializedName("winery_coin")
    private float winery_coin;
    @SerializedName("festival_coin")
    private float festival_coin;
    @SerializedName("event_coin")
    private float event_coin;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCompany() {
        return Company;
    }

    @Override
    public String getAddress() {
        return Address;
    }

    @Override
    public String getCity() {
        return City;
    }

    @Override
    public String getState() {
        return State;
    }

    @Override
    public String getZip() {
        return Zip;
    }

    @Override
    public String getCounty() {
        return County;
    }


    @Override
    public String getPhone() {
        return Phone;
    }

    @Override
    public String getWebsite() {
        return Website;
    }

    @Override
    public String getContact() {
        return Contact;
    }

    @Override
    public String getTitle() {
        return Title;
    }

    @Override
    public String getEmail() {
        return Email;
    }

    @Override
    public String getSIC_Code() {
        return SIC_Code;
    }

    @Override
    public String getIndustry() {
        return Industry;
    }

    @Override
    public String getDescription() {
        return Description;
    }

    @Override
    public String getDate_Signup() {
        return Date_Signup;
    }

    @Override
    public String getStatus() {
        return Status;
    }


    @Override
    public String getreview_text() {
        return review_text;
    }

    @Override
    public String getid_event() {
        return id_event;
    }

    @Override
    public String getevent_name() {
        return event_name;
    }


    @Override
    public String gettime_event() {
        return time_event;
    }

    @Override
    public String getlocation_event() {
        return location_event;
    }

    @Override
    public String getdescription_event() {
        return description_event;
    }

    @Override
    public String getstart_time() {
        return start_time;
    }

    @Override
    public String getend_time() {
        return end_time;
    }

    @Override
    public String getevent_logo() {
        return event_logo;
    }

    @Override
    public String getevent_winery_name() {
        return event_winery_name;
    }

    @Override
    public String getevent_zip() {
        return event_zip;
    }

    @Override
    public String getevent_Address() {
        return event_Address;
    }

    @Override
    public String getlogo() {
        return logo;
    }

    @Override
    public String getfestival_id() {
        return festival_id;
    }

    @Override
    public String getfestival_name() {
        return festival_name;
    }

    @Override
    public String getfestival_date() {
        return festival_date;
    }

    @Override
    public String getfestival_venue() {
        return festival_venue;
    }

    @Override
    public String getfestival_address() {
        return festival_address;
    }

    @Override
    public String getfestival_city() {
        return festival_city;
    }

    @Override
    public String getfestival_state() {
        return festival_state;
    }

    @Override
    public String getfestival_zip_code() {
        return festival_zip_code;
    }

    @Override
    public String getfestival_phone() {
        return festival_phone;
    }

    @Override
    public String getfestival_website_url() {
        return festival_website_url;
    }

    @Override
    public String getfestival_time() {
        return festival_time;
    }

    @Override
    public String getfestival_cost() {
        return festival_cost;
    }

    @Override
    public String getfestival_description() {
        return festival_description;
    }

    @Override
    public String getfestival_logo() {
        return festival_logo;
    }

    @Override
    public String getads_logo() {
        return ads_logo;
    }

    @Override
    public String getcategory() {
        return category;
    }

    @Override
    public String getcontact_name() {
        return contact_name;
    }

    @Override
    public String getwinery_id() {
        return winery_id;
    }

    @Override
    public String gettour_id() {
        return tour_id;
    }

    @Override
    public String gettour_review_content() {
        return tour_review_content;
    }

    @Override
    public float gettour_review_stars() {
        return tour_review_stars;
    }

    @Override
    public float gettour_stars() {
        return tour_stars;
    }

    @Override
    public float getwinery_coin() {
        return winery_coin;
    }

    @Override
    public float getfestival_coin() {
        return festival_coin;
    }

    @Override
    public float getevent_coin() {
        return event_coin;
    }

    @Override
    public String getdistance() {
        return distance;
    }

    @Override
    public String getWineTasting() {
        return wine_tasting;
    }

    @Override
    public String getsetting_Zipcode() {
        return setting_Zipcode;
    }

    @Override
    public String getsetting_xmiles() {
        return setting_xmiles;
    }

    @Override
    public String getusername() {
        return username;
    }

    @Override
    public String getemail_review() {
        return email_review;
    }

    @Override
    public String getdate() {
        return date;
    }

    @Override
    public float getstars() {
        return stars;
    }

    public void setId(String id) {
        this.id = id;
    }
}
