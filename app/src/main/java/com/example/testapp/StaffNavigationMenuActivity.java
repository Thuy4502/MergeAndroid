package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.testapp.api.ApiService;
import com.example.testapp.response.ApiResponse;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffNavigationMenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView leftNav;
    ImageButton ibOpenMenu;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_navigation_menu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token = "Bearer " + sharedPreferences.getString("token", null);

        ibOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        leftNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Xác định ID của item được chọn
                int id = item.getItemId();
                // Xử lý sự kiện cho từng item
                switch (id) {
                    case R.id.navProduct:
                        // Xử lý khi click vào item "Sản phẩm"
                        // Ví dụ: mở màn hình Sản phẩm
                        openListProduct();
                        break;
                    case R.id.navOrder:
                        openListOrder();

                        break;
                    case R.id.navCustomer:
                        // Xử lý khi click vào item "Khách hàng"

                        break;
                    case R.id.navStatistic:
                        // Xử lý khi click vào item "Thống kê"
                        // Ví dụ: mở màn hình Thống kê
                        openStatisticScreen();
                        break;
                    case R.id.navCoupon:
                        // Xử lý khi click vào item "Khuyến mãi"
                        // Ví dụ: mở màn hình Khuyến mãi
                        openListCoupon();
                        break;
                    case R.id.navLogout:
                        // Xử lý khi click vào item "Đăng xuất"
                        // Ví dụ: thực hiện đăng xuất khỏi ứng dụng
                        logOut(token);
                        break;
                }

                // Đánh dấu item đã được chọn
                item.setChecked(true);

                // Đóng drawer layout sau khi chọn item
                drawerLayout.closeDrawers();

                return true;
            }
        });

    }

    private void logOut(String token) {
        ApiService.apiService.logOut(token).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", null);
                        long expirationTime = sharedPreferences.getLong("expiration_time", 0);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("token");
                        editor.remove("expiration_time");
                        editor.remove("address");
                        editor.apply();
                        token = null; // Đảm bảo token bị xóa khỏi bộ nhớ
                        openLoginActivity();
                    }else
                        Log.i("fail: ", "log out fail");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("error logout: ", t.getMessage());
            }
        });

    }

    private void openLoginActivity() {
        Intent intent = new Intent(StaffNavigationMenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void openListCoupon() {
        Intent intent = new Intent(StaffNavigationMenuActivity.this, CouponListAdminActivity.class);
        startActivity(intent);
    }

    private void openListProduct() {
        Intent intent = new Intent(StaffNavigationMenuActivity.this, ProductListActivity.class);
        startActivity(intent);
    }

    private void setControl() {
        drawerLayout = findViewById(R.id.drawerLayout);
        ibOpenMenu = findViewById(R.id.ib_drawerMenu);
        leftNav = findViewById(R.id.leftNav);
    }

    public void openStatisticScreen() {
        Intent intent = new Intent(StaffNavigationMenuActivity.this, AdminSaleStatisticActivity.class);
        startActivity(intent);

    }


    private void openListOrder() {
        Intent intent = new Intent(StaffNavigationMenuActivity.this,StaffOderListActivity.class);
        startActivity(intent);
    }
}