package com.example.loginandsignup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<fViewHolder> implements AdapterView.OnItemSelectedListener {
    AddFriend friendList;
    List<fModel> modelList;
    Context context;
    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    String UserID;
    String dbid, dbName, dbPhone;
    String choice;

    public FriendAdapter(AddFriend friendList, List<fModel> modelList) {
        this.friendList = friendList;
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
                //click

                //show data
                String id = modelList.get(position).getId();
                String name = modelList.get(position).getName();
                String phone = modelList.get(position).getPhone();
//
//                firebaseAuth = FirebaseAuth.getInstance();
//                UserID = firebaseAuth.getCurrentUser().getUid();
//                //Toast.makeText(scheduleList, dbid,Toast.LENGTH_LONG).show();
                Toast.makeText(friendList, name+"  "+phone,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //long click
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                String [] action = {"發送訊息", "刪除好友"};
                builder.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //傳訊息
                            dbid = modelList.get(position).getId();
                            dbName = modelList.get(position).getName();
                            dbPhone = modelList.get(position).getPhone();

                            sendMsg();
                        }
                        if (which == 1){
                            //刪除資料
                            dbName = modelList.get(position).getName();
                            deleteData(dbid);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }




    private void sendMsg() {

    }

    private void deleteData(String id) {
        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference doc= firestoredb.collection("Users").document(UserID).collection("Friend").document(id);
        doc.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        friendList.showFriendList();
                        Log.d("DeleteFriend","Successful:User Profile is deleted for " + UserID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w("DeleteFriend","Fail:"+e.getMessage());
                    }
                });
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull fViewHolder holder, int position) {
        //綁畫面和資料
        holder.Name.setText(modelList.get(position).getName());
        holder.Phone.setText(modelList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
