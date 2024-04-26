package com.example.testapp.model;

public class StatisticRequest {
    private int moth;
    private long total_price;
    public int getMoth() {
        return moth;
    }
    public void setMoth(int moth) {
        this.moth = moth;
    }
    public long getTotal_price() {
        return total_price;
    }
    public void setTotal_price(long total_price) {
        this.total_price = total_price;
    }
    public StatisticRequest(int moth, long total_price) {
        super();
        this.moth = moth;
        this.total_price = total_price;
    }
    public StatisticRequest() {
        super();
    }
}
