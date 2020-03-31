package com.demo.winery.my_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.Validation;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Activity_Before_My extends AppCompatActivity implements View.OnClickListener {

Button btn_email;
EditText edt_email, edt_password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_before_my);

        insertBoolean(constant.is_admin, false);

        edt_email = findViewById(R.id.edt_email);
        edt_email.setText(getString(constant.EMAIL));
        btn_email = findViewById(R.id.btn_email);
        btn_email.setOnClickListener(this);

        edt_password = findViewById(R.id.edt_password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_email:
                String mail = edt_email.getText().toString();
                if (Methods.isOnline(Activity_Before_My.this)) {
                    if (TextUtils.isEmpty(mail)) {
                        Methods.showAlertDialog("Enter Email.", this);
                    } else {
                        if (Validation.isValidEmail(mail)) {
                            Methods.showProgress(Activity_Before_My.this);
                            Email_presence(mail);
                        } else {
                            Methods.showAlertDialog("Please enter valid Email.", this);
                        }
                    }
                } else {
                    Methods.showAlertDialog("Please check Internet connection.", this);
                }
                break;
        }
    }

    private void Email_presence(final String mail) {
        StringRequest res_signin = new StringRequest(Request.Method.POST, constant.EMAIL_PRESENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Methods.closeProgress();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);
                        if(objRes.getStatus().equals("100")){
                            //Methods.showAlertDialog("This is Admin", Activity_Before_My.this);
                            insertString(constant.MYEMAIL, mail);
                            insertBoolean(constant.is_admin, true);
                            Intent intent = new Intent(Activity_Before_My.this, Activity_My.class);
                            intent.putExtra("MYEMAIL", mail);
                            startActivity(intent);
                        }else if (objRes.getStatus().equals("200")) {
                            insertString(constant.MYEMAIL, mail);
                            Intent intent = new Intent(Activity_Before_My.this, Activity_My.class);
                            intent.putExtra("MYEMAIL", mail);
                            startActivity(intent);
                        }else {
                            Methods.showAlertDialog(objRes.getMsg(), Activity_Before_My.this);
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
                params.put(constant.EMAIL, edt_email.getText().toString());
                params.put(constant.PASSWORD, edt_password.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(res_signin);
    }

    public synchronized void insertString(String key, String value) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
    public synchronized void insertBoolean(String key, boolean value) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }
}
