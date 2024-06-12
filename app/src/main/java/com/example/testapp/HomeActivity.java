package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.testapp.adapter.ProductAdapter;
import com.example.testapp.model.Product;
import com.example.testapp.model.ReviewStar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ImageButton ib_avtUser;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Product> data = new ArrayList<>();
    private  List<ReviewStar> listReviewStar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setControl();
        setEvent();
    }

    private void setEvent() {
        recyclerView.setHasFixedSize(true);
        // Thiết lập LayoutManager GridLayoutManager với 2 cột
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Khởi tạo adapter và gán vào RecyclerView
        adapter = new ProductAdapter(this, data, listReviewStar);
        recyclerView.setAdapter(adapter);

        ib_avtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileUser();
            }
        });

    }

    private void openProfileUser() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recycler_listProduct);

//        ib_avtUser = findViewById(R.id.ib_avtUser);
    }
}