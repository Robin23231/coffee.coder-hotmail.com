package com.demo.winery.my_fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.demo.winery.model.Company_content;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_My;
import com.demo.winery.my_activities.activity_Details_my;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Tab3Fragment_my extends Fragment implements View.OnClickListener{

    Button btn_submit;
    ResponseModel responseModel;
    Company_content objRes;
    String id;
    TextView et_winery_name, et_phonenumber, et_email, et_reason;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three_my, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity_Details_my activity_details = (activity_Details_my) getActivity();
        id = activity_details.id_selector();
        winery_info_progress(id);

        et_winery_name = view.findViewById(R.id.et_winery_name);
        et_phonenumber = view.findViewById(R.id.et_phonenumber);
        et_email = view.findViewById(R.id.et_email);
        et_reason = view.findViewById(R.id.et_reason);

        btn_submit = view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    private void winery_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.WINERY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            et_winery_name.setText(objRes.getCompany());
                            et_phonenumber.setText(objRes.getPhone());
                            et_email.setText(objRes.getEmail());
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
                params.put("id", id);
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
            case R.id.btn_submit:
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                close_account_process();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }


    }

        private void close_account_process() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.WINERY_CLOSE_ACCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);

                        if(responseModel.getStatus().equals("200")){
                            Intent askpay_intent = new Intent(getContext(), Activity_My.class);
                            startActivity(askpay_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), getContext());
                        }
                        Toast.makeText(getContext(), responseModel.getMsg(), Toast.LENGTH_LONG).show();
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
                params.put(constant.ID, id);
                params.put(constant.EMAIL, et_email.getText().toString());
                params.put(constant.PHONE, et_phonenumber.getText().toString());
                params.put(constant.WINERYNAME, et_winery_name.getText().toString());
                params.put(constant.REASON, et_reason.getText().toString());
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
}