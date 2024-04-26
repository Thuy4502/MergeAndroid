package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccessfully extends AppCompatActivity {
    private ImageView ivHome, ivBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sucessfully_add);
        setControl();
        setEvent();


    }

    public void setEvent() {
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessfully.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setControl() {
        ivHome = findViewById(R.id.ivHome1);
        ivBack = findViewById(R.id.ivBack);

    }
}
