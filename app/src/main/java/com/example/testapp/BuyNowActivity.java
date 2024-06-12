package com.example.testapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.adapter.ProductInOrderAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.function.Function;
import com.example.testapp.model.OrderID;
import com.example.testapp.model.Product;
import com.example.testapp.model.UserTemp;
import com.example.testapp.model.request.OrderRequest;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.EntityStatusResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyNowActivity extends AppCompatActivity {
    List<Product> productList;
    ProductInOrderAdapter adapter;
    RecyclerView rvProduct;
    OrderRequest orderRequest;
    TextView tvProductName, tvProductPrice, tvQuantity, tvTotalPrice, tvDeliveryCost, tvSize, tvAddress, tvPrice, tvFlexible, tvPointUser, tv_usePoint;
    ImageView ivProductImg, ivAdd, ivMinus;
    public static int quantity ;
    public Long orderId;
    public static float totalPrice;
    public static Product sp;
    EntityStatusResponse<UserTemp> userInfor;
    String newAddress;
    LinearLayout lyEditAdress;
    private Toolbar appBar;
    String token, point;
    Button btnOrder, btnShowListCoupon, btnChangeAddress, btnDelivery, btnPickUp;
    public static float price,  tPrice,  dCost, priceBySize;

    Switch swUsePoint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token =  sharedPreferences.getString("token", null);
        point = sharedPreferences.getString("point", null);
        setControl();
        quantity = Integer.parseInt(String.valueOf(tvQuantity.getText()));
        String numbers = String.valueOf(tvDeliveryCost.getText()).replaceAll("\\D+", "");
        dCost = Float.parseFloat(numbers);
        callApiGetUserInfor();
        setInfor();
        setEvent();
    }

    public void setControl() {
        tvProductName = findViewById(R.id.tvProductName);
        ivProductImg = findViewById(R.id.ivProductImg);
        ivMinus = findViewById(R.id.ivMinus);
        ivAdd = findViewById(R.id.ivAdd);
        tvProductPrice = findViewById(R.id.tv_productPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvDeliveryCost = findViewById(R.id.tvDeliveryCost);
        btnOrder = findViewById(R.id.btnOrder);
        tvSize = findViewById(R.id.tvSize);
        tvAddress = findViewById(R.id.tvUserAddress);
        btnChangeAddress = findViewById(R.id.btnChangeAddress2);
        tvPrice = findViewById(R.id.tvPrice);
        btnDelivery = findViewById(R.id.btnDelivery);
        btnPickUp = findViewById(R.id.btnPickUp);
        tvFlexible = findViewById(R.id.tvFlexible);
        lyEditAdress = findViewById(R.id.lyEditAdress);
        appBar = findViewById(R.id.app_bar);
        btnShowListCoupon = findViewById(R.id.btnShowListCoupon);
        tvPointUser = findViewById(R.id.tv_pointUser);
        swUsePoint = findViewById(R.id.sw_usePoint);
        tv_usePoint = findViewById(R.id.tv_usePoint);
    }

    public void setEvent() {
        int cornerRadiusPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12, // giá trị ban đầu của bán kính ở đơn vị dp
                getResources().getDisplayMetrics()
        );


        tvPointUser.setText("Sử dụng " + point + " điểm");

        swUsePoint.setChecked(false);
        swUsePoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swUsePoint.isChecked()) {
                    // The switch is enabled/checked
                    totalPrice = totalPrice - Integer.valueOf(point);
                    tv_usePoint.setText(Function.formatToVND(Integer.valueOf(point)));
                    tvTotalPrice.setText(Function.formatToVND((int) totalPrice));
                } else {
                    // The switch is disabled/unchecked
                    totalPrice = totalPrice + Integer.valueOf(point);
                    tv_usePoint.setText("0 đ");
                    tvTotalPrice.setText(Function.formatToVND((int) (totalPrice)));
                }
            }
        });



        GradientDrawable drawableEnable = new GradientDrawable();
        drawableEnable.setShape(GradientDrawable.RECTANGLE);
        drawableEnable.setCornerRadius(cornerRadiusPixels);
        drawableEnable.setColor(Color.parseColor("#b42329"));

        GradientDrawable drawableDisable = new GradientDrawable();
        drawableDisable.setShape(GradientDrawable.RECTANGLE);
        drawableDisable.setCornerRadius(cornerRadiusPixels);
        drawableDisable.setColor(Color.parseColor("#FFFFFF"));
        drawableDisable.setStroke(4, Color.parseColor("#b42329"));

        btnShowListCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyNowActivity.this, CouponUserActivity.class);
                startActivity(intent);
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity+=1;
                tvQuantity.setText(String.valueOf(quantity));
                updateProductPrice();

            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity >=2) {
                    quantity-=1;
                    tvQuantity.setText(String.valueOf(quantity));
                    updateProductPrice();
                }


            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiBuyNow();
            }
        });

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddressDialog(Gravity.CENTER);
            }
        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelivery.setBackground(drawableEnable);
                btnPickUp.setBackground(drawableDisable);
                btnPickUp.setTextColor(Color.BLACK);
                btnDelivery.setTextColor(Color.WHITE);
                dCost = 15000;
                updateProductPrice();
                lyEditAdress.setVisibility(View.VISIBLE);
                callApiGetUserInfor();
            }
        });

        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelivery.setBackground(drawableDisable);
                btnPickUp.setBackground(drawableEnable);
                btnPickUp.setTextColor(Color.WHITE);
                btnDelivery.setTextColor(Color.BLACK);
                dCost = 0;
                updateProductPrice();
                lyEditAdress.setVisibility(View.GONE);
                tvAddress.setText("97 Man Thiện, P.Hiệp Phú, Tp.Thủ Đức, Tp.HCM");
                lyEditAdress.setVisibility(View.GONE);
            }
        });

        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyNowActivity.this, CustomerHomeActivity.class);
            }
        });
    }

    private void updateProductPrice() {
        float newPrice = priceBySize * quantity;
        totalPrice = dCost + newPrice;
        tvProductPrice.setText(UserOrderActivity.formatNumber(newPrice));
        tvTotalPrice.setText(UserOrderActivity.formatNumber(totalPrice));
    }



    public void getApiBuyNow() {
        if (orderRequest == null) {
            orderRequest = new OrderRequest();
        }
        orderRequest.setProduct_id(sp.getProductId());
        orderRequest.setSize(ProductDetailActivity.size);
        updateProductPrice();
        orderRequest.setPrice(totalPrice);
        orderRequest.setQuantity(1);
        orderRequest.setTopping("");
        ApiService.apiService.buyNow("Bearer " + token, orderRequest).enqueue(new Callback<EntityStatusResponse<OrderID>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<OrderID>> call, Response<EntityStatusResponse<OrderID>> response) {
                orderId = response.body().getData().getId();
                Toast.makeText(BuyNowActivity.this, "Đặt hàng thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BuyNowActivity.this, UserDeliveryProcessActivity.class);
                intent.putExtra("OrderID", orderId);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<OrderID>> call, Throwable t) {
                System.out.println("Buy now thất bại!");
            }
        });

    }

    public void setInfor() {
        Intent intent = getIntent();
        if (intent != null) {
            OrderRequest orderRequest = (OrderRequest) intent.getSerializableExtra("buyNow");
            sp  = (Product) intent.getSerializableExtra("product");
            priceBySize = (float) intent.getSerializableExtra("priceBySize");
            productList = new ArrayList<>();
            productList.add(sp);
            System.out.println("Danh sách sản phẩm " + productList.get(0).getProductName());
            tvProductName.setText(sp.getProductName());
            tvSize.setText(ProductDetailActivity.size);
            Glide.with(BuyNowActivity.this)
                    .load(sp.getImage())
                    .into(ivProductImg);
            tvProductPrice.setText(UserOrderActivity.formatNumber(priceBySize));
            tvPrice.setText(UserOrderActivity.formatNumber(priceBySize));
            updateProductPrice();
        }


    }

    public void callApiChangeAdress() {
        UserTemp user = new UserTemp();
        user.setAddress(newAddress);
        user.setBirthday(userInfor.getData().getBirthday());
        user.setCccd(userInfor.getData().getCccd());
        user.setEmail(userInfor.getData().getEmail());
        user.setFirstname(userInfor.getData().getFirstname());
        user.setLastname(userInfor.getData().getLastname());
        user.setTax_id(userInfor.getData().getTax_id());

        ApiService.apiService.changeAddress("Bearer "+token, user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(BuyNowActivity.this, "Thay đổi địa chỉ thành công", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void callApiGetUserInfor() {
        ApiService.apiService.getUserInfor("Bearer "+token).enqueue(new Callback<EntityStatusResponse<UserTemp>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<UserTemp>> call, Response<EntityStatusResponse<UserTemp>> response) {
                userInfor = response.body();
                tvAddress.setText(String.valueOf(userInfor.getData().getAddress()));
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<UserTemp>> call, Throwable t) {
                System.out.println("Lay thong tin user thanh cong");

            }
        });
    }

    private void updateAddressDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_address);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        if(Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        }
        else {
            dialog.setCancelable(true);
        }

        EditText edtAddress = dialog.findViewById(R.id.edtAddress);
        Button btnSave = dialog.findViewById(R.id.btnSaveChange);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAddress = String.valueOf(edtAddress.getText());
                callApiChangeAdress();
                dialog.dismiss();
                callApiGetUserInfor();
            }
        });

        dialog.show();

    }
}
