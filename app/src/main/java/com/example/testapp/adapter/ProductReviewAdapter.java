package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.UserReviewActivity;
import com.example.testapp.model.OrderDetail;

import java.util.List;

public class ProductReviewAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<OrderDetail> data;



    public ProductReviewAdapter(Context context, int resource, List <OrderDetail> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }
    static class ViewHolder {
        TextView tvProductName, tvProductQuantity;
        ImageView ivProductImg;
        RadioButton radioBtnSelect;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        ViewHolder holder;
        if(convertView == null) {

            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvProductName = convertView.findViewById(R.id.tv_productName);
            holder.tvProductQuantity = convertView.findViewById(R.id.tv_productQuantity);
            holder.ivProductImg = convertView.findViewById(R.id.img_product);
            holder.radioBtnSelect = convertView.findViewById(R.id.radioBtn_selectProduct);

            holder.radioBtnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton radio = (RadioButton) view;
                    OrderDetail orderDetail = (OrderDetail) radio.getTag();
                    if (radio.isChecked()) {
                        Toast.makeText(context, "checked:"+ orderDetail.getProduct().getProductId(), Toast.LENGTH_SHORT).show();
                        UserReviewActivity.product_id = orderDetail.getProduct().getProductId();
                    } else{
                        Toast.makeText(context, "no check:"+ orderDetail.getProduct().getProductId(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // set data
        OrderDetail orderDetail = data.get(position);

        holder.tvProductName.setText(orderDetail.getProduct().getProductName());
        holder.tvProductQuantity.setText("SL: " + String.valueOf(orderDetail.getQuantity()));
        Glide.with(convertView)
                .load(orderDetail.getProduct().getImage())
                .into(holder.ivProductImg);
        holder.radioBtnSelect.setTag(orderDetail);


        return convertView;
    }

}
