package com.demo.winery.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.winery.R;
import com.demo.winery.event.Event_detail;
import com.demo.winery.model.BaseCompanyInfo;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;

import java.util.List;

//import com.demo.winery.fragment.DetailsFragment;

public class MyListAdapter_event extends RecyclerView.Adapter<MyListAdapter_event.ViewHolder>{
    List<BaseCompanyInfo> baseCompanyInfo;
    Context mContext;
    // RecyclerView recyclerView;
    public MyListAdapter_event(Context context, List<BaseCompanyInfo> objects) {
        super();
        this.mContext = context;
        baseCompanyInfo = objects;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_event, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Methods.closeProgress();
        final BaseCompanyInfo baseCompanyInfo1 = baseCompanyInfo.get(position);
        if (baseCompanyInfo1.getevent_name().equals("")){
            holder.tv_evnet_name.setText("<No Event Name>");
        }else {
            holder.tv_evnet_name.setText(baseCompanyInfo1.getevent_name());
        }
        holder.tv_evnet_period.setText(baseCompanyInfo1.getdate()+"-"+baseCompanyInfo1.getstart_time()+" ~ "+baseCompanyInfo1.getdate()+"-"+baseCompanyInfo1.getend_time());
        holder.tv_event_location.setText(baseCompanyInfo1.getlocation_event());
        holder.tv_event_distance.setText(baseCompanyInfo1.getid_event());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open activity from adapter
                Intent intent = new Intent(mContext, Event_detail.class);
                intent.putExtra(constant.ID, baseCompanyInfo1.getid_event());
                intent.putExtra(constant.ZIP, baseCompanyInfo1.getevent_zip());

                Log.e("event_adapter_zip", baseCompanyInfo1.getevent_zip());
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
        //public ImageView imageView;
        TextView tv_evnet_name, tv_evnet_period, tv_event_location, tv_event_distance;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            //this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.tv_evnet_name = (TextView) itemView.findViewById(R.id.tv_evnet_name);
            this.tv_evnet_period = (TextView) itemView.findViewById(R.id.tv_evnet_period);
            this.tv_event_location = (TextView) itemView.findViewById(R.id.tv_event_location);
            this.tv_event_distance = (TextView) itemView.findViewById(R.id.tv_event_distance);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}