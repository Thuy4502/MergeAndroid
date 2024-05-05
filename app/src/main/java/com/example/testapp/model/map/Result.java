package com.example.testapp.model.map;

import java.util.List;

public class Result {
    private List<Routes> routes;
    private String status;

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
