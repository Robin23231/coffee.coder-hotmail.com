package com.demo.winery.tour;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.demo.winery.model.Company_content;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_My;
import com.demo.winery.utils.BaseActivity;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class Tour_detail_my  extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {


    String id;

    Button btn_tour_Update;
    EditText edt_tour_name, edt_tour_address,edt_tour_City,edt_tour_State,edt_tour_Zip,edt_tour_Phone,edt_tour_Website,edt_tour_Description;
    ImageView img_tour_Logo;
    private static final String IMAGE_DIRECTORY = "/mytour";
    BaseActivity imageuploadActivity;
    ResponseModel responseModel;
    String btn1="";
    Company_content objRes;
    byte[] imageBytes;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //will hide the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_tour_detail_my);

        Methods.showProgress(this);
     //for logo capture
        requestMultiplePermissions();
        imageuploadActivity = new BaseActivity();

//get id
        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);
        Log.e("tour_id", id);
///for get info by id
        winery_info_progress(id);

        edt_tour_name = findViewById(R.id.edt_tour_name);
        edt_tour_address = findViewById(R.id.edt_tour_address);
        edt_tour_City = findViewById(R.id.edt_tour_City);
        edt_tour_State = findViewById(R.id.edt_tour_State);
        edt_tour_Zip = findViewById(R.id.edt_tour_Zip);
        edt_tour_Phone = findViewById(R.id.edt_tour_Phone);
        edt_tour_Website = findViewById(R.id.edt_tour_Website);
        edt_tour_Description = findViewById(R.id.edt_tour_Description);
        img_tour_Logo = findViewById(R.id.img_tour_Logo);
        img_tour_Logo.setOnClickListener(this);

        btn_tour_Update = findViewById(R.id.btn_tour_Update);
        btn_tour_Update.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_tour_Logo:
                showPictureDialog();
                break;
            case R.id.btn_tour_Update:
                Tout_update_process();
                Methods.showProgress(Tour_detail_my.this);
                break;
        }
    }

    private void winery_info_progress(final String id) {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.TOUR_GETINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        objRes = gson.fromJson(response, Company_content.class);

                        if(objRes.getStatus().equals("200")){
                            edt_tour_name.setText(objRes.getCompany());
                            edt_tour_address.setText(objRes.getAddress());
                            edt_tour_City.setText(objRes.getCity());
                            edt_tour_State.setText(objRes.getState());
                            edt_tour_Zip.setText(objRes.getZip());
                            edt_tour_Phone.setText(objRes.getPhone());
                            edt_tour_Website.setText(objRes.getWebsite());
                            edt_tour_Description.setText(objRes.getDescription());
                            imageBytes = Base64.decode(objRes.getlogo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            img_tour_Logo.setImageBitmap(decodedImage);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), Tour_detail_my.this);
                        }
                        Methods.closeProgress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), Tour_detail_my.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.ID, id);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srStatus);
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
    public void onMapReady(GoogleMap googleMap) {

    }

    private void Tout_update_process() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.TOUR_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);

                        if(responseModel.getStatus().equals("200")){
                            finish();
                            Intent my_intent = new Intent(Tour_detail_my.this, Activity_My.class);
                            startActivity(my_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), Tour_detail_my.this);
                        }
                        Methods.closeProgress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), Tour_detail_my.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tourname", edt_tour_name.getText().toString());
                params.put("touraddress", edt_tour_address.getText().toString());
                params.put("tourcity", edt_tour_City.getText().toString());
                params.put("tourstate", edt_tour_State.getText().toString());
                params.put("tourzip", edt_tour_Zip.getText().toString());
                params.put("tourphone", edt_tour_Phone.getText().toString());
                params.put("email", getString(constant.EMAIL));
                params.put("tourwebsite", edt_tour_Website.getText().toString());
                params.put("tourdescription", edt_tour_Description.getText().toString());
                if (!TextUtils.isEmpty((btn1.substring(btn1.lastIndexOf(".")+1)))) {
                    params.put("tourlogo", imageuploadActivity.encodeImgTOBase64(btn1, (btn1.substring(btn1.lastIndexOf(".") + 1))));
                }
                params.put(constant.ID, id);
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
                    img_tour_Logo.setImageBitmap(thumbnail);
                    img_tour_Logo.setVisibility(View.VISIBLE);
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
                            img_tour_Logo.setImageBitmap(bitmap);
                            img_tour_Logo.setVisibility(View.VISIBLE);
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