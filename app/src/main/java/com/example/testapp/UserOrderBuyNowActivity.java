package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserOrderBuyNowActivity extends AppCompatActivity {
    private Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_buy_now);

        setControl();
        setEvent();
    }

    private void setEvent() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProcessActivity();
            }
        });
    }

    private void openProcessActivity() {
        Intent intent = new Intent(this, UserDeliveryProcessActivity.class);
        startActivity(intent);
    }

    private void setControl() {
        btnBuy = findViewById(R.id.btn_buyNow);
    }
}