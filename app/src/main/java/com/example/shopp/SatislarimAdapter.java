package com.example.shopp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

public class SatislarimAdapter extends RecyclerView.Adapter<SatislarimAdapter.SatisHolder> {
    Context context;
    List<Satis> satis;
    public SatislarimAdapter(Context context,List<Satis> satis)
    {
        this.context=context;
        this.satis=satis;
    }
    @NonNull
    @Override
    public SatisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.satisimlayout,parent,false);
        return new SatisHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SatisHolder holder, int position) {
        DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("uruns").child(satis.get(position).getUrunId());
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        holder.tvsatisurunfiyat.setText("+ "+decimalFormat.format(satis.get(position).getPrice()*satis.get(position).getAdet())+" TL");
        holder.tvusername.setText("x"+satis.get(position).getAdet());
        firebaseDatabase.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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

    public class SatisHolder extends RecyclerView.ViewHolder
    {
        ImageView ivurunresim;
        TextView tvurunsatisname,tvusername,tvsatisurunfiyat;
        public SatisHolder(@NonNull View itemView) {
            super(itemView);
            ivurunresim=itemView.findViewById(R.id.ivurunresim);
            tvurunsatisname=itemView.findViewById(R.id.tvurunsatisname);
            tvusername=itemView.findViewById(R.id.tvusername);
            tvsatisurunfiyat=itemView.findViewById(R.id.tvsatisurunfiyat);
        }
    }
}
