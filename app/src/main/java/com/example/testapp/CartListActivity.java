package com.example.testapp;

import static com.example.testapp.CustomerHomeActivity.countCart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapter.CartAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Cart;
import com.example.testapp.model.FullCart;
import com.example.testapp.response.EntityStatusResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends AppCompatActivity {

    private static RecyclerView rvCart;
    private Button btnShowCart;
    private static Button btnBuy;
    static CartAdapter cartAdapter;
    private static TextView tvTotalPrice;
    private ImageView ivBack;

    public static List<Cart> cartList;

    public static float totalPrice;

    //    public static int countCart;
    static String token ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token =  sharedPreferences.getString("token", null);
        setControl();
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCart.getContext(),
                DividerItemDecoration.VERTICAL);
        rvCart.addItemDecoration(dividerItemDecoration);
        callApiGetAllCart();
        setEvent();
        System.out.println("Thành công");

    }

    public static void setButtonBuy(int countCart) {
        if(CustomerHomeActivity.countCart == 0) {
            btnBuy.setEnabled(false);
        }
        else {
            btnBuy.setEnabled(true);
        }
    }


    public void setControl() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCart = findViewById(R.id.rvCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnBuy = findViewById(R.id.btnBuyInCart);
        ivBack = findViewById(R.id.ivBack);
    }

    public void setEvent() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCartList();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getCartList() {
        Intent intent = new Intent(CartListActivity.this, UserOrderActivity.class);
        startActivity(intent);
    }


    public static void callApiGetAllCart() {
        ApiService.apiService.getAllCart("Bearer " + token).enqueue(new Callback<EntityStatusResponse<FullCart>>() {
            @Override
            public void onResponse(@NonNull Call<EntityStatusResponse<FullCart>> call, @NonNull Response<EntityStatusResponse<FullCart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EntityStatusResponse<FullCart> list = response.body();
                    if (list.getData() != null && list.getData().getCart_detail() != null) {
                        cartList = list.getData().getCart_detail();
                        cartAdapter = new CartAdapter(cartList);
                        rvCart.setAdapter(cartAdapter);
                        totalPrice = list.getData().getTotal_price();
                        tvTotalPrice.setText(UserOrderActivity.formatNumber(totalPrice));
                        cartAdapter.updateTotalPrice(totalPrice);
                        cartAdapter.notifyDataSetChanged();
                        countCart = cartList.size();
                        setButtonBuy(countCart);
                    } else {
                        System.out.println("Gio hang rong");
                        // Xử lý khi danh sách giỏ hàng rỗng
                        GradientDrawable drawableEnable = new GradientDrawable();
                        drawableEnable.setShape(GradientDrawable.RECTANGLE);
                        drawableEnable.setCornerRadius(54);
                    }
                } else {
                    // Xử lý khi response không thành công
                    System.out.println("Response không thành công");
                }
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<FullCart>> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gọi API
                System.out.println("-----------------------Lỗi truy cập giỏ hàng");
            }
        });
    }


    public static void setTotalPrice(float price) {
        tvTotalPrice.setText(UserOrderActivity.formatNumber(price));
    }


    public static float getTotalPrice() {
        return totalPrice;
    }
}