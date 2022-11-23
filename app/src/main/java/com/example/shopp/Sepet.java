package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sepet {
    private String id;
    private String urunid;
    private String userid;
    private int ekle;

    public Sepet(String id, String urunid, String userid, int ekle) {
        this.id = id;
        this.urunid = urunid;
        this.userid = userid;
        this.ekle = ekle;
    }
    public Sepet()
    {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrunid() {
        return urunid;
    }

    public void setUrunid(String urunid) {
        this.urunid = urunid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getEkle() {
        return ekle;
    }

    public void setEkle(int ekle) {
        this.ekle = ekle;
    }
}
