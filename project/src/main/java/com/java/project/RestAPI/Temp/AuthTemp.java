package com.java.project.RestAPI.Temp;

import org.springframework.stereotype.Service;

import java.sql.Date;
@Service
public class AuthTemp {
    private String username, password, status = "logout";
    private Date active_period;
    private Long id_customer;

    public AuthTemp(String username, String password, String status, Date active_period, Long id_customer) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.active_period = active_period;
        this.id_customer = id_customer;
    }

    public AuthTemp() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getActive_period() {
        return active_period;
    }

    public void setActive_period(Date active_period) {
        this.active_period = active_period;
    }

    public Long getId_customer() {
        return id_customer;
    }

    public void setId_customer(Long id_customer) {
        this.id_customer = id_customer;
    }

    @Override
    public String toString() {
        return "AuthTemp{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", active_period=" + active_period +
                ", id_customer=" + id_customer +
                '}';
    }
}
