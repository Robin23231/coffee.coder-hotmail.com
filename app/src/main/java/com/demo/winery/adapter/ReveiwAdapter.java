package com.demo.winery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.demo.winery.R;
import com.demo.winery.model.BaseCompanyInfo;

import java.util.List;

public class ReveiwAdapter extends ArrayAdapter<BaseCompanyInfo> {

    List<String> l;
    Context mContext;
    public ReveiwAdapter(@NonNull Context context, List<BaseCompanyInfo> object) {
        super(context, 0, object);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.list_item_review,null);

        TextView tv_review = (TextView) convertView.findViewById(R.id.tv_review);
        TextView tv_review_detail = (TextView) convertView.findViewById(R.id.tv_review_detail);
        RatingBar review_rating = (RatingBar) convertView.findViewById(R.id.review_rating);

        tv_review.setText(getItem(position).getreview_text());
        tv_review_detail.setText(getItem(position).getusername());
        review_rating.setRating(getItem(position).getstars());
        return convertView;
    }
}
