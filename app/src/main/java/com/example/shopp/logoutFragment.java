package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class logoutFragment extends Fragment {


    boolean kbIcontrol;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_logout, container, false);
        TextView tvKullanicisEmailFT = view.findViewById(R.id.tvKullanicisEmailFT);
        TextView tvKullanicisSifreFT = view.findViewById(R.id.tvKullanicisSifreFT);
        Button btncikis=view.findViewById(R.id.btncisiks);
        Button btnkullaniciyonet=view.findViewById(R.id.btnKullanicilariYonet);
        Button btnsatislarim=view.findViewById(R.id.btnsatislarim);
        Button btnSikayet=view.findViewById(R.id.btnonerisikeyet);
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
/*
                * sharedPreferences.edit().putString("kullanicisEmail",edemail.getText().toString()).apply();
                    sharedPreferences.edit().putString("kullanicisPassword",edpassword.getText().toString()).apply();*/

        tvKullanicisEmailFT.setText("Email:  "+sharedPreferences.getString("kullanicisEmail",""));
        tvKullanicisSifreFT.setText("Şifre:  "+sharedPreferences.getString("kullanicisPassword",""));

        LinearLayout lyKullaniciInfo =view.findViewById(R.id.lyKullaniciInfo);
        Button btnKullaniciInfo =view.findViewById(R.id.btnKullaniciInfo);
        kbIcontrol = false;


        btnKullaniciInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (kbIcontrol==false){
                    btnKullaniciInfo.setText("Kullanıcı Bilgilerimi Kapat");
                    lyKullaniciInfo.setVisibility(View.VISIBLE);
                    kbIcontrol=true;
                }
                else{
                    btnKullaniciInfo.setText("Kullanıcı Bilgilerimi Görüntüle");
                    lyKullaniciInfo.setVisibility(View.GONE);
                    kbIcontrol=false;
                }

            }
        });



        BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottomvan);
        if (bottomNavigationView.getSelectedItemId()!=R.id.logout)
        {
            bottomNavigationView.setSelectedItemId(R.id.logout);
        }
        if (sharedPreferences.getString("uid","")=="")
        {
            navController.navigate(R.id.homeFragment);
        }

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("uid",""));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if (user!=null)
                {
                    if (user.getUserStatus()==2)
                    {
                        btnkullaniciyonet.setVisibility(View.VISIBLE);
                        btnkullaniciyonet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                navController.navigate(R.id.userManagment);
                            }
                        });
                    }
                    else
                    {
                        btnkullaniciyonet.setVisibility(View.GONE);
                    }
                    if (user.getUserStatus()==2 || user.getUserStatus()==1)
                    {
                        btnsatislarim.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        btnsatislarim.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnsatislarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.satislarimFragment);
            }
        });
        btncikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().commit();
                Toast.makeText(getContext(),"Çıkış Yapıldı",Toast.LENGTH_SHORT).show();
                bottomNavigationView.getMenu().findItem(R.id.login).setVisible(true);
                bottomNavigationView.getMenu().findItem(R.id.logout).setVisible(false);
                bottomNavigationView.getMenu().findItem(R.id.urunadd).setVisible(false);
                bottomNavigationView.setSelectedItemId(R.id.homeFragment);
                navController.navigate(R.id.action_logoutFragment_to_homeFragment);
            }
        });
        btnSikayet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.oneriFragment);
            }
        });
        return view;
    }
}