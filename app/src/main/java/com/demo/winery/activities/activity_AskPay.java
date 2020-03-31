package com.demo.winery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.CostModel;
import com.demo.winery.stripe.stripe_main;
import com.demo.winery.utils.Config;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.HashMap;
import java.util.Map;

public class activity_AskPay extends AppCompatActivity implements View.OnClickListener {

    LinearLayout plan5;
    String from;
    TextView tv_money;

    int winey_cost, festival_cost, tour_cost;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.layout_askmoney);

        get_cost();

        Bundle extras = getIntent().getExtras();
        from = extras.getString(constant.PAYMONEY);


        plan5 = findViewById(R.id.plan5);
        plan5.setOnClickListener(this);
    }

    private void get_cost() {
        StringRequest res_signin = new StringRequest(Request.Method.POST, constant.USER_PAYMOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CostModel objRes = gson.fromJson(response, CostModel.class);
                        if (objRes.getStatus().equals("200")) {
                            winey_cost = objRes.getWinery_coin();
                            festival_cost = objRes.getFestival_coin();
                            tour_cost = objRes.getTour_coin();

                            tv_money = findViewById(R.id.tv_money);
                            if (from.equals("winery")){
                                tv_money.setText("$"+winey_cost+" per month \n\n1. Update my winery information \n2. Reply to reviews made by users\n\n3. Add events that will show on their winery listing page and on the events section of the app");
                            }else if (from.equals("tour")){
                                tv_money.setText("$"+tour_cost+" per month \n\n1. Update my Tour information \n");
                            }else if (from.equals("festival")){
                                tv_money.setText("$"+festival_cost+" onece submit festival \n");
                            }else if (from.equals("ads")){
                                tv_money.setText("$10 once submit Ads \n\n1. Update their Ads information \n");
                            }else if (from.equals("pdf")){
                                tv_money.setText("$100 once submit\n\n Download list of Wineries as PDF file.");
                            }


                        }else{
                            Methods.showAlertDialog("Some Error", activity_AskPay.this);
                        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(res_signin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.plan5:
                processPayment();
                break;
        }
    }
    private void processPayment() {
        Intent progressIntent = new Intent(activity_AskPay.this, stripe_main.class);
        progressIntent.putExtra("PLAN_FLAG", from);
        startActivity(progressIntent);
        finish();
    }
}