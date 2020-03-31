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
import com.demo.winery.fetival.Festival_detail_one;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;

import java.util.List;

//import com.demo.winery.fragment.DetailsFragment;

public class MyListAdapter_festival extends RecyclerView.Adapter<MyListAdapter_festival.ViewHolder>{
    List<BaseCompanyInfo> baseCompanyInfo;
    Context mContext;
    public MyListAdapter_festival(Context context, List<BaseCompanyInfo> objects) {
        super();
        this.mContext = context;
        baseCompanyInfo = objects;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_festival, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Methods.closeProgress();
        final BaseCompanyInfo baseCompanyInfo1 = baseCompanyInfo.get(position);
        byte[] imageBytes = Base64.decode(baseCompanyInfo1.getlogo(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.img_logo.setImageBitmap(decodedImage);

        holder.tv_festival_name.setText(baseCompanyInfo1.getfestival_name());
        holder.tv_festival_period.setText(baseCompanyInfo1.getfestival_date()+" - "+baseCompanyInfo1.getfestival_time());
        holder.tv_festival_distance.setText(baseCompanyInfo1.getfestival_id());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent = new Intent(mContext, Festival_detail_one.class);

        intent.putExtra(constant.ID, baseCompanyInfo1.getfestival_id());
        intent.putExtra(constant.ZIP, baseCompanyInfo1.getfestival_zip_code());
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
        public ImageView img_logo;
        TextView tv_festival_name, tv_festival_period, tv_festival_distance;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            this.tv_festival_name = (TextView) itemView.findViewById(R.id.tv_festival_name);
            this.tv_festival_period = (TextView) itemView.findViewById(R.id.tv_festival_period);
            this.tv_festival_distance = (TextView) itemView.findViewById(R.id.tv_festival_distance);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}