package com.demo.winery.fetival;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.winery.R;
import com.demo.winery.activities.activity_Main;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.BaseActivity;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Claim_Festival extends AppCompatActivity implements View.OnClickListener {

    Button btn_Signup;
    EditText edt_festival_name, edt_time_start, edt_time_end, edt_venue, edt_address, edt_city ,edt_State,edt_Zip,edt_Phone,edt_Website, edt_cost_1, edt_cost_2, edt_description;
    TextView tv_date;
    ImageView img_festival_Logo;
    private static final String IMAGE_DIRECTORY = "/mywinery";
    BaseActivity imageuploadActivity;

    ResponseModel responseModel;
    String btn1="";
    private DatePickerDialog datePickerDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_claim_festival);


        requestMultiplePermissions();
        imageuploadActivity = new BaseActivity();

        edt_festival_name = findViewById(R.id.edt_festival_name);
        tv_date = findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);

        edt_time_start = findViewById(R.id.edt_time_start);
        edt_time_end = findViewById(R.id.edt_time_end);
//        edt_venue = findViewById(R.id.edt_venue);
        edt_address = findViewById(R.id.edt_address);
        edt_city = findViewById(R.id.edt_city);
        edt_State = findViewById(R.id.edt_State);
        edt_Zip = findViewById(R.id.edt_Zip);
        edt_Phone = findViewById(R.id.edt_Phone);
        edt_Website = findViewById(R.id.edt_Website);
        edt_cost_1 = findViewById(R.id.edt_cost_1);
        edt_cost_2 = findViewById(R.id.edt_cost_2);
        edt_description = findViewById(R.id.edt_description);

        img_festival_Logo = findViewById(R.id.img_festival_Logo);
        img_festival_Logo.setOnClickListener(this);

        btn_Signup = findViewById(R.id.btn_Signup);
        btn_Signup.setOnClickListener(this);
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_festival_Logo:
                showPictureDialog();
                break;
            case R.id.btn_Signup:
                if (Methods.isOnline(Claim_Festival.this)) {
                    winery_submit_process(v);
                } else {
                    Methods.showAlertDialog("Please check Internet connection ", Claim_Festival.this);
                }
                break;
            case R.id.tv_date:
                final Calendar c_f = Calendar.getInstance();
                int mYear = c_f.get(Calendar.YEAR); // current year
                int mMonth = c_f.get(Calendar.MONTH); // current month
                int mDay = c_f.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(Claim_Festival.this),
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

                                tv_date.setText(year + "-"+ month + "-" + day);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }

    private void winery_submit_process(final View v) {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.FESTIVAL_CLAIM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);

                        if(responseModel.getStatus().equals("200")){
                            Toast.makeText(Claim_Festival.this, "Submit Success", Toast.LENGTH_LONG).show();
                            finish();
                            Intent askpay_intent = new Intent(Claim_Festival.this, activity_Main.class);
                            startActivity(askpay_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), Claim_Festival.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), Claim_Festival.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.FESTIVAL_NAME, edt_festival_name.getText().toString());
                params.put(constant.DATE, tv_date.getText().toString());
                params.put(constant.TIME, edt_time_start.getText().toString()+"-"+edt_time_end.getText().toString());
//                params.put(constant.VENUE, edt_venue.getText().toString());
                params.put(constant.ADDRESS, edt_address.getText().toString());
                params.put(constant.CITY, edt_city.getText().toString());
                params.put(constant.STATE, edt_State.getText().toString());
                params.put(constant.ZIP, edt_Zip.getText().toString());
                params.put(constant.PHONE, edt_Phone.getText().toString());
                params.put(constant.WEBSITE, edt_Website.getText().toString());
                params.put(constant.COST, edt_cost_1.getText().toString()+"-"+edt_cost_2.getText().toString());
                params.put(constant.DESCRIPTION, edt_description.getText().toString());
                if (!TextUtils.isEmpty((btn1.substring(btn1.lastIndexOf(".")+1)))) {
                    params.put(constant.FESTIVALLOGO, imageuploadActivity.encodeImgTOBase64(btn1, (btn1.substring(btn1.lastIndexOf(".") + 1))));
                }
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

    private void showPictureDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Select Logo with below");
        alertDialog.setIcon(getResources().getDrawable(R.drawable.winery_logo));
        alertDialog.setPositiveButton("GALLARY",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choosePhotoFromGallery();
            }
        });
        alertDialog.setNegativeButton("CAMERA",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhotoFromCamera();
            }
        });
        alertDialog.show();
    }
    public void choosePhotoFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        int GALLERY = 1;
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int CAMERA = 0;
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == Activity.RESULT_OK){
                    Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    img_festival_Logo.setImageBitmap(thumbnail);
                    img_festival_Logo.setVisibility(View.VISIBLE);
                    assert thumbnail != null;
                    btn1 = saveImage(thumbnail);
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            btn1 = saveImage(bitmap);
                            img_festival_Logo.setImageBitmap(bitmap);
                            img_festival_Logo.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
}
