package com.demo.winery.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.text.TextUtils.isEmpty;

public class Claim_event  extends AppCompatActivity implements View.OnClickListener {

    Button btn_event_Submit;
    EditText edt_location, edt_Description;
    TextView txtFrom;
    EditText event_name, start_time, end_time;
    private DatePickerDialog datePickerDialog;

    ResponseModel responseModel;
    String btn1="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_claim_event);

        event_name = findViewById(R.id.event_name);
        edt_location = findViewById(R.id.edt_Location);
        edt_Description = findViewById(R.id.edt_Description);
        txtFrom = findViewById(R.id.txtFrom);
        txtFrom.setOnClickListener(this);

        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);

        btn_event_Submit = (Button) findViewById(R.id.btn_event_Submit);
        btn_event_Submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_event_Submit:

                String t_event_name = event_name.getText().toString().trim();
                String t_txtFrom = txtFrom.getText().toString().trim();
                String t_start_time = start_time.getText().toString().trim();
                String t_end_time = end_time.getText().toString().trim();
                String t_edt_Description = edt_Description.getText().toString().trim();


                if (Methods.isOnline(Claim_event.this)) {
                    if (isEmpty(t_event_name) || isEmpty(t_txtFrom) || isEmpty(t_start_time) || isEmpty(t_end_time) || isEmpty(t_edt_Description)) {
                        Methods.showAlertDialog("Enter Details.", this);
                    } else {
                        event_submit_process();
                    }
                } else {
                    Methods.showAlertDialog("Please check Internet connection.", this);
                }

                break;
            case R.id.txtFrom:
                final Calendar c_f = Calendar.getInstance();
                int mYear = c_f.get(Calendar.YEAR); // current year
                int mMonth = c_f.get(Calendar.MONTH); // current month
                int mDay = c_f.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(Claim_event.this),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String month = ""+(monthOfYear+1), day = ""+dayOfMonth;
                                if ((monthOfYear+1) < 10){
                                    month = "0"+(monthOfYear+1);
                                }
                                if (dayOfMonth <10){
                                    day = "0"+dayOfMonth;
                                }

                                txtFrom.setText(year + "-"+ month + "-" + day);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }

    private void event_submit_process() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.EVENT_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);

                        if(responseModel.getStatus().equals("200")){
                            Methods.showAlertDialog(responseModel.getMsg(), Claim_event.this);
                            finish();
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), Claim_event.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), Claim_event.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
                params.put("event_name", event_name.getText().toString());
                params.put(constant.EVENT_DATE, txtFrom.getText().toString());
                params.put("starttime", start_time.getText().toString());
                params.put("endtime", end_time.getText().toString());
                params.put(constant.LOCATION, edt_location.getText().toString());
                params.put(constant.DESCRIPTION, edt_Description.getText().toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srStatus);
    }


    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",constant.Auth_UserName, constant.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
}
