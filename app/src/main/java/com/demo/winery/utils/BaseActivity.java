package com.demo.winery.utils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by Satyawan on 2/4/15.
 */
public class BaseActivity extends AppCompatActivity {


	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}


    public String encodeImgTOBase64(String path,String extension){
        DisplayMetrics metrics = new DisplayMetrics();
        Bitmap bm = ImageDecoding.decodeBitmapFromFile(path,metrics.widthPixels, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")){
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }else if(extension.equalsIgnoreCase("png")){
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

}
