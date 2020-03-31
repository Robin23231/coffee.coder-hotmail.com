package com.demo.winery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import static java.lang.Boolean.getBoolean;

public class activity_SignIn extends AppCompatActivity implements View.OnClickListener  {

    Button btn_SignIn, btn_toSignUp;
    EditText edt_Email;
    String t_Email;
    CheckBox chkRememberMe;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_signin);

        btn_toSignUp = findViewById(R.id.btn_toSignUp);
        btn_toSignUp.setOnClickListener(this);
        btn_SignIn = findViewById(R.id.btn_SignIn);
        btn_SignIn.setOnClickListener(this);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Email.setText(getString(constant.EMAIL));
        chkRememberMe = findViewById(R.id.chkRememberMe);

        if (getBoolean("SaveLogin")) {
            edt_Email.setText(getString(constant.EMAIL));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_toSignUp:
                Intent signupIntent = new Intent(activity_SignIn.this, activity_SignUp.class);
                startActivity(signupIntent);
                break;
            case R.id.btn_SignIn:
                Methods.showProgress(activity_SignIn.this);
                ChkLogin();
                break;

        }
    }

    private void ChkLogin() {
        t_Email = edt_Email.getText().toString().trim();
        if (Methods.isOnline(activity_SignIn.this)) {
            if (TextUtils.isEmpty(t_Email)) {
                Methods.showAlertDialog("Enter Email", activity_SignIn.this);
            } else {
                if (Validation.isValidEmail(t_Email)) {
                    login_process();
                } else {
                    Methods.showAlertDialog("Please enter valid EmailId", activity_SignIn.this);
                }
            }
        } else {
            Methods.showAlertDialog("Please check Internet connection ", activity_SignIn.this);
        }
    }

    private void login_process() {
        StringRequest res_signin = new StringRequest(Request.Method.POST, constant.USER_SIGNIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);
                        if (objRes.getStatus().equals("100")) {
                            if (chkRememberMe.isChecked()) {
                                insertBoolean("SaveLogin", true);
                            }
                            Methods.closeProgress();
                            insertString(constant.EMAIL, t_Email);
                            Intent mainIntent = new Intent(activity_SignIn.this, activity_Main.class);
                            startActivity(mainIntent);
                        }else if (objRes.getStatus().equals("200")) {
                            if (chkRememberMe.isChecked()) {
                                insertBoolean("SaveLogin", true);
                            }
                            Methods.closeProgress();
                            insertString(constant.EMAIL, t_Email);
                            Intent mainIntent = new Intent(activity_SignIn.this, activity_Main.class);
                            startActivity(mainIntent);
                        }else{
                            Methods.showAlertDialog(objRes.getMsg(), activity_SignIn.this);
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
                params.put(constant.EMAIL, t_Email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(res_signin);
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
    public synchronized void insertString(String key, String value) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }
    public synchronized void clear() {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public void onBackPressed() {
        //finish();
        //System.exit(0);
        moveTaskToBack(true);

    }
}
