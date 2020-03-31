package com.demo.winery.my_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.demo.winery.adapter.ReveiwAdapter_my;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.model.CompanyInfoModel;
import com.demo.winery.my_activities.activity_Details_my;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tab2Fragment_my extends Fragment implements View.OnClickListener {

    String id;
    CompanyInfoModel objRes;
    ListView lv_review;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reply_review, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv_review = view.findViewById(R.id.lv_review);


        activity_Details_my activity_details = (activity_Details_my) getActivity();
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
                            Toast.makeText(getContext(), "No Reviews", Toast.LENGTH_LONG).show();
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
       lv_review.setAdapter(new ReveiwAdapter_my(getActivity(), reviewList));

       lv_review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String selectedItem = (String) parent.getItemAtPosition(position);
               Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_LONG).show();
           }
       });
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */

}