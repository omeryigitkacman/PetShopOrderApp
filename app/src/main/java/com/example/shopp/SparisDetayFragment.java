package com.example.shopp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SparisDetayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_sparis_detay, container, false);
        RecyclerView rcdesk=view.findViewById(R.id.rcdesk);
        rcdesk.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Satis> satis=new ArrayList<>();
        SparisDetayAdapter sparisDetayFragment=new SparisDetayAdapter(satis,getContext());
        rcdesk.setAdapter(sparisDetayFragment);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("satisdetay");
        String satisid= getArguments().getString("satisid","");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Satis sepet=snapshot.getValue(Satis.class);
                    int findedfirst= (int) satis.stream().filter(e->e.getId()==sepet.getId()).count();
                    if (findedfirst==0)
                    {
                        if (sepet.getSatisId().equals(satisid))
                        {
                            satis.add(sepet);
                            sparisDetayFragment.notifyDataSetChanged();
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

                for (Satis ev:satis) {
                    Satis kes= snapshot.getValue(Satis.class);
                    if (ev.getId().contains(kes.getId()))
                    {
                        satis.set(index,kes);
                        sparisDetayFragment.notifyDataSetChanged();
                    }
                    index++;
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Satis kes= snapshot.getValue(Satis.class);

                for (int i=0;i<satis.size();i++) {
                    if (satis.get(i).getId().equals(kes.getId()))
                    {
                        satis.remove(i);
                        sparisDetayFragment.notifyDataSetChanged();
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