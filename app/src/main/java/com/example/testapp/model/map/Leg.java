package com.example.testapp.model.map;

import android.location.Location;

import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

import java.util.List;

public class Leg {
    private Distance distance;
    private Duration duration;
    private String end_address;
    private Location end_location;
    private String start_address;
    private Location start_location;

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getEnd_address() {
        return end_address;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public String getStart_address() {
        return start_address;
    }

    public Location getStart_location() {
        return start_location;
    }
    //    private List<Step> steps;

}
