package com.example.testapp.model;

import java.io.Serializable;

public class Cart implements Serializable {
    private int quantity;
    private int price;
    private String size;
    private Product product;

    private String topping;

    public Cart() {
    }

    private String product_id;

    public Cart(int quantity, int price, String size, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.product = product;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
