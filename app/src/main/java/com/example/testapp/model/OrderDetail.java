package com.example.testapp.model;

public class OrderDetail {
    private String size;
    private Integer price;
    private Integer quantity;
    private Product product;
    private Review review;

    public OrderDetail() {
        super();
    }

    public OrderDetail(String size, Integer price, Integer quantity, Product product, Review review) {
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.review = review;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Review getReview() {
        return review;
    }
}
