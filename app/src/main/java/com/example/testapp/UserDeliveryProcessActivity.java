package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testapp.api.ApiService;
import com.example.testapp.function.Function;
import com.example.testapp.model.Order;
import com.example.testapp.response.EntityStatusResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDeliveryProcessActivity extends AppCompatActivity {
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private TextView tvStatusName, tvLineStatus1, tvLineStatus2, tvLineStatus3, tvLineStatus4, tvOrderId, tvTotalQuantity, tvTotalPrice;
    private ImageView tvStatus;
    private ImageButton ibCallShipper;
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_process);
        setControl();
        setEvent();
    }

    private void setEvent() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        getOrderById(token,BuyNowActivity.orderId);
            ibCallShipper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    autoCallShipper();
                }
            });
        }

    private  void showStatusOrder(Integer status_id){
        if(status_id == 0){
            tvStatusName.setText(getString(R.string.statusName_0));
            Glide.with(this)
                    .load(R.drawable.gif_step0)
                    .into(tvStatus);
        }
        // Dat thanh cong
        else if(status_id == 1){
            tvStatusName.setText(getString(R.string.statusName_1));
            Glide.with(this)
                    .load(R.drawable.gif_step1)
                    .into(tvStatus);
            tvLineStatus1.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
        }
        // Dang thuc hien
        else if (status_id ==2){
            tvStatusName.setText(getString(R.string.statusName_2));
            Glide.with(this)
                    .load(R.drawable.gif_step2)
                    .into(tvStatus);
            tvLineStatus1.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
            tvLineStatus2.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
        }
        // Dang giao hang
        else if (status_id == 3){
            tvStatusName.setText(getString(R.string.statusName_3));
            Glide.with(this)
                    .load(R.drawable.gif_step3)
                    .into(tvStatus);
            tvLineStatus1.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
            tvLineStatus2.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
            tvLineStatus3.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
        }
        // Giao thanh cong
        else if (status_id == 4) {
            tvStatusName.setText(getString(R.string.statusName_4));
            Glide.with(this)
                    .load(R.drawable.gif_step4)
                    .into(tvStatus);
            tvLineStatus1.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
            tvLineStatus2.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
            tvLineStatus3.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
            tvLineStatus4.setBackgroundTintList(ContextCompat.getColorStateList(UserDeliveryProcessActivity.this, R.color.green));
    }
}

    private void getOrderById(String token, Long id) {
        ApiService.apiService.getOrderById(token, id).enqueue(new Callback<EntityStatusResponse<Order>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<Order>> call, Response<EntityStatusResponse<Order>> response) {
                if (response.isSuccessful()){
                    EntityStatusResponse<Order> resultResponse = response.body();
                    if(resultResponse != null){
                        Order orderResponse = resultResponse.getData();
                        showStatusOrder(orderResponse.getStatus());
                        tvOrderId.setText(id.toString());
                        tvTotalQuantity.setText(orderResponse.getTotal_quantity().toString());
                        tvTotalPrice.setText(Function.formatToVND(orderResponse.getTotal_price()));
                        Log.i("status",orderResponse.getStatus().toString());
                        Log.i("message", "onResponse: " + resultResponse.getMessage());
                    }
                }
                handler.postDelayed(() -> getOrderById(token,id), 10000); // 3 giây
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<Order>> call, Throwable t) {
                Log.i("error get order: ", t.getMessage());
            }
        });
    }

    private void autoCallShipper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+" + "")));
        } else {
            // Quyền chưa được cấp, bạn có thể yêu cầu người dùng cấp quyền bằng cách hiển thị một hộp thoại yêu cầu quyền
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        }

    }

    private void setControl() {
        tvStatus = findViewById(R.id.iv_status);
        ibCallShipper = findViewById(R.id.ib_autoCallShipper);

        tvLineStatus1 = findViewById(R.id.status_1);
        tvLineStatus2 = findViewById(R.id.status_2);
        tvLineStatus3 = findViewById(R.id.status_3);
        tvLineStatus4 = findViewById(R.id.status_4);
        tvStatusName = findViewById(R.id.tv_statusName);

        tvOrderId = findViewById(R.id.tv_orderId);
        tvTotalQuantity = findViewById(R.id.tv_totalQuantity);
        tvTotalPrice = findViewById(R.id.tv_totalPrice);
    }
}