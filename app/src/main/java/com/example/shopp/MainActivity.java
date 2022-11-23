package com.example.shopp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        BottomNavigationView bottomvan=findViewById(R.id.bottomvan);
        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        String uid= sharedPreferences.getString("uid","");
        if (uid!="")
        {
            bottomvan.getMenu().findItem(R.id.logout).setVisible(true);
            bottomvan.getMenu().findItem(R.id.login).setVisible(false);
            DatabaseReference firebaseDatabase =FirebaseDatabase.getInstance().getReference("users").child(uid);
            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user=snapshot.getValue(User.class);
                    if (user.getId().equals(uid))
                    {
                        if (user.getUserStatus()==1 || user.getUserStatus()==2)
                        {
                            bottomvan.getMenu().findItem(R.id.urunadd).setVisible(true);
                        }
                        else
                        {
                            bottomvan.getMenu().findItem(R.id.urunadd).setVisible(false);
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
        else{
            bottomvan.getMenu().findItem(R.id.login).setVisible(true);
            bottomvan.getMenu().findItem(R.id.logout).setVisible(false);
        }

        bottomvan.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.main)
                {
                    navController.navigate(R.id.homeFragment);
                }
                else if (item.getItemId()==R.id.basket)
                {
                    navController.navigate(R.id.basketFragment);
                }
                else if (item.getItemId()==R.id.login)
                {
                    navController.navigate(R.id.loginFragment);
                }
                else if (item.getItemId()==R.id.logout)
                {
                    navController.navigate(R.id.logoutFragment);
                }
                else if (item.getItemId()==R.id.urunadd)
                {
                    navController.navigate(R.id.urunAddFragment);
                }
                return true;
            }
        });
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}