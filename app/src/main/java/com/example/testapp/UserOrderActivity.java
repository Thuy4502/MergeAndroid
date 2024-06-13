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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapter.ProductInOrderAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.function.Function;
import com.example.testapp.model.Cart;
import com.example.testapp.model.FullCart;
import com.example.testapp.model.OrderID;
import com.example.testapp.model.UserInfo;
import com.example.testapp.response.EntityStatusResponse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderActivity extends AppCompatActivity {
    RecyclerView rvProductList;
    Button btnOrder, btnEditAddress, btnPickUp, btnDelivery;
    static ProductInOrderAdapter adapter;
    static TextView tvProductPrice;
    static TextView tvTotalPrice;
    TextView tvAddress, tvFlexible, tvDeliveryCost, tvUserAddress,  tvPointUser, tv_usePoint;;
    public static EntityStatusResponse<UserInfo> userInfor;
    String newAddress;
    public static float totalProductPrice;
    LinearLayout lyDeliveryCost;

    private Toolbar appBar;

    static List<Cart> productList = new ArrayList<>();
    public static Long orderInCartID;

    public float dCost = 15000;

    static String token, point;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token =  sharedPreferences.getString("token", null);
        point = sharedPreferences.getString("point", null);
        setControl();
        dCost = Float.parseFloat(String.valueOf(tvDeliveryCost.getText()));
        dCost = 15000;
        setTotalPrice(totalProductPrice, dCost, 0);
        callApiGetListProduct();
        setValue();
        setEvent();
        callApiGetUserInfor();

    }

    public void setValue() {
        callApiGetListProduct();
        adapter = new ProductInOrderAdapter(productList);
        rvProductList.setAdapter(adapter);
        rvProductList.setLayoutManager(new GridLayoutManager(this, 1));
        rvProductList.addItemDecoration(new DividerItemDecoration(rvProductList.getContext(), DividerItemDecoration.VERTICAL));

    }

    public static void callApiGetListProduct() {
        ApiService.apiService.getAllCart("Bearer "+token).enqueue(new Callback<EntityStatusResponse<FullCart>>() {
            @Override
            public void onResponse(@NonNull Call<EntityStatusResponse<FullCart>> call, @NonNull Response<EntityStatusResponse<FullCart>> response) {
                EntityStatusResponse<FullCart> list = response.body();
                productList.clear();
                productList.addAll(list.getData().getCart_detail());
                totalProductPrice = list.getData().getTotal_price();
                tvProductPrice.setText(UserOrderActivity.formatNumber(totalProductPrice));
                tvTotalPrice.setText(formatNumber(totalProductPrice + 15000));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<EntityStatusResponse<FullCart>> call, Throwable t) {
                System.out.println("Lỗi truy cập giỏ hàng");
            }
        });
    }

    public void setTotalPrice(float total, float delivery, float discount) {
        float updatePrice = totalProductPrice + delivery - discount;
        tvTotalPrice.setText(formatNumber(updatePrice));
    }

    public static String formatNumber(float number) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedNumber = formatter.format(number);
        return formattedNumber;
    }


    public void setControl() {
        rvProductList = findViewById(R.id.rvProductList);
        btnOrder = findViewById(R.id.btn_buyNow);
        tvProductPrice = findViewById(R.id.tv_productPrice);
        tvTotalPrice = findViewById(R.id.tv_totalPrice);
        btnEditAddress = findViewById(R.id.btnEditAddress);
        tvAddress = findViewById(R.id.tvUserAddress);
        tvFlexible = findViewById(R.id.tvFlexible);
        tvDeliveryCost = findViewById(R.id.tvDeliveryCost);
        btnPickUp = findViewById(R.id.btnPickUp);
        lyDeliveryCost = findViewById(R.id.lyDeliveryCost);
        btnDelivery = findViewById(R.id.btnDelivery);
        appBar = findViewById(R.id.app_bar);
        tvPointUser = findViewById(R.id.tv_pointUser);
        tv_usePoint = findViewById(R.id.tv_usePoint);
    }

    public void setEvent() {
        int cornerRadiusPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12,
                getResources().getDisplayMetrics()
        );

        GradientDrawable drawableEnable = new GradientDrawable();
        drawableEnable.setShape(GradientDrawable.RECTANGLE);
        drawableEnable.setCornerRadius(cornerRadiusPixels);
        drawableEnable.setColor(Color.parseColor("#b42329"));

        GradientDrawable drawableDisable = new GradientDrawable();
        drawableDisable.setShape(GradientDrawable.RECTANGLE);
        drawableDisable.setCornerRadius(cornerRadiusPixels);
        drawableDisable.setColor(Color.parseColor("#FFFFFF"));
        drawableDisable.setStroke(4, Color.parseColor("#b42329"));

        tvPointUser.setText("Sử dụng " + point + " điểm");

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiOrderInCart();
            }
        });

        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddressDialog(Gravity.CENTER);
            }
        });

        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelivery.setBackground(drawableDisable);
                btnPickUp.setBackground(drawableEnable);
                btnPickUp.setTextColor(Color.WHITE);
                btnDelivery.setTextColor(Color.BLACK);
                lyDeliveryCost.setVisibility(View.GONE);
                tvAddress.setText("97 Man Thiện, P.Hiệp Phú, Tp.Thủ Đức, Tp.HCM");
                btnEditAddress.setVisibility(View.GONE);
                dCost = 0;
                setTotalPrice(totalProductPrice, dCost, 0);
            }
        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPickUp.setBackground(drawableDisable);
                btnDelivery.setBackground(drawableEnable);
                btnDelivery.setTextColor(Color.WHITE);
                btnPickUp.setTextColor(Color.BLACK);
                lyDeliveryCost.setVisibility(View.VISIBLE);
                callApiGetUserInfor();
                btnEditAddress.setVisibility(View.VISIBLE);
                dCost = 15000;
                setTotalPrice(totalProductPrice, dCost, 0);
            }
        });

        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void callApiChangeAdress() {
        UserInfo user = new UserInfo();
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
                Toast.makeText(UserOrderActivity.this, "Thay đổi địa chỉ thành công", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void callApiOrderInCart() {
        ApiService.apiService.orderInCart("Bearer "+token).enqueue(new Callback<EntityStatusResponse<OrderID>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<OrderID>> call, Response<EntityStatusResponse<OrderID>> response) {
                System.out.println("Đặt hàng thành công!");
                orderInCartID = response.body().getData().getId();
                Intent intent = new Intent(UserOrderActivity.this, UserDeliveryProcessActivity.class);
                intent.putExtra("OrderID", orderInCartID);
                startActivity(intent);
                Toast.makeText(UserOrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<EntityStatusResponse<OrderID>> call, Throwable t) {
                System.out.println("Đặt hàng thất bại!");
                Toast.makeText(UserOrderActivity.this, "Đặt hàng thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void callApiGetUserInfor() {
        ApiService.apiService.getUserInfor("Bearer "+token).enqueue(new Callback<EntityStatusResponse<UserInfo>
                >() {
            @Override
            public void onResponse(Call<EntityStatusResponse<UserInfo>> call, Response<EntityStatusResponse<UserInfo>> response) {
                userInfor = response.body();
                System.out.println("Lay thong tin user thanh cong");
                System.out.println("Ddddddddddddd" + userInfor.getData().getAddress());
                tvAddress.setText(String.valueOf(userInfor.getData().getAddress()));
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<UserInfo>> call, Throwable t) {
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
