package com.example.testapp.api;

import com.example.testapp.model.Result;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    //google map
    @GET("maps/api/directions/json")
    Single<Result> getDirection(@Query("mode") String mode, @Query("transit_routing_perferance") String perferance,
                                @Query("origin") String origin, @Query("destinaiton") String destinaiton,
                                @Query("key") String key);
}
