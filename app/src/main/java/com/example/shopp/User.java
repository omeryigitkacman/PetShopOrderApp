package com.example.shopp;

public class User {
    private String Id;
    private String Ad;
    private String Soyad;
    private String Email;
    private int userStatus;
    private double cüzdan;

    public User(String id, String ad, String soyad, String email,int userStatus,double cüzdan) {
        Id = id;
        Ad = ad;
        Soyad = soyad;
        Email = email;
        this.userStatus=userStatus;
        this.cüzdan=cüzdan;
    }
    public User()
    {

    }

    public double getCüzdan() {
        return cüzdan;
    }

    public void setCüzdan(double cüzdan) {
        this.cüzdan = cüzdan;
    }

    public int getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getSoyad() {
        return Soyad;
    }

    public void setSoyad(String soyad) {
        Soyad = soyad;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
