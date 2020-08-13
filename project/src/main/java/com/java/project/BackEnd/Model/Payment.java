package com.java.project.BackEnd.Model;

public class Payment {
    private String method_payment, amount, code;
    private Long id_customer, paid;

    public Payment(String method_payment, String amount, String code, Long id_customer, Long paid) {
        this.method_payment = method_payment;
        this.amount = amount;
        this.code = code;
        this.id_customer = id_customer;
        this.paid = paid;
    }

    public Payment() {
    }

    @Override
    public String toString() {
        return "Payment{" +
                "method_payment='" + method_payment + '\'' +
                ", amount='" + amount + '\'' +
                ", code='" + code + '\'' +
                ", id_customer=" + id_customer +
                ", paid=" + paid +
                '}';
    }

    public String getMethod_payment() {
        return method_payment;
    }

    public void setMethod_payment(String method_payment) {
        this.method_payment = method_payment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
}
