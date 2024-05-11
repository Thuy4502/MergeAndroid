package com.example.testapp;




import static com.example.testapp.ProductListActivity.categoryList;
import static com.example.testapp.ProductListActivity.getProductList;
import static com.example.testapp.ProductListActivity.tokenStaff;
import static com.example.testapp.api.ApiService.apiService;
import static okhttp3.MediaType.parse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;

import com.example.testapp.adapter.ProductCategoryAdapterVer2;
import com.example.testapp.function.RealPathUtil;
import com.example.testapp.model.Product;
import com.example.testapp.response.ApiResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEditingActivity extends AppCompatActivity {
    //String token="Bearer "+"eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTI1NDg3NTYsImV4cCI6MTcxMzE1MzU1NiwidXNlcm5hbWUiOiIwOTI3MDE0MDUxIn0.PVKK4TKgcvxDuP5f1fFuGQT7dTKInqJ-3S75codT9DLk31uEbz87ELnTOZ5FlWgx";
    private TextInputEditText etProductName, etProductPrice, etDescription;
    private Button btnSelectImg, btnSave;
    private ProgressDialog mProgressDialog;
    private Uri selectedImageUri;
    public final static int GALLERY_REQ_CODE = 1000;
    private JSONObject jsonData = new JSONObject();
    public static String categoryName, productId;
    private ImageView ivBack, ivProductImg, ivInfo;
    TextView tvCategoryItem;

    SwitchCompat swStatus;
    String status;
    AppCompatSpinner spnCategoryOption2;
    ProductCategoryAdapterVer2 product;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_editing);
        productId = getIntent().getStringExtra("PRODUCT_ID");
        setControl();
        getProductDetail(productId);
        setEvent();
    }



    protected void setControl() {
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etDescription = findViewById(R.id.etDescription);
        btnSelectImg = findViewById(R.id.btnSelectImg);
        btnSave = findViewById(R.id.btnSave);
        ivProductImg = findViewById(R.id.ivProductImg);
        ivBack = findViewById(R.id.ivBack);
        ivInfo = findViewById(R.id.ivInfo);
        tvCategoryItem = findViewById(R.id.tvCategoryItemVer2);
        swStatus = findViewById(R.id.swStatus);
        spnCategoryOption2 = findViewById(R.id.spnCategoryOption2);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK  ) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ivProductImg.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }



    protected void setEvent() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");

        product = new ProductCategoryAdapterVer2(ProductEditingActivity.this, R.layout.item_category_ver2, categoryList);
        spnCategoryOption2.setAdapter(product);

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductEditingActivity.this, ProductInfoDetail.class);
                startActivity(intent);
            }
        });

        // set sự kiện chọn category
        spnCategoryOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // set sự kiện cho nút Back
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductEditingActivity.this, ProductListActivity.class);
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

        // set sự kiện cho nút Lưu thay đổi
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduct();
            }
        });

        swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
    }



    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("/image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }



    private void editProduct( ) {
        mProgressDialog.show();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        String price = etProductPrice.getText().toString().substring(0, etProductPrice.getText().toString().length()-2);
        // đổ data vào khuôn json
        try {
            jsonData.put("product_name", etProductName.getText().toString());
            jsonData.put("price", price);
            jsonData.put("description", etDescription.getText().toString());
            jsonData.put("category_name", categoryName);
            jsonData.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = jsonData.toString();
        Log.i("JSON",""+jsonString);
        RequestBody requestBody = RequestBody.create(parse("multipart/form-data"), jsonString);
        mProgressDialog.setMessage(jsonString);

        // Kiểm tra xem ảnh đã được chọn chưa
        if (selectedImageUri == null && ivProductImg.getDrawable() == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        // Lấy đường dẫn thực của file ảnh từ Uri
        String imagePath ;
        MultipartBody.Part imagePart;
        if (selectedImageUri == null){
            imagePart = MultipartBody.Part.createFormData("file", "", RequestBody.create(null, new byte[0]));

        } else {
            imagePath = RealPathUtil.getRealPath(this, selectedImageUri);
            String[] onlyImagePath = imagePath.split("/");
            String lastElement = onlyImagePath[onlyImagePath.length - 1];
            // Tạo File từ đường dẫn ảnh
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(parse("image/jpeg"), file);
            imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }

        apiService.updateProduct(tokenStaff, productId, imagePart, requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    ApiResponse result = response.body();

                    if (result != null){
                        Toast.makeText(ProductEditingActivity.this, result.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();

                        getProductList();
                        Intent intent = new Intent(ProductEditingActivity.this, ProductListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProductEditingActivity.this, "result null", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(ProductEditingActivity.this,"response false", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(
                        ProductEditingActivity.this, " không call được api"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getProductDetail(String product_id){
        apiService.getProductDetail(tokenStaff, product_id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){
                    Product product2 = response.body();
                    if (product2 != null){
                        etProductName.setText(product2.getProductName().toString());
                        etProductPrice.setText(String.valueOf(product2.getPrice_update_detail().get(0).getPriceNew()));
                        etDescription.setText(product2.getDescription());
                        Picasso.get()
                                .load(product2.getImage())    //load(url)
                                .into(ivProductImg);    //into(variant)
                        if(product2.getStatus().equals("Active")){
                            swStatus.setChecked(true);
                            status = product2.getStatus();
                        } else if(product2.getStatus().equals("Unactive")){
                            swStatus.setChecked(false);
                            status = product2.getStatus();
                        }
                        else {
                            Toast.makeText(ProductEditingActivity.this, "status ko ton tai", Toast.LENGTH_SHORT).show();
                        }
                        spnCategoryOption2.setSelection(getCategoryPosition(product2.getCategory().getCategory_name()));
                    } else {
                        Toast.makeText(ProductEditingActivity.this, "product null",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ProductEditingActivity.this, "response false",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductEditingActivity.this, "call api fail",Toast.LENGTH_SHORT).show();
            }
        });
    }






    public static int getCategoryPosition(String categoryName){
        for (int position = 0; position < categoryList.size(); position++){
            if (categoryName.equals(categoryList.get(position).getCategory_name())){
                return position;
            }
        }
            return -1;
    }

}
