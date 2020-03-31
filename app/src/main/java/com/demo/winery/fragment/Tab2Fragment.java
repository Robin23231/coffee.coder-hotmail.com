package com.demo.winery.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.activities.activity_Details;
import com.demo.winery.adapter.ReveiwAdapter;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.model.CompanyInfoModel;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Tab2Fragment extends Fragment implements View.OnClickListener {


    Button btn_review_submit;
    RatingBar review_rate;
    float rating_counter;
    EditText edt_review;
    String t_review;
    String id;
    ListView lv_review;
    CompanyInfoModel objRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_review = (EditText) view.findViewById(R.id.edt_review);
        review_rate = (RatingBar) view.findViewById(R.id.review_rate_user);
        btn_review_submit= (Button) view.findViewById(R.id.btn_review_submit);

        t_review = edt_review.getText().toString();
        btn_review_submit.setOnClickListener(this);

        lv_review = view.findViewById(R.id.lv_review);
        activity_Details activity_details = (activity_Details) getActivity();
        id = activity_details.id_selector();
        winery_info_progress(id);
    }
    private void winery_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.WINERY_REVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, CompanyInfoModel.class);

                        if (objRes.getStatus().equals("200")) {
                            review_process();
                        }else {
                            Toast.makeText(getActivity(), "No Review", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(res_companyInfo);
    }
    private void review_process(){
        List<BaseCompanyInfo> reviewList = new ArrayList<BaseCompanyInfo>(objRes.getCompany_contents());
        lv_review.setAdapter(new ReveiwAdapter(getActivity(), reviewList));
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_review_submit:
                left_review_progress();
                break;
        }
    }

    private void left_review_progress() {
        StringRequest srReg = new StringRequest(Request.Method.POST, constant.USER_REVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);

                        if(objRes!=null){
                            if (objRes.getStatus() == "200") {
                                Methods.showAlertDialog(objRes.getMsg(), getActivity());
                            }else{
                                Methods.showAlertDialog(objRes.getMsg(), getActivity());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("winery_id", id);
                params.put(constant.EMAIL, getString(constant.EMAIL));
                params.put("review_text", edt_review.getText().toString());
                params.put("stars", String.valueOf(review_rate.getRating()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(srReg);
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
}