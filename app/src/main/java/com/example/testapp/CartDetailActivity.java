package com.example.testapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CartDetailActivity extends AppCompatActivity {
    TextView tvQuantity, tvName, tvPrice;
    ImageView ivMinus, ivAdd, ivProductImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setControl();
        setEvent();
    }
    public void setControl() {
        ivMinus = findViewById(R.id.ivMinusCart);
        ivAdd = findViewById(R.id.ivAddCart);

    }

    public void setEvent() {}

}
