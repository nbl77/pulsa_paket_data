package com.java.project.BackEnd.Model;


import java.util.Date;

public class Customer {
    private Long id_customer;
    private String fullname, username, password;

    public Customer(Long id_customer, String fullname, String username, String password) {
        this.id_customer = id_customer;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }

    public Customer() {
    }

    public Long getId_customer() {
        return id_customer;
    }

    public void setId_customer(Long id_customer) {
        this.id_customer = id_customer;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
