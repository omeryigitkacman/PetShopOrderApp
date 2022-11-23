package com.example.shopp;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class UrunAddFragment extends Fragment {
    final int PICK_IMAGE = 100;
    Uri imguri;
    ImageView imageView;
    StorageReference storageReference;
    EditText edUrunName;
    EditText edUrunFiyat;
    EditText edurunacıklama;
    LinearLayout lyEklemeScreen;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (data!=null)
        {
            imguri=data.getData();
            if (resultCode == RESULT_OK ){
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }


    private void uploadimage(Uri file) {
        final String randomKey= UUID.randomUUID().toString();
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Resim Yükleniyor");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference riversRef = storageReference.child("images/"+randomKey);

        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        final String urunkey= UUID.randomUUID().toString();
                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        Urun urun=new Urun(urunkey,edUrunName.getText().toString(),Double.parseDouble(edUrunFiyat.getText().toString()) ,edurunacıklama.getText().toString(),randomKey,sharedPreferences.getString("uid",""));
                        DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("uruns").child(urunkey);
                        firebaseDatabase.setValue(urun);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double proc=(100.00 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("Yükleniyor: "+(int) proc+"%");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_urun_add, container, false);
        NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        Button btnurunguncelle=view.findViewById(R.id.btnurunguncelle);
        btnurunguncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.urunlerimFragment);
            }
        });
        imageView = view.findViewById(R.id.imgviwwduzen);
        storageReference=FirebaseStorage.getInstance().getReference();
        edUrunName=view.findViewById(R.id.edDuzenUrunIsimi);
        edUrunFiyat=view.findViewById(R.id.edDuzenUrunFiyat);
        edurunacıklama=view.findViewById(R.id.edDuzenUrunAciklama);
        Button brncre=view.findViewById(R.id.btnduzensec);
        Button btnCreate=view.findViewById(R.id.btnduzenle);

        lyEklemeScreen=view.findViewById(R.id.lyEklemeScreen);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imguri!=null)
                {
                    uploadimage(imguri);
                    new CountDownTimer(3000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            navController.navigate(R.id.homeFragment);

                            Snackbar snackBarView = Snackbar.make(lyEklemeScreen,"Ürün Eklendi!",2000);
                            snackBarView.setBackgroundTint(Color.parseColor("#57cc99"));
                            snackBarView.show();
                        }
                    }.start();



                }
            }
        });
        brncre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
        return view;
    }

}