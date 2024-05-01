package com.example.testapp;

import static io.reactivex.Scheduler.*;

import android.location.Location;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.testapp.api.ApiInterface;
import com.example.testapp.model.Result;
import com.example.testapp.model.Route;
import com.google.android.gms.maps.CameraUpdate;
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

import io.reactivex.Scheduler;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_map_directions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_mapLayout);
        mapFragment.getMapAsync( this::onMapReady);

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://maps.googleapis.com/").build();
        apiInterface = retrofit.create(ApiInterface.class);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        origion = new LatLng(30.7333, 76.7794);
        dest = new LatLng(28.7041, 77.1025);
        getDirection("30.7333" + "," + "76.7794", "28.7041" + "," + "77.1025");
    }

    private void getDirection(String origin, String destination){
        apiInterface.getDirection("driving", "less_driving", origin, destination,
                getString(R.string.API_KEY)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
                
            }

            @Override
            public void onSuccess(Result result) {
                polylineList = new ArrayList<>();
                List<Route> routeList = result.getRoutes();
                for(Route route:routeList){
                    String polyline = route.getOverViewPolyline().getPoints();
                    polylineList.addAll(decodePoly(polyline));
                }
                polylineOptions = new PolylineOptions();
                polylineOptions.color(getColor(R.color.mainColor));
                polylineOptions.width(8);
                polylineOptions.startCap(new ButtCap());
                polylineOptions.jointType(JointType.ROUND);
                polylineOptions.addAll(polylineList);
                map.addPolyline(polylineOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origion);
                builder.include(dest);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
            }

            @Override
            public void onError(Throwable e) {

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