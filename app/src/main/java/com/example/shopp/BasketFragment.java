package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class BasketFragment extends Fragment implements SepetAdapter.OnButtonListener {
    List<Sepet> sepets;
    SepetAdapter sepetAdapter;
    RecyclerView rcadapter;
    TextView tvfiat;
    Button btnGetir;
    double toplamfiat=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_basket, container, false);
         rcadapter=view.findViewById(R.id.rcadapter);
        sepets=new ArrayList<>();
        btnGetir=view.findViewById(R.id.button3);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("sepets");
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        sepetAdapter=new SepetAdapter(getContext(),sepets,this);
        rcadapter.setHasFixedSize(true);
        rcadapter.setItemViewCacheSize(20);
        rcadapter.setDrawingCacheEnabled(true);
        rcadapter.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rcadapter.setAdapter(sepetAdapter);
        tvfiat=view.findViewById(R.id.tvtoplamfiat);
        rcadapter.setLayoutManager(new LinearLayoutManager(getContext()));
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        Button btnSparisiTekrarla=view.findViewById(R.id.btnsparisitamamla);
        btnSparisiTekrarla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString("uid","")!="")
                {
                    if (toplamfiat!=0)
                        navController.navigate(R.id.chardFragment);
                    else{
                        Snackbar snackbar = Snackbar.make(view, "Lütfen Sepetinize Ürün Ekleyiniz", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#d00000"));
                        snackbar.show();
                    }
                }
                else
                {
                    Snackbar snackbar = Snackbar.make(view, "Lütfen Giriş Yapınız", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#d00000"));
                    snackbar.show();
                }


            }
        });
        btnGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(sharedPreferences.getString("uid","")!=""){
                        navController.navigate(R.id.sparislerimFragment);
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(view, "Lütfen Giriş Yapınız", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#d00000"));
                        snackbar.show();
                    }

            }
        });
        if (sharedPreferences.getString("uid","")!="")
        {
            databaseReference.addChildEventListener(new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        Sepet kes=snapshot.getValue(Sepet.class);
                        int findedfirst= (int) sepets.stream().filter(e->e.getId()==kes.getId()).count();
                        if (findedfirst==0)
                        {
                            if (kes.getUserid().equals(sharedPreferences.getString("uid","")))
                            {
                                sepets.add(kes);
                                ToplamHesapla();
                            }
                        }
                        sepetAdapter.notifyDataSetChanged();
                    }
                    catch (Exception ex)
                    {

                    }

                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    int index=0;
                    for (Sepet ev:sepets) {
                        Sepet kes= snapshot.getValue(Sepet.class);
                        if (ev.getId().contains(kes.getId()))
                        {
                            sepets.set(index,kes);
                            ToplamHesapla();
                        }
                        index++;
                    }
                    sepetAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Sepet kes= snapshot.getValue(Sepet.class);

                    for (int i=0;i<sepets.size();i++) {
                        if (sepets.get(i).getId().equals(kes.getId()))
                        {
                            sepets.remove(i);
                            ToplamHesapla();
                        }
                    }
                    sepetAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        return view;
    }
    public void ToplamHesapla()
    {
        DecimalFormat  decimalFormat=new DecimalFormat("0.00");
        final double[] Topla = {0};
        if (sepets.size()>0)
        {
            for (Sepet sepet : sepets) {
                DatabaseReference urunreferance=FirebaseDatabase.getInstance().getReference("uruns").child(sepet.getUrunid());
                urunreferance.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Urun urun=dataSnapshot.getValue(Urun.class);
                        if (urun!=null)
                        {
                            Topla[0] +=urun.getUrunFiyat()*sepet.getEkle();
                            toplamfiat=Topla[0];
                            tvfiat.setText(decimalFormat.format(toplamfiat) +" TL");
                        }

                    }
                });
                /*urunreferance.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Urun urun=snapshot.getValue(Urun.class);
                        Topla[0] +=urun.getUrunFiyat()*sepet.getEkle();
                        toplamfiat=Topla[0];
                        tvfiat.setText(decimalFormat.format(toplamfiat) +" TL");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
            }
        }
        else
        {
            toplamfiat=0;
            tvfiat.setText(decimalFormat.format(toplamfiat)+" TL");
        }

    };

    @Override
    public void onButtonClick(int position, String type) {
        try {
            DatabaseReference databaseReferencesepet=FirebaseDatabase.getInstance().getReference("sepets").child(sepets.get(position).getId());
            if (type.equals("sil"))
            {
                Sepet sepet= sepets.get(position);
                sepet.setEkle(sepet.getEkle()-1);
                if (sepet.getEkle()==0)
                {
                    databaseReferencesepet.removeValue();
                }
                else
                {
                    databaseReferencesepet.setValue(sepet);
                }
            }
            else if (type.equals("ekle"))
            {
                Sepet sepet= sepets.get(position);
                sepet.setEkle(sepet.getEkle()+1);

                databaseReferencesepet.setValue(sepet);
            }
            else if (type.equals("tamamensil"))
            {
                databaseReferencesepet.removeValue();
            }
        }
        catch (Exception ex)
        {

        }

    }


}