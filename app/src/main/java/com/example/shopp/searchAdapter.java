package com.example.shopp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.SearchHolder> {
    List<Urun> uruns;
    Context context;
    public searchAdapter(List<Urun> uruns,Context context)
    {
        this.uruns=uruns;
        this.context=context;
    }
    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SearchHolder extends RecyclerView.ViewHolder{

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
