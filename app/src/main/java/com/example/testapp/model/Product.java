package com.example.testapp.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Product implements Serializable {

        @SerializedName("product_id")
        private String productId;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("image")
        private String image;

        @SerializedName("description")
        private String description;

        private String status;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("category")
        private Category category;

        @SerializedName("staff_created")
        private Staff staffCreated;

        @SerializedName("staff_updated")
        private Staff staffUpdated;



//        @SerializedName("price_update_detail")
        private List<PriceUpdateDetail> price_update_detail;

        private boolean isAddToCart = false;

        // Getter và setter


    public Product() {
    }

    public Product(String productId, String productName, String image, String description, String createdAt, String updatedAt, Category category, Staff staffCreated, Staff staffUpdated, String status, List<PriceUpdateDetail> price_update_detail, boolean isAddToCart) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.staffCreated = staffCreated;
        this.staffUpdated = staffUpdated;
        this.status = status;
        this.price_update_detail = price_update_detail;
        this.isAddToCart = isAddToCart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Staff getStaffCreated() {
        return staffCreated;
    }

    public void setStaffCreated(Staff staffCreated) {
        this.staffCreated = staffCreated;
    }

    public Staff getStaffUpdated() {
        return staffUpdated;
    }

    public void setStaffUpdated(Staff staffUpdated) {
        this.staffUpdated = staffUpdated;
    }

    public List<PriceUpdateDetail> getPrice_update_detail() {
        return price_update_detail;
    }

    public void setPrice_update_detail(List<PriceUpdateDetail> price_update_detail) {
        this.price_update_detail = price_update_detail;
    }

    public boolean isAddToCart() {
        return isAddToCart;
    }

    public void setAddToCart(boolean addToCart) {
        isAddToCart = addToCart;
    }
}
