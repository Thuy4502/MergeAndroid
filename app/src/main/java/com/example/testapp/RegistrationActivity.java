package com.example.testapp;

import static com.example.testapp.function.Function.isValidPhoneNumber;
import static com.example.testapp.function.Function.setRequired;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.api.ApiService;
import com.example.testapp.model.Role;
import com.example.testapp.response.ApiResponse;

import com.example.testapp.model.User;

import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    TextView tvLinkLogin;
    EditText etFirstName, etLastName, etPhone, etPassword, etRePassword;
    Button btnRegister;
    ProgressBar progressBarLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setControl();
        setUp();
        setEvent();
    }

    private void setUp() {
        String defaultText = "+84";
        etPhone.setText(defaultText);
        etPhone.setSelection(defaultText.length());
    }
    private void setControl() {
        tvLinkLogin = findViewById(R.id.tvLinkLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etFirstName = findViewById(R.id.et_firstName);
        etLastName = findViewById(R.id.et_lastName);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etRePassword = findViewById(R.id.et_rePassword);

        progressBarLoading = findViewById(R.id.proBar_loading);

    }
    private void setEvent() {
        tvLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });
    }
    private void checkInput() {
        List<EditText> listEditText = Arrays.asList(etFirstName,etLastName, etPhone, etPassword, etRePassword);
        if(setRequired(listEditText, "Vui lòng điền đầy đủ thông tin")) {
            if (isValidPhoneNumber(etPhone)) {
                if (etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                    sendUser();
                } else {
                    etRePassword.setError("Xác nhận mật khẩu không khớp");
                }
            } else {
                etPhone.setError("SĐT bắt đầu bằng +84, đủ 10 số mới hợp lệ");
            }
        }

    }

    private void openActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendUser() {
        progressBarLoading.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);
        // get data
        User user = new User(null, 0, etPhone.getText().toString(), etPassword.getText().toString(),"CUSTOMER", null,null,etFirstName.getText().toString(),etLastName.getText().toString(), null, null, true);
        ApiService.apiService.sendUser(user).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                int code = 200;
                ApiResponse userResponse = response.body();
                if (response.isSuccessful()) {
                    if (userResponse != null) {
                        progressBarLoading.setVisibility(View.GONE);
                        Log.i("message",userResponse.getMessage());
                        Toast.makeText(RegistrationActivity.this,userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        openActivityLogin();
                    }
                } else{
                    code = response.code();
                    progressBarLoading.setVisibility(View.GONE);
                    btnRegister.setVisibility(View.VISIBLE);
                    if(code == HttpsURLConnection.HTTP_CONFLICT){
                        Toast.makeText(RegistrationActivity.this,"Số điện thoại đã được đăng ký", Toast.LENGTH_SHORT).show();
                    } else if (code == HttpsURLConnection.HTTP_INTERNAL_ERROR){
                        Toast.makeText(RegistrationActivity.this,"Lỗi đăng kí", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressBarLoading.setVisibility(View.GONE);
                Toast.makeText(RegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
