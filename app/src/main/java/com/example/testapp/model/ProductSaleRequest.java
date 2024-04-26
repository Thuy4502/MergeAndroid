package com.example.testapp.model;

public class ProductSaleRequest {
    private String product_id;
    private String product_name;
    private long total_sold;
    private long total_quantity;
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public long getTotal_sold() {
        return total_sold;
    }
    public void setTotal_sold(long total_sold) {
        this.total_sold = total_sold;
    }
    public long getTotal_quantity() {
        return total_quantity;
    }
    public void setTotal_quantity(long total_quantity) {
        this.total_quantity = total_quantity;
    }
    public ProductSaleRequest(String product_id, String product_name, long total_sold, long total_quantity) {
        super();
        this.product_id = product_id;
        this.product_name = product_name;
        this.total_sold = total_sold;
        this.total_quantity = total_quantity;
    }
    public ProductSaleRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
}
