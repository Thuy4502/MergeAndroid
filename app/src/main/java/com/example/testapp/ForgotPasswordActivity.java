package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView tvPhoneUser, tvResend;
    private EditText etOtpCode1, etOtpCode2, etOtpCode3, etOtpCode4, etOtpCode5, etOtpCode6;

    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setControl();
        setupInputs();
        setEvent();
    }

    private void setupInputs() {
        etOtpCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    etOtpCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etOtpCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    etOtpCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etOtpCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    etOtpCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etOtpCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    etOtpCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etOtpCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    etOtpCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public boolean setRequired(List<EditText> listEt) {
        for (int i = 0; i < listEt.size(); i++) {
            if (TextUtils.isEmpty(listEt.get(i).getText())) {
                Toast.makeText(this, "Vui lòng nhập mã OTP để thiết lập mật khẩu mới", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void setControl() {
        tvPhoneUser = findViewById(R.id.tv_PhoneNumber);

        etOtpCode1 = findViewById(R.id.et_otpCode1);
        etOtpCode2 = findViewById(R.id.et_otpCode2);
        etOtpCode3 = findViewById(R.id.et_otpCode3);
        etOtpCode4 = findViewById(R.id.et_otpCode4);
        etOtpCode5 = findViewById(R.id.et_otpCode5);
        etOtpCode6 = findViewById(R.id.et_otpCode6);

        tvResend = findViewById(R.id.tv_reSendOTP);

        setupInputs();


    }
    private void setEvent() {
        //take info from loginActivity
        Intent intent = getIntent();
        if (intent != null) {
            tvPhoneUser.setText(String.format(intent.getStringExtra("phoneUser")
            ));
        }

        final ProgressBar progressBar = findViewById(R.id.proBar_accept);
        final Button btnAccept = findViewById(R.id.btnAccept);

        verificationId = getIntent().getStringExtra("verificationId");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            //check OTP
            @Override
            public void onClick(View view) {
                List<EditText> listEditText = Arrays.asList(etOtpCode1, etOtpCode2, etOtpCode3, etOtpCode4, etOtpCode5, etOtpCode6);
                if(!setRequired(listEditText)){
                    return;
                }
                String code = listEditText.get(0).getText().toString();
                for (int i=1; i < listEditText.size(); i++){
                    code = code + listEditText.get(i).getText().toString();
                }
                if (verificationId != null){
                    progressBar.setVisibility(View.VISIBLE);
                    btnAccept.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            btnAccept.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), SetNewPasswordActivity.class);
                                intent.putExtra("username", tvPhoneUser.getText());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(ForgotPasswordActivity.this, "OTP chưa chính xác, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            //Resend OTP
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.verifyPhoneNumber(
                        PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                .setPhoneNumber(tvPhoneUser.getText().toString())
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(ForgotPasswordActivity.this)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        // Xử lý khi xác minh hoàn thành


                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        // Xử lý khi xác minh thất bại

                                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(newVerificationId, forceResendingToken);
                                        verificationId = newVerificationId;
                                        Toast.makeText(ForgotPasswordActivity.this, "OTP mới đã được gửi", Toast.LENGTH_SHORT).show();
//                                        tvResend.setEnabled(false);
//                                        tvResend.setTextColor(getColor(R.color.lightGrey));
                                    }
                                })
                                .build());
            }
        });
    }

}

