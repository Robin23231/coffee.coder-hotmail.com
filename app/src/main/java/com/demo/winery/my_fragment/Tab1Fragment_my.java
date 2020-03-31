package com.demo.winery.my_fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.demo.winery.model.Company_content;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_My;
import com.demo.winery.my_activities.activity_Details_my;
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

public class Tab1Fragment_my extends Fragment implements View.OnClickListener{

    Button btn_submit;
    EditText edt_winery_name, edt_address,edt_City,edt_State,edt_Zip,edt_County,edt_Phone,edt_Website,edt_Email,edt_WineTasking, edt_Description;
    ImageView img_Logo_editWinery;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    BaseActivity imageuploadActivity;

    ResponseModel responseModel;
    String btn1="";
    String id;

    Company_content objRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one_my, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        requestMultiplePermissions();
        imageuploadActivity = new BaseActivity();

        activity_Details_my activity_details = (activity_Details_my) getActivity();
        id = activity_details.id_selector();

        winery_info_progress(id);

        edt_winery_name = view.findViewById(R.id.edt_winery_name);
        edt_address = view.findViewById(R.id.edt_address);
        edt_City = view.findViewById(R.id.edt_City);
        edt_State = view.findViewById(R.id.edt_State);
        edt_Zip = view.findViewById(R.id.edt_Zip);
        edt_County = view.findViewById(R.id.edt_County);
        edt_Phone = view.findViewById(R.id.edt_Phone);
        edt_Website = view.findViewById(R.id.edt_Website);
        edt_Email = view.findViewById(R.id.edt_Email);
        edt_WineTasking = view.findViewById(R.id.edt_WineTasking);
        edt_Description = view.findViewById(R.id.edt_Description);

//        btn_close_account = view.findViewById(R.id.btn_close_account);
//        btn_close_account.setOnClickListener(this);

        img_Logo_editWinery = view.findViewById(R.id.img_Logo_editWinery);
        img_Logo_editWinery.setOnClickListener(this);

        btn_submit = view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
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


    private void winery_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.WINERY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            edt_winery_name.setText(objRes.getCompany());
                            edt_address.setText(objRes.getAddress());
                            edt_City.setText(objRes.getCity());
                            edt_State.setText(objRes.getState());
                            edt_Zip.setText(objRes.getZip());
                            edt_County.setText(objRes.getCounty());
                            edt_Phone.setText(objRes.getPhone());
                            edt_Website.setText(objRes.getWebsite());
                            edt_Email.setText(objRes.getEmail());
                            edt_WineTasking.setText(objRes.getWineTasting());
                            edt_Description.setText(objRes.getDescription());
                            byte[] imageBytes = Base64.decode(objRes.getlogo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            img_Logo_editWinery.setImageBitmap(decodedImage);

                        }else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                        }
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
            case R.id.img_Logo_editWinery:
                showPictureDialog();
                break;
            case R.id.btn_submit:
                winery_edit_progress();
                break;
//            case R.id.btn_close_account:
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Delete Account")
//                        .setMessage("Are you sure you want to delete your account?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                close_account_process();
//                                //Toast.makeText(getContext(), "close", Toast.LENGTH_LONG).show();
//                            }
//                        })
//
//                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();

        }
    }

//    private void close_account_process() {
//        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.WINERY_CLOSE_ACCOUNT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Methods.closeProgress();
//                        Gson gson = new Gson();
//                        responseModel = gson.fromJson(response, ResponseModel.class);
//
//                        if(responseModel.getStatus().equals("200")){
//                            Intent askpay_intent = new Intent(getContext(), Activity_My.class);
//                            startActivity(askpay_intent);
//                        }else {
//                            Methods.showAlertDialog(responseModel.getMsg(), getContext());
//                        }
//                        Toast.makeText(getContext(), responseModel.getMsg(), Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Methods.showAlertDialog(getString(R.string.error_network_check), getContext());
//                    }
//                }) {
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(constant.ID, id);
//                return params;
//            }
//            @Override
//            public Map<String, String> getHeaders() {
//                return addHeader();
//            }
//        };
//        MySingleton.getInstance(getContext()).addToRequestQueue(srStatus);
//    }

    private void winery_edit_progress() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.WINERY_UPDATE,
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
                params.put(constant.WINERYNAME, edt_winery_name.getText().toString());
                params.put(constant.ADDRESS, edt_address.getText().toString());
                params.put(constant.CITY, edt_City.getText().toString());
                params.put(constant.STATE, edt_State.getText().toString());
                params.put(constant.ZIP, edt_Zip.getText().toString());
                params.put(constant.PHONE, edt_Phone.getText().toString());
                params.put(constant.WEBSITE, edt_Website.getText().toString());
                params.put(constant.EMAIL, edt_Email.getText().toString());
                params.put(constant.WINETASKING, edt_WineTasking.getText().toString());
                params.put(constant.DESCRIPTION, edt_Description.getText().toString());
                if (!TextUtils.isEmpty((btn1.substring(btn1.lastIndexOf(".")+1)))) {
                    params.put(constant.WINERYLOGO, imageuploadActivity.encodeImgTOBase64(btn1, (btn1.substring(btn1.lastIndexOf(".") + 1))));
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

                    img_Logo_editWinery.setImageBitmap(thumbnail);
                    img_Logo_editWinery.setVisibility(View.VISIBLE);
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

                            img_Logo_editWinery.setImageBitmap(bitmap);
                            img_Logo_editWinery.setVisibility(View.VISIBLE);


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