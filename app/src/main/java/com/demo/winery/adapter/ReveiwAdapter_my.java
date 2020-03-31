package com.demo.winery.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.demo.winery.R;
import com.demo.winery.model.BaseCompanyInfo;

import java.util.List;

public class ReveiwAdapter_my extends ArrayAdapter<BaseCompanyInfo> {

    List<String> l;
    Context mContext;
    /**
     *
     * Constructor
     *
     * @param context  The current context.
     * @param object The resource ID for a layout file containing a TextView to use when
     */
    public ReveiwAdapter_my(@NonNull Context context, List<BaseCompanyInfo> object) {
        super(context, 0, object);
        mContext = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView= LayoutInflater.from(mContext).inflate(R.layout.list_item_review_my,null);

        LinearLayout ly_review = (LinearLayout) convertView.findViewById(R.id.ly_review);
        holder = new ViewHolder();
        TextView tv_review = (TextView) convertView.findViewById(R.id.tv_review);
        TextView tv_review_detail = (TextView) convertView.findViewById(R.id.tv_review_detail);
        RatingBar review_rating = (RatingBar) convertView.findViewById(R.id.review_rating);
        holder.reply_review = convertView.findViewById(R.id.reply_review);
        holder.reply_btn = convertView.findViewById(R.id.reply_btn);
        tv_review.setText(getItem(position).getreview_text());
        tv_review_detail.setText(getItem(position).getusername());
        review_rating.setNumStars((int) getItem(position).getstars());

        holder.reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.putExtra(Intent.EXTRA_EMAIL, new String[]{getItem(position).getemail_review()});
                it.putExtra(Intent.EXTRA_SUBJECT,"Thank for your reply");
                it.putExtra(Intent.EXTRA_TEXT,holder.reply_review.getText());
                it.setType("message/rfc822");
                mContext.startActivity(Intent.createChooser(it,"Choose Mail App"));
            }
        });
        return convertView;
    }

    public static class ViewHolder{
        EditText reply_review;
        Button reply_btn;
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

}
