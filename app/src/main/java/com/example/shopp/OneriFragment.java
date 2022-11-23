package com.example.shopp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;


public class OneriFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_oneri, container, false);
        EditText tvonerisikayet=view.findViewById(R.id.tvonerisikayet);
        Button btnsikayet=view.findViewById(R.id.btnsikayet);
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        btnsikayet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvonerisikayet.getText().toString().length()!=0)
                {
                    navController.navigate(R.id.homeFragment);
                    Snackbar snackbar = Snackbar.make(view, "Öneri veya Şikayetiniz Teknik Ekibimize İletilmiştir", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#57cc99"));
                    snackbar.show();
                }
                else
                {
                    Snackbar snackbar = Snackbar.make(view, "Lütfen Öneri Veya Şikayetinizi Giriniz", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#d00000"));
                    snackbar.show();
                }

            }
        });
        return view;
    }
}