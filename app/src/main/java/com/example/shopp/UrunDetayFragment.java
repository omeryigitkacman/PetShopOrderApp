package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class UrunDetayFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_urun_detay, container, false);
        String urunid=getArguments().getString("urunid");
        TextView tvurunisim2=view.findViewById(R.id.tvurunisim2);
        TextView tvurunfiyat2=view.findViewById(R.id.tvurunfiyat2);
        TextView tvurunaciklama=view.findViewById(R.id.tvurunaciklama);
        ImageView imgurunresim=view.findViewById(R.id.imgurunresim);
        Button btnsepeteekle=view.findViewById(R.id.btnsepeteekle);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("uruns").child(urunid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Urun urun=snapshot.getValue(Urun.class);
                if (urun!=null)
                {
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference("images/"+urun.getUrunResimId());
                    storageReference.getBytes(1920*1080).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            imgurunresim.setImageBitmap(bmp);
                        }
                    });
                    tvurunisim2.setText(urun.getUrunAd());
                    tvurunaciklama.setText(urun.getUrunAciklama());
                    DecimalFormat decimalFormat=new DecimalFormat("0.00");
                    tvurunfiyat2.setText(decimalFormat.format(urun.getUrunFiyat())+ " TL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference sepetref=FirebaseDatabase.getInstance().getReference("sepets");
        List<Sepet> sepets=new ArrayList<>();
        sepetref.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try
                {
                    Sepet sepet=snapshot.getValue(Sepet.class);
                    if (sepet!=null)
                    {
                        int findedfirst= (int) sepets.stream().filter(e->e.getId()==sepet.getId()).count();
                        if (findedfirst==0)
                        {
                            sepets.add(sepet);
                        }
                    }
                }
                catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
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
                    }
                    index++;
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                try {
                    Sepet kes= snapshot.getValue(Sepet.class);
                    for (int i=0;i<sepets.size();i++) {
                        if (sepets.get(i).getId().equals(kes.getId()))
                        {
                            sepets.remove(i);
                        }
                    }
                }
                catch (Exception ex)
                {

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnsepeteekle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getString("uid","")!="")
                {
                    if (sepets.size()>0)
                    {
                        Sepet sepet=sepets.stream().filter(sepet1 -> sepet1.getUserid().equals(sharedPreferences.getString("uid",""))  &&  sepet1.getUrunid().equals(urunid)).findFirst().orElse(null);
                        if (sepet!=null)
                        {
                            sepet.setEkle(sepet.getEkle()+1);
                            sepetref.child(sepet.getId()).setValue(sepet);

                            Snackbar snackbar = Snackbar.make(view, "Ürün Sepete Eklendi", Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                            snackbar.show();
                        }
                        else
                        {
                            System.out.println("Salam");
                            Snackbar snackbar = Snackbar.make(view, "Ürün Sepete Eklendi", Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                            snackbar.show();
                            final String urunkey= UUID.randomUUID().toString();
                            Sepet sepet1=new Sepet(urunkey,urunid,sharedPreferences.getString("uid",""),1);
                            sepetref.child(urunkey).setValue(sepet1);
                        }
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(view, "Ürün Sepete Eklendi", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                        snackbar.show();
                        final String urunkey= UUID.randomUUID().toString();
                        Sepet sepet1=new Sepet(urunkey,urunid,sharedPreferences.getString("uid",""),1);
                        sepetref.child(urunkey).setValue(sepet1);
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
        return view;
    }
}