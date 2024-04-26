package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminInfoCouponActivity extends AppCompatActivity {
    private Button btnUploadImg;
    private ImageView ivImgCoupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info_coupon);
        setControl();
        setEvent();

    }

    private void setEvent() {
    }

    private void setControl() {
        btnUploadImg = findViewById(R.id.btn_uploadImg);
        ivImgCoupon = findViewById(R.id.iv_imgCoupon);
    }
}