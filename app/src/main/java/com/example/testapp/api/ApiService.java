package com.example.testapp.api;

import com.example.testapp.model.Coupon;
//import com.example.testapp.model.CouponDetail;
import com.example.testapp.model.CouponDetail;
import com.example.testapp.model.Customer;
import com.example.testapp.model.FullCart;
import com.example.testapp.model.Order;
import com.example.testapp.model.OrderID;
import com.example.testapp.model.Product;
import com.example.testapp.model.ProductSaleRequest;
import com.example.testapp.model.Review;
import com.example.testapp.model.ReviewDTO;
import com.example.testapp.model.ReviewStar;
import com.example.testapp.model.Size;
import com.example.testapp.model.StatisticRequest;
import com.example.testapp.model.User;
import com.example.testapp.model.UserInfo;
import com.example.testapp.model.request.CartRequest;
import com.example.testapp.model.request.OrderRequest;
import com.example.testapp.response.ApiResponse;
import com.example.testapp.response.EntityStatusResponse;
import com.example.testapp.response.CommonResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //base link:http://....:9999/
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder().baseUrl("http://192.168.1.3:9999/").addConverterFactory(GsonConverterFactory.create(gson))

            .build().create(ApiService.class);

    @POST("auth/signup")
    Call<ApiResponse> sendUser(@Body User user);

    @POST("auth/logout")
    Call<ApiResponse> logOut(@Header("Authorization") String token);

    @POST("auth/signin")
    Call<User> loginUser(@Body User user);

    @PUT("auth/change/{username}")
    Call<ApiResponse> changePassword(@Path("username") String username, @Body User user);

    @GET("auth/check/{username}")
    Call<ApiResponse> checkUserNameExist(@Path("username") String username);

    @GET("api/users/find")
    Call<EntityStatusResponse<Customer>> getUserProfile(@Header("Authorization") String token);

    //statistic
    @GET("api/admin/statistic/product")
    Call<CommonResponse<ProductSaleRequest>> getStatisticProduct(@Header("Authorization") String token);

    @GET("api/admin/statistic/year")
    Call<CommonResponse<StatisticRequest>> getStatisticYear(@Header("Authorization") String token, @Query("year") String year);

    @GET("api/admin/statistic/date")
    Call<CommonResponse<ProductSaleRequest>> getStatisticProductByDate(@Header("Authorization") String token, @Query("start") String start, @Query("end") String end);

    //order
    @GET("api/admin/order/{orderId}/find")
    Call<EntityStatusResponse<Order>> getOrderById(@Header("Authorization") String token, @Path("orderId") Long orderId);

    @PUT("api/admin/order/{orderId}/status")
    Call<EntityStatusResponse<Order>> updateStatusOrder(@Header("Authorization") String token, @Path("orderId") Long orderId, @Body Order order);

    @GET("api/admin/order/all")
    Call<CommonResponse<Order>> getAllOrder(@Header("Authorization") String token);
    @GET ("api/admin/order/status")
    Call<CommonResponse<Order>> getOrderByStatus(@Header("Authorization") String token, @Query("status") Integer status );
    @GET ("api/admin/order/date")
    Call<CommonResponse<Order>> getOrderByDate(@Header("Authorization")String token, @Query("start") String start, @Query("end") String end);
    @GET("api/order/all")
    Call<CommonResponse<Order>> getOrderHistoryByJwt(@Header("Authorization") String token);
    @GET("api/coupon/all")
    Call<CommonResponse<Coupon>> getAllCoupon(@Header("Authorization") String token);
    @POST("api/review/add")
    Call<EntityStatusResponse<Review>> addReview(@Header("Authorization") String token, @Body ReviewDTO reviewDTO);
    @GET("api/review/{product_id}/get")
    Call<EntityStatusResponse<Review>> getReviewProduct(@Path("product_id") String product_id);




    @GET("api/products/category?")
    Call<CommonResponse<Product>> filterProductByCategory(@Header("Authorization") String token, @Query("name") String nameParam);

    @GET("api/product/all")
    Call<CommonResponse<Product>> getProduct(@Header("Authorization") String token);

    @GET("api/cart/")
    Call<EntityStatusResponse<FullCart>> getAllCart(@Header("Authorization") String token);

    @GET("api/admin/product/find/{productID}")
    Call<Product> getProductDetail(@Header("Authorization") String token, @Path("productID") String productID);

    @PUT("api/cart/add")
    Call<ApiResponse> addToCart(@Header("Authorization") String token, @Body CartRequest cart);

    @PUT("api/cart/reduce/quantity")
    Call<ApiResponse> reduceCart(@Header("Authorization") String token, @Body CartRequest cart);

    @PUT("api/cart/increment/quantity")
    Call<ApiResponse> incrementCart(@Header("Authorization") String token, @Body CartRequest cart);

    @POST("api/cart/delete/item")
    Call<ApiResponse> deleteCart(@Header("Authorization") String token, @Body CartRequest cart);

    @POST("api/order/create")
    Call<Void> addOrder(@Header("Authorization") String token);

    @POST("api/order/buynow")
    Call<EntityStatusResponse<OrderID>> buyNow(@Header("Authorization") String token, @Body OrderRequest orderRequest);

    @POST("api/order/create")
    Call<EntityStatusResponse<OrderID>> orderInCart(@Header("Authorization") String token);

    @GET("api/order/size/all")
    Call<CommonResponse<Size>> getPriceBySize(@Header("Authorization") String token);

    @PUT("api/customer/3/update")
    Call<Void> changeAddress(@Header("Authorization") String token, @Body UserInfo user);

    @GET("api/users/profile")
    Call<EntityStatusResponse<UserInfo>> getUserInfor(@Header("Authorization") String token);


    @GET("api/admin/product/all")
    Call<CommonResponse<Product>> getProductAll(@Header("Authorization") String token);

    @Multipart
    @POST("api/admin/product/add")
    Call<ApiResponse> addProduct(@Header("Authorization") String token,
                                 @Part MultipartBody.Part image,
                                 @Part("data") RequestBody data);

    @DELETE("api/admin/product/{product_id}/delete/")
    Call<ApiResponse> deleteProduct(@Header("Authorization") String token,
                                    @Path("product_id") String product_id);

    @Multipart
    @PUT("api/admin/product/{product_id}/update")
    Call<ApiResponse> updateProduct(@Header("Authorization") String token,
                                    @Path("product_id") String product_id,
                                    @Part MultipartBody.Part image,
                                    @Part("data") RequestBody data);



    // Coupon API Admin
    @GET("api/admin/coupon/all")
    Call<CommonResponse<Coupon>> adminGetAllCoupon(@Header("Authorization") String token);

    @Multipart
    @POST("api/admin/coupon/add")
    Call<ApiResponse> addCoupon(@Header("Authorization") String token,
                                @Part MultipartBody.Part image,
                                @Part("data") RequestBody data);

    @GET("api/coupon/mycoupon")
    Call<CommonResponse<CouponDetail>> getMyCoupon(@Header("Authorization") String token);

    @GET("api/coupon/get")
    Call<ApiResponse> customerGetCoupon(@Header("Authorization") String token,
                                        @Query("coupon_id") String coupon_id);

    @PUT("api/admin/coupon/{coupon_id}/status")
    Call<ApiResponse> disableCoupon(@Header("Authorization") String token, @Path("coupon_id") String coupon_id, @Body Coupon coupon);

    @GET("api/review/avg/all")
    Call<CommonResponse<ReviewStar>> getAllProductStar();

    @GET("api/review/{productID}")
    Call<CommonResponse<ReviewStar>> getAvgStar(@Path("productID") String productID);
}
