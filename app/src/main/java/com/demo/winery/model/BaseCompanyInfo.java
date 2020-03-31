package com.demo.winery.model;

public abstract class BaseCompanyInfo {
    abstract public String getId();
    abstract public String getCompany();
    abstract public String getAddress();
    abstract public String getCity();
    abstract public String getState();
    abstract public String getZip();
    abstract public String getCounty();
    abstract public String getPhone();
    abstract public String getWebsite();
    abstract public String getContact();
    abstract public String getTitle();
    abstract public String getEmail();
    abstract public String getSIC_Code();
    abstract public String getIndustry();
    abstract public String getDescription();
    abstract public String getDate_Signup();
    abstract public String getStatus();
    abstract public String getWineTasting();
    abstract public String getsetting_Zipcode();
    abstract public String getsetting_xmiles();


    abstract public String getusername();
    abstract public String getemail_review();
    abstract public String getdate();
    abstract public float getstars();
    abstract public String getreview_text();

//event
    abstract public String getid_event();
    abstract public String getevent_name();
    abstract public String gettime_event();
    abstract public String getlocation_event();
    abstract public String getdescription_event();
    abstract public String getstart_time();
    abstract public String getend_time();
    abstract public String getevent_logo();
    abstract public String getevent_winery_name();
    abstract public String getevent_zip();

    abstract public String getevent_Address();

    abstract public String getlogo();

    //festival

    abstract public String getfestival_id();
    abstract public String getfestival_name();
    abstract public String getfestival_date();
    abstract public String getfestival_venue();
    abstract public String getfestival_address();
    abstract public String getfestival_city();
    abstract public String getfestival_state();
    abstract public String getfestival_zip_code();
    abstract public String getfestival_phone();
    abstract public String getfestival_website_url();
    abstract public String getfestival_time();
    abstract public String getfestival_cost();
    abstract public String getfestival_description();
    abstract public String getfestival_logo();

    //ads

    abstract public String getads_logo();
    abstract public String getcategory();
    abstract public String getcontact_name();


    abstract public String getwinery_id();

    //tour get review
    abstract public String gettour_id();
    abstract public String gettour_review_content();
    abstract public float gettour_review_stars();


    abstract public float gettour_stars();

    abstract public float getwinery_coin();
    abstract public float getfestival_coin();
    abstract public float getevent_coin();


    abstract public String getdistance();

}
