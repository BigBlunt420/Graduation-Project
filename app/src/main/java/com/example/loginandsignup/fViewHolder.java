package com.example.loginandsignup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class fViewHolder extends RecyclerView.ViewHolder {

    TextView Name, Phone, Identify;
    View view;

    public fViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        view = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClickListener.onItemLongClick(v, getAdapterPosition());
                return false;
            }
        });

        Name = itemView.findViewById(R.id.all_users_profile_full_name);
        Phone = itemView.findViewById(R.id.all_users_profile_phone_number);
        Identify = itemView.findViewById(R.id.all_users_profile_identify);

    }
    private fViewHolder.ClickListener ClickListener;
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(fViewHolder.ClickListener clickListener){
        ClickListener = clickListener;
    }
}
