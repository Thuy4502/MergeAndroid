package com.example.testapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.testapp.adapter.PhotoAdapter;
import com.example.testapp.adapter.ProductCustomerAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.FullCart;
import com.example.testapp.model.Photo;
import com.example.testapp.model.Product;
import com.example.testapp.model.request.CartRequest;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.CommonResponse;
import com.example.testapp.response.EntityStatusResponse;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeActivity extends AppCompatActivity {
    static RecyclerView rvProduct;
    private static List<Product> listPro;
    static ProductCustomerAdapter productApiAdapter;
    private TextView btnAddItem;
    private ImageButton ib_avtUser;

    private Button btnCaPhe, btnPhindi, btnTra, btnFreeze, btnBanh, btnAll, btnCartList;
    private SearchView searchView;

    static BottomNavigationView bottomNavigationView;

    private static int mcountProduct = 0;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;

    static int countCart = 0;
    private static final int SLIDE_DELAY = 3000; // Thời gian chờ giữa mỗi lần chuyển hình (2 giây)
    private Timer timer;

    String name;
//    static String token = "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTM4NDU0OTgsImV4cCI6MTcxNDQ1MDI5OCwidXNlcm5hbWUiOiIwOTI3MDE0MDUxIiwiYXV0aG9yaXRpZXMiOiJDVVNUT01FUiJ9.nzaEjAP5q0ZUyiSIRNJCQtJegz8wS_UgnG8lo89TgJDydUx5zJsAiz-Gnsx7oqUm";
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        token =  sharedPreferences.getString("token", null);

        setControls();
        rvProduct.setHasFixedSize(true);
        // Thiết lập LayoutManager GridLayoutManager với 2 cột
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        callApiGetProduct();
        setCountProductInCart();
        setEvent();
        setSlider();
    }

    public void setSlider() {
        photoAdapter = new PhotoAdapter(this, getListPhoto());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int nextPage = viewPager.getCurrentItem() + 1;
                        if (nextPage >= photoAdapter.getCount()) {
                            nextPage = 0; // Quay lại trang đầu tiên nếu đang ở trang cuối cùng
                        }
                        viewPager.setCurrentItem(nextPage, true); // Chuyển đến trang kế tiếp với hiệu ứng mượt mà
                    }
                });
            }
        };

        // Khởi tạo Timer với nhiệm vụ thực thi TimerTask sau mỗi khoảng thời gian SLIDE_DELAY
        timer = new Timer();
        timer.schedule(timerTask, SLIDE_DELAY, SLIDE_DELAY);
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img_slider));
        list.add(new Photo(R.drawable.img_slider2));
        list.add(new Photo(R.drawable.img_slider3));
        list.add(new Photo(R.drawable.img_slider4));
        return list;
    }

    public void setControls() {
        rvProduct = findViewById(R.id.recycler_listProduct);
        btnCaPhe = findViewById(R.id.btnCaPhe);
        btnPhindi = findViewById(R.id.btnPhindi);
        btnTra = findViewById(R.id.btnTra);
        btnBanh = findViewById(R.id.btnBanh);
        btnFreeze = findViewById(R.id.btnFreeze);
        searchView = findViewById(R.id.search_view);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAll =  findViewById(R.id.btnAll);
        searchView.clearFocus();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circle_indicator);
        ib_avtUser = findViewById(R.id.ib_avtUser);

    }

    public void setEvent() {
        int id = btnCaPhe.getId();
        System.out.println(id);
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
        drawableDisable.setColor(Color.parseColor("#FFFFFF"));

        ib_avtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileUser();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAll.setBackground(drawableEnable);
                btnFreeze.setBackground(drawableDisable);
                btnPhindi.setBackground(drawableDisable);
                btnTra.setBackground(drawableDisable);
                btnCaPhe.setBackground(drawableDisable);
                btnBanh.setBackground(drawableDisable);
                callApiGetProduct();

            }
        });
        btnCaPhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCaPhe.setBackground(drawableEnable);
                btnFreeze.setBackground(drawableDisable);
                btnPhindi.setBackground(drawableDisable);
                btnTra.setBackground(drawableDisable);
                btnBanh.setBackground(drawableDisable);
                btnAll.setBackground(drawableDisable);
                name = "Cà phê";
                callApiFilterProductByCategory();




            }
        });
        btnTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTra.setBackground(drawableEnable);
                btnFreeze.setBackground(drawableDisable);
                btnPhindi.setBackground(drawableDisable);
                btnCaPhe.setBackground(drawableDisable);
                btnBanh.setBackground(drawableDisable);
                btnAll.setBackground(drawableDisable);
                name = "Trà";
                callApiFilterProductByCategory();

            }
        });
        btnPhindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPhindi.setBackground(drawableEnable);
                btnFreeze.setBackground(drawableDisable);
                btnTra.setBackground(drawableDisable);
                btnCaPhe.setBackground(drawableDisable);
                btnBanh.setBackground(drawableDisable);
                btnAll.setBackground(drawableDisable);
                name = "Phindi";
                callApiFilterProductByCategory();

            }
        });
        btnFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFreeze.setBackground(drawableEnable);
                btnPhindi.setBackground(drawableDisable);
                btnTra.setBackground(drawableDisable);
                btnCaPhe.setBackground(drawableDisable);
                btnBanh.setBackground(drawableDisable);
                btnAll.setBackground(drawableDisable);
                name = "Freeze";
                callApiFilterProductByCategory();

            }
        });
        btnBanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBanh.setBackground(drawableEnable);
                btnFreeze.setBackground(drawableDisable);
                btnTra.setBackground(drawableDisable);
                btnCaPhe.setBackground(drawableDisable);
                btnPhindi.setBackground(drawableDisable);
                btnAll.setBackground(drawableDisable);
                name = "Bánh";
                callApiFilterProductByCategory();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.btn_home:
                        return true;
                    case R.id.btn_listOder:
                        return true;
                    case R.id.btn_showCart:
                        Intent cartIntent = new Intent(CustomerHomeActivity.this, CartListActivity.class);
                        startActivity(cartIntent);
                        return true;
                }
                return false;
            }
        });
    }

    private void fileList(String text) {
        List<Product> filterdList = new ArrayList<>();
        if(listPro != null) {
            for (Product i : listPro) {
                if(i.getProductName().toLowerCase().contains(text.toLowerCase())) {
                    filterdList.add(i);
                }
            }
            if (filterdList.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm");
            }
            else {
                productApiAdapter.setFilteredList(filterdList);

            }
        }
        else {
            System.out.println("Lỗi lọc sản phẩm");
        }
    }

    public void callApiGetProduct() {
        ApiService.apiService.getProduct("Bearer "+token).enqueue(new Callback<CommonResponse<Product>>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse<Product>> call, @NonNull Response<CommonResponse<Product>> response) {
                CommonResponse<Product> list = new CommonResponse<>();
                list = response.body();
                System.out.println("Token ne:    "+ token);
                listPro = list.getData();
                productApiAdapter = new ProductCustomerAdapter(list.getData());
                rvProduct.setAdapter(productApiAdapter);
                productApiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CommonResponse<Product>> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, "Lỗi lấy tất cả sản phẩm", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openProfileUser() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public static void updateAfterRemove() {
        ApiService.apiService.getProduct("Bearer "+token).enqueue(new Callback<CommonResponse<Product>>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse<Product>> call, @NonNull Response<CommonResponse<Product>> response) {
                CommonResponse<Product> list = new CommonResponse<>();
                list = response.body();
                listPro = list.getData();
                productApiAdapter = new ProductCustomerAdapter(list.getData());
                rvProduct.setAdapter(productApiAdapter);
                productApiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CommonResponse<Product>> call, Throwable t) {
                System.out.println("Lỗi cập nhật trạng thái sản phẩm");
            }
        });
    }

    private void callApiFilterProductByCategory() {

        ApiService.apiService.filterProductByCategory("Bearer "+token, name).enqueue(new Callback<CommonResponse<Product>>() {
            @Override
            public void onResponse(Call<CommonResponse<Product>> call, Response<CommonResponse<Product>> response) {
                CommonResponse<Product> list = new CommonResponse<>();
                list = response.body();
                assert list != null;
                listPro = list.getData();
                productApiAdapter = new ProductCustomerAdapter(list.getData());
                rvProduct.setAdapter(productApiAdapter);
            }

            @Override
            public void onFailure(Call<CommonResponse<Product>> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, "Lỗi lọc sản phẩm theo loại", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void callApiAddCart(CartRequest cart) {
        ApiService.apiService.addToCart("Bearer "+token, cart).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                System.out.println("Them vao gio hang thanh cong");
                ApiResponse cartResponse = response.body();
                CustomerHomeActivity.setCountProductInCart();
                if(cartResponse != null) {
                    System.out.println("Thêm sản phẩm vào giỏ hành thất bại");
                }
                else {
                    System.out.println("Thêm sản phẩm vào gi hàng thành công" );
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println("Them vao gio hang that bai");
            }
        });
    }

    public static void callApiReduceCart(CartRequest cart) {
        ApiService.apiService.reduceCart("Bearer "+token, cart).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                System.out.println("Xóa sản phảm khỏi gio hang thanh cong");
                ApiResponse cartResponse = response.body();
                if(cartResponse != null) {
                    System.out.println("Giảm số lượng sản phẩm thành công");
                }
                else {
                    System.out.println("Giảm số lượng sản phẩm thất bại" );
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println("Xóa sản phảm khỏi gio hang that bai");
            }
        });
    }

    public static void callApiIncrementCart(CartRequest cart) {
        ApiService.apiService.incrementCart("Bearer "+token, cart).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                System.out.println("Them vao gio hang thanh cong");
                ApiResponse cartResponse = response.body();
                if(cartResponse != null) {
                    System.out.println("Tăng số lượng sản phẩm thất bại");
                }
                else {
                    System.out.println("Tăng số lượng sản phẩm thành công" );
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println("Them vao gio hang that bai");
            }
        });
    }

    public static void callApiDeleteCart(CartRequest cart) {
        ApiService.apiService.deleteCart("Bearer "+token, cart).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                System.out.println("Xoa san pham khoi gio hang thanh cong");
                ApiResponse cartResponse = response.body();
                setCountProductInCart();

                if(cartResponse != null) {
                    System.out.println("Giỏ hàng rỗng nên không thể xóa");
                }
                else {
                    System.out.println("Xóa sản phẩm trong giỏ hàng thành công");
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println("Xoa san pham khoi gio hang that bai");
            }
        });
    }

    public static void setCountProductInCart() {
        getCountCart(new CountCartCallback() {
            @Override
            public void onCountCartReceived(int count) {
                mcountProduct = count;
                System.out.println("SỐ LƯỢNG SẢN PHẨM TRONG GIỎ HÀNG: " + count);
                BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.btn_showCart);
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(count);
            }
        });
    }

    public static void getCountCart(CountCartCallback callback) {
        ApiService.apiService.getAllCart("Bearer " + token).enqueue(new Callback<EntityStatusResponse<FullCart>>() {
            @Override
            public void onResponse(@NonNull Call<EntityStatusResponse<FullCart>> call, @NonNull Response<EntityStatusResponse<FullCart>> response) {
                EntityStatusResponse<FullCart> list = response.body();
                if (list != null && list.getData() != null && list.getData().getCart_detail() != null) {
                    countCart = list.getData().getCart_detail().size();
                    System.out.println("Số lượng sản phẩm hiện có trong giỏ hàng là: " + countCart);
                    callback.onCountCartReceived(countCart);
                } else {
                    System.out.println("Giỏ hàng rỗng");
                }
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<FullCart>> call, Throwable t) {
                System.out.println("Lỗi truy cập giỏ hàng");
            }
        });
    }

    interface CountCartCallback {
        void onCountCartReceived(int count);
    }



}
