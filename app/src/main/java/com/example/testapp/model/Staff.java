package com.example.testapp.model;

import java.io.Serializable;

public class Staff implements Serializable {
    private int id;
    private String firstName, lastName, phone;

    public String getPhone() {
        return phone;
}

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
