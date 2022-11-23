package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SatislarimFragment extends Fragment {
    List<Satis> satis;
    RecyclerView rcles;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_satislarim, container, false);
        TextView tvCuzdan=view.findViewById(R.id.tvCuzdan);
        satis=new ArrayList<>();
        rcles=view.findViewById(R.id.rcles);
        rcles.setHasFixedSize(true);
        rcles.setItemViewCacheSize(20);
        rcles.setDrawingCacheEnabled(true);
        rcles.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        SatislarimAdapter satislarimAdapter=new SatislarimAdapter(getContext(),satis);
        rcles.setAdapter(satislarimAdapter);
        rcles.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("uid",""));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                DecimalFormat decimalFormat=new DecimalFormat("0.00");
                tvCuzdan.setText(decimalFormat.format(user.getCüzdan()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference SatisFrag=FirebaseDatabase.getInstance().getReference("satisdetay");
        SatisFrag.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Satis sepet=snapshot.getValue(Satis.class);
                    int findedfirst= (int) satis.stream().filter(e->e.getId()==sepet.getId()).count();
                    if (findedfirst==0)
                    {
                            FirebaseDatabase.getInstance().getReference("uruns").child(sepet.getUrunId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    try {
                                        Urun urun=dataSnapshot.getValue(Urun.class);
                                        if (sharedPreferences.getString("uid","").equals(urun.getUrunAuthorId())&&urun.getId().equals(sepet.getUrunId()))
                                        {
                                            satis.add(sepet);
                                            satislarimAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            });
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
                        satislarimAdapter.notifyDataSetChanged();
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
                        satislarimAdapter.notifyDataSetChanged();
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