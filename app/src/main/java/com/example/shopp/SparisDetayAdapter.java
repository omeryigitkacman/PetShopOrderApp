package com.example.shopp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.telephony.ims.RcsUceAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

public class SparisDetayAdapter extends RecyclerView.Adapter<SparisDetayAdapter.SparisDetayHolder> {
    List<Satis> satis;
    Context context;

    public SparisDetayAdapter(List<Satis> satis, Context context) {
        this.satis = satis;
        this.context = context;
    }

    @NonNull
    @Override
    public SparisDetayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.satisimlayout,parent,false);
        return new SparisDetayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SparisDetayHolder holder, int position) {
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        holder.tvsatisurunfiyat.setText(decimalFormat.format(satis.get(position).getPrice()*satis.get(position).getAdet())+" TL");
        FirebaseDatabase.getInstance().getReference("uruns").child(satis.get(position).getUrunId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Urun urun=dataSnapshot.getValue(Urun.class);
                holder.tvurunsatisname.setText(urun.getUrunAd());
                FirebaseStorage.getInstance().getReference("images/"+urun.getUrunResimId()).getBytes(1920*1080).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        holder.ivurunresim.setImageBitmap(bmp);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return satis.size();
    }

    public class SparisDetayHolder extends RecyclerView.ViewHolder{

        ImageView ivurunresim;
        TextView tvurunsatisname,tvusername,tvsatisurunfiyat;
        public SparisDetayHolder(@NonNull View itemView) {
            super(itemView);
            ivurunresim=itemView.findViewById(R.id.ivurunresim);
            tvurunsatisname=itemView.findViewById(R.id.tvurunsatisname);
            tvusername=itemView.findViewById(R.id.tvusername);
            tvsatisurunfiyat=itemView.findViewById(R.id.tvsatisurunfiyat);
        }
    }
}
