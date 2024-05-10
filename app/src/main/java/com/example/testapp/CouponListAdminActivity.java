package com.example.testapp;


import static com.example.testapp.LoginActivity.isLoad;
import static com.example.testapp.function.Function.formatDateTimeToDate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;


import com.example.testapp.adapter.CouponAdapter;
import com.example.testapp.adapter.ProductCategoryAdapterVer2;
import com.example.testapp.adapter.ProductManagerAdapter;
import com.example.testapp.adapter.StatusAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Coupon;
import com.example.testapp.model.Product;
import com.example.testapp.response.CommonResponse;
import com.google.android.gms.common.internal.service.Common;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CouponListAdminActivity extends AppCompatActivity {
    private Button btnAddCoupon;
    public static ListView lvCouponList;
    public static CouponAdapter couponManagerAdapter;
    private ImageView ivBack;
    public static List<Coupon> couponList= new ArrayList<>();
    private SearchView svCoupon;
    public static String tokenStaff;
    AppCompatSpinner spnCouponFilter;
    List<String> listStatus = new ArrayList<>();
    boolean isLoadCouponList = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_coupon_admin);
        listStatus.add("all");
        listStatus.add("active");
        listStatus.add("unactive");
        setControl();
        setEvent();
    }

    private void setControl() {
        btnAddCoupon = findViewById(R.id.btnAddCoupon);
        lvCouponList = findViewById(R.id.lv_couponList);
        svCoupon = findViewById(R.id.svCoupon);
        ivBack = findViewById(R.id.ivBack);
        spnCouponFilter = findViewById(R.id.spnCouponFilter);
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
        btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponListAdminActivity.this, CouponAddingAdminActivity.class));
            }
        });



        StatusAdapter spinnerAdapter = new StatusAdapter(this, R.layout.item_category_ver2, listStatus);
        spnCouponFilter.setAdapter(spinnerAdapter);
        spnCouponFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = listStatus.get(position);
                Log.i("Category", categoryName);

                List<Coupon> filtedList = new ArrayList<>();
                if(categoryName.equals("all")) {
                    filtedList = couponList; // Nếu chọn "Tất cả", tải tất cả sản phẩm
                } else {
                    for(Coupon coupon: couponList){
                        if(coupon.getStatus().equals(categoryName)){
                            filtedList.add(coupon); // Nếu không, lọc sản phẩm theo danh mục
                        }
                    }
                }

                if(filtedList.isEmpty() && isLoadCouponList==true){
                    Toast.makeText(CouponListAdminActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
                couponManagerAdapter = new CouponAdapter(CouponListAdminActivity.this, R.layout.item_coupon_admin, filtedList);
                lvCouponList.setAdapter(couponManagerAdapter);
                isLoadCouponList = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvCouponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy coupon tại vị trí được nhấp
                Coupon clickedCoupon = (Coupon) parent.getItemAtPosition(position);
                Log.i("QQ", ""+clickedCoupon.getCoupon_id());
                // Tạo một Intent để chuyển sang CouponDetailActivity
                Intent intent = new Intent(CouponListAdminActivity.this, CouponDetailAdminActivity.class);

                // Đưa dữ liệu của coupon vào Intent
                intent.putExtra("title", clickedCoupon.getType());
                intent.putExtra("id", String.valueOf(clickedCoupon.getCoupon_id()));
                intent.putExtra("startDate", formatDateTimeToDate(clickedCoupon.getStart_date()));
                intent.putExtra("endDate", formatDateTimeToDate(clickedCoupon.getEnd_date()));
                intent.putExtra("content", clickedCoupon.getContent());

                // Bắt đầu CouponDetailActivity
                startActivity(intent);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CouponListAdminActivity.this, StaffNavigationMenuActivity.class);
                startActivity(intent);
            }
        });
        getAllCoupon();
        svCoupon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Coupon> filteredData = new ArrayList<>();
                for (Coupon coupon : couponList) {
                    if (coupon.getCoupon_id().toString().contains(newText)) {
                        filteredData.add(coupon);
                    }
                }

                if (filteredData.isEmpty()) {
                    Toast.makeText(CouponListAdminActivity.this, "No data", Toast.LENGTH_LONG).show();
                }
                couponManagerAdapter = new CouponAdapter(CouponListAdminActivity.this, R.layout.item_coupon_admin, filteredData);
                lvCouponList.setAdapter(couponManagerAdapter);


                return false;
            }

        });
    }









    public static void getAllCoupon() {
        ApiService.apiService.adminGetAllCoupon(tokenStaff).enqueue(new Callback<CommonResponse<Coupon>>() {
            @Override
            public void onResponse(Call<CommonResponse<Coupon>> call, Response<CommonResponse<Coupon>> response) {
                if(response.isSuccessful()){
                    CommonResponse<Coupon> resultResponse = response.body();
                    if(resultResponse != null){
                        couponList = resultResponse.getData();
                        couponManagerAdapter = new CouponAdapter(lvCouponList.getContext(), R.layout.item_coupon_admin, couponList);
                        lvCouponList.setAdapter(couponManagerAdapter);
                    } else{
                        Toast.makeText(lvCouponList.getContext(), "result null",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(lvCouponList.getContext(), "response false",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CommonResponse<Coupon>> call, Throwable t) {
                Toast.makeText(lvCouponList.getContext(), "ko call dc api",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
