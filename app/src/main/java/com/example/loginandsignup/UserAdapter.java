package com.example.loginandsignup;

import android.content.Context;
import android.service.quicksettings.Tile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<ViewHolder> {
    Schedule scheduleList;
    List<Model> modelList;
    Context context;

    public UserAdapter(Schedule scheduleList, List<Model> modelList) {
        this.scheduleList = scheduleList;
        this.modelList = modelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout,parent,false);

        //處理 item click 事件
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //click

                //show data
                String title = modelList.get(position).getTile();
                String startTime = modelList.get(position).getStartTime();
                String endTime = modelList.get(position).getEndTime();
                String location = modelList.get(position).getLocation();
                String description = modelList.get(position).getDescription();
                Toast.makeText(scheduleList.getActivity(), title+"\n"+startTime+" "+endTime+" "+location+"\n"+description,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //long click
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //綁畫面和資料

        holder.Title.setText(modelList.get(position).getTile());
        holder.startTime.setText(modelList.get(position).getStartTime());
        holder.endTime.setText(modelList.get(position).getEndTime());
        holder.Location.setText(modelList.get(position).getLocation());
        holder.Description.setText(modelList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
