package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.adapter.ProductDetailOrderAdapter;
import com.example.testapp.model.OrderDetail;
import com.example.testapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class UserReviewActivity extends AppCompatActivity {
    private ListView lvListProduct;
    private final List<OrderDetail> data = new ArrayList<>();
    private ProductDetailOrderAdapter productReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setControl();
        setEvent();
    }

    private void setControl() {
        lvListProduct = findViewById(R.id.lv_listProductReview);
    }

    private void setEvent() {
        ArrayList<OrderDetail> receivedOrderList = UserOrderDetailActivity.orderDetailsReview;
        Log.i("list review product", String.valueOf(receivedOrderList.size()));
//                getIntent().getParcelableArrayListExtra("orderList");
//        if (receivedOrderList != null) {
//            Log.i("listReview", String.valueOf(receivedOrderList.get(0).getProduct().getProductName()));
//        }

    }
}