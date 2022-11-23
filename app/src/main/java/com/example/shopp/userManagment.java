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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class userManagment extends Fragment implements UserAdapter.OnItemListener{
    List<User> users;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_managment, container, false);
        users=new ArrayList<>();
        RecyclerView rcusers=view.findViewById(R.id.rcusers);
        UserAdapter userAdapter=new UserAdapter(users,getContext(),this);
        rcusers.setHasFixedSize(true);
        rcusers.setItemViewCacheSize(20);
        rcusers.setDrawingCacheEnabled(true);
        rcusers.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rcusers.setAdapter(userAdapter);
        rcusers.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        ChildEventListener childUrunListener=new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user=snapshot.getValue(User.class);
                int findedfirst= (int) users.stream().filter(e->e.getId()==user.getId()).count();
                if (findedfirst==0)
                    users.add(user);
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index=0;

                for (User ev:users) {
                    User kes= snapshot.getValue(User.class);
                    if (ev.getId().contains(kes.getId()))
                    {
                        users.set(index,kes);
                    }
                    index++;
                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User kes= snapshot.getValue(User.class);

                for (int i=0;i<users.size();i++) {
                    if (users.get(i).getId().equals(kes.getId()))
                    {
                        users.remove(i);
                    }
                }
                userAdapter.notifyDataSetChanged();
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
    public void onButtonClick(int position, String type) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users").child(users.get(position).getId());

        if (type=="admin")
        {
            if (users.get(position).getUserStatus()==2)
            {
                users.get(position).setUserStatus(0);
                databaseReference.setValue(users.get(position));
            }
            else
            {
                users.get(position).setUserStatus(2);
                databaseReference.setValue(users.get(position));
            }
        }
        else if (type=="satici")
        {
            if (users.get(position).getUserStatus()==1)
            {
                users.get(position).setUserStatus(0);
                databaseReference.setValue(users.get(position));
            }
            else
            {
                users.get(position).setUserStatus(1);
                databaseReference.setValue(users.get(position));
            }
        }
    }
}