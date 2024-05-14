package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.api.ApiService;
import com.example.testapp.model.Customer;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.EntityStatusResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvPoint;
    private EditText etFullName, etPhoneUser, etAddressUser, etEmail;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setControl();
        setEvent();
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        getInfoUser(token);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(token);
            }
        });

    }

    private void logOut(String token) {
        ApiService.apiService.logOut(token).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", null);
                        long expirationTime = sharedPreferences.getLong("expiration_time", 0);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("token");
                        editor.remove("expiration_time");
                        editor.remove("address");
                        editor.apply();
                        token = null; // Đảm bảo token bị xóa khỏi bộ nhớ

                        openLoginActivity();
                    }else
                        Log.i("fail: ", "log out fail");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("error logout: ", t.getMessage());
            }
        });

    }

    private void openLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void getInfoUser(String token){
        ApiService.apiService.getUserProfile(token).enqueue(new Callback<EntityStatusResponse<Customer>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<Customer>> call, Response<EntityStatusResponse<Customer>> response) {
                if(response.isSuccessful()){
                    EntityStatusResponse<Customer> resultResponse = response.body();
                    if(resultResponse != null){
                        Customer profileResponse = resultResponse.getData();
                        etFullName.setText(profileResponse.getFirstname() +" "+ profileResponse.getLastname());
                        etAddressUser.setText(profileResponse.getAddress());
                        etPhoneUser.setText(profileResponse.getPhone());
                        etEmail.setText(profileResponse.getEmail());
                        tvPoint.setText(profileResponse.getUser().getPoints().toString() + " điểm");
                        Log.i("message profile response: ", resultResponse.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<EntityStatusResponse<Customer>> call, Throwable t) {
                Log.i("error profile", t.getMessage());
            }
        });

    }

    private void setControl() {
        etAddressUser = findViewById(R.id.et_addressUser);
        etFullName = findViewById(R.id.et_fullName);
        etPhoneUser = findViewById(R.id.et_phoneUser);
        etEmail = findViewById(R.id.et_emailUser);

        tvPoint = findViewById(R.id.tv_pointUser);

        btnLogout = findViewById(R.id.btn_logout);
    }
}