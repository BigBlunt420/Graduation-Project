package com.example.loginandsignup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class FriendFriendViewHolder extends RecyclerView.ViewHolder {
    TextView Title,startTime,endTime,Location,Description,Date;
    View view;

    public FriendFriendViewHolder(@NonNull @NotNull View itemView) {
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

        Title = itemView.findViewById(R.id.title);
        startTime = itemView.findViewById(R.id.starttime);
        endTime = itemView.findViewById(R.id.endtime);
        Location = itemView.findViewById(R.id.location);
        Description = itemView.findViewById(R.id.description);
        Date = itemView.findViewById(R.id.date);

    }
    private FriendFriendViewHolder.ClickListener ClickListener;
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(FriendFriendViewHolder.ClickListener clickListener){
        ClickListener = clickListener;
    }
}
