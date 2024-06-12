package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.adapter.ProductReviewAdapter;
import com.example.testapp.api.ApiService;
import com.example.testapp.model.OrderDetail;
import com.example.testapp.model.Review;
import com.example.testapp.model.ReviewDTO;
import com.example.testapp.response.EntityStatusResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReviewActivity extends AppCompatActivity {
    private ListView lvListProduct;
    private Button btnSendReview;
    private EditText etContent;
    private RatingBar rtbStar;
    private final List<OrderDetail> data = new ArrayList<>();
    private ProductReviewAdapter reviewAdapter;
    private int selectedPosition = -1;

    public static String product_id;

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
    //fix

    private void setControl() {
        lvListProduct = findViewById(R.id.lv_listProductReview);

        btnSendReview = findViewById(R.id.btn_sendReview);

        rtbStar = findViewById(R.id.rtb_star);

        etContent = findViewById(R.id.et_reviewContent);
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        ArrayList<OrderDetail> receivedOrderList = UserOrderDetailActivity.orderDetailsReview;

        reviewAdapter = new ProductReviewAdapter(UserReviewActivity.this, R.layout.layout_item_review, receivedOrderList);
        lvListProduct.setAdapter(reviewAdapter);

        btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReview(token);
            }
        });
    }
    private void sendReview(String token){
        ReviewDTO reviewDTO = new ReviewDTO(UserOrderDetailActivity.order_id, product_id, etContent.getText().toString(), rtbStar.getNumStars());
        ApiService.apiService.addReview(token, reviewDTO).enqueue(new Callback<EntityStatusResponse<Review>>() {
            @Override
            public void onResponse(Call<EntityStatusResponse<Review>> call, Response<EntityStatusResponse<Review>> response) {
                if(response.isSuccessful()){
                    EntityStatusResponse<Review> resultResponse = response.body();
                    if(resultResponse != null){
                        Review reviewUser = resultResponse.getData();
                        String point = String.valueOf(reviewUser.getPoint_review());
                        Toast.makeText(UserReviewActivity.this, "Bạn sẽ nhận được " +point, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<EntityStatusResponse<Review>> call, Throwable t) {
            }
        });
    }
}