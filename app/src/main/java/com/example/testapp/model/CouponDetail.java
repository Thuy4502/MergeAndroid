package com.example.testapp.model;

public class CouponDetail {
    private Long coupon_id;
    private Long customer_id;
    private Long coupon_detail_id;
    private String status;
    private Coupon coupon;

    public CouponDetail() {
    }

    public CouponDetail(Long coupon_id, Long customer_id, Long coupon_detail_id, String status, Coupon coupon) {
        this.coupon_id = coupon_id;
        this.customer_id = customer_id;
        this.coupon_detail_id = coupon_detail_id;
        this.status = status;
        this.coupon = coupon;
    }

    public Long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Long getCoupon_detail_id() {
        return coupon_detail_id;
    }

    public void setCoupon_detail_id(Long coupon_detail_id) {
        this.coupon_detail_id = coupon_detail_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
