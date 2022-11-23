package com.example.shopp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

public class UrunlerimAdapter extends RecyclerView.Adapter<UrunlerimAdapter.UrunlerimHolder>
{
    OnItemListener onItemListener;
    List<Urun> uruns;
    Context context;
    public UrunlerimAdapter(List<Urun> uruns, Context context, OnItemListener onItemListener)
    {
        this.uruns=uruns;
        this.context=context;
        this.onItemListener=onItemListener;
    }
    @NonNull
    @Override
    public UrunlerimHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.urunlerimlayout,parent,false);
        return new UrunlerimHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UrunlerimHolder holder, int position) {
        StorageReference firebaseStorage=FirebaseStorage.getInstance().getReference();
        firebaseStorage.child("images/"+uruns.get(position).getUrunResimId()).getBytes(1920*1080).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                holder.imgUrun.setImageBitmap(bmp);
            }
        });


        holder.tvurunisim.setText(uruns.get(position).getUrunAd());

    }

    @Override
    public int getItemCount() {
        return uruns.size();
    }

    public class UrunlerimHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvurunisim;
        ImageView imgUrun;
        ImageButton imgbtndelete;
        ImageButton imgbtnedit;
        OnItemListener onItemListener;

        public UrunlerimHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            tvurunisim=itemView.findViewById(R.id.tvuruns);
            imgUrun=itemView.findViewById(R.id.imgurunlerim);
            imgbtndelete=itemView.findViewById(R.id.imgbtndelete);
            imgbtnedit=itemView.findViewById(R.id.imgbtnedit);
            imgbtndelete.setOnClickListener(this);
            imgbtnedit.setOnClickListener(this);
            this.onItemListener=onItemListener;
        }

        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.imgbtndelete)
            onItemListener.OnItemClick(getAdapterPosition(),"sil");
            else if (view.getId()==R.id.imgbtnedit)
                onItemListener.OnItemClick(getAdapterPosition(),"tiklandi");
        }
    }
    public interface OnItemListener
    {
        void OnItemClick(int position,String type);
    }
}