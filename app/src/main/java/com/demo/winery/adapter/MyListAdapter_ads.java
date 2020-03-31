package com.demo.winery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.winery.R;
import com.demo.winery.ads.Ads_detail;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;

import java.util.List;

//import com.demo.winery.fragment.DetailsFragment;

public class MyListAdapter_ads extends RecyclerView.Adapter<MyListAdapter_ads.ViewHolder>{
    List<BaseCompanyInfo> baseCompanyInfo;
    Context mContext;
    public MyListAdapter_ads(Context context, List<BaseCompanyInfo> objects) {
        super();
        this.mContext = context;
        baseCompanyInfo = objects;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_tour, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Methods.closeProgress();
        final BaseCompanyInfo baseCompanyInfo1 = baseCompanyInfo.get(position);

        byte[] imageBytes = Base64.decode(baseCompanyInfo1.getads_logo(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imageView.setImageBitmap(decodedImage);

        holder.tv_company.setText(baseCompanyInfo1.getCompany());
        holder.tv_address.setText(baseCompanyInfo1.getAddress());
        //holder.tv_distance.setText(baseCompanyInfo1.getcontact_name());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent = new Intent(mContext, Ads_detail.class);

        intent.putExtra(constant.ID, baseCompanyInfo1.getId());
        intent.putExtra(constant.ZIP, baseCompanyInfo1.getZip());
            Methods.showProgress(mContext);
        mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return baseCompanyInfo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        TextView tv_company, tv_address, tv_distance;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.tv_company = (TextView) itemView.findViewById(R.id.tv_company);
            this.tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            //this.tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}