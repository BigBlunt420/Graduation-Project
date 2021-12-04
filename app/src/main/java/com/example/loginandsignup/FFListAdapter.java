package com.example.loginandsignup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FFListAdapter extends RecyclerView.Adapter<fViewHolder> implements AdapterView.OnItemSelectedListener {
    AddFriend addFriend;
    List<fModel> modelList;

    public FFListAdapter(AddFriend addFriend, List<fModel> modelList) {
        this.addFriend = addFriend;
        this.modelList = modelList;
    }

    @NonNull
    @NotNull
    @Override
    public fViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType){
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout,parent,false);

        //處理 item click 事件
        fViewHolder viewHolder = new fViewHolder(itemView);
        viewHolder.setOnClickListener(new fViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        return viewHolder;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull fViewHolder holder, int position) {
        //綁畫面和資料
        holder.Name.setText(modelList.get(position).getName());
        holder.Phone.setText(modelList.get(position).getPhone());
        holder.Identify.setText(modelList.get(position).getIdentify());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
