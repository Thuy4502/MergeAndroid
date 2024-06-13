package com.example.testapp;


import static com.example.testapp.api.ApiService.apiService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.Interface.OnSaveClickListener;
import com.example.testapp.adapter.CouponAdapter;
import com.example.testapp.model.Coupon;
import com.example.testapp.model.CouponDetail;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.CommonResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CouponUserActivity extends AppCompatActivity {
    private TextView tvShowList, tvShowListReceived, tvSaveCoupon;
    private ListView lv_listCoupon;
    private CouponAdapter couponAdapter;
    private ImageView ivBack;
    boolean isCallApi=false, isChangeList=false;
    static List<CouponDetail> listMyCoupon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_coupon_user);
        setControl();
        setEvent();
    }

    protected void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String tokenUser =  sharedPreferences.getString("token", null);

        getCouponList(tokenUser);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvShowListReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowList.setTextColor(getColor(R.color.black));
                tvShowList.setBackground(null);
                tvShowListReceived.setBackground(getDrawable(R.drawable.style_border_bottom_3));
                tvShowListReceived.setTextColor(getColor(R.color.mainColor));
                getMyCoupon(tokenUser);
            }
        });

        tvShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowListReceived.setTextColor(getColor(R.color.black));
                tvShowListReceived.setBackground(null);
                tvShowList.setBackground(getDrawable(R.drawable.style_border_bottom_3));
                tvShowList.setTextColor(getColor(R.color.mainColor));
                getCouponList(tokenUser);
            }
        });

    }

    protected void setControl() {
        lv_listCoupon = findViewById(R.id.lv_listCoupon);
        tvShowList = findViewById(R.id.tv_showListCoupon);
        tvShowListReceived = findViewById(R.id.tv_showListReceivedCoupon);
        ivBack = findViewById(R.id.ivBack);
        tvSaveCoupon = findViewById(R.id.tvSaveCoupon);
    }

    public void getCouponList(String tokenUser) {
        List<Coupon> listDataCoupon = new ArrayList<>();
        apiService.getAllCoupon("Bearer "+tokenUser).enqueue(new Callback<CommonResponse<Coupon>>() {
            @Override
            public void onResponse(Call<CommonResponse<Coupon>> call, Response<CommonResponse<Coupon>> response) {

                if (response.isSuccessful()) {
                    CommonResponse<Coupon> result = response.body();
                    if (result != null) {
                        List<Coupon> listAllCoupon = result.getData();
                        for(Coupon coupon:listAllCoupon){
                            if(coupon.getStatus().equals("active")&&coupon.getRemaining_amount()>0){
                                listDataCoupon.add(coupon);
                            }
                        }

                        couponAdapter = new CouponAdapter(CouponUserActivity.this, R.layout.item_coupon, listDataCoupon);
                        lv_listCoupon.setAdapter(couponAdapter);

                        couponAdapter.setOnSaveClickListener(new OnSaveClickListener() {
                            @Override
                            public void onSaveClick(String coupon_id) {
                                Long couponId = Long.valueOf(coupon_id);
                                for(Coupon coupon:listAllCoupon){
                                    if(coupon.getStatus().equals("active") && coupon.getRemaining_amount()>0){
                                        listDataCoupon.add(coupon);
                                    }
                                }
                                customerGetCoupon(tokenUser, couponId);
                                Log.i("coupon id", String.valueOf(couponId));
                            }
                        });
                    }
                } else {
                    Toast.makeText(CouponUserActivity.this, "response false", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CommonResponse<Coupon>> call, Throwable t) {
                Toast.makeText(CouponUserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getMyCoupon(String tokenUser) {
        List<Coupon> MyCouponList = new ArrayList<>();
        apiService.getMyCoupon("Bearer "+ tokenUser).enqueue(new Callback<CommonResponse<CouponDetail>>() {
            @Override
            public void onResponse(Call<CommonResponse<CouponDetail>> call, Response<CommonResponse<CouponDetail>> response) {
                if (response.isSuccessful()) {
                    CommonResponse<CouponDetail> result = response.body();
                    if (result != null) {
                        List<CouponDetail> couponList = result.getData();
                        listMyCoupon = couponList;
                        for (CouponDetail couponDetail: couponList){
                            Coupon coupon =  couponDetail.getCoupon();
                            MyCouponList.add(coupon);
                        }
                        couponAdapter = new CouponAdapter(CouponUserActivity.this, R.layout.item_coupon_user, MyCouponList);;
                        lv_listCoupon.setAdapter(couponAdapter);
                        //MyCouponList.clear();
                    }
                } else {
                    Toast.makeText(CouponUserActivity.this, "getMyCoupon false", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CommonResponse<CouponDetail>> call, Throwable t) {
                Toast.makeText(CouponUserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void customerGetCoupon(String tokenUser, Long coupon_id) {
        apiService.customerGetCoupon("Bearer "+ tokenUser, coupon_id.toString()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.i("YY", ""+response);
                if (response.isSuccessful()) {
                    Toast.makeText(CouponUserActivity.this, "đã lưu mã giảm giá!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CouponUserActivity.this, "response false", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CouponUserActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

