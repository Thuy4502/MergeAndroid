package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.function.Function;
import com.example.testapp.model.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private List<Review> data;

    public ReviewAdapter(Context context, int resource, List <Review> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    static class ViewHolder {
        TextView tvUserName, tvContentReview, tvDateReview;
        RatingBar rtbStar;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        ReviewAdapter.ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ReviewAdapter.ViewHolder();
            holder.tvUserName = convertView.findViewById(R.id.tv_userName);
            holder.tvContentReview = convertView.findViewById(R.id.tv_contentReview);
            holder.rtbStar = convertView.findViewById(R.id.rtb_star);
            holder.tvDateReview = convertView.findViewById(R.id.tv_dateReview);
            convertView.setTag(holder);
        }else {
            holder = (ReviewAdapter.ViewHolder) convertView.getTag();
        }

        Review review = data.get(position);

        holder.tvUserName.setText(review.getCustomer_name());
        holder.tvContentReview.setText(review.getContent());
        holder.rtbStar.setRating(review.getStar());
        holder.tvDateReview.setText(Function.formatDateTimeToDate(review.getDate()));

        return convertView;
    }
}
