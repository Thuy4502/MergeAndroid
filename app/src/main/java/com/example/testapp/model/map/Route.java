package com.example.testapp.model.map;

import java.util.List;

public class Route {
    private OverViewPolyline overview_polyline;
    private List<Leg> legs;

    public OverViewPolyline getOverViewPolyline() {
        return overview_polyline;
    }

    public void setOverViewPolyline(OverViewPolyline overViewPolyline) {
        this.overview_polyline = overViewPolyline;
    }
}
