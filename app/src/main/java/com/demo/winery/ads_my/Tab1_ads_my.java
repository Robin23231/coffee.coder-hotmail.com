package com.demo.winery.ads_my;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.demo.winery.ads.Ads_details_my;
import com.demo.winery.model.Company_content;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_My;
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

import static android.content.Context.MODE_PRIVATE;

public class Tab1_ads_my extends Fragment implements View.OnClickListener{

    Button btn_ads_Signup, btn_ads_close;
    EditText edt_ads_onwer, edt_ads_name, edt_ads_address,edt_ads_City,edt_ads_Zip,edt_ads_Phone,edt_ads_Website,edt_ads_Description;
    ImageView img_ads_Logo;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    BaseActivity imageuploadActivity;

    ResponseModel responseModel;
    String btn1="";
    String id;

    Company_content objRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ads_my_tab1, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        requestMultiplePermissions();
        imageuploadActivity = new BaseActivity();

        Ads_details_my Ads_id = (Ads_details_my) getActivity();
        id = Ads_id.id_selector();

        AdsGet_info_progress(id);

        edt_ads_onwer = view.findViewById(R.id.edt_ads_onwer);
        edt_ads_name = view.findViewById(R.id.edt_ads_name);
        edt_ads_address = view.findViewById(R.id.edt_ads_address);
        edt_ads_City = view.findViewById(R.id.edt_ads_City);
        edt_ads_Zip = view.findViewById(R.id.edt_ads_Zip);
        edt_ads_Phone = view.findViewById(R.id.edt_ads_Phone);
        edt_ads_Website = view.findViewById(R.id.edt_ads_Website);
        edt_ads_Description = view.findViewById(R.id.edt_ads_Description);
        img_ads_Logo = view.findViewById(R.id.img_ads_Logo);
        img_ads_Logo.setOnClickListener(this);

        btn_ads_Signup = view.findViewById(R.id.btn_ads_Signup);
        btn_ads_Signup.setOnClickListener(this);

        btn_ads_close = view.findViewById(R.id.btn_ads_close);
        btn_ads_close.setOnClickListener(this);
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
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
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void AdsGet_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.ADS_GETINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            edt_ads_onwer.setText(objRes.getcontact_name());
                            edt_ads_name.setText(objRes.getCompany());
                            edt_ads_address.setText(objRes.getAddress());
                            edt_ads_City.setText(objRes.getCity());
                            edt_ads_Zip.setText(objRes.getZip());
                            edt_ads_Phone.setText(objRes.getPhone());
                            edt_ads_Website.setText(objRes.getWebsite());
                            edt_ads_Description.setText(objRes.getDescription());
                            byte[] imageBytes = Base64.decode(objRes.getads_logo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            img_ads_Logo.setImageBitmap(decodedImage);

                        }else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
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
            case R.id.img_ads_Logo:
                showPictureDialog();
                break;
            case R.id.btn_ads_Signup:
                winery_edit_progress();
                break;
            case R.id.btn_ads_close:
                close_account();
        }
    }

    private void close_account() {
        StringRequest res_signin = new StringRequest(Request.Method.POST, constant.ADS_CLOSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);

                            Methods.showAlertDialog(objRes.getMsg(), getContext());

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
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(res_signin);
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

    private void winery_edit_progress() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.ADS_UPDATE,
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
                params.put("advertiser_name", edt_ads_onwer.getText().toString());
                params.put("company_name", edt_ads_name.getText().toString());
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
                params.put(constant.ID, id);
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

    private void showPictureDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Picture Option");
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
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                    img_ads_Logo.setImageBitmap(thumbnail);
                    img_ads_Logo.setVisibility(View.VISIBLE);
                    saveImage(thumbnail);
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK){

                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                            String path = saveImage(bitmap);

                            img_ads_Logo.setImageBitmap(bitmap);
                            img_ads_Logo.setVisibility(View.VISIBLE);


                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
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
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            btn1 = f.getAbsolutePath();


            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}