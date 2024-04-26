package com.example.testapp.model.request;

import java.io.Serializable;

public class OrderRequest implements Serializable {
    private String product_id;
    private float price;
    private int quantity;
    private String size;
    private String topping;

    public OrderRequest() {
    }

    public OrderRequest(String product_id, int quantity, float price, String size, String topping) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.topping = topping;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "product_id='" + product_id + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", topping='" + topping + '\'' +
                '}';
    }
}
