package com.example.testapp.model;


public class ReviewDTO {
    private Long order_id;
    private String product_id, content;
    private Integer star;


    public ReviewDTO(Long order_id, String product_id, String content, Integer star) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.content = content;
        this.star = star;
    }


    public ReviewDTO() {
        super();
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }
}
