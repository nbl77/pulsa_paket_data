package com.java.project.BackEnd.Model.PaketData;

public class PaketData {
    private float data;
    private int harga;

    public PaketData(float data, int harga) {
        this.data = data;
        this.harga = harga;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
