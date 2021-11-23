package com.example.loginandsignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendScheduleAdapter extends RecyclerView.Adapter<FriendListViewHolder> {
    FriendListForSchedule friendListForSchedule;
    List<Friend> Friend;


    public FriendScheduleAdapter(FriendListForSchedule friendListForSchedule, List<com.example.loginandsignup.Friend> friend) {
        this.friendListForSchedule = friendListForSchedule;
        this.Friend = friend;
    }

    @NonNull
    @NotNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_schedule_model,parent,false);
        FriendListViewHolder friendListViewHolder = new FriendListViewHolder(itemView);
        friendListViewHolder.setOnClickListener(new FriendListViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = Friend.get(position).id;
                ActivityStarter.startActivityB(friendListForSchedule,id);
                Log.d("friendId", id);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        return friendListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendListViewHolder holder, int position) {
        holder.Name.setText(Friend.get(position).name);
    }

    @Override
    public int getItemCount() {
        return Friend.size();
    }
}
 class ActivityStarter {

    public static void startActivityB(FriendListForSchedule context,String id) {
        Intent intent = new Intent(context, FriendFriendSchedule.class);
        intent.putExtra("friendId",id);
        context.startActivity(intent);
    }
}
