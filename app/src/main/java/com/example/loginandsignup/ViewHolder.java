package com.example.loginandsignup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView Title,startTime,endTime,Location,Description;
    View view;

    public ViewHolder(@NonNull @NotNull View itemView) {
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

        Title = itemView.findViewById(R.id.tile);
        startTime = itemView.findViewById(R.id.starttime);
        endTime = itemView.findViewById(R.id.endtime);
        Location = itemView.findViewById(R.id.location);
        Description = itemView.findViewById(R.id.description);

    }
    private ViewHolder.ClickListener ClickListener;
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        ClickListener = clickListener;
    }
}
