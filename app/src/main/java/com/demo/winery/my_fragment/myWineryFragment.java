package com.demo.winery.my_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.demo.winery.adapter.MyListAdapter_My;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.model.CompanyInfoModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class myWineryFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    String myEmaile;
    Bundle bundle = getArguments();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_winery, container, false);
        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myEmaile = getString(constant.MYEMAIL);
        Log.e("myEmail", myEmaile);


        Methods.waitingProgress(getActivity());
        getCompanyInfo(view);
    }

    private void getCompanyInfo(final View view) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.WINERY_MY_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        CompanyInfoModel objRes = gson.fromJson(response, CompanyInfoModel.class);

                        if (objRes.getStatus().equals("200")) {

                            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                            List<BaseCompanyInfo> allAnsweredList = new ArrayList<BaseCompanyInfo>(objRes.getCompany_contents());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            MyListAdapter_My myListAdapter = new MyListAdapter_My(getActivity(), allAnsweredList);
                            recyclerView.setAdapter(myListAdapter);

                        }else {
                            Methods.closeProgress();
//                            Methods.showAlertDialog("There is no your Data", getActivity());
                            Toast.makeText(getContext(), "There is no your Data", Toast.LENGTH_SHORT).show();
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
                //if (getBoolean(constant.is_admin)){
                    params.put("MyEmail", myEmaile);
                //}
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(res_companyInfo);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        DetailsFragment frag = new DetailsFragment();
//        Bundle data = new Bundle();
//        data.putString("id", ((BaseCompanyInfo)parent.getItemAtPosition(position)).getId());
//        frag.setArguments(data);
//        transaction.replace(R.id.fragment_container, frag);
//        transaction.addToBackStack(null);
//        transaction.commit();
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
    public synchronized boolean getBoolean(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        Boolean  selected =  mSharedPreferences.getBoolean(key, false);
        return selected;
    }
}
