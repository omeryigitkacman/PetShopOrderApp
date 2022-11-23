package com.example.shopp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.metrics.Event;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UrunAdapter extends RecyclerView.Adapter<UrunAdapter.UrunViewHolder>{

    Context context;
    private OnItemListener onItemListener;
    List<Urun> uruns;
    List<Sepet> sepets=new ArrayList<>();
    Activity activity;
    DatabaseReference databaseReferencesepet= FirebaseDatabase.getInstance().getReference("sepets");
    public interface OnItemClickListener {
        void onItemClick(Event item);
    }
    public UrunAdapter()
    {

    }
    public UrunAdapter(Context context, List<Urun> uruns, Activity activity,OnItemListener onItemListener)
    {
        this.activity=activity;
        this.context=context;
        this.uruns=uruns;
        this.onItemListener=onItemListener;
        databaseReferencesepet.addChildEventListener(new ChildEventListener() {
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
    }

    @NonNull
    @Override
    public UrunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UrunViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.urunlayout,parent,false),onItemListener);
    }
    @Override
    public void onBindViewHolder(@NonNull UrunViewHolder holder, int position) {
        try {
            holder.tvUrunFiyat.setText(String.valueOf(uruns.get(position).getUrunFiyat())+" TL");
            holder.tvUrunİsimi.setText(uruns.get(position).getUrunAd());
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageRef.child("images/"+uruns.get(position).getUrunResimId());
            final long ONE_MEGABYTE = 1920 * 1080;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    holder.imgVi.setImageBitmap(bmp);
                }
            });
            holder.btnSepeteEkle.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences= context.getSharedPreferences("user", Context.MODE_PRIVATE);
                    if (sharedPreferences.getString("uid","")!="")
                    {
                        if (sepets.size()>0)
                        {
                            Sepet sepet=sepets.stream().filter(sepet1 -> sepet1.getUserid().equals(sharedPreferences.getString("uid",""))  &&  sepet1.getUrunid().equals(uruns.get(position).getId())).findFirst().orElse(null);
                            if (sepet!=null)
                            {
                                sepet.setEkle(sepet.getEkle()+1);
                                databaseReferencesepet.child(sepet.getId()).setValue(sepet);

                                Snackbar snackbar = Snackbar.make(view, "Ürün Sepete Eklendi", Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                                snackbar.show();
                            }
                            else
                            {
                                Snackbar snackbar = Snackbar.make(view, "Ürün Sepete Eklendi", Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                                snackbar.show();
                                final String urunkey= UUID.randomUUID().toString();
                                Sepet sepet1=new Sepet(urunkey,uruns.get(position).getId(),sharedPreferences.getString("uid",""),1);
                                databaseReferencesepet.child(urunkey).setValue(sepet1);
                            }
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(view, "Ürün Sepete Eklendi", Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                            snackbar.show();
                            final String urunkey= UUID.randomUUID().toString();
                            Sepet sepet1=new Sepet(urunkey,uruns.get(position).getId(),sharedPreferences.getString("uid",""),1);
                            databaseReferencesepet.child(urunkey).setValue(sepet1);
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
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return uruns.size();
    }

    public class UrunViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgVi;
        TextView tvUrunİsimi,tvUrunFiyat;
        Button btnSepeteEkle;
        OnItemListener onItemListener;
        public UrunViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            btnSepeteEkle=itemView.findViewById(R.id.btnsepetekle);
            tvUrunİsimi= itemView.findViewById(R.id.tvurunisim);
            tvUrunFiyat= itemView.findViewById(R.id.tvurunfiyat);
            this.onItemListener= onItemListener;
            itemView.setOnClickListener(this);
            imgVi=itemView.findViewById(R.id.imgurun);
        }
        @Override
        public void onClick(View view) {
            onItemListener.OnItemClickListener(getAdapterPosition());
        }
    }
    public interface OnItemListener{

        void OnItemClickListener(int position);
    }


}
