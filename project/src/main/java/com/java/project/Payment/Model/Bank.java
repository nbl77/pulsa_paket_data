package com.java.project.Payment.Model;

public class Bank {
    private Long id, balance;
    private String marketplace, norek;

    public Bank(Long id, Long balance, String marketplace, String norek) {
        this.id = id;
        this.balance = balance;
        this.marketplace = marketplace;
        this.norek = norek;
    }

    public Bank() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", balance=" + balance +
                ", marketplace='" + marketplace + '\'' +
                ", norek='" + norek + '\'' +
                '}';
    }
}
