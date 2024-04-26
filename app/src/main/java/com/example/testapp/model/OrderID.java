package com.example.testapp.model;

public class OrderID {
    private Long order_id;

    public OrderID() {
    }

    public OrderID(Long id) {
        this.order_id = id;
    }

    public Long getId() {
        return order_id;
    }

    public void setId(Long id) {
        this.order_id = id;
    }


}
