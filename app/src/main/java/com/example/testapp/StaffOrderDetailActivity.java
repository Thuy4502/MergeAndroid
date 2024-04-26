package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testapp.adapter.ProductDetailOrderAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.function.Function;
import com.example.testapp.model.Customer;
import com.example.testapp.model.Order;
import com.example.testapp.model.OrderDetail;
import com.example.testapp.response.EntityStatusResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffOrderDetailActivity extends AppCompatActivity {
    private ListView lvListProduct;
    private Button btnUpdateStatus;
    private List<OrderDetail> data = new ArrayList<>();
    private Toolbar tb_app_bar;
    private ProductDetailOrderAdapter adapter_productDetail;

    private TextView tvOrderId, tvProductPrice, tvTotalPrice, tvFreightCost, tvOrderAddress, tvCompletedOrder;

    private ProgressBar proBar_loading;

    private LinearLayout lnl_showOrderDetail;
    private final Handler handler = new Handler();
    static Long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_change_order_status);
        setControl();
        setEvent();
    }

    private void setControl() {
        lvListProduct = findViewById(R.id.lv_listProduct);

        btnUpdateStatus = findViewById(R.id.btn_updateStatus);

        tvOrderId = findViewById(R.id.tv_orderId);
        tvProductPrice = findViewById(R.id.tv_productPrice);
        tvTotalPrice = findViewById(R.id.tv_totalPrice);
        tvFreightCost = findViewById(R.id.tv_freightCost);
        tvOrderAddress = findViewById(R.id.tv_orderAddress);
        tvCompletedOrder = findViewById(R.id.tv_completedOrder);

        tb_app_bar = findViewById(R.id.app_bar);

        proBar_loading = findViewById(R.id.proBar_loading);

        lnl_showOrderDetail = findViewById(R.id.lnl_showOrderDetail);
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        proBar_loading.setVisibility(View.VISIBLE);
        lnl_showOrderDetail.setVisibility(View.GONE);

        orderId = getIntent().getLongExtra("orderId", 0);
        getOrderById(token, orderId);


    }

    private void showStatusName(Integer status_id) {
        btnUpdateStatus.setVisibility(View.VISIBLE);
        tvCompletedOrder.setVisibility(View.GONE);
        if (status_id == 0) {
            btnUpdateStatus.setText("Nhận đơn");
        } else if (status_id == 1) {
            btnUpdateStatus.setText("Đang thực hiện");
        } else if (status_id == 2) {
            btnUpdateStatus.setText("Đang giao");
        } else if (status_id == 3) {
            btnUpdateStatus.setText("Đã giao");
        } else if (status_id == 4) {
            btnUpdateStatus.setVisibility(View.GONE);
            tvCompletedOrder.setVisibility(View.VISIBLE);
        }

    }


    private void getOrderById(String token, Long orderId) {
        ApiService.apiService.getOrderById(token, orderId).enqueue(new Callback<EntityStatusResponse<Order>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<Order>> call, Response<EntityStatusResponse<Order>> response) {
                if (response.isSuccessful()) {
                    EntityStatusResponse<Order> resultResponse = response.body();
                    if (resultResponse != null) {

                        proBar_loading.setVisibility(View.GONE);
                        lnl_showOrderDetail.setVisibility(View.VISIBLE);
                        //get info order
                        Order orderResponse = resultResponse.getData();

                        //order id
                        tvOrderId.setText(orderResponse.getOrder_id().toString());
                        //total price product
                        tvProductPrice.setText(Function.formatToVND(orderResponse.getTotal_price()));
                        //order total price
                        Integer totalPrice = orderResponse.getTotal_price() + Integer.parseInt((String) tvFreightCost.getText());
                        Integer format = Integer.parseInt((String) tvFreightCost.getText());

                        tvFreightCost.setText(Function.formatToVND(format));

                        tvTotalPrice.setText(Function.formatToVND(totalPrice));

                        //show statusName for button change status
                        Log.i("statusId", orderResponse.getStatus().toString());
                        showStatusName(orderResponse.getStatus());

                        //set title action bar
                        tb_app_bar.setTitle(orderResponse.getUpdate_at().toString());

                        //get info customer
                        Customer customerOfOrder = orderResponse.getCustomer();

                        //user address
                        tvOrderAddress.setText(customerOfOrder.getAddress());

                        //set data for list view show order detail
                        List<OrderDetail> listOrderDetail = orderResponse.getOrder_detail();
                        adapter_productDetail = new ProductDetailOrderAdapter(StaffOrderDetailActivity.this, R.layout.layout_item_product_order, listOrderDetail);
                        lvListProduct.setAdapter(adapter_productDetail);
                        //set height for list view
                        if (listOrderDetail.size() <= 3){
                            lvListProduct.getLayoutParams().height = listOrderDetail.size() * 230;
                        }else {
                            lvListProduct.getLayoutParams().height = 690;
                        }
                        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateStatusOrder(token,orderResponse.getStatus()+1, orderId);
                                getOrderById(token, orderId);
                            }
                        });

                        Log.i("status", orderResponse.getStatus().toString());
                        Log.i("message", "onResponse: " + resultResponse.getMessage());
                    }
                }
//                handler.postDelayed(() -> getOrderById(token, orderId), 10000); // 10 giây
            }
            @Override
            public void onFailure(Call<EntityStatusResponse<Order>> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }

    private void updateStatusOrder(String token, Integer statusUpdate, Long orderId) {
        Order order = new Order();
        order.setStatus(statusUpdate);
        ApiService.apiService.updateStatusOrder(token, orderId, order).enqueue(new Callback<EntityStatusResponse<Order>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<Order>> call, Response<EntityStatusResponse<Order>> response) {
                if (response.isSuccessful()) {
                    EntityStatusResponse<Order> resultResponse = response.body();
                    if (resultResponse != null) {
                        Order orderResponse = resultResponse.getData();
//                        showStatusName(orderResponse.getStatus());
                        Log.i("message update status", resultResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityStatusResponse<Order>> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });

    }


}