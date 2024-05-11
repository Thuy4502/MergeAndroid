package com.example.testapp;


import static com.example.testapp.CouponListAdminActivity.getAllCoupon;
import static com.example.testapp.CouponListAdminActivity.tokenStaff;
import static com.example.testapp.api.ApiService.apiService;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.util.Pair;

import com.example.testapp.adapter.CouponAdapter;
import com.example.testapp.function.RealPathUtil;
import com.example.testapp.response.ApiResponse;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponAddingAdminActivity extends AppCompatActivity {
    private ImageView ivCouponImg, ivBack;
    Button btnUploadImg, btnAddCoupon;

    private TextInputEditText etCouponType, etCouponQuantity, etCouponValue, etCouponMinium, etCouponContent, etDateRange;
    private SwitchCompat swStatus;
    private int GALLERY_REQ_CODE = 2000;
    private Uri selectedImageUri;
    private ProgressDialog mProgressDialog;
    private CouponAdapter couponManagerAdapter;
    private ListView lvCouponList;
    private String status;
    private JSONObject jsonData = new JSONObject();
    String startDateString, endDateString;
    ImageButton btnDateRange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_coupon_adding_admin);
        setControl();
        setEvent();
    }



    protected void setControl() {
        ivCouponImg = findViewById(R.id.ivCouponImg);
        etDateRange = findViewById(R.id.etDateRange);
        btnUploadImg = findViewById(R.id.btnUploadImg);
        etCouponType = findViewById(R.id.etCouponTitle);
        etCouponQuantity = findViewById(R.id.etCouponQuantity);
        etCouponValue = findViewById(R.id.etCouponValue);
        etCouponContent = findViewById(R.id.etCouponContent);
        etCouponMinium = findViewById(R.id.etCouponMinium);
        swStatus = findViewById(R.id.swStatus);
        lvCouponList = findViewById(R.id.lv_couponList);
        btnDateRange = findViewById(R.id.btnDateRange);
        btnAddCoupon = findViewById(R.id.btnAddCoupon);
        ivBack = findViewById(R.id.ivBack);
    }




    // xử lý kết quả trả về từ việc chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // xác định xem kết quả trả về có phải từ việc chọn ảnh trong Gallery không
        // xác định xem người dùng có đã chọn một hình ảnh và nhấn ‘OK’ không
        // kiểm tra data và data.getData() không phải là null
        if(requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ivCouponImg.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



    protected void setEvent() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CouponAddingAdminActivity.this, CouponListAdminActivity.class);
                startActivity(intent);
            }
        });

        // set sự kiện cho nút Chọn ảnh
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        swStatus.setChecked(false);
        status = "unactive";
        swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swStatus.isChecked()) {
                    // The switch is enabled/checked
                    status = "active";
                    swStatus.setText("Active");
                } else {
                    // The switch is disabled/unchecked
                    status = "unactive";
                    swStatus.setText("Inactive");
                }
            }
        });


        //bấm vào nút Chọn ngày áp dụng khuyến mại
        btnDateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(tokenStaff);
            }
        });




        btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Kiểm tra xem ảnh đã được chọn chưa
                if (selectedImageUri == null) {
                    Toast.makeText(CouponAddingAdminActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etCouponType.getText().toString().isEmpty()) {
                    Toast.makeText(CouponAddingAdminActivity.this, "Vui lòng nhập id giảm giá", Toast.LENGTH_SHORT).show();
                } else if (etCouponValue.getText().toString().isEmpty()) {
                    Toast.makeText(CouponAddingAdminActivity.this, "Vui lòng nhập giá trị mã giảm giá", Toast.LENGTH_SHORT).show();
                } else if (etCouponContent.getText().toString().isEmpty()) {
                    Toast.makeText(CouponAddingAdminActivity.this, "Vui lòng nhập mô tả mã giảm giá", Toast.LENGTH_SHORT).show();
                } else if (etCouponMinium.getText().toString().isEmpty()) {
                    Toast.makeText(CouponAddingAdminActivity.this, "Vui lòng nhập giá trị tối thiểu để áp dụng mã giảm giá", Toast.LENGTH_SHORT).show();
                } else if (etCouponQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(CouponAddingAdminActivity.this, "Vui lòng nhập tên mã giảm giá", Toast.LENGTH_SHORT).show();
                } else {
                    addCoupon();
                }
            }
        });
    }

    private void openDatePicker(String token) {
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
        )).build();
        materialDatePicker. addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                String dateStart = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection.first));
                String dateEnd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection.second));

                String showStartDate =  new SimpleDateFormat("MM-dd", Locale.getDefault()).format(new Date(selection.first));
                String showEndDate =  new SimpleDateFormat("MM-dd", Locale.getDefault()).format(new Date(selection.second));

                etDateRange.setText(dateStart + " -> " + dateEnd);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "tag");
    }



    public void addCoupon() {
        mProgressDialog.show();
        String dateRange = String.valueOf(etDateRange.getText());
        String[] parts = dateRange.split(" ");
        String startdate = parts[0];
        String enddate = parts[2];
        // đổ data vào khuôn json
        try {
            jsonData.put("content", etCouponContent.getText().toString());
            jsonData.put("status", status.toString());
            jsonData.put("type", etCouponType.getText().toString());
            jsonData.put("use_value", etCouponValue.getText().toString());
            jsonData.put("minimum_value", etCouponMinium.getText().toString());
            jsonData.put("quantity", etCouponQuantity.getText().toString());
            jsonData.put("start_date", startdate);
            jsonData.put("end_date", enddate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = jsonData.toString();
        Log.i("YYY",jsonString);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), jsonString);
        // Lấy đường dẫn thực của file ảnh từ Uri
        String imagePath = RealPathUtil.getRealPath(this,selectedImageUri);
        String[] onlyImagePath = imagePath.split("/");
        String lastElement = onlyImagePath[onlyImagePath.length-1];


        // Tạo File từ đường dẫn ảnh
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        apiService.addCoupon(tokenStaff, imagePart, requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.i("YYY",response.toString()+tokenStaff);
                if (response.isSuccessful()) {
                    ApiResponse result = response.body();
                    if (result != null){
                        Toast.makeText(CouponAddingAdminActivity.this, "Thêm khuyến mại thành công", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();

                        getAllCoupon();
                        startActivity(new Intent(CouponAddingAdminActivity.this, CouponListAdminActivity.class));
                    } else {
                        Toast.makeText(CouponAddingAdminActivity.this, "result null", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }

                } else {
                Toast.makeText(CouponAddingAdminActivity.this,"response false", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CouponAddingAdminActivity.this, ""+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("/image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }




}
