package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.colormoon.readmoretextview.ReadMoreTextView;
import com.example.testapp.adapter.ProductCustomerAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Product;
import com.example.testapp.model.Size;
import com.example.testapp.model.request.CartRequest;
import com.example.testapp.model.request.OrderRequest;
import com.example.testapp.response.CommonResponse;



import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvNameDetail, tvPriceDetail;
    private ImageView ivProductImg, ivBack;
    private ProductCustomerAdapter adapter;
    private Button btnBuyNow, btnAddItem, btnSizeS, btnSizeM, btnSizeL;
    private Toolbar appBar;
    public static Product sp;
    private LinearLayout lnlListReview;

    OrderRequest orderRequest;
    CommonResponse<Size> allSize;
    public static String size="";
    String categoryName="";
    public static float percent= 0;
    public String category = "";

    float priceBySize;
    LinearLayout llSize;
    String token;

    ReadMoreTextView tvReadMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customer_product);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token =  sharedPreferences.getString("token", null);
        setControl();
        btnBuyNow.setEnabled(false);
        btnAddItem.setEnabled(false);

        getProductDetail();
        setEvent();
        getAllSize();

    }


    public void setEvent() {
        tvReadMore.setCollapsedText("Read more");
        tvReadMore.setTrimLines(2);


        int cornerRadiusPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12, // giá trị ban đầu của bán kính ở đơn vị dp
                getResources().getDisplayMetrics()
        );
        GradientDrawable drawableEnable = new GradientDrawable();
        drawableEnable.setShape(GradientDrawable.RECTANGLE);
        drawableEnable.setCornerRadius(cornerRadiusPixels);
        drawableEnable.setColor(Color.parseColor("#b42329"));

        GradientDrawable drawableDisable = new GradientDrawable();
        drawableDisable.setShape(GradientDrawable.RECTANGLE);
        drawableDisable.setCornerRadius(cornerRadiusPixels);
        drawableDisable.setStroke(4, Color.parseColor("#b42329"));
        drawableDisable.setColor(Color.parseColor("#FFFFFF"));


        btnSizeS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuyNow.setEnabled(true);
                btnAddItem.setEnabled(true);
                size = "S";
                getAllSize();
                btnSizeS.setBackground(drawableEnable);
                btnSizeM.setBackground(drawableDisable);
                btnSizeL.setBackground(drawableDisable);

            }

        });
        lnlListReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, UserViewProductReviewActivity.class);
                startActivity(intent);
            }
        });
        btnSizeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuyNow.setEnabled(true);
                btnAddItem.setEnabled(true);
                size ="M";
                getAllSize();
                btnSizeM.setBackground(drawableEnable);
                btnSizeL.setBackground(drawableDisable);
                btnSizeS.setBackground(drawableDisable);

            }
        });

        btnSizeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuyNow.setEnabled(true);
                btnAddItem.setEnabled(true);
                size="L";
                getAllSize();
                btnSizeL.setBackground(drawableEnable);
                btnSizeM.setBackground(drawableDisable);
                btnSizeS.setBackground(drawableDisable);

            }
        });


        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DAY LA SIZEEE" + size);
                Product sendProduct;
                sendProduct = getProductDetail();
                Intent intent = new Intent(ProductDetailActivity.this, BuyNowActivity.class);
                if (orderRequest != null) {
                    intent.putExtra("buyNow", orderRequest);
                }
                if (priceBySize != 0) {
                    intent.putExtra("priceBySize", priceBySize);
                }

                if (sendProduct != null) {
                    intent.putExtra("product", sendProduct);
                } else {
                    System.out.println("Không có thông tin sản phẩm");
                }
                startActivity(intent);

            }
        });


        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartRequest cartRequest = new CartRequest();
                cartRequest.setProduct_name(sp.getProductName());
                if(!categoryName.equals("Bánh")) {
                    if(size.equals("S") || size.equals("M") || size.equals("L")) {

                        cartRequest.setSize(size);
                        cartRequest.setTopping("");
                        CustomerHomeActivity.callApiAddCart(cartRequest);
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(ProductDetailActivity.this, "Bạn chưa chọn size", Toast.LENGTH_LONG).show();

                    }
                }
                else if(categoryName.equals("Bánh")) {
                    size = "M";
                    cartRequest.setSize(size);
                    cartRequest.setTopping("");
                    CustomerHomeActivity.callApiAddCart(cartRequest);
                    Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();

                }



            }
        });

        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setTextForProduct() {
        tvNameDetail.setText(sp.getProductName());
        tvPriceDetail.setText(UserOrderActivity.formatNumber(sp.getPrice_update_detail().get(0).getPriceNew()));
        tvReadMore.setText(sp.getDescription());
        Glide.with(ProductDetailActivity.this)
                .load(sp.getImage()) // Lấy URL hình ảnh từ đối tượng ProAPI
                .into(ivProductImg);
    }

    public void setOrderInfor() {
        orderRequest.setProduct_id(sp.getProductId());
        orderRequest.setSize(size);
        orderRequest.setQuantity(1);
        orderRequest.setTopping("");
    }



    public Product getProductDetail() {
        String id = adapter.productID;

        ApiService.apiService.getProductDetail("Bearer "+token, id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                sp = response.body();
                categoryName = String.valueOf(sp.getCategory().getCategory_name());
                setTextForProduct();
//                if(categoryName.equals("Bánh")) {
//                    llSize.setVisibility(View.GONE);
//                    size = "M";
//                    btnBuyNow.setEnabled(true);
//                    btnAddItem.setEnabled(true);
//                }

                if (orderRequest == null) {
                    orderRequest = new OrderRequest();
                }
                setOrderInfor();
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi lấy thông tin chi tiết sản phẩm", Toast.LENGTH_LONG).show();
            }
        });
        return sp;
    }


    public void getAllSize() {
        ApiService.apiService.getPriceBySize("Bearer " + token).enqueue(new Callback<CommonResponse<Size>>() {
            @Override
            public void onResponse(Call<CommonResponse<Size>> call, Response<CommonResponse<Size>> response) {
                allSize = response.body();
                List<Size> listSize= allSize.getData();
                for(Size i: listSize) {
                    if(i.getCategory().getCategory_name().equals(categoryName) && i.getSize().getSize_name().equals(size)) {
                        percent = i.getPercent();
                        priceBySize = sp.getPrice_update_detail().get(0).getPriceNew() + (percent/100)*sp.getPrice_update_detail().get(0).getPriceNew();
                        tvPriceDetail.setText(String.valueOf(UserOrderActivity.formatNumber(priceBySize)));
                        orderRequest.setPrice(priceBySize);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse<Size>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lấy tất cả size thất bại", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void setControl() {
        tvReadMore = findViewById(R.id.tvReadMore);
        tvNameDetail = findViewById(R.id.tv_productName);
        tvPriceDetail = findViewById(R.id.tv_productPrice);
        ivProductImg = findViewById(R.id.img_product);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddItem = findViewById(R.id.btn_addItem);
        btnSizeS = findViewById(R.id.btn_sizeS);
        btnSizeM = findViewById(R.id.btn_sizeM);
        btnSizeL = findViewById(R.id.btn_sizeL);
        appBar = findViewById(R.id.app_bar);
        llSize = findViewById(R.id.llSize);

        lnlListReview = findViewById(R.id.lnl_openListReview);
    }
}
