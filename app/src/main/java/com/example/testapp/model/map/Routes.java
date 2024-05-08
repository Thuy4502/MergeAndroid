package com.example.testapp.model.map;

import java.util.List;

public class Routes {
    private OverViewPolyline overview_polyline;
    private List<Leg> legs;

    public OverViewPolyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(OverViewPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
}
