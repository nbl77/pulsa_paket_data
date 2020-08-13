package com.java.project.BackEnd.Model;

import java.util.Date;

public class CustomerBalance {
    private String action, type, phone_number, provider,username,paket_data,amount, balance, payment_method, code;
    private Long id_customer, paid;
    private Date active_period;

    public CustomerBalance() {
    }

    public CustomerBalance(String action, String type, String phone_number, String provider, String username, String paket_data, String amount, String balance, String payment_method, String code, Long id_customer, Long paid, Date active_period) {
        this.action = action;
        this.type = type;
        this.phone_number = phone_number;
        this.provider = provider;
        this.username = username;
        this.paket_data = paket_data;
        this.amount = amount;
        this.balance = balance;
        this.payment_method = payment_method;
        this.code = code;
        this.id_customer = id_customer;
        this.paid = paid;
        this.active_period = active_period;
    }

    @Override
    public String toString() {
        return "CustomerBalance{" +
                "action='" + action + '\'' +
                ", type='" + type + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", provider='" + provider + '\'' +
                ", username='" + username + '\'' +
                ", paket_data='" + paket_data + '\'' +
                ", amount='" + amount + '\'' +
                ", balance='" + balance + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", code='" + code + '\'' +
                ", id_customer=" + id_customer +
                ", paid=" + paid +
                ", active_period=" + active_period +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaket_data() {
        return paket_data;
    }

    public void setPaket_data(String paket_data) {
        this.paket_data = paket_data;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId_customer() {
        return id_customer;
    }

    public void setId_customer(Long id_customer) {
        this.id_customer = id_customer;
    }

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }

    public Date getActive_period() {
        return active_period;
    }

    public void setActive_period(Date active_period) {
        this.active_period = active_period;
    }
}
