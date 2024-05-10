package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.adapter.OrderListAdapter;
import com.example.testapp.adapter.OrderStatusAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Order;
import com.example.testapp.model.OrderStatus;
import com.example.testapp.response.CommonResponse;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderHistoryActivity extends AppCompatActivity {
    private List<OrderStatus> listStatus = new ArrayList<>();
    OrderListAdapter orderAdapter;
    OrderStatusAdapter statusAdapter;
    ListView lvOrderList;
    TextView txtStatus, tvSelectStarDate, tvSelectEndDate, tvLine, tvNoData;
    Spinner spinnerList;
    ProgressBar proBarShowList;
    CardView btnShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setControl();
        setEvent();
    }
    private void setControl() {
        lvOrderList = findViewById(R.id.lvCustomerOrderList);

        spinnerList = (Spinner) findViewById(R.id.spnOrderFilter);

        txtStatus = findViewById(R.id.txtOrderStatus);
        tvSelectStarDate = findViewById(R.id.tv_selectStarDate);
        tvSelectEndDate = findViewById(R.id.tv_selectEndDate);
        tvLine = findViewById(R.id.tv_line);
        tvNoData = findViewById(R.id.tv_noData1);

        proBarShowList = findViewById(R.id.proBar_showList);

        btnShow = findViewById(R.id.btn_showDate);
    }

    private void setEvent() {
        SetData();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        proBarShowList.setVisibility(View.VISIBLE);
        getAllOrder(token);

        tvNoData.setVisibility(View.GONE);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(token);
            }
        });

        statusAdapter = new OrderStatusAdapter(this, R.layout.layout_item_order_status, listStatus);
        spinnerList.setAdapter(statusAdapter);

        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = String.valueOf(listStatus.get(position).getName());
//                Toast.makeText(StaffOderListActivity.this, selectedStatus, Toast.LENGTH_SHORT).show();
                int statusId = listStatus.get(position).getId();
                if(statusId != -1) {
                    getOrderById(token, statusId);
                    System.out.println("statsu: " + statusId); }
                else{
                    getAllOrder(token);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void SetData() {
        listStatus.add(new OrderStatus(-1,"Tất cả"));
        listStatus.add(new OrderStatus(0,"Chờ xác nhận"));
        listStatus.add(new OrderStatus(1,"Đã xác nhận"));
        listStatus.add(new OrderStatus(2,"Đang thực hiện"));
        listStatus.add(new OrderStatus(3,"Đang vận chuyển"));
        listStatus.add(new OrderStatus(4,"Đã hoàn thành"));
        listStatus.add(new OrderStatus(5,"Đã hủy"));
    }

    private void getAllOrder(String token){
        ApiService.apiService.getOrderHistoryByJwt(token).enqueue(new Callback<CommonResponse<Order>>() {
            @Override
            public void onResponse(Call<CommonResponse<Order>> call, Response<CommonResponse<Order>> response) {
                if(response.isSuccessful()){
                    CommonResponse<Order> resultResponse = response.body();
                    if(resultResponse != null){
                        List<Order> orderList = resultResponse.getData();
                        for (Order od : orderList){

                        }
                        if(orderList.isEmpty()){

                            tvNoData.setVisibility(View.VISIBLE);
                            proBarShowList.setVisibility(View.GONE);
                        }else {
                            orderAdapter = new OrderListAdapter(UserOrderHistoryActivity.this, R.layout.layout_item_order, orderList);
                            lvOrderList.setAdapter(orderAdapter);
                            lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Lấy dữ liệu từ item được click
                                    Order order = (Order) parent.getItemAtPosition(position);

                                    // Lấy ID của Order
                                    Long orderId = order.getOrder_id();

                                    // Chuyển dữ liệu đến Activity mới để hiển thị chi tiết
                                    Intent intent = new Intent(UserOrderHistoryActivity.this, UserOrderDetailActivity.class);
                                    intent.putExtra("orderId", orderId);
                                    startActivity(intent);
                                }
                            });
                            proBarShowList.setVisibility(View.GONE);
                        }
                        Log.i("get all order: ", resultResponse.getMessage());
                    }
                }else{
                    tvNoData.setVisibility(View.VISIBLE);
                    proBarShowList.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<CommonResponse<Order>> call, Throwable t) {
                Log.i("error get all order: ", t.getMessage());
            }
        });
    }

    private  void getOrderById(String token, Integer statusId){
        ApiService.apiService.getOrderByStatus(token, statusId).enqueue(new Callback<CommonResponse<Order>>() {
            @Override
            public void onResponse(Call<CommonResponse<Order>> call, Response<CommonResponse<Order>> response) {
                if(response.isSuccessful()){
                    CommonResponse<Order> resultResponse = null;
                    try {
                         resultResponse = response.body();
                        System.out.println("HIENTHI" + resultResponse.getStatus() + resultResponse.getMessage());
                    }catch (Exception e){
                        System.out.println("eee" +e.getMessage());
                    }


                    if(resultResponse != null){
                        List<Order> listOrder = resultResponse.getData();
                        System.out.println("LISTORDER");
                        if(listOrder.isEmpty()){
                            tvNoData.setVisibility(View.VISIBLE);
                        }else {
                            orderAdapter = new OrderListAdapter(UserOrderHistoryActivity.this, R.layout.layout_item_order, listOrder);
                            lvOrderList.setAdapter(orderAdapter);
                            orderAdapter.notifyDataSetChanged();

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<CommonResponse<Order>> call, Throwable t) {
                Log.i("error", t.getMessage());
                System.out.println("Troongsssssssssssssssssssssssssss" + t.getMessage());
            }
        });
    }

    private void openDatePicker(String token) {
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
        )).build();
        materialDatePicker. addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                String dateStart = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection.first));
                String dateEnd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection.second));

                String showStartDate =  new SimpleDateFormat("MM-dd", Locale.getDefault()).format(new Date(selection.first));
                String showEndDate =  new SimpleDateFormat("MM-dd", Locale.getDefault()).format(new Date(selection.second));

                tvSelectStarDate.setText(showStartDate);
                tvSelectEndDate.setText(showEndDate);
                tvLine.setVisibility(View.VISIBLE);
                tvSelectEndDate.setVisibility(View.VISIBLE);
                Log.i("test", dateStart + " " + dateEnd);
//                getOrderByDate(token, dateStart, dateEnd);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "tag");
    }

//    private void getOrderByDate(String token, String startDate, String endDate){
//        ApiService.apiservice.getOrderByDate(token, startDate, endDate).enqueue(new Callback<CommonResponse<Order>>() {
//            @Override
//            public void onResponse(Call<CommonResponse<Order>> call, Response<CommonResponse<Order>> response) {
//                if(response.isSuccessful()){
//                    CommonResponse<Order> resultResponse = response.body();
//                    if(resultResponse != null){
//                        List<Order> listOrder = resultResponse.getData();
//                        orderAdapter = new OrderListAdapter(UserOrderHistoryActivity.this, R.layout.layout_item_order, listOrder);
//                        lvOrderList.setAdapter(orderAdapter);
//                        Log.i("get order by status:", resultResponse.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CommonResponse<Order>> call, Throwable t) {
//                Log.i("error api get order by date ", t.getMessage());
//            }
//        });
//    }
}