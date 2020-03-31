package com.demo.winery.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.adapter.MyListAdapter_festival;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.model.CompanyInfoModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FestivalFragment extends Fragment  implements View.OnClickListener{

    List<BaseCompanyInfo> allAnsweredList;
    MyListAdapter_festival myListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival, null);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Methods.waitingProgress(getActivity());
        getFestivalInfo(view);
    }

    private void getFestivalInfo(final View view) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.FESTIVAL_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CompanyInfoModel objRes = gson.fromJson(response, CompanyInfoModel.class);
                        if (objRes.getStatus().equals("200")) {
                            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                            allAnsweredList = new ArrayList<BaseCompanyInfo>(objRes.getCompany_contents());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            myListAdapter = new MyListAdapter_festival(getActivity(), allAnsweredList);
                            recyclerView.setAdapter(myListAdapter);
                        }else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(res_companyInfo);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
