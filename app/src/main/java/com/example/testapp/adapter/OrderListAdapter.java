package com.example.testapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.testapp.R;
import com.example.testapp.StaffOrderDetailActivity;
import com.example.testapp.function.Function;
import com.example.testapp.model.Order;

import java.util.List;

public class OrderListAdapter extends ArrayAdapter {
    private Context context;
    private int resource;               // layout id
    private List <Order> data;          // sample data

    private TextView txtAddress, txtOrderPrice, txtStatus, txtCreatedDate, tvRewardPoint;
    private LinearLayout orderItem;

    public OrderListAdapter(@NonNull Context context, int resource, List <Order> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        txtAddress = convertView.findViewById(R.id.txtAddress);
        txtOrderPrice = convertView.findViewById(R.id.txtOrderPrice);
        txtStatus = convertView.findViewById(R.id.txtOrderStatus);
        txtCreatedDate = convertView.findViewById(R.id.txtCreatedDate);
        tvRewardPoint = convertView.findViewById(R.id.tvRewardPoint);


        Order order = data.get(position);
//        txtAddress.setText(order.getAddress());
        txtAddress.setText("35/2A, đường 339, phuờng Phước Long");
        txtOrderPrice.setText(String.valueOf(Function.formatToVND(order.getTotal_price())));
        tvRewardPoint.setText("0 điểm");
//        txtStatus.setText(order.getOrderType()+"  |  "+order.getStatus());
        txtCreatedDate.setText(order.getCreate_at());

        // set màu cho từng trạng thái đơn hàng
        if(order.getStatus().equals(0)){
            txtStatus.setText("Chờ xác nhận");
            txtStatus.setBackgroundTintList (ContextCompat.getColorStateList(OrderListAdapter.this.getContext(), R.color.top1Color));
        }
        if (order.getStatus().equals(1)){
            txtStatus.setText("Đã xác nhận");
            txtStatus.setBackgroundTintList (ContextCompat.getColorStateList(OrderListAdapter.this.getContext(), R.color.top2Color));
        } else if (order.getStatus().equals(2)) {
            txtStatus.setText("Đang thực hiện");
            txtStatus.setBackgroundTintList (ContextCompat.getColorStateList(OrderListAdapter.this.getContext(), R.color.top3Color));
        } else if (order.getStatus().equals(3)){
            txtStatus.setText("Đang vận chuyển");
            txtStatus.setBackgroundTintList (ContextCompat.getColorStateList(OrderListAdapter.this.getContext(), R.color.top4Color));
        } else if(order.getStatus().equals(4)){
            txtStatus.setText("Đã hoàn thành");
            txtStatus.setBackgroundTintList (ContextCompat.getColorStateList(OrderListAdapter.this.getContext(), R.color.top5Color));
            tvRewardPoint.setText( "+ " +String.valueOf(order.getTotal_price() / 1000) + " điểm");
        }else if (order.getStatus().equals(5)){
            txtStatus.setText("Đã hủy");
            txtStatus.setBackgroundTintList (ContextCompat.getColorStateList(OrderListAdapter.this.getContext(), R.color.mainColor));

        }



        // click xem chi tiết đơn hàng
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order != null){
                    Long orderId = order.getOrder_id();
                    Intent intent = new Intent(context, StaffOrderDetailActivity.class);
                    intent.putExtra("orderId",orderId);
                    context.startActivity(intent);
                }

            }
        });
        return convertView;
    }

}