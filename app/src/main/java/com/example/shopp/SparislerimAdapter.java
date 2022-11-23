package com.example.shopp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class SparislerimAdapter extends RecyclerView.Adapter<SparislerimAdapter.SparisHolder> {
    List<Satisex> satis;
    Context context;
    NavController navController;
    public SparislerimAdapter(List<Satisex> satis, Context context,NavController navController) {
        this.satis = satis;
        this.context = context;
        this.navController=navController;
    }

    @NonNull
    @Override
    public SparisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.sparislayout,parent,false);

        return new SparisHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SparisHolder holder, int position) {
        holder.tvsparisname.setText(satis.get(position).getSparisDate()+" Tarihinde Yapılan Spariş");
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        holder.tvsparisfiat.setText(decimalFormat.format(satis.get(position).getTotalPrice())+" TL");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("satisid",satis.get(position).getId());
                navController.navigate(R.id.sparisDetayFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return satis.size();
    }

    public class SparisHolder extends RecyclerView.ViewHolder{

        TextView tvsparisname,tvsparisfiat,tvdeskt;
        public SparisHolder(@NonNull View itemView) {
            super(itemView);
            tvdeskt=itemView.findViewById(R.id.tvdeskt);
            tvsparisfiat=itemView.findViewById(R.id.tvsparisfiat);
            tvsparisname=itemView.findViewById(R.id.tvsparisname);
        }
    }
}
