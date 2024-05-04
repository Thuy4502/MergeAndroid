package com.example.testapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;

import com.example.testapp.api.ApiInterface;
import com.example.testapp.model.map.Result;
import com.example.testapp.model.map.Route;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserMapDirectionsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private ApiInterface apiInterface;
    private List<LatLng> polylineList;
    private PolylineOptions polylineOptions;
    private LatLng origion, dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map_directions);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);


        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://maps.googleapis.com/").build();
        apiInterface = retrofit.create(ApiInterface.class);

    }
    public void testcallApi(){

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        origion = new LatLng(30.7333, 76.7794);
        dest = new LatLng(28.7041, 77.1025);
        getDirection("Montreal", "Toronto");
    }

    private void getDirection(String destination,String origin){
        apiInterface.getDirection(destination,origin,
                getString(R.string.API_KEY)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Result result) {
                //Log.d("success", result.getStatus());
                try{
                    String stt = result.getStatus();
                    System.out.println("?syy" + stt);
                }catch (ExceptionInInitializerError e){
                    System.out.println("erorr" + e.getMessage());
                }

//                polylineList = new ArrayList<>();
//                List<Route> routeList = result.getRoutes();
//                for(Route route:routeList){
//                    String polyline = route.getOverViewPolyline().getPoints();
//                    polylineList.addAll(decodePoly(polyline));
//                }
//                polylineOptions = new PolylineOptions();
//                polylineOptions.color(getColor(R.color.mainColor));
//                polylineOptions.width(8);
//                polylineOptions.startCap(new ButtCap());
//                polylineOptions.jointType(JointType.ROUND);
//                polylineOptions.addAll(polylineList);
//                map.addPolyline(polylineOptions);
//
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                builder.include(origion);
//                builder.include(dest);
//                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
            }

            @Override
            public void onError(Throwable e) {
                Log.d("error", e.getMessage());
            }
        });

    }

    private List<LatLng> decodePoly (String encoded){
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len){
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1 ):(result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1 ):(result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);

        }
        return poly;
    }
}