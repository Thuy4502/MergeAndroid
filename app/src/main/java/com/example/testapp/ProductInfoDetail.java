package com.example.testapp;

import static com.example.testapp.ProductEditingActivity.productId;
import static com.example.testapp.ProductListActivity.tokenStaff;
import static com.example.testapp.api.ApiService.apiService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.model.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInfoDetail extends AppCompatActivity {

    private ImageView ivProductImg, ivBack;
    private TextInputEditText etProductId, etProductStatus, etProductCategory, etProductName,
            etDescription, etCreatedStaff, etCreatedDate, etUpdatedStaff, etUpdatedDate, etProductPrice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_info);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getProductInfo(productId);
    }

    private void setControl() {
        ivProductImg = findViewById(R.id.ivProductImg);
        ivBack = findViewById(R.id.ivBack);
        etProductId = findViewById(R.id.etProductId);
        etProductStatus = findViewById(R.id.etProductStatus);
        etProductCategory = findViewById(R.id.etProductCategory);
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etCreatedStaff = findViewById(R.id.etCreatedStaff);
        etCreatedDate = findViewById(R.id.etCreatedDate);
        etUpdatedStaff = findViewById(R.id.etUpdatedStaff);
        etUpdatedDate = findViewById(R.id.etUpdatedDate);
        etProductPrice = findViewById(R.id.etProductPrice);
    }

    public void getProductInfo(String product_id){
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
                        etProductId.setText(product2.getProductId().toString());
                        etProductStatus.setText(product2.getStatus().toString());
                        if(product2.getStatus().toString().equals("Active")){
                            etProductStatus.setTextColor(Integer.valueOf(R.color.green));
                        } else {
                            etProductStatus.setTextColor(Integer.valueOf(R.color.mainColor));
                        }
                        etProductCategory.setText(product2.getCategory().getCategory_name());

                        etCreatedStaff.setText(product2.getStaffCreated().getFirstName()+" "+product2.getStaffCreated().getLastName());

                        etCreatedDate.setText(product2.getCreatedAt());

                        etUpdatedStaff.setText(product2.getStaffUpdated().getFirstName()+" "+product2.getStaffUpdated().getLastName());

                        etUpdatedDate.setText(product2.getUpdatedAt());
                    } else {
                        Toast.makeText(ProductInfoDetail.this, "product không tồn tại",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ProductInfoDetail.this, "response false",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductInfoDetail.this, "call api fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
