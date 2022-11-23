package com.example.shopp;

public class Satis {
    private String Id;
    private String urunId;
    private String satisId;
    private int adet;
    private double price;
    private int KargoDurumu;

    public Satis(String id, String urunId, String satisId, double price,int adet,int KargoDurumu) {
        Id = id;
        this.urunId = urunId;
        this.satisId = satisId;
        this.adet=adet;
        this.price = price;
        this.KargoDurumu=KargoDurumu;
    }
    public Satis()
    {

    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public int getKargoDurumu() {
        return KargoDurumu;
    }

    public void setKargoDurumu(int kargoDurumu) {
        KargoDurumu = kargoDurumu;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUrunId() {
        return urunId;
    }

    public void setUrunId(String urunId) {
        this.urunId = urunId;
    }

    public String getSatisId() {
        return satisId;
    }

    public void setSatisId(String satisId) {
        this.satisId = satisId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
