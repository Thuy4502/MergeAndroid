// Hien thi danh sach san pham

package com.example.testapp;



import static com.example.testapp.LoginActivity.isInit;
import static com.example.testapp.LoginActivity.isLoad;
import static com.example.testapp.R.id.spnFilter;
import static com.example.testapp.api.ApiService.apiService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testapp.adapter.ProductCategoryAdapterVer2;
import com.example.testapp.adapter.ProductManagerAdapter;
import com.example.testapp.model.Category;
import com.example.testapp.model.Product;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.CommonResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    public static ProductManagerAdapter productManagerAdapter;
    public static RecyclerView rvProduct;
    public static List<Product> productList = new ArrayList<>();
    private ImageView ivBack;
    private AppCompatSpinner spnCategoryFilter;
    private ImageButton btnAdd;
    private SearchView svProduct;
    public static String tokenStaff;
    public static List<Category> categoryList = new ArrayList<>();
    public static List<Category> categoryListFilter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_list);
        if (isInit==false){
            createCategoryList();
            createCategoryFilterList();
            isInit=true;
        }
        setControl();
        setEvent();

    }
    private void createCategoryList() {
        categoryList.add(new Category("Cà phê"));
        categoryList.add(new Category("Trà sữa"));
        categoryList.add(new Category("Trà"));
        categoryList.add(new Category("Freeze"));
        categoryList.add(new Category("Nước ngọt"));
    }

    private void createCategoryFilterList() {
        categoryListFilter.add(new Category("Tất cả"));
        categoryListFilter.add(new Category("Cà phê"));
        categoryListFilter.add(new Category("Trà sữa"));
        categoryListFilter.add(new Category("Trà"));
        categoryListFilter.add(new Category("Freeze"));
        categoryListFilter.add(new Category("Nước ngọt"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StaffNavigationMenuActivity.class);
        startActivity(intent);
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        tokenStaff =  "Bearer "+sharedPreferences.getString("token", null);

        // click vào nút Back trên ActionBar
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, StaffNavigationMenuActivity.class);
                startActivity(intent);
            }
        });

        // click vào nút Thêm sẽ chuyển đn giao diện thêm sản phẩm
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, ProductAddingActivity.class);
                startActivity(intent);
            }
        });

        ProductCategoryAdapterVer2 productCategoryAdapterVer2 = new ProductCategoryAdapterVer2(ProductListActivity.this, R.layout.item_category_ver2, categoryListFilter);
        spnCategoryFilter.setAdapter(productCategoryAdapterVer2);
        spnCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = categoryListFilter.get(position).getCategory_name();
                Log.i("Category", categoryName);

                List<Product> filtedList = new ArrayList<>();
                if(categoryName.equals("Tất cả")) {
                    filtedList = productList; // Nếu chọn "Tất cả", tải tất cả sản phẩm
                } else {
                    for(Product product: productList){
                        if(product.getCategory().getCategory_name().equals(categoryName)){
                            filtedList.add(product); // Nếu không, lọc sản phẩm theo danh mục
                        }
                    }
                }

                if(filtedList.isEmpty() && isLoad==true){
                    Toast.makeText(ProductListActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
                productManagerAdapter = new ProductManagerAdapter(rvProduct.getContext(), filtedList);
                rvProduct.setAdapter(productManagerAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // gạch ngang dưới chân từng item
        rvProduct.setLayoutManager(new LinearLayoutManager(this));

        // call api lấy danh sách product
        getProductList();

        svProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(String.valueOf(newText));
                return true;
            }

        });
    }



    // filter Searchview
    private void filterList(String newText) {
        List<Product> filterList = new ArrayList<>();

        for(Product itemProduct: productList){
            if(itemProduct.getProductName().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(itemProduct);
            }
        }

        if(filterList.isEmpty()){
            Toast.makeText(ProductListActivity.this, "No data", Toast.LENGTH_SHORT).show();
        } else{
            productManagerAdapter.setFilterList(filterList);
        }
    }


    @SuppressLint("WrongViewCast")
    private void setControl() {
        rvProduct = findViewById(R.id.rvProduct);
        spnCategoryFilter = findViewById(R.id.spnCategoryFilter);
        btnAdd = findViewById(R.id.btnAdd);
        ivBack = findViewById(R.id.ivBack);
        svProduct = findViewById(R.id.svProduct);
    }


    public static void getProductList(){
        apiService.getProductAll(tokenStaff).enqueue(new Callback<CommonResponse<Product>>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse<Product>> call, @NonNull Response<CommonResponse<Product>> response) {
                if(response.isSuccessful()){
                    CommonResponse<Product> result = response.body();
                    if(result != null){
                        productList = result.getData();     // lấy list sp từ json
                        productManagerAdapter = new ProductManagerAdapter(rvProduct.getContext(), productList);
                        rvProduct.setAdapter(productManagerAdapter);
                        isLoad=true;
                    } else {
                        Toast.makeText(rvProduct.getContext(), "result null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(rvProduct.getContext(), "response false", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<CommonResponse<Product>> call, Throwable t) {
                Toast.makeText(rvProduct.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deletePd(String productId, int itemSelected, Context context1){
        apiService.deleteProduct(tokenStaff, String.valueOf(productId))
                .enqueue(new Callback<ApiResponse>() {

                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Log.i("TAGGGGGGG",productId);
                        Log.i("QQQQQQQ", ""+itemSelected);
                        if (response.isSuccessful()){
                            ApiResponse result = response.body();
                            if(result != null){
                                Toast.makeText(context1, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                getProductList();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }, 1000);
                            }
                        } else {
                            Toast.makeText(context1, "response false", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(context1, "xóa lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}