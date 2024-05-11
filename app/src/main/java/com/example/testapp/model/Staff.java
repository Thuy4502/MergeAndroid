package com.example.testapp.model;

import java.io.Serializable;

public class Staff implements Serializable {
    private int id;
    private String firstname;
    private String lastname;
    private String  phone;

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
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }
}
