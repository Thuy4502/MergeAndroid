package com.example.testapp;

import static com.example.testapp.function.Function.isValidPhoneNumber;
import static com.example.testapp.function.Function.setRequired;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.trusted.TokenStore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.api.ApiService;
import com.example.testapp.model.Customer;
import com.example.testapp.model.User;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.EntityStatusResponse;
import com.google.android.gms.common.api.Api;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView tvForgotPass, tvLinkSignup;
    private Button btnLogin;
    private EditText etPhone, etPassword;
    private ProgressBar pbLogin;
    public static String role, staffName, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setUp() {
        String defaultText = "+84";
        etPhone.setText(defaultText);
        etPhone.setSelection(defaultText.length());
    }


    private void setControl() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPass = findViewById(R.id.forgetPass);
        tvLinkSignup = findViewById(R.id.tvLinkSignup);
        btnLogin = findViewById(R.id.btnLogin);
        pbLogin = findViewById(R.id.proBar_login);
        tvForgotPass.setPaintFlags(tvForgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLinkSignup.setPaintFlags(tvLinkSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setEvent() {
        setUp();

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserExist();
            }
        });

        tvLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySignup();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postUser();
            }
        });
    }
    //open Home
    private void openActivityCustomerHome() {
        Intent intent = new Intent(this, CustomerHomeActivity.class);
        startActivity(intent);
    }
    private void openActivityStaffHome() {
        Intent intent = new Intent(this, StaffNavigationMenuActivity.class);
        startActivity(intent);
    }
    //open Sign Up
    private void openActivitySignup() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private  void checkUserExist (){
        List<EditText> listEditText = Arrays.asList(etPhone);

        final ProgressBar proBarForgotPass;
        proBarForgotPass = findViewById((R.id.proBar_forgotPass));

        if(setRequired(listEditText, "Vui lòng nhập số điện thoại để thiết lập mật khẩu mới") && isValidPhoneNumber(etPhone)) {
            proBarForgotPass.setVisibility(View.VISIBLE);
            tvForgotPass.setVisibility(View.INVISIBLE);
            ApiService.apiService.checkUserNameExist(etPhone.getText().toString()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse resultResponse = response.body();
                        if (resultResponse != null) {
                            proBarForgotPass.setVisibility(View.GONE);
                            tvForgotPass.setVisibility(View.VISIBLE);
                            sendOTP();
                        }
                    }else{
                        proBarForgotPass.setVisibility(View.GONE);
                        tvForgotPass.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Số điện thoại chưa được đăng kí vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    proBarForgotPass.setVisibility(View.GONE);
                    tvForgotPass.setVisibility(View.VISIBLE);
                    Log.i("error login: ", t.getMessage());
                }
            });
        }else {
            // Nothing
        }
    }

    //Send OTP
    private void sendOTP() {
        final ProgressBar proBarForgotPass;
        proBarForgotPass = findViewById((R.id.proBar_forgotPass));

        //Send OTP by firebase
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(etPhone.getText().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(LoginActivity.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // Xử lý khi xác minh hoàn thành
                                proBarForgotPass.setVisibility(View.GONE);
                                tvForgotPass.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // Xử lý khi xác minh thất bại
                                proBarForgotPass.setVisibility(View.GONE);
                                tvForgotPass.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("error send OTP: ", e.getMessage());
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);

                                //Send phone user to forgot password activity
                                proBarForgotPass.setVisibility(View.GONE);
                                tvForgotPass.setVisibility(View.VISIBLE);

                                //Open forgot password activity
                                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                                intent.putExtra("phoneUser", etPhone.getText().toString());
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);

                            }
                        })
                        .build());
    }
    
    //Get role user
    private void getRole(String token){
        ApiService.apiService.getUserProfile("Bearer " + token).enqueue(new Callback<EntityStatusResponse<Customer>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<Customer>> call, Response<EntityStatusResponse<Customer>> response) {
                if(response.isSuccessful()){
                    EntityStatusResponse<Customer> resultResponse = response.body();
                    if (resultResponse != null){
                        Customer customerResponse = resultResponse.getData();
                        role = customerResponse.getUser().getRole().getRole_name();
                        staffName = customerResponse.getUser().getFirst_name() + " " + customerResponse.getUser().getLast_name();
                        address = customerResponse.getAddress();
                        Log.i("role", customerResponse.getUser().getRole().getRole_name());
                        if(customerResponse.getUser().getRole().getRole_name().equals("CUSTOMER")){
                            pbLogin.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            openActivityCustomerHome();
                        }else {
                            pbLogin.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            openActivityStaffHome();
                        }
                    }
                } else
                    pbLogin.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<Customer>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pbLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        });
    }

    //Post data user
    private void postUser() {
        SharedPreferences sharedPreferences =getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<EditText> listRequired = Arrays.asList(etPhone, etPassword);
        if(setRequired(listRequired, "Vui lòng nhập đầy đủ thông tin")){
            User user = new User();
            user.setUsername(etPhone.getText().toString());
            user.setPassword(etPassword.getText().toString());

            pbLogin.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);

            //call API method POST to login
            ApiService.apiService.loginUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User userResult = response.body();
                        if (userResult != null){
                            String token = userResult.getToken();
                            Log.i("Token:", token);
                            getRole(token);
                            editor.putString("token", token);
                            editor.apply();
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            pbLogin.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
                        pbLogin.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                }
            });
        }else{
            pbLogin.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
}