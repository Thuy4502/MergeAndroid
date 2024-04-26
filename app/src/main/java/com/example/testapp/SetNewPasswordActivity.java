package com.example.testapp;

import static com.example.testapp.function.Function.setRequired;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.api.ApiService;
import com.example.testapp.model.User;
import com.example.testapp.response.ApiResponse;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetNewPasswordActivity extends AppCompatActivity {
    EditText et_setNewPass, et_rePass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        setControl();
        setEvent();

    }
    private void setControl() {
        btnLogin = findViewById(R.id.btnLogin);
        et_setNewPass = findViewById(R.id.et_setNewPassword);
        et_rePass = findViewById(R.id.et_rePassword);

    }

    private void putUser(){
        Intent intent = getIntent();
        User user = new User();
        user.setPassword(String.valueOf(et_setNewPass.getText()));
        if (intent != null) {
            ApiService.apiService.changePassword(String.format(intent.getStringExtra("username")),user).enqueue(new Callback<ApiResponse>() {
                int code = 200;
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse changePassResult = response.body();
                        if (changePassResult != null){
                            Toast.makeText(SetNewPasswordActivity.this, changePassResult.getMessage(), Toast.LENGTH_SHORT).show();
                            openActivityLogin();
                        }
                    }else {
                        code = response.code();
                        Toast.makeText(SetNewPasswordActivity.this, "Thiết lập mật khẩu mới thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(SetNewPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setEvent() {

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                List<EditText> listEditText = Arrays.asList(et_setNewPass, et_rePass);
                if(setRequired(listEditText, "Vui lòng điền đầy đủ thông tin")) {
                        if (et_setNewPass.getText().toString().equals(et_rePass.getText().toString())) {
                        } else {
                            et_rePass.setError("Xác nhận mật khẩu không khớp");
                        }
                        putUser();
                }
            }

        });
    }
    private void openActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}