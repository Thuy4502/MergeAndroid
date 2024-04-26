package com.example.testapp.model;

import java.io.Serializable;
import java.util.List;

public class FullCart implements Serializable {
    private float total_price;
    private int total_quantity;

    private String created_at;
    private String updated_at;
    private int customer_id;

    private List<Cart> cart_detail;

    public FullCart() {
    }

    public FullCart(float total_price, int total_quantity, String created_at, String updated_at, int customer_id, List<Cart> cart_detail) {
        this.total_price = total_price;
        this.total_quantity = total_quantity;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.customer_id = customer_id;
        this.cart_detail = cart_detail;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public List<Cart> getCart_detail() {
        return cart_detail;
    }

    public void setCart_detail(List<Cart> cart_detail) {
        this.cart_detail = cart_detail;
    }
}
