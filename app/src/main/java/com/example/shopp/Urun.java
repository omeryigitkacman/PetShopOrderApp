package com.example.shopp;

public class Urun {
    private String  Id;
    private String  UrunAd;
    private double  UrunFiyat;
    private String  UrunAciklama;
    private String  UrunResimId;
    private String  UrunAuthorId;

    public Urun(String id, String urunAd, double urunFiyat, String urunAciklama, String urunResimId,String UrunAuthorId) {
        Id = id;
        UrunAd = urunAd;
        UrunFiyat = urunFiyat;
        UrunAciklama = urunAciklama;
        UrunResimId = urunResimId;
        this.UrunAuthorId=UrunAuthorId;
    }
    public Urun(){

    }

    public String getUrunAuthorId() {
        return UrunAuthorId;
    }

    public void setUrunAuthorId(String urunAuthorId) {
        UrunAuthorId = urunAuthorId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUrunAd() {
        return UrunAd;
    }

    public void setUrunAd(String urunAd) {
        UrunAd = urunAd;
    }

    public double getUrunFiyat() {
        return UrunFiyat;
    }

    public void setUrunFiyat(double urunFiyat) {
        UrunFiyat = urunFiyat;
    }

    public String getUrunAciklama() {
        return UrunAciklama;
    }

    public void setUrunAciklama(String urunAciklama) {
        UrunAciklama = urunAciklama;
    }

    public String getUrunResimId() {
        return UrunResimId;
    }

    public void setUrunResimId(String urunResimId) {
        UrunResimId = urunResimId;
    }
}
