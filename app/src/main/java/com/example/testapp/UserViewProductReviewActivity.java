package com.example.testapp;

import static com.example.testapp.ProductEditingActivity.productId;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.adapter.ProductCustomerAdapter;
import com.example.testapp.adapter.ReviewAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.Review;
import com.example.testapp.response.CommonResponse;
import com.example.testapp.response.EntityStatusResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewProductReviewActivity extends AppCompatActivity {
    ReviewAdapter reviewAdapter;
    ListView lv_listReview;
    private ProductCustomerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_product_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setControl();
        setEvent();
    }

    private void setEvent() {
        getReview(adapter.productID);
    }

    private void getReview(String productId) {
        ApiService.apiService.getReviewProduct(productId).enqueue(new Callback<CommonResponse<Review>>() {
            @Override
            public void onResponse(Call <CommonResponse<Review>> call, Response <CommonResponse<Review>> response) {
                if(response.isSuccessful()){
                    CommonResponse<Review> responseResult = response.body();
                    if(responseResult != null){
                        List<Review> listReview = responseResult.getData();
                        reviewAdapter = new ReviewAdapter(UserViewProductReviewActivity.this,R.layout.layout_item_review_user, listReview);
                        lv_listReview.setAdapter(reviewAdapter);
                    }

                    Log.i("get list review: ", responseResult.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse<Review>> call, Throwable t) {
                Log.e("error get list review: ", t.getMessage());
            }
        });
    }

    private void setControl() {
        lv_listReview = findViewById(R.id.lv_listReview);
    }
}