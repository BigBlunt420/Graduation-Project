package com.example.loginandsignup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.jar.Attributes;

public class FriendListViewHolder extends RecyclerView.ViewHolder  {

    TextView Name;
    View view;

    public FriendListViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ClickListener.onItemClick(v, getAdapterPosition()); }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        Name = itemView.findViewById(R.id.friendName);
    }


    private FriendListViewHolder.ClickListener ClickListener;
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(FriendListViewHolder.ClickListener clickListener){
        ClickListener = clickListener;
    }
}
