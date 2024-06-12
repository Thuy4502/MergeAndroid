package com.example.testapp.model;

public class Review {
    private Long review_id;

    private int star;

    private String content;

    private String status;

    private String created_at;

    private String updated_at;

    private Long order_detail_id;

    private String product_id;
    private Long created_by;
    private float point_review;

    public Review() {
        super();
    }

    public Review(Long review_id, int star, String content, String status, String created_at,
                  String updated_at, Long order_detail_id, String product_id, Long created_by,
                  float point_review) {
        this.review_id = review_id;
        this.star = star;
        this.content = content;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.order_detail_id = order_detail_id;
        this.product_id = product_id;
        this.created_by = created_by;
        this.point_review = point_review;
    }

    public Long getReview_id() {
        return review_id;
    }

    public void setReview_id(Long review_id) {
        this.review_id = review_id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(Long order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Long created_by) {
        this.created_by = created_by;
    }

    public float getPoint_review() {
        return point_review;
    }

    public void setPoint_review(float point_review) {
        this.point_review = point_review;
    }
}
