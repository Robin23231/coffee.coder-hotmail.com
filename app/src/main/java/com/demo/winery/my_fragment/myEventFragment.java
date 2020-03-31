package com.demo.winery.my_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.winery.R;
import com.demo.winery.event.Claim_event;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class myEventFragment extends Fragment implements View.OnClickListener{

    Button btn_event;
    ResponseModel responseModel;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_event, container, false);
        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_event = view.findViewById(R.id.btn_event);
        btn_event.setOnClickListener(this);
        if (getBoolean(constant.is_admin)){
            btn_event.setVisibility(View.GONE);
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_event:
                Enable_add_event();
                break;
        }
    }

    private void Enable_add_event() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.ENABLE_ADD_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);
                        if(responseModel.getStatus().equals("200")){
                            Toast.makeText(getContext(), responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                            Intent event_intent = new Intent(getContext(), Claim_event.class);
                            startActivity(event_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), getContext());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), getContext());
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.MYEMAIL));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(srStatus);
    }
    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",constant.Auth_UserName, constant.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
    public synchronized boolean getBoolean(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
        Boolean  selected =  mSharedPreferences.getBoolean(key, false);
        return selected;
    }
}
