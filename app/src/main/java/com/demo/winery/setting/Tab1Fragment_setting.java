package com.demo.winery.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.model.UserinfoModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Tab1Fragment_setting extends Fragment implements View.OnClickListener{

    EditText edt_fName, edt_lName, edt_Email, edt_Address, edt_City, edt_State, edt_Zip;
    Button btn_Signup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one_setting, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_fName = view.findViewById(R.id.edt_fName);
        edt_lName = view.findViewById(R.id.edt_lName);
        edt_Email = view.findViewById(R.id.edt_Email);
        edt_Address = view.findViewById(R.id.edt_Address);
        edt_City = view.findViewById(R.id.edt_City);
        edt_State = view.findViewById(R.id.edt_State);
        edt_Zip = view.findViewById(R.id.edt_Zip);

        btn_Signup = view.findViewById(R.id.btn_Signup);
        btn_Signup.setOnClickListener(this);
        getUserInfo(view);
    }

    private void getUserInfo(final View view) {
        StringRequest srReg = new StringRequest(Request.Method.POST, constant.USER_GETINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UserinfoModel objRes = gson.fromJson(response, UserinfoModel.class);

                        if(objRes!=null){
                            if (objRes.getStatus().equals("200")) {
                                edt_fName.setText(objRes.getfName());
                                edt_lName.setText(objRes.getlName());
                                edt_Email.setText(objRes.getEmail());
                                edt_Address.setText(objRes.getAddress());
                                edt_City.setText(objRes.getCity());
                                edt_State.setText(objRes.getState());
                                edt_Zip.setText(objRes.getZip());
                            }else{
                                Methods.showAlertDialog("Some Error, No user info.", getActivity());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Methods.showAlertDialog(getString(R.string.error_network_check), activity_SignUp.this);
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Signup:
                update_process();
        }
    }

    private void update_process() {
        StringRequest srReg = new StringRequest(Request.Method.POST, constant.USER_PROFILE_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);

                        if(objRes!=null){
                            if (objRes.getStatus().equals("200")) {
                                Methods.showAlertDialog(objRes.getMsg(), getContext());
                            }else{
                                Methods.showAlertDialog(objRes.getMsg(), getContext());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Methods.showAlertDialog(getString(R.string.error_network_check), activity_SignUp.this);
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.FNAME, edt_fName.getText().toString());
                params.put(constant.LNAME, edt_lName.getText().toString());
                params.put(constant.EMAIL, edt_Email.getText().toString());
                params.put(constant.ADDRESS, edt_Address.getText().toString());
                params.put(constant.CITY, edt_City.getText().toString());
                params.put(constant.STATE, edt_State.getText().toString());
                params.put(constant.ZIP, edt_Zip.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(srReg);
    }

}