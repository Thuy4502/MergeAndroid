package com.example.testapp.model.request;

import com.google.gson.Gson;

import java.io.Serializable;

public class CartRequest implements Serializable {
    private String product_name;
    private String product_id;
    private String size;
    private String topping;



    public CartRequest() {
    }

    public CartRequest(String product_name, String product_id, String size, String topping) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.size = size;
        this.topping = topping;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
