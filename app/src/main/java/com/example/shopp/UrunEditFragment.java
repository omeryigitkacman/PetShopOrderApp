package com.example.shopp;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UrunEditFragment extends Fragment {
    ImageView imgviwwduzen;
    Urun urun;
    final int PICK_IMAGE = 100;
    Uri imguri=null;
    StorageReference storageReference;
    EditText edDuzenUrunIsimi;
    EditText edDuzenUrunFiyat;
    EditText edDuzenUrunAciklama;
    LinearLayout lyDuzenlemesScreen;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (data!=null)
        {
            imguri=data.getData();
            if (resultCode == RESULT_OK ){
                imageUri = data.getData();
                imgviwwduzen.setImageURI(imageUri);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_urun_edit, container, false);
        storageReference=FirebaseStorage.getInstance().getReference();
        lyDuzenlemesScreen=view.findViewById(R.id.lyDuzenlemesScreen);
        imgviwwduzen=view.findViewById(R.id.imgviwwduzen);
        edDuzenUrunIsimi=view.findViewById(R.id.edDuzenUrunIsimi);
        edDuzenUrunFiyat=view.findViewById(R.id.edDuzenUrunFiyat);
        edDuzenUrunAciklama=view.findViewById(R.id.edDuzenUrunAciklama);
        Button btnduzensec=view.findViewById(R.id.btnduzensec);
        btnduzensec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
        Button btnduzenle=view.findViewById(R.id.btnduzenle);
        btnduzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrunChange();
                NavHostFragment navHostFragment=(NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                NavController navController=navHostFragment.getNavController();
                navController.navigate(R.id.homeFragment);

                Snackbar snackBarView = Snackbar.make(lyDuzenlemesScreen,"Ürün Değiştirildi!",2000);
                snackBarView.setBackgroundTint(Color.parseColor("#57cc99"));
                snackBarView.show();
            }
        });
        String urunid=getArguments().getString("urunid");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("uruns").child(urunid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                urun=snapshot.getValue(Urun.class);
                if (urun!=null)
                {
                    edDuzenUrunIsimi.setText(urun.getUrunAd());
                    edDuzenUrunFiyat.setText(String.valueOf(urun.getUrunFiyat()));
                    edDuzenUrunAciklama.setText(urun.getUrunAciklama());
                    storageReference.child("images/"+urun.getUrunResimId()).getBytes(1920*1080).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            imgviwwduzen.setImageBitmap(bmp);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void UrunChange()
    {
        urun.setUrunAd(edDuzenUrunIsimi.getText().toString());
        urun.setUrunAciklama(edDuzenUrunAciklama.getText().toString());
        urun.setUrunFiyat(Double.parseDouble(edDuzenUrunFiyat.getText().toString()));
        if (imguri!=null)
        {
            final String randomKey= UUID.randomUUID().toString();
            final ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setTitle("Resim Yükleniyor");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference riversRef = storageReference.child("images/"+randomKey);
            riversRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    System.out.println("Sellll");
                    urun.setUrunResimId(randomKey);
                    FirebaseDatabase.getInstance().getReference("uruns").child(urun.getId()).setValue(urun);
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
        else
        {
            FirebaseDatabase.getInstance().getReference("uruns").child(urun.getId()).setValue(urun);
        }
    }

}