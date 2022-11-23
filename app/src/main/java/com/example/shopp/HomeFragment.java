package com.example.shopp;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UrunAdapter.OnItemListener {

    static UrunAdapter homeRecyclerViewAdapter;
    List<Urun> uruns;
    RecyclerView recyclerView;
    public List<Sepet> sepets;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencesepet;
    NavController navController;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home, container, false);
        uruns=new ArrayList<>();
        sepets=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("uruns");
        databaseReferencesepet= FirebaseDatabase.getInstance().getReference("sepets");
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController=navHostFragment.getNavController();

        ChildEventListener childUrunListener=new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Urun urun=snapshot.getValue(Urun.class);
                int findedfirst= (int) uruns.stream().filter(e->e.getId()==urun.getId()).count();
                if (findedfirst==0)
                    uruns.add(urun);
                homeRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index=0;

                for (Urun ev:uruns) {
                    Urun kes= snapshot.getValue(Urun.class);
                    if (ev.getId().contains(kes.getId()))
                    {
                        uruns.set(index,kes);
                    }
                    index++;
                }
                homeRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Urun kes= snapshot.getValue(Urun.class);

                for (int i=0;i<uruns.size();i++) {
                    if (uruns.get(i).getId().equals(kes.getId()))
                    {
                        uruns.remove(i);
                    }
                }
                homeRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childUrunListener);
        homeRecyclerViewAdapter=new UrunAdapter(getContext(),uruns,getActivity(),this);
        BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottomvan);
        recyclerView=view.findViewById(R.id.rcuruns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(homeRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            if (bottomNavigationView!=null)
            {
                if (bottomNavigationView.getSelectedItemId()!=R.id.main)
                {
                    bottomNavigationView.setSelectedItemId(R.id.main);
                }
            }

        databaseReferencesepet.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Sepet sepet=snapshot.getValue(Sepet.class);
                    int findedfirst= (int) sepets.stream().filter(e->e.getId()==sepet.getId()).count();
                    if (findedfirst==0)
                    {
                        sepets.add(sepet);
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
                Sepet kes= snapshot.getValue(Sepet.class);

                for (int i=0;i<sepets.size();i++) {
                    if (sepets.get(i).getId().equals(kes.getId()))
                    {
                        sepets.remove(i);
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

    @Override
    public void OnItemClickListener(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("urunid",uruns.get(position).getId());
        navController.navigate(R.id.urunDetayFragment,bundle);
    }
}