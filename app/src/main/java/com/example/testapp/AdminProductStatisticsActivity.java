package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.api.ApiService;
import com.example.testapp.model.ProductSaleRequest;
import com.example.testapp.response.CommonResponse;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductStatisticsActivity extends AppCompatActivity {
    private TextView tvTop1, tvTop2, tvTop3, tvTop4, tvTop5, tvProductNameTop1,tvProductNameTop2,
            tvProductNameTop3, tvProductNameTop4, tvProductNameTop5, tvCommentTop1, tvCommentTop2, tvCommentTop3,
            tvCommentTop4, tvCommentTop5;
    private PieChart pieChart;
    private LinearLayout layoutCommment1, layoutCommment2, layoutCommment3, layoutCommment4, layoutCommment5;
    private RelativeLayout rlt_top1, rlt_top2, rlt_top3, rlt_top4, rlt_top5;
    private TextView tvSelectStarDate, tvSelectEndDate, tvNoData1, tvNoData2, tvLine;
    private CardView btnShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_statistics);
        setControl();
        setEvent();
    }

    private void setEvent() {
       SharedPreferences sharedPreferences = getSharedPreferences("MyPerfs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(token);
            }
        });
        //get best-selling product data all time
        getDataStatistic(token);

    }


    private void showDataByChart(List<ProductSaleRequest> productSaleRequests) {
        List<TextView> listProductQuantity = Arrays.asList(tvTop1, tvTop2, tvTop3, tvTop4, tvTop5);
        List<TextView> listProductName = Arrays.asList(tvProductNameTop1, tvProductNameTop2, tvProductNameTop3, tvProductNameTop4, tvProductNameTop5);
        List<TextView> listProductNameComment = Arrays.asList(tvCommentTop1, tvCommentTop2, tvCommentTop3, tvCommentTop4, tvCommentTop5);
        List<Integer> listColor = Arrays.asList(R.color.top1Color, R.color.top2Color, R.color.top3Color, R.color.top4Color, R.color.top5Color);
        List<LinearLayout> listLinnerLayout = Arrays.asList(layoutCommment1,layoutCommment2, layoutCommment3, layoutCommment4, layoutCommment5);
        List<RelativeLayout> listRelativeLayout = Arrays.asList(rlt_top1, rlt_top2, rlt_top3, rlt_top4, rlt_top5);

        //reset pie chart before set new data
        pieChart.clearChart();
        for (int i = 0; i < 5; i++) {
            listLinnerLayout.get(i).setVisibility(View.GONE);
            listRelativeLayout.get(i).setVisibility(View.GONE);
        }
        tvNoData1.setVisibility(View.VISIBLE);
        tvNoData2.setVisibility(View.VISIBLE);

        // set and show data by API response
        for (int i = 0; i < productSaleRequests.size(); i++) {

            tvNoData1.setVisibility(View.GONE);
            tvNoData2.setVisibility(View.GONE);

            listProductName.get(i).setText(productSaleRequests.get(i).getProduct_name());
            listProductQuantity.get(i).setText(String.valueOf((int) productSaleRequests.get(i).getTotal_quantity()));
            listProductNameComment.get(i).setText(productSaleRequests.get(i).getProduct_name());
            pieChart.addPieSlice(
                    new PieModel(
                            productSaleRequests.get(i).getProduct_name(),
                            Integer.parseInt(listProductQuantity.get(i).getText().toString()), getColor(listColor.get(i))));
            listLinnerLayout.get(i).setVisibility(View.VISIBLE);
            listRelativeLayout.get(i).setVisibility(View.VISIBLE);
        }

//        Toast.makeText(AdminProductStatisticsActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
        Log.i("status", "success");
        pieChart.startAnimation();
    }

    private void getDataStatistic(String token) {
        ApiService.apiService.getStatisticProduct(token).enqueue(new Callback<CommonResponse<ProductSaleRequest>>() {
            @Override
            public void onResponse(Call<CommonResponse<ProductSaleRequest>> call, Response<CommonResponse<ProductSaleRequest>> response) {
                if (response.isSuccessful()) {
                    CommonResponse<ProductSaleRequest> productSaleRequest = response.body();
                    if (productSaleRequest != null) {
                        List<ProductSaleRequest> productSaleRequests = productSaleRequest.getData();
                        showDataByChart(productSaleRequests);
                    }
                    Log.i("message", "onResponse:" + productSaleRequest.getMessage());
                }else {
                    try {
                        Log.i("error",response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse<ProductSaleRequest>> call, Throwable t) {
                Toast.makeText(AdminProductStatisticsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Call API", "fail");
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

                tvSelectStarDate.setText(dateStart);
                tvSelectEndDate.setText(dateEnd);
                tvLine.setVisibility(View.VISIBLE);
                tvSelectEndDate.setVisibility(View.VISIBLE);

                //get best-selling product data by date
                getDataStatisticByDate(token, dateStart, dateEnd);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "tag");
    }

    private void getDataStatisticByDate(String token,String startDate, String endDate){
        Log.i(startDate, endDate);

        ApiService.apiService.getStatisticProductByDate(token, startDate, endDate).enqueue(new Callback<CommonResponse<ProductSaleRequest>>() {
            @Override
            public void onResponse(Call<CommonResponse<ProductSaleRequest>> call, Response<CommonResponse<ProductSaleRequest>> response) {
                if(response.isSuccessful()){

                    CommonResponse<ProductSaleRequest> dataResponse = response.body();
                    if(dataResponse != null){
                        List<ProductSaleRequest> listProduct = dataResponse.getData();
                        showDataByChart(listProduct);
                        Log.i("message", "onResponse:" + dataResponse.getMessage());
                    }else {
                        try {
                            Log.i("error",response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);

                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonResponse<ProductSaleRequest>> call, Throwable t) {
                Toast.makeText(AdminProductStatisticsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setControl() {
        tvTop1 = findViewById(R.id.tv_top1);
        tvTop2 = findViewById(R.id.tv_top2);
        tvTop3 = findViewById(R.id.tv_top3);
        tvTop4 = findViewById(R.id.tv_top4);
        tvTop5 = findViewById(R.id.tv_top5);

        tvCommentTop1 = findViewById(R.id.tv_commentTop1);
        tvCommentTop2 = findViewById(R.id.tv_commentTop2);
        tvCommentTop3 = findViewById(R.id.tv_commentTop3);
        tvCommentTop4 = findViewById(R.id.tv_commentTop4);
        tvCommentTop5 = findViewById(R.id.tv_commentTop5);

        tvProductNameTop1 = findViewById(R.id.tv_productNameTop1);
        tvProductNameTop2 = findViewById(R.id.tv_productNameTop2);
        tvProductNameTop3 = findViewById(R.id.tv_productNameTop3);
        tvProductNameTop4 = findViewById(R.id.tv_productNameTop4);
        tvProductNameTop5 = findViewById(R.id.tv_productNameTop5);

        layoutCommment1 = findViewById(R.id.layout_comment1);
        layoutCommment2 = findViewById(R.id.layout_comment2);
        layoutCommment3 = findViewById(R.id.layout_comment3);
        layoutCommment4 = findViewById(R.id.layout_comment4);
        layoutCommment5 = findViewById(R.id.layout_comment5);

        rlt_top1 = findViewById(R.id.rlt_top1);
        rlt_top2 = findViewById(R.id.rlt_top2);
        rlt_top3 = findViewById(R.id.rlt_top3);
        rlt_top4 = findViewById(R.id.rlt_top4);
        rlt_top5 = findViewById(R.id.rlt_top5);

        pieChart = findViewById(R.id.piechart);
        btnShow = findViewById(R.id.btn_showDate);

        tvSelectStarDate = findViewById(R.id.tv_selectStarDate);
        tvSelectEndDate = findViewById(R.id.tv_selectEndDate);

        tvNoData1 = findViewById(R.id.tv_noData1);
        tvNoData2 = findViewById(R.id.tv_noData2);
        tvLine = findViewById(R.id.tv_line);
    }
}