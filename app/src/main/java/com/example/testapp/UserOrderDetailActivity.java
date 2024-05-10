package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class UserOrderDetailActivity extends AppCompatActivity {
    private ListView lvListProduct;
    private Button btnShowProcess;
    private Toolbar tb_app_bar;
    private ProductDetailOrderAdapter adapter_productDetail;

    private TextView tvOrderId, tvProductPrice, tvTotalPrice, tvFreightCost, tvOrderAddress, tvCompletedOrder;

    private ProgressBar proBar_loading;

    private LinearLayout lnl_showOrderDetail;
    private Integer statusId;
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setControl();
        setEvent();
    }

    private void setControl() {
        lvListProduct = findViewById(R.id.lv_listProduct);

        btnShowProcess = findViewById(R.id.btn_showProcess);

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

        Long orderId = getIntent().getLongExtra("orderId", 0);
        getOrderById(token, orderId);



    }

    private void getOrderById(String token, Long orderId) {
        ArrayList<OrderDetail> orderDetailsReview = new ArrayList<>();
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

                        //set title action bar
                        tb_app_bar.setTitle(orderResponse.getUpdate_at().toString());

                        //get info customer
                        Customer customerOfOrder = orderResponse.getCustomer();

                        //user address
                        tvOrderAddress.setText(customerOfOrder.getAddress());

                        //set data for list view show order detail
                        List<OrderDetail> listOrderDetail = orderResponse.getOrder_detail();

                        adapter_productDetail = new ProductDetailOrderAdapter(UserOrderDetailActivity.this, R.layout.layout_item_product_order, listOrderDetail);
                        lvListProduct.setAdapter(adapter_productDetail);

//                        String productName = listOrderDetail.get(0).getProduct().getProduct_name();
//
//                        orderDetailsReview.add(new OrderDetail(null, listOrderDetail.get(0).getPrice(), null, listOrderDetail.get(0).getProduct()));
//
//                        for (int i = 1; i < listOrderDetail.size(); i++) {
//                            if (!listOrderDetail.get(i).getProduct().getProduct_name().equals(productName)) {
//                                orderDetailsReview.add(new OrderDetail(null, listOrderDetail.get(i).getPrice(), null, listOrderDetail.get(i).getProduct()));
//                                productName = listOrderDetail.get(i).getProduct().getProduct_name();
//                            }
//                        }
//                        Intent intent = new Intent(UserOrderDetailActivity.this, UserReviewActivity.class);
//                        intent.putParcelableArrayListExtra("orderList", (ArrayList<? extends Parcelable>) orderDetailsReview);
//                        startActivity(intent);
//                        Log.i("listReview", String.valueOf(orderDetailsReview.get(0).getProduct().getProduct_name()));
                        statusId = orderResponse.getStatus();

                        //set height for list view
                        if (listOrderDetail.size() <= 3){
                            lvListProduct.getLayoutParams().height = listOrderDetail.size() * 230;
                        }else {
                            lvListProduct.getLayoutParams().height = 690;
                        }

                        if(statusId != 4){
                            btnShowProcess.setText("Tiến trình đơn hàng");
                            btnShowProcess.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(UserOrderDetailActivity.this, UserDeliveryProcessActivity.class);
                                    intent.putExtra("orderId", orderId);
                                    startActivity(intent);
                                }
                            });
                        }

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
}