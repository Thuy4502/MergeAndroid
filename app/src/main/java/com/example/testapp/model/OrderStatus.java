package com.example.testapp.model;

public class OrderStatus {
    private  Integer id;
    private String name;

    public OrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public OrderStatus() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
