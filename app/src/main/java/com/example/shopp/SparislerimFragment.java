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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SparislerimFragment extends Fragment {
    Button btnsparislerim;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sparislerim, container, false);
        btnsparislerim =view.findViewById(R.id.button3);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        List<Satisex> satis=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("sparis");
        RecyclerView recles=view.findViewById(R.id.rcsparislerim);
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        SparislerimAdapter sparislerimAdapter=new SparislerimAdapter(satis,getContext(),navController);
        recles.setLayoutManager(new LinearLayoutManager(getContext()));
        recles.setAdapter(sparislerimAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Satisex kes=snapshot.getValue(Satisex.class);
                    int findedfirst= (int) satis.stream().filter(e->e.getId()==kes.getId()).count();
                    if (findedfirst==0)
                    {
                        if (kes.getSatisUserId().equals(sharedPreferences.getString("uid","")))
                        {
                            satis.add(kes);
                            Collections.sort(satis, (a, b) -> -a.getSparisDate().compareTo(b.getSparisDate()));
                            sparislerimAdapter.notifyDataSetChanged();


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
                for (Satisex ev:satis) {
                    Satisex kes= snapshot.getValue(Satisex.class);
                    if (ev.getId().contains(kes.getId()))
                    {
                        satis.set(index,kes);
                        sparislerimAdapter.notifyDataSetChanged();
                    }
                    index++;
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Satisex kes= snapshot.getValue(Satisex.class);

                for (int i=0;i<satis.size();i++) {
                    if (satis.get(i).getId().equals(kes.getId()))
                    {
                        satis.remove(i);
                        sparislerimAdapter.notifyDataSetChanged();
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
        return view;
    }
}