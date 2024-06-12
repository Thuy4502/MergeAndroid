package com.example.testapp.model;

public class UserInfo {
    private String address;
    private String birthday;
    private String cccd;
    private String email;
    private String firstname;
    private String lastname;
    private String tax_id;

    public UserInfo() {
    }

    public UserInfo(String address, String birthday, String cccd, String email, String firstname, String lastname, String tax_id) {
        this.address = address;
        this.birthday = birthday;
        this.cccd = cccd;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.tax_id = tax_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }
}
