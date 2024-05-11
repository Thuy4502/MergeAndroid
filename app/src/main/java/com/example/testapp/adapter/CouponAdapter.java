package com.example.testapp.adapter;


import static com.example.testapp.BuyNowActivity.totalPrice;

import static com.example.testapp.function.Function.formatDateTimeToDate;
import static com.example.testapp.function.Function.formatToVND;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.testapp.BuyNowActivity;
import com.example.testapp.R;
import com.example.testapp.UserOrderActivity;
import com.example.testapp.function.OnSaveClickListener;
import com.example.testapp.model.Coupon;

import java.util.List;

public class CouponAdapter extends ArrayAdapter<Coupon> {
    private Context context;
    private int resource;
    private List<Coupon> data;
    private List<Coupon> filterCouponList;
    private OnSaveClickListener onSaveClickListener;
    public void setOnSaveClickListener(OnSaveClickListener listener) {
        this.onSaveClickListener = listener;
    }

    public CouponAdapter(Context context, List<Coupon> coupons) {
        super(context, 0, coupons);
    }

    public CouponAdapter(@NonNull Context context, int resource, List<Coupon> data){
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;

    }
    static class ViewHolder{
        TextView tvValue, tvQuantity, tvMinium, tvTime, tvCouponId, tvStatus, tvSaveCoupon, tvUseCoupon;
        ImageView ivCoupon, ivDelete;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvValue = convertView.findViewById(R.id.tv_value);
            holder.tvMinium = convertView.findViewById(R.id.tv_minium);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            holder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
            holder.ivCoupon = convertView.findViewById(R.id.iv_coupon);
            holder.tvCouponId = convertView.findViewById(R.id.tv_couponId);
            holder.tvStatus = convertView.findViewById(R.id.tv_couponStatus);
            holder.ivDelete = convertView.findViewById(R.id.btnDelete);
            holder.tvSaveCoupon = convertView.findViewById(R.id.tvSaveCoupon);
            holder.tvUseCoupon = convertView.findViewById(R.id.tv_useCoupon);
            convertView.setTag(holder);
        }
        else{
                holder = (ViewHolder) convertView.getTag();
            }
        Coupon coupon = data.get(position);
        holder.tvValue.setText("Giảm " + formatToVND(coupon.getUse_value()));
//        holder.tvQuantity.setText("Số lượng: "+ String.valueOf(coupon.getQuantity()));
        holder.tvMinium.setText("Đơn tối thiểu " + formatToVND(coupon.getMinimum_value()));
        holder.tvTime.setText("từ "+formatDateTimeToDate(coupon.getStart_date()) +" đến " + formatDateTimeToDate(coupon.getEnd_date()));
        Glide.with(convertView)
                .load(coupon.getImage())
                .into(holder.ivCoupon);
        if(holder.tvCouponId != null){
            holder.tvCouponId.setText("Mã Coupon: " + coupon.getCoupon_id().toString());
            holder.tvStatus.setText(coupon.getStatus());
            if(coupon.getStatus().equals("active")){
                holder.tvStatus.setText("Active");
                holder.tvStatus.setTextColor(ContextCompat.getColorStateList(CouponAdapter.this.getContext(), R.color.green));

            } else if(coupon.getStatus().equals("unactive")) {
                holder.tvStatus.setText("Inactive");
                holder.tvStatus.setTextColor(ContextCompat.getColorStateList(CouponAdapter.this.getContext(), R.color.mainColor));

            }
        }


        if(holder.tvSaveCoupon!=null) {
            holder.tvSaveCoupon = convertView.findViewById(R.id.tvSaveCoupon);
            holder.tvSaveCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSaveClickListener != null) {
                        onSaveClickListener.onSaveClick(String.valueOf(coupon.getCoupon_id()));
                    }
                    Log.i("YYY", "đã bấm"+coupon.getCoupon_id());
                }
            });
        }

        if(holder.tvUseCoupon!=null){
            holder.tvUseCoupon = convertView.findViewById(R.id.tv_useCoupon);
            if(coupon.getMinimum_value()>totalPrice){
                holder.tvUseCoupon.setTextColor(Integer.valueOf(R.color.darkGrey));
            } else {
                holder.tvUseCoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), BuyNowActivity.class);
                        intent.putExtra("USE_VALUE", holder.tvValue.getText());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
        return convertView;
    }


}
