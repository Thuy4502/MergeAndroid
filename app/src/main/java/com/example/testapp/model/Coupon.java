package com.example.testapp.model;

public class Coupon {
    private Long coupon_id;

    private String image;


    private String start_date;


    private String end_date;


    private String type;

    private String content;


    private int quantity;


    private int remaining_amount;


    private int minimum_value;


    private String status;


//    private LocalDateTime created_at;
//
//
//    private LocalDateTime updated_at;


    private Long created_by;


    private Long updated_by;

    public Coupon() {
        super();
    }

    public Coupon(Long coupon_id, String image, String start_date, String end_date, String type, String content, int quantity, int remaining_amount, int minimum_value, String status, Long created_by, Long updated_by) {
        this.coupon_id = coupon_id;
        this.image = image;
        this.start_date = start_date;
        this.end_date = end_date;
        this.type = type;
        this.content = content;
        this.quantity = quantity;
        this.remaining_amount = remaining_amount;
        this.minimum_value = minimum_value;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public Long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(int remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public int getMinimum_value() {
        return minimum_value;
    }

    public void setMinimum_value(int minimum_value) {
        this.minimum_value = minimum_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Long created_by) {
        this.created_by = created_by;
    }

    public Long getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Long updated_by) {
        this.updated_by = updated_by;
    }
}
