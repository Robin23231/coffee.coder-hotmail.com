package com.demo.winery.ads;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_Before_My;
import com.demo.winery.utils.BaseActivity;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
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

public class Claim_ads  extends AppCompatActivity implements View.OnClickListener {

    Button btn_ads_Signup;
    EditText edt_ads_onwer, edt_ads_name, edt_ads_address,edt_ads_City,edt_ads_State,edt_ads_Zip,edt_ads_Phone,edt_ads_Website,edt_ads_Description;
    ImageView img_ads_Logo;
    private static final String IMAGE_DIRECTORY = "/myads";
    BaseActivity imageuploadActivity;
    ResponseModel responseModel;
    String btn1="";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_claim_ads);

        requestMultiplePermissions();
        imageuploadActivity = new BaseActivity();

        edt_ads_onwer = findViewById(R.id.edt_ads_onwer);
        edt_ads_name = findViewById(R.id.edt_ads_name);
        edt_ads_address = findViewById(R.id.edt_ads_address);
        edt_ads_City = findViewById(R.id.edt_ads_City);
        edt_ads_State = findViewById(R.id.edt_ads_State);
        edt_ads_Zip = findViewById(R.id.edt_ads_Zip);
        edt_ads_Phone = findViewById(R.id.edt_ads_Phone);
        edt_ads_Website = findViewById(R.id.edt_ads_Website);
        edt_ads_Description = findViewById(R.id.edt_ads_Description);
        img_ads_Logo = findViewById(R.id.img_ads_Logo);
        img_ads_Logo.setOnClickListener(this);

        btn_ads_Signup = findViewById(R.id.btn_ads_Signup);
        btn_ads_Signup.setOnClickListener(this);
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
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
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
            case R.id.img_ads_Logo:
                showPictureDialog();
                break;
            case R.id.btn_ads_Signup:
                ads_submit_process();
                Methods.showProgress(Claim_ads.this);
                break;
        }
    }

    private void ads_submit_process() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.ADS_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);

                        if(responseModel.getStatus().equals("200")){
                            finish();
                            Intent askpay_intent = new Intent(Claim_ads.this, Activity_Before_My.class);
                            startActivity(askpay_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), Claim_ads.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), Claim_ads.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("advertiser_name", edt_ads_onwer.getText().toString());
                params.put("company_name", edt_ads_name.getText().toString());
                params.put(constant.EMAIL, getString(constant.EMAIL));
                //params.put("contact_name", edt_tour_address.getText().toString());
                params.put("contact_number", edt_ads_Phone.getText().toString());
                params.put("website_url", edt_ads_Website.getText().toString());
                params.put("description", edt_ads_Description.getText().toString());
                params.put("address", edt_ads_address.getText().toString());
                params.put("city", edt_ads_City.getText().toString());
                params.put("zip", edt_ads_Zip.getText().toString());
                if (!TextUtils.isEmpty((btn1.substring(btn1.lastIndexOf(".")+1)))) {
                    params.put("logo", imageuploadActivity.encodeImgTOBase64(btn1, (btn1.substring(btn1.lastIndexOf(".") + 1))));
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
    /**
     * Called when a view has been clicked.
     *
     */
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
                    img_ads_Logo.setImageBitmap(thumbnail);
                    img_ads_Logo.setVisibility(View.VISIBLE);
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
                            img_ads_Logo.setImageBitmap(bitmap);
                            img_ads_Logo.setVisibility(View.VISIBLE);
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