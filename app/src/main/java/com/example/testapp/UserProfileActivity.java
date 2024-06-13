package com.example.testapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.api.ApiService;
import com.example.testapp.model.Customer;
import com.example.testapp.model.UserInfo;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.EntityStatusResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvPoint, btnLogout;
    private TextInputEditText etFullName, etPhoneUser, etAddressUser, etEmail;
    private Button btnChangeInfo;
    private Customer profileResponse;
    public static UserInfo userData;
    static BottomNavigationView bottomNavigationView;
    UserInfo userInfo;
    String token1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setControl();
        setEvent();
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token1 = "Bearer " + sharedPreferences.getString("token", null);

        getInfoUser(token1);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(token1);
            }
        });

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiGetUserInfor();
                changeUserInfo();

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

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.btn_home:
                        Intent homeIntent = new Intent(UserProfileActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.btn_listOder:
                        Intent intent = new Intent(UserProfileActivity.this, UserOrderHistoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.btn_showCart:
                        Intent cartIntent = new Intent(UserProfileActivity.this, CartListActivity.class);
                        startActivity(cartIntent);
                        return true;
                }
                return false;
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
                        profileResponse = resultResponse.getData();
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


    public void callApiGetUserInfor() {
        ApiService.apiService.getUserInfor(token1).enqueue(new Callback<EntityStatusResponse<UserInfo>
                >() {
            @Override
            public void onResponse(Call<EntityStatusResponse<UserInfo>> call, Response<EntityStatusResponse<UserInfo>> response) {
                userData = response.body().getData();
                userInfo = new UserInfo();
                userInfo.setAddress(String.valueOf(etAddressUser.getText()));
                userInfo.setCccd(String.valueOf(userData.getCccd()));
                userInfo.setEmail(String.valueOf(etEmail.getText()));


                String[] name = String.valueOf(etFullName.getText()).split(" ");
                userInfo.setFirstname(name[0]);
                userInfo.setLastname(name[1]);
                userInfo.setTax_id(String.valueOf(userData.getTax_id()));

            }

            @Override
            public void onFailure(Call<EntityStatusResponse<UserInfo>> call, Throwable t) {
                System.out.println("Lay thong tin user thanh cong");

            }
        });
    }

    public void changeUserInfo() {
        ApiService.apiService.changeAddress(token1, userInfo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UserProfileActivity.this, "Thay đổi thông tin thành công", Toast.LENGTH_LONG).show();
                getInfoUser(token1);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Thay đổi thông tin thất bại", Toast.LENGTH_LONG).show();
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
        btnChangeInfo = findViewById(R.id.btnChangeInfo);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}