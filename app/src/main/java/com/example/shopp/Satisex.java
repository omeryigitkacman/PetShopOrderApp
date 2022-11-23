package com.example.shopp;

import java.util.Date;

public class Satisex {
    private String Id;
    private double TotalPrice;
    private String SparisDate;
    private String SatisUserId;

    public Satisex(String id, double totalPrice, String sparisDate,String SatisUserId) {
        Id = id;
        TotalPrice = totalPrice;
        SparisDate = sparisDate;
        this.SatisUserId=SatisUserId;
    }
    public Satisex()
    {
    }

    public String getSatisUserId() {
        return SatisUserId;
    }

    public void setSatisUserId(String satisUserId) {
        SatisUserId = satisUserId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getSparisDate() {
        return SparisDate;
    }

    public void setSparisDate(String sparisDate) {
        SparisDate = sparisDate;
    }
}
