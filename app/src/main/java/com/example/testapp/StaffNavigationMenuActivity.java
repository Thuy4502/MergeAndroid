package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.navigation.NavigationView;

public class StaffNavigationMenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView leftNav;
    ImageButton ibOpenMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_navigation_menu);
        setControl();
        setEvent();
    }

    private void setEvent() {
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