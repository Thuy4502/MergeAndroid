package com.example.testapp.model.map;


public class Leg {
    private Distance distance;
    private Duration duration;
    private Location end_location;

    private  Location start_location;
    private String start_address, end_address;

    public Duration getDuration() {
        return duration;
    }

    public Location getStart_location() {
        return start_location;
    }

    public String getStart_address() {
        return start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setStart_location(Location start_location) {
        this.start_location = start_location;
    }

    public Distance getDistance() {
        return distance;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Location end_location) {
        this.end_location = end_location;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
}
