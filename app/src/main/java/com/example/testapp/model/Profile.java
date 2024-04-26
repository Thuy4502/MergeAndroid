package com.example.testapp.model;

public class Profile {
    private User user;
    private Customer customer;
    public Profile(User user, Customer customer) {
        this.user = user;
        this.customer = customer;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
