package com.demo.winery.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.adapter.MyListAdapter;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.model.CompanyInfoModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class WineryFragment extends Fragment implements  View.OnClickListener {

    List<BaseCompanyInfo> allAnsweredList;
    MyListAdapter myListAdapter;

        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
        Boolean isScrolling = false;
        int currentItems, totalItems, scrollOutItems;
        int currentPage = 0;
        ProgressBar progressBar, first_progress;
        int oh = 0;
        boolean doubleToPrev = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_winery, container, false);
        return root;
    }
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Methods.waitingProgress(getActivity());

        getCompanyInfo(view);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        first_progress = (ProgressBar) view.findViewById(R.id.first_progress);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    isScrolling = true;
//                }
//
//                if (!recyclerView.canScrollVertically(1)) {
//                    currentPage = currentPage+1;
//                    getCompanyInfo(view);
//                    Log.e("scroll", "Last"+currentPage);
//                    fetchData(0);
//                }
//                if (!recyclerView.canScrollVertically(-1)) {
//                        //Log.e("scroll", "first"+currentPage);
//                        //if (currentPage > 0){
//                        //currentPage = currentPage -1;
//                        //    getCompanyInfo(view);
//                       // }
////                    }
//
//                    if(doubleToPrev){
//                        oh = oh + 1;
//                        if (oh % 2 == 0){
//                            if (currentPage > 0){
//                                currentPage = currentPage -1;
//                            }
//                            getCompanyInfo(view);
//                            Log.e("scroll", "first"+currentPage);
//                            fetchData(1);
//                        }
//                    }
//                    else {
//                        doubleToPrev = true;
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                doubleToPrev = false;
//                            }
//                        }, 1000);
//                    }
//                }
//            }
//
//
////            @Override
////            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
////                super.onScrolled(recyclerView, dx, dy);
////
////                currentItems = layoutManager.getChildCount();
////                totalItems = layoutManager.getItemCount();
////                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
////
////                Log.e("currentItems", String.valueOf(currentItems));
////                Log.e("totalItems", String.valueOf(totalItems));
////                Log.e("scrollOutItems", String.valueOf(scrollOutItems));
////
//////                if (isScrolling && (currentItems + scrollOutItems == totalItems)){
//////                    isScrolling = false;
//////                    fetchData();
//////                }
////            }
//        });

    }

//        private void loadNextDataFromApi(int page) {
//        }


        private void fetchData(int i) {
        if (i == 0){
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    myListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
            }, 2000);
        }else {
            first_progress.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    myListAdapter.notifyDataSetChanged();
                    first_progress.setVisibility(View.GONE);

                }
            }, 2000);
        }

        }

        private void getCompanyInfo(View view) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.WINERY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CompanyInfoModel objRes = gson.fromJson(response, CompanyInfoModel.class);
                        if (objRes.getStatus().equals("200")) {
                            allAnsweredList = new ArrayList<BaseCompanyInfo>(objRes.getCompany_contents());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            myListAdapter = new MyListAdapter(getActivity(), allAnsweredList);
                            recyclerView.setAdapter(myListAdapter);
                        }else {
                            Methods.closeProgress();
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_signup", error.toString());
                        Methods.closeProgress();
                        //Methods.showAlertDialog("Time out Error.", getContext());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
                params.put(constant.CURRENT_PAGE, String.valueOf(currentPage));
                return params;
            }
        };
        res_companyInfo.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(res_companyInfo);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
        public synchronized String getString(String key) {
            SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
            String  selected =  mSharedPreferences.getString(key, "");
            return selected;
        }
}
