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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class ChardFragment extends Fragment {
    List<Sepet> sepets;
    List<Urun> uruns;
    List<User> users;

    EditText etKartNo,etKartYy,etKartMm,etKartCvv,etKartIsimSoyisim,etAdres;

    double ToplamFiyat=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chard, container, false);

        EditText etKartNo = view.findViewById(R.id.edcardnumber);
        EditText etKartYy = view.findViewById(R.id.etKartYy);
        EditText etKartMm = view.findViewById(R.id.etKartMm);
        EditText etKartCvv = view.findViewById(R.id.edcvv);
        EditText etKartIsimSoyisim = view.findViewById(R.id.edchardno);
        EditText etAdres = view.findViewById(R.id.etAdres);

        Button btnSparisiTamamla=view.findViewById(R.id.btnSparisiTamamla);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("sparis");
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        sepets=new ArrayList<>();
        DatabaseReference datasepet= FirebaseDatabase.getInstance().getReference("sepets");
        uruns=new ArrayList<>();
        users=new ArrayList<>();
        datasepet.addChildEventListener(new ChildEventListener() {
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
                            System.out.println(kes.getId());
                            sepets.add(kes);
                            ToplamHesapla();
                        }
                    }
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
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference dataurun= FirebaseDatabase.getInstance().getReference("uruns");
        dataurun.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Urun kes=snapshot.getValue(Urun.class);
                    int findedfirst= (int) uruns.stream().filter(e->e.getId()==kes.getId()).count();
                    if (findedfirst==0)
                    {
                        uruns.add(kes);
                    }
                }
                catch (Exception ex)
                {

                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index=0;
                for (Urun ev:uruns) {
                    Urun kes= snapshot.getValue(Urun.class);
                    if (ev.getId().contains(kes.getId()))
                    {
                        uruns.set(index,kes);
                    }
                    index++;
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Urun kes= snapshot.getValue(Urun.class);
                for (int i=0;i<uruns.size();i++) {
                    if (uruns.get(i).getId().equals(kes.getId()))
                    {
                        sepets.remove(i);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference userstor= FirebaseDatabase.getInstance().getReference("users");
        userstor.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    User kes=snapshot.getValue(User.class);
                    int findedfirst= (int) users.stream().filter(e->e.getId()==kes.getId()).count();
                    if (findedfirst==0)
                    {
                        users.add(kes);
                    }
                }
                catch (Exception ex)
                {

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index=0;
                for (User ev:users) {
                    User kes= snapshot.getValue(User.class);
                    if (ev.getId().contains(kes.getId()))
                    {
                        users.set(index,kes);
                    }
                    index++;
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User kes= snapshot.getValue(User.class);
                for (int i=0;i<users.size();i++) {
                    if (users.get(i).getId().equals(kes.getId()))
                    {
                        users.remove(i);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final double[] donus = {0};
        DatabaseReference satisdetay=FirebaseDatabase.getInstance().getReference("satisdetay");
        btnSparisiTamamla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etKartNo.getText().toString().isEmpty() &&
                        !etKartYy.getText().toString().isEmpty() &&
                        !etKartMm.getText().toString().isEmpty() &&
                        !etKartCvv.getText().toString().isEmpty() &&
                        !etKartIsimSoyisim.getText().toString().isEmpty() &&
                        !etAdres.getText().toString().isEmpty() ){
                    if (etKartNo.getText().toString().length()==16 &&
                            etKartMm.getText().toString().length()==2 &&
                            etKartYy.getText().toString().length()==2 &&
                            etKartCvv.getText().toString().length()==3 &&
                            Integer.valueOf(etKartMm.getText().toString())<=12 &&
                            Integer.valueOf(etKartMm.getText().toString())>=1){

                        String satisid= UUID.randomUUID().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                        Satisex satisex=new Satisex(satisid,ToplamFiyat,sdf.format(System.currentTimeMillis()),sharedPreferences.getString("uid",""));
                        databaseReference.child(satisid).setValue(satisex);

                        for (User user:users) {
                            double toplamcüz = 0;
                            for (Sepet sepet : sepets) {
                                {
                                    for (Urun urun : uruns) {
                                        if (urun.getId().equals(sepet.getUrunid())) {
                                            System.out.println(urun.getUrunFiyat());
                                            if (user.getId().equals(urun.getUrunAuthorId())) {
                                                String sed = UUID.randomUUID().toString();
                                                Satis satis = new Satis(sed, sepet.getUrunid(), satisid, urun.getUrunFiyat(), sepet.getEkle(), 0);
                                                satisdetay.child(sed).setValue(satis);
                                                toplamcüz +=user.getCüzdan()+ (urun.getUrunFiyat() * sepet.getEkle());
                                            }
                                            FirebaseDatabase.getInstance().getReference("sepets").child(sepet.getId()).removeValue();
                                        }
                                    }
                                }
                                if (toplamcüz != 0) {
                                    FirebaseDatabase.getInstance().getReference("users").child(user.getId()).child("cüzdan").setValue(toplamcüz);
                                }
                            }
                        }


                        Snackbar snackbar = Snackbar.make(view, "Spariş Tamamlandı", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                        snackbar.show();
                        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                        NavController navController=navHostFragment.getNavController();
                        navController.navigate(R.id.sparislerimFragment);
                    }
                    else{
                        Snackbar snackbar = Snackbar.make(view, "Kart Bilgileri Eksik ya da Yanlış!", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#FF1538"));
                        snackbar.show();

                    }
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "Bazı Alanlar Boş!", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#FF1538"));
                    snackbar.show();
                }
            }
        });
        return view;
    }
    public void ToplamHesapla()
    {
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
                            ToplamFiyat=Topla[0];
                        }

                    }
                });
            }
        }
        else
        {
            Topla[0]=0;
            ToplamFiyat=0;
        }
    };
}