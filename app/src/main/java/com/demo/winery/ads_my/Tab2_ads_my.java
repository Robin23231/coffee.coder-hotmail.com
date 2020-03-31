package com.demo.winery.ads_my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.stripe.stripe_main;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Tab2_ads_my extends Fragment implements View.OnClickListener {

Button btn_download, btn_pay;
Intent Askpay_intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_my, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_download = view.findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);
        btn_download.setEnabled(false);

        btn_pay = view.findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_download:
                download_pdf_request();
                break;
            case R.id.btn_pay:
                Askpay_intent = new Intent(getContext(), stripe_main.class);
                Askpay_intent.putExtra("PLAN_FLAG", "pdf");
                startActivityForResult(Askpay_intent, 2);
                break;

        }
    }

    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
//            String message=data.getStringExtra("MESSAGE");
//            textView1.setText(message);
            btn_download.setEnabled(true);
            btn_pay.setEnabled(false);
        }
    }

    private void download_pdf_request() {
        StringRequest res_signin = new StringRequest(Request.Method.POST, constant.PDF_DOWNLOAD_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
//                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);
//
//                        Methods.showAlertDialog(objRes.getMsg(), getContext());
                        new DownloadTask(getActivity(), constant.PDF_DOWNLOAD);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(res_signin);
    }
}