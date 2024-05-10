package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        long expirationTime = sharedPreferences.getLong("expiration_time", 0);
        String roleName = sharedPreferences.getString("role", "");

        Log.i("role Name ", roleName);

        // Kiểm tra xem token có hết hạn chưa
        if (System.currentTimeMillis() > expirationTime) {
            // Xóa token
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token");
            editor.remove("expiration_time");
            editor.apply();
            token = null; // Đảm bảo token bị xóa khỏi bộ nhớ
            Toast.makeText(this, R.string.login_overTime, Toast.LENGTH_SHORT).show();
        }
        else {
            if(roleName.equals("CUSTOMER")){
                openActivityCustomerHome();
            }else {
                openActivityStaffHome();
            }
        }

        btnStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });
    }

    private void openActivityStaffHome() {
        Intent intent = new Intent(this, StaffNavigationMenuActivity.class);
        startActivity(intent);
    }

    private void openActivityCustomerHome() {
        Intent intent = new Intent(this, CustomerHomeActivity.class);
        startActivity(intent);
    }


    private void setControl() {
        btnStarted = findViewById(R.id.btnStarted);
    }

    public void openActivityLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}