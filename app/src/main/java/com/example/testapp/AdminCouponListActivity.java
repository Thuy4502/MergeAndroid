package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.adapter.CouponAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Coupon;
import com.example.testapp.response.CommonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminCouponListActivity extends AppCompatActivity {
    private Button btnAddCoupon;
    private ListView lvCouponList;
    private CouponAdapter couponManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_coupon_list);
        setControl();
        setEvent();
    }

    private void setEvent() {
        String token = "Bearer " + "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTI4NDMxNjgsImV4cCI6MTcxMzQ0Nzk2OCwidXNlcm5hbWUiOiIrODQ4NDUwMDI0MDUifQ.E3Ds9y-OpJ1DkxmE3EtrueoBsNfJ8GJr_5oqkFkp_bCAwyCX_xzgk0V9geraQh8C";
        getAllCoupon(token);

        btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCouponActivity();
            }
        });
    }

    private void getAllCoupon(String token) {
        ApiService.apiService.getAllCoupon(token).enqueue(new Callback<CommonResponse<Coupon>>() {
            @Override
            public void onResponse(Call<CommonResponse<Coupon>> call, Response<CommonResponse<Coupon>> response) {
                if(response.isSuccessful()){
                    CommonResponse<Coupon> resultResponse = response.body();
                    if(resultResponse != null){
                        List<Coupon> listCoupon = resultResponse.getData();
                        couponManagerAdapter = new CouponAdapter (AdminCouponListActivity.this, R.layout.layout_item_coupon_manager, listCoupon);
                        lvCouponList.setAdapter(couponManagerAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonResponse<Coupon>> call, Throwable t) {

            }
        });
    }


    private void openAddCouponActivity() {
        Intent intent = new Intent(this, AdminInfoCouponActivity.class);
        startActivity(intent);
    }

    private void setControl() {
        btnAddCoupon = findViewById(R.id.btn_addCoupon);
        lvCouponList = findViewById(R.id.lv_couponList);
    }
}