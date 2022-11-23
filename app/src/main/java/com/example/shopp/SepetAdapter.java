package com.example.shopp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

public class SepetAdapter extends RecyclerView.Adapter<SepetAdapter.SepetView> {

    Context context;
    List<Sepet> sepets;
    BasketFragment basketFragment=new BasketFragment();
    private OnButtonListener onButtonListener;

    public SepetAdapter(Context context, List<Sepet> sepets,OnButtonListener onButtonListener)
    {
        this.context=context;
        this.sepets=sepets;
        this.onButtonListener=onButtonListener;
    }
    @NonNull
    @Override
    public SepetView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.basketlayout,parent,false);
        return new SepetView(view,onButtonListener);

    }

    @Override
    public void onBindViewHolder(@NonNull SepetView holder, int position) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("uruns").child(sepets.get(position).getUrunid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Urun urun=snapshot.getValue(Urun.class);
                    holder.tvUrunname.setText(urun.getUrunAd());
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("images/"+urun.getUrunResimId());
                    storageReference.getBytes(1920*1080).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            holder.İmgBasket.setImageBitmap(bmp);
                        }
                    });
                    holder.tvUrunadet.setText(Integer.toString(sepets.get(position).getEkle()));
                }
                catch (Exception ex)
                {

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReferencesepet=FirebaseDatabase.getInstance().getReference("sepets").child(sepets.get(position).getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Urun urun=snapshot.getValue(Urun.class);

                if (urun!=null)
                    {
                        double fiat=urun.getUrunFiyat();
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");
                        String fiatfpr= decimalFormat.format(fiat);
                        holder.tvfiat.setText(fiatfpr+" TL");
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.imgsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferencesepet.removeValue();
            }
        });
     /*   holder.btnekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sepet sepet= sepets.get(position);

                basketFragment.ToplamHesapla(sepet,true);

                sepet.setEkle(sepet.getEkle()+1);
                databaseReferencesepet.setValue(sepet);

            }
        });
        holder.btnsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sepet sepet= sepets.get(position);
                sepet.setEkle(sepet.getEkle()-1);
                BasketFragment basketFragment=new BasketFragment();

                if (sepet.getEkle()==0)
                {
                    databaseReferencesepet.removeValue();
                }
                else
                {
                    databaseReferencesepet.setValue(sepet);
                    basketFragment.ToplamHesapla(sepet,false);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return sepets.size();
    }

    public class SepetView extends RecyclerView.ViewHolder implements  View.OnClickListener {
        ImageView İmgBasket;
        TextView tvUrunname,tvUrunadet,tvfiat;
        Button btnekle;
        ImageButton imgsil;
        Button btnsil;
        OnButtonListener onButtonListener;
        public SepetView(@NonNull View itemView ,OnButtonListener onButtonListener ) {
            super(itemView);
            İmgBasket=itemView.findViewById(R.id.basketimgurun);
            tvUrunname=itemView.findViewById(R.id.tvurunname);
            tvUrunadet=itemView.findViewById(R.id.tvadet);
            btnekle=itemView.findViewById(R.id.btnarttir);
            btnsil=itemView.findViewById(R.id.btnazalt);
            imgsil=itemView.findViewById(R.id.imgsil);
            tvfiat=itemView.findViewById(R.id.tvFiat);
            this.onButtonListener=onButtonListener;
            btnekle.setOnClickListener(this);
            imgsil.setOnClickListener(this);
            btnsil.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.btnarttir)
            {
                onButtonListener.onButtonClick(getAdapterPosition(),"ekle");

            }
            else if (view.getId()==R.id.btnazalt)
            {
                onButtonListener.onButtonClick(getAdapterPosition(),"sil");

            }
            else if (view.getId()==R.id.imgsil)
            {
                onButtonListener.onButtonClick(getAdapterPosition(),"tamamensil");
            }
        }
    }
    public interface OnButtonListener
    {
        void onButtonClick(int position,String type);
    }

}
