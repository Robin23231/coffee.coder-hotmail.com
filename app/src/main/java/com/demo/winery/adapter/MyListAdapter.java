package com.demo.winery.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.winery.R;
import com.demo.winery.activities.activity_Details;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> implements LocationListener {
    List<BaseCompanyInfo> baseCompanyInfo;
    Context mContext;
    public MyListAdapter(Context context, List<BaseCompanyInfo> objects) {
        super();
        this.mContext = context;
        baseCompanyInfo = objects;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BaseCompanyInfo baseCompanyInfo12 = baseCompanyInfo.get(position);
        if (baseCompanyInfo12.getlogo().equals("")){
            holder.imageView.setImageResource(R.drawable.winery_logo);

        }else {
            byte[] imageBytes = Base64.decode(baseCompanyInfo12.getlogo(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imageView.setImageBitmap(decodedImage);
        }

        holder.review_v.setRating(baseCompanyInfo12.getstars());
        holder.tv_company.setText(baseCompanyInfo12.getCompany());
        holder.tv_address.setText(baseCompanyInfo12.getAddress());
        holder.tv_distance.setText(baseCompanyInfo12.getdistance() + " Miles");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, activity_Details.class);

                intent.putExtra(constant.ID, baseCompanyInfo12.getId());
                intent.putExtra(constant.ADDRESS, baseCompanyInfo12.getAddress());
                intent.putExtra(constant.ZIP, baseCompanyInfo12.getZip());
                intent.putExtra(constant.REVIEW_STAR, baseCompanyInfo12.getstars());
                intent.putExtra(constant.DISTANCE, baseCompanyInfo12.getdistance());
                //float start = baseCompanyInfo1.getstars();
                Methods.showProgress(mContext);

                mContext.startActivity(intent);
            }
        });
        Methods.closeProgress();
    }

    @Override
    public int getItemCount() {
        return baseCompanyInfo.size();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        RatingBar review_v;
        TextView tv_company, tv_address, tv_distance;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.review_v = (RatingBar) itemView.findViewById(R.id.review_v);
            this.tv_company = (TextView) itemView.findViewById(R.id.tv_company);
            this.tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            this.tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;

                dist = dist * 0.8684;

            return (dist);
        }
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
}
