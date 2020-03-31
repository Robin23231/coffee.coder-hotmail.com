package com.demo.winery.stripe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.activities.Claim_Winery;
import com.demo.winery.ads.Claim_ads;
import com.demo.winery.fetival.Claim_Festival;
import com.demo.winery.model.CostModel;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.tour.Claim_Tour;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.Validation;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import java.util.HashMap;
import java.util.Map;


public class stripe_main extends AppCompatActivity implements View.OnClickListener {


    int flag;
    String from;
    EditText edt_mail;
    Button submit_payment;
    String t_mail, t_Token;
    CardMultilineWidget cardMultilineWidget;
    Card card;
    int winey_cost, festival_cost, tour_cost;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_stripe_main);

        Bundle extras = getIntent().getExtras();

        from = extras.getString("PLAN_FLAG");

        get_cost();
        edt_mail = findViewById(R.id.edt_mail);
        cardMultilineWidget = findViewById(R.id.card_input_widget);
        submit_payment = findViewById(R.id.submit_payment);
        submit_payment.setOnClickListener(this);
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

                            if (from .equals("winery")){
                                flag = winey_cost;
                            }else if (from.equals("tour")){
                                flag = tour_cost;
                            }else if (from.equals("festival")){
                                flag = festival_cost;
                            }else if (from.equals("ads")){
                                flag = 10;
                            }else if (from.equals("pdf")){
                                flag = 100;
                            }

                        }else{
                            Methods.showAlertDialog("Some Error", stripe_main.this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_payment:
                card =  cardMultilineWidget.getCard();
                if(card == null){
                    Toast.makeText(getApplicationContext(),"Invalid card",Toast.LENGTH_SHORT).show();
                }else {
                    if (!card.validateCard()) {
                        Toast.makeText(getApplicationContext(), "Invalid card", Toast.LENGTH_SHORT).show();
                    } else {
                        t_mail = edt_mail.getText().toString().trim();
                        if (Methods.isOnline(stripe_main.this)) {
                            if (TextUtils.isEmpty(t_mail)) {
                                Toast.makeText(this, "Enter Email.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (Validation.isValidEmail(t_mail)) {
                                    Methods.showProgress(stripe_main.this);
                                    CreateToken(card);
                                } else {
                                    Toast.makeText(this, "Please enter valid EmailId", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(this, "Please check Internet connection ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;

        }
    }

    private void CreateToken(final Card card) {
        Stripe stripe = new Stripe(getApplicationContext(), getString(R.string.publishablekey));
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        t_Token = token.getId();
                        payment_progress();
                    }
                    public void onError(Exception error) {
                        Toast.makeText(getApplicationContext(),
                                error.getLocalizedMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void payment_progress() {
        Methods.closeProgress();
        StringRequest paymentReq = new StringRequest(Request.Method.POST, constant.STRIPE_PAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);
                        if (objRes.getStatus().equals("200")) {
                            if (from.equals("winery")){
                                flag = winey_cost;
                                Intent mainIntent = new Intent(stripe_main.this, Claim_Winery.class);
                                startActivity(mainIntent);
                            }else if (from.equals("tour")){
                                flag = tour_cost;
                                Intent mainIntent = new Intent(stripe_main.this, Claim_Tour.class);
                                startActivity(mainIntent);
                            }else if (from.equals("festival")){
                                flag = festival_cost;
                                Intent mainIntent = new Intent(stripe_main.this, Claim_Festival.class);
                                startActivity(mainIntent);
                            }else if (from.equals("ads")){
                                Intent mainIntent = new Intent(stripe_main.this, Claim_ads.class);
                                startActivity(mainIntent);
                            }else if (from.equals("pdf")){
                                Intent intent=new Intent();
                                setResult(2,intent);
                            }

                            finish();
                        }
                        Toast.makeText(stripe_main.this, objRes.getMsg(), Toast.LENGTH_LONG).show();
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
                params.put(constant.TOKEN, t_Token);
                params.put(constant.PLAN, String.valueOf(flag));
                params.put(constant.EMAIL, t_mail);
                params.put(constant.CARD_NUM, card.getNumber());
                params.put(constant.EXPIRE_M, card.getExpMonth().toString());
                params.put(constant.EXPIRE_Y, card.getExpYear().toString());
                params.put(constant.CVC, card.getCVC());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(paymentReq);
    }
}