package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.adapter.CouponAdapter;
import com.example.testapp.model.Coupon;

import java.util.ArrayList;
import java.util.List;

public class UserCouponActivity extends AppCompatActivity {
    private TextView tvShowList, tvShowListReceived;
    private ListView lv_listCoupon;
    private List<Coupon> listDataCoupon = new ArrayList<>();
    private CouponAdapter couponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coupon);
        setControl();
        setEvent();
    }

    private void setEvent() {
        couponAdapter = new CouponAdapter(this, R.layout.layout_item_coupon, listDataCoupon);
        lv_listCoupon.setAdapter(couponAdapter);

        tvShowListReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowList.setTextColor(getColor(R.color.black));
                tvShowList.setBackground(null);
                tvShowListReceived.setBackground(getDrawable(R.drawable.style_border_bottom_3));
                tvShowListReceived.setTextColor(getColor(R.color.mainColor));
                couponAdapter = new CouponAdapter(UserCouponActivity.this, R.layout.layout_item_coupon_customer, listDataCoupon);
                lv_listCoupon.setAdapter(couponAdapter);
            }
        });
        tvShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowListReceived.setTextColor(getColor(R.color.black));
                tvShowListReceived.setBackground(null);
                tvShowList.setBackground(getDrawable(R.drawable.style_border_bottom_3));
                tvShowList.setTextColor(getColor(R.color.mainColor));
                couponAdapter = new CouponAdapter(UserCouponActivity.this, R.layout.layout_item_coupon, listDataCoupon);
                lv_listCoupon.setAdapter(couponAdapter);
            }
        });

    }

    private void setControl() {
        lv_listCoupon = findViewById(R.id.lv_listCoupon);

        tvShowList = findViewById(R.id.tv_showListCoupon);
        tvShowListReceived = findViewById(R.id.tv_showListReceivedCoupon);
    }
}