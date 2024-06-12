package com.example.testapp.model;

public class ReviewStar {
    private String product_id;
    private float star;
    private int count;

    public ReviewStar() {
    }

    public ReviewStar(String product_id, float star, int count) {
        this.product_id = product_id;
        this.star = star;
        this.count = count;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
