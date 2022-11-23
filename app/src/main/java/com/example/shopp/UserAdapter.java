package com.example.shopp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    List<User> users;
    Context context;
    OnItemListener onButtonListener;
    public UserAdapter(List<User> users, Context context, OnItemListener onButtonListener)
    {
        this.users=users;
        this.context=context;
        this.onButtonListener=onButtonListener;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.userlayout,parent,false);
        return new UserHolder(view,onButtonListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.tvAd.setText(users.get(position).getAd()+" "+users.get(position).getSoyad());
        holder.tvemail.setText(users.get(position).getEmail());
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(users.get(position).getId());
        if (users.get(position).getUserStatus()==1)
        {
            holder.btnsatici.setText("Staıcı Yetkisini Al");
            holder.btnsatici.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else if (users.get(position).getUserStatus()==2)
        {
            holder.btnadmin.setText("Admin Yetkisini Al");
            holder.btnadmin.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        holder.btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users.get(position).getUserStatus()==2)
                {
                    users.get(position).setUserStatus(0);
                    databaseReference.setValue(users.get(position));
                    holder.btnsatici.setText("Satıcı Yetkisi Ver");
                    holder.btnadmin.setText("Admin Yetkisi Ver");
                    holder.btnadmin.setBackgroundColor(Color.parseColor("#3F51B5"));
                    holder.btnsatici.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
                else
                {
                    users.get(position).setUserStatus(2);
                    databaseReference.setValue(users.get(position));
                    holder.btnsatici.setText("Satıcı Yetkisi Ver");
                    holder.btnadmin.setBackgroundColor(Color.parseColor("#ff0000"));
                    holder.btnsatici.setBackgroundColor(Color.parseColor("#4CAF50"));
                    holder.btnadmin.setText("Admin Yetkisi Al");
                }
            }
        });
        holder.btnsatici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users.get(position).getUserStatus()==1)
                {
                    users.get(position).setUserStatus(0);
                    databaseReference.setValue(users.get(position));
                    holder.btnsatici.setText("Satıcı Yetkisi Ver");
                    holder.btnadmin.setText("Admin Yetkisi Ver");
                    holder.btnadmin.setBackgroundColor(Color.parseColor("#3F51B5"));
                    holder.btnsatici.setBackgroundColor(Color.parseColor("#4CAF50"));
                    //
                }
                else
                {
                    users.get(position).setUserStatus(1);
                    databaseReference.setValue(users.get(position));
                    holder.btnsatici.setText("Satıcı Yetkisini Al");
                    holder.btnadmin.setText("Admin Yetkisi Ver");
                    holder.btnsatici.setBackgroundColor(Color.parseColor("#ff0000"));
                    holder.btnadmin.setBackgroundColor(Color.parseColor("#3F51B5"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvAd,tvemail;
        Button btnadmin,btnsatici;
        OnItemListener onItemListener;
        public UserHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            tvAd=itemView.findViewById(R.id.tvname);
            btnadmin=itemView.findViewById(R.id.btnadmin);
            btnsatici=itemView.findViewById(R.id.btnsatici);
            tvemail=itemView.findViewById(R.id.tvemail);
            this.onItemListener=onItemListener;
        }

        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.btnadmin)
            {
                onButtonListener.onButtonClick(getAdapterPosition(),"admin");
            }
            else if (view.getId()==R.id.btnsatici)
            {
                onButtonListener.onButtonClick(getAdapterPosition(),"satici");
            }
        }
    }
    public interface OnItemListener
    {
        void onButtonClick(int position,String type);
    }
}
