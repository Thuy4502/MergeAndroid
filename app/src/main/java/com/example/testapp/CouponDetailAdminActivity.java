package com.example.testapp;

import static com.example.testapp.CouponListAdminActivity.tokenStaff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.testapp.adapter.CouponAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Coupon;
import com.example.testapp.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CouponDetailAdminActivity extends AppCompatActivity {
    TextView tvTitleCoupon, tvStartDate, tvEndDate, tvCouponContent, tvCouponId;
    Button btnDisableCoupon;
    String coupon_id, coupon_title, startDate, endDate, content, status;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_coupon_detail_admin);
        Intent intent = getIntent();
        coupon_title = intent.getStringExtra("title");
        coupon_id = intent.getStringExtra("id");
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        content = intent.getStringExtra("content");
        status = intent.getStringExtra("status");
        setControl();
        setEvent();
    }


    protected void setEvent() {
        tvTitleCoupon.setText(coupon_title);
        tvCouponId.setText(coupon_id);
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
        tvCouponContent.setText(content);
        Log.i("RR", ""+status);
        if(status.equals("unactive")){
            btnDisableCoupon.setVisibility(View.GONE);
        }
        if (status.equals("active")){
            btnDisableCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Coupon coupon = new Coupon();
                    coupon.setStatus("unactive");
                    disableCoupon(tokenStaff, coupon_id, coupon);
                }
            });
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CouponDetailAdminActivity.this, CouponListAdminActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void setControl() {
        tvTitleCoupon = findViewById(R.id.tvTitleCoupon);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvCouponContent = findViewById(R.id.tvCouponContent);
        tvCouponId = findViewById(R.id.tvCouponId);
        btnDisableCoupon = findViewById(R.id.btnDisableCoupon);
        ivBack = findViewById(R.id.ivBack);
    }

    private void disableCoupon(String token, String id, Coupon coupon){
        ApiService.apiService.disableCoupon(token, id, coupon).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(CouponDetailAdminActivity.this, CouponListAdminActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(CouponDetailAdminActivity.this, "response false",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CouponDetailAdminActivity.this, "call api fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}