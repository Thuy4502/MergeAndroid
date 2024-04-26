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

import com.example.testapp.api.ApiService;
import com.example.testapp.model.Customer;
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
                openLoginActivity();
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