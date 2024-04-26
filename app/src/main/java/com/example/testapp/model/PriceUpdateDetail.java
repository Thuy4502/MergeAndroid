package com.example.testapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PriceUpdateDetail implements Serializable {
    @SerializedName("price_new")
    private float priceNew;

    public float getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(float priceNew) {
        this.priceNew = priceNew;
    }

    // Getter và setter
}

