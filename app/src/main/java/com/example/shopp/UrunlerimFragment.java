package com.example.shopp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class UrunlerimFragment extends Fragment implements UrunlerimAdapter.OnItemListener{
    List<Urun> uruns;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_urunlerim, container, false);
        RecyclerView rcurunlerims=view.findViewById(R.id.rcurunlerims);
        uruns= new ArrayList<>();
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController=navHostFragment.getNavController();
        UrunlerimAdapter urunlerimAdapter=new UrunlerimAdapter(uruns,getContext(),this);
        rcurunlerims.setAdapter(urunlerimAdapter);
        rcurunlerims.setHasFixedSize(true);
        rcurunlerims.setItemViewCacheSize(20);
        rcurunlerims.setDrawingCacheEnabled(true);
        rcurunlerims.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rcurunlerims.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("uruns");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        ChildEventListener childUrunListener=new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Urun urun=snapshot.getValue(Urun.class);
                int findedfirst= (int) uruns.stream().filter(e->e.getId()==urun.getId()).count();
                if (findedfirst==0)
                {
                    if (sharedPreferences.getString("uid","").equals(urun.getUrunAuthorId()))
                    {
                        uruns.add(urun);
                    }
                }
                urunlerimAdapter.notifyDataSetChanged();

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
                urunlerimAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Urun kes= snapshot.getValue(Urun.class);

                for (int i=0;i<uruns.size();i++) {
                    if (uruns.get(i).getId().equals(kes.getId()))
                    {
                        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("sepets");
                        databaseReference1.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Sepet sepet=snapshot.getValue(Sepet.class);
                                if (sepet.getUrunid().equals(kes.getId()))
                                {
                                    databaseReference1.child(sepet.getId()).removeValue();
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        uruns.remove(i);
                    }
                }
                urunlerimAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childUrunListener);
        return view;
    }


    @Override
    public void OnItemClick(int position, String type) {
        if (type=="sil")
        {
            AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
            alert.setCancelable(false);
            alert.setTitle("Uyarı");
            alert.setMessage("Ürün Silinsin Mi ?");
            alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseDatabase.getInstance().getReference("uruns").child(uruns.get(position).getId()).removeValue();
                }
            });
            alert.create().show();
        }
        else if (type=="tiklandi")
        {
            Bundle bundle=new Bundle();
            bundle.putString("urunid",uruns.get(position).getId());
            navController.navigate(R.id.urunEditFragment,bundle);
        }
    }
}