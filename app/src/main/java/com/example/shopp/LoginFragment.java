package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        TextView tvhesap=view.findViewById(R.id.tvhesap);
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        Button btnGiris=view.findViewById(R.id.btngiris);
        FirebaseAuth Auth=FirebaseAuth.getInstance();
        EditText edemail=view.findViewById(R.id.edloginemail);
        EditText edpassword=view.findViewById(R.id.edloginpassword);

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("uid","")!="")
        {
            navController.navigate(R.id.homeFragment);
        }
        BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottomvan);
        if (bottomNavigationView.getSelectedItemId()!=R.id.login)
        {
            bottomNavigationView.setSelectedItemId(R.id.login);
        }
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edemail.getText().toString().isEmpty() && !edpassword.getText().toString().isEmpty()){
                    sharedPreferences.edit().putString("kullanicisEmail",edemail.getText().toString()).apply();
                    sharedPreferences.edit().putString("kullanicisPassword",edpassword.getText().toString()).apply();
                    Auth.signInWithEmailAndPassword(edemail.getText().toString(),edpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                SharedPreferences.Editor sharedEditror=sharedPreferences.edit();
                                sharedEditror.putString("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                sharedEditror.commit();
                                BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottomvan);
                                bottomNavigationView.getMenu().findItem(R.id.login).setVisible(false);
                                bottomNavigationView.getMenu().findItem(R.id.logout).setVisible(true);
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("uid",""));
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user=snapshot.getValue(User.class);
                                        if (user.getUserStatus()==1 || user.getUserStatus()==2)
                                        {
                                            bottomNavigationView.getMenu().findItem(R.id.urunadd).setVisible(true);
                                        }
                                        else
                                        {
                                            bottomNavigationView.getMenu().findItem(R.id.urunadd).setVisible(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                navController.navigate(R.id.action_loginFragment_to_homeFragment);
                                Toast.makeText(getContext(),"Başarılı Bir Şekilde Giriş Yapıldı ",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Kullanıcı Adı veya Şifre yanlış",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(),"Boş Alanlar Mevcut!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvhesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
                BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottomvan);
            }
        });
        return view;
    }
}