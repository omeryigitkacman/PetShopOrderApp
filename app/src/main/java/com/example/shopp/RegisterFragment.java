package com.example.shopp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.bottomvan);
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        bottomNavigationView.setSelectedItemId(R.id.loginFragment);
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("uid","")!="")
        {
            navController.navigate(R.id.homeFragment);
        }
        if (bottomNavigationView.getSelectedItemId()!=R.id.login)
        {
            bottomNavigationView.setSelectedItemId(R.id.login);
        }
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        TextView tvhesap=view.findViewById(R.id.tvhesapolustur);
        EditText edAd=view.findViewById(R.id.edad);
        EditText edsoyad=view.findViewById(R.id.edsoyad);
        EditText edmail=view.findViewById(R.id.edmail);
        EditText edsifre=view.findViewById(R.id.edsifre);
        Button btnOlustur=view.findViewById(R.id.btnregister);
        tvhesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        btnOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edAd.getText().toString().isEmpty())
                {
                    edAd.setError("Lütfen Adınızı Giriniz");
                    return;
                }
                if (edsoyad.getText().toString().isEmpty())
                {
                    edsoyad.setError("Lütfen Soyadınızı Giriniz");
                    return;
                }
                if (edsifre.getText().toString().isEmpty())
                {
                    edsoyad.setError("Lütfen Şifrenizi Giriniz");
                    return;
                }
                if (edmail.getText().toString().isEmpty())
                {
                    edsoyad.setError("Lütfen Şifrenizi Giriniz");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(edmail.getText().toString()).matches())
                {
                    edmail.setError("Bu Bir E-mail Değil");
                    return;
                }
                if (edsifre.getText().toString().trim().length()<6) {
                    edsifre.setError("Şifrenin Uzunluğu 6 karakter olmalıdır");
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(edmail.getText().toString(),edsifre.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                           User user=new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),edAd.getText().toString(),edsoyad.getText().toString(),edmail.getText().toString(),0,0);
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getContext(),"Kullanıcı Balaşarılı Bir Şekilde Oluşturuldu",Toast.LENGTH_SHORT).show();
                                        navController.navigate(R.id.loginFragment);
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(),"Hata",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(getContext(),"Hesap Oluşturulurken Bir Hata Oluştu",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}