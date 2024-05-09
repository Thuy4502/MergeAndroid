// Them san pham moi

package com.example.testapp;




import static com.example.testapp.ProductListActivity.categoryList;
import static com.example.testapp.ProductListActivity.getProductList;
import static com.example.testapp.ProductListActivity.tokenStaff;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testapp.adapter.ProductCategoryAdapterVer2;
import com.example.testapp.function.RealPathUtil;
import com.example.testapp.model.Category;
import com.example.testapp.response.ApiResponse;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAddingActivity extends AppCompatActivity {
    TextView tvCategoryItem;
    Button btnAddProduct, btnSelectImg;
    private ImageView ivGallery, ivBack;
    private String status;;
    private TextInputEditText etProductName, etProductPrice, etDescription;
    private RecyclerView rvCategoryOption;
    private Uri selectedImageUri;
    public final static int GALLERY_REQ_CODE = 1000;
    private ProgressDialog mProgressDialog;

    SwitchCompat swStatus;
    JSONObject jsonData = new JSONObject();
    String categoryName;
    AppCompatSpinner spnCategoryOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_adding);


        setControl();
        setEvent();

    }


    protected void setControl() {
        btnAddProduct = findViewById(R.id.btnSave);
        btnSelectImg = findViewById(R.id.btnSelectImg);
        ivBack = findViewById(R.id.ivBack);
        ivGallery = findViewById(R.id.ivGallery);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etDescription = findViewById(R.id.etDescription);
        tvCategoryItem = findViewById(R.id.tvCategoryItem);
        spnCategoryOption = findViewById(R.id.spnCategoryOption);
        swStatus = findViewById(R.id.swStatus);
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
                ivGallery.setImageBitmap(bitmap);
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
        ProductCategoryAdapterVer2 productCategoryAdapterVer2 = new ProductCategoryAdapterVer2(ProductAddingActivity.this, R.layout.item_category_ver2, categoryList);
        spnCategoryOption.setAdapter(productCategoryAdapterVer2);


        // set sự kiện cho nút Back
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAddingActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });


        // set sự kiện cho nút Chọn ảnh
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        swStatus.setChecked(false);
        status = "Unactive";
        swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swStatus.isChecked()) {
                    // The switch is enabled/checked
                    status = "Active";
                    swStatus.setText("Đang bán");
                } else {
                    // The switch is disabled/unchecked
                    status = "Unactive";
                    swStatus.setText("Ngưng bán");
                }
            }
        });

        // set sự kiện chọn category
        spnCategoryOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = view.findViewById(R.id.tvCategoryItemVer2);

                // Lấy giá trị của TextView
                categoryName = selectedTextView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryName = categoryList.get(0).getCategory_name();
            }
        });

        // set sự kiện cho nút Thêm sản phẩm
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Kiểm tra xem ảnh đã được chọn chưa
                if (selectedImageUri == null) {
                    Toast.makeText(ProductAddingActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etProductName.getText().toString().isEmpty()) {
                    Toast.makeText(ProductAddingActivity.this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (etProductPrice.getText().toString().isEmpty()) {
                    Toast.makeText(ProductAddingActivity.this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (etDescription.getText().toString().isEmpty()) {
                    Toast.makeText(ProductAddingActivity.this, "Vui lòng nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        createProduct();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }



    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("/image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createProduct() throws IOException {
        mProgressDialog.show();
        // đổ data vào khuôn json
        try {
                    jsonData.put("product_name", etProductName.getText().toString());
                    jsonData.put("price", etProductPrice.getText().toString());
                    jsonData.put("description", etDescription.getText().toString());
                    jsonData.put("status", status.toString());
                    jsonData.put("category_name", categoryName.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        String jsonString = jsonData.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), jsonString);


        // Lấy đường dẫn thực của file ảnh từ Uri
        String imagePath = RealPathUtil.getRealPath(this,selectedImageUri);
        String[] onlyImagePath = imagePath.split("/");
        String lastElement = onlyImagePath[onlyImagePath.length-1];


        // Tạo File từ đường dẫn ảnh
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        apiService.addProduct(tokenStaff, imagePart, requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    ApiResponse result = response.body();
                    if (result != null){
                        Toast.makeText(ProductAddingActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();

                        getProductList();
                        Intent intent = new Intent(ProductAddingActivity.this, ProductListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProductAddingActivity.this, "result null", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(ProductAddingActivity.this,"response false", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ProductAddingActivity.this, ""+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

