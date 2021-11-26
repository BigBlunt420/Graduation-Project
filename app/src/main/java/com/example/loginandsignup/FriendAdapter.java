package com.example.loginandsignup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FriendAdapter extends RecyclerView.Adapter<fViewHolder> implements AdapterView.OnItemSelectedListener {
    AddFriend addFriend;
    List<fModel> modelList;
    Context context;
    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    String UserID;
    String dbid, dbName, dbPhone;
    String choice;
    String Message_ID;
    boolean messageIsSent = false;


    public FriendAdapter(AddFriend addFriend, List<fModel> modelList) {
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
                //click

                //show data
//                String id = modelList.get(position).getId();
                String name = modelList.get(position).getName();
                String phone = modelList.get(position).getPhone();

                Toast.makeText(addFriend, name+"  "+phone,Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(addFriend, dbid,Toast.LENGTH_LONG).show();
                            sendMSG(dbid);
                        }
                        if (which == 1){
                            //刪除資料
                            dbid = modelList.get(position).getId();
                            deleteData(dbid);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
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
                        addFriend.showFriendList();
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


    private void sendMSG(String R_ID) {
        firebaseAuth = FirebaseAuth.getInstance();
        UserID = firebaseAuth.getCurrentUser().getUid();
        Dialog dialog = new Dialog(addFriend);
        dialog.setTitle("輸入訊息");
        dialog.setContentView(R.layout.send_message);
        dialog.show();

        EditText sentMessage = dialog.findViewById(R.id.input_message);

        Button btnSent = dialog.findViewById(R.id.sent_message);

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sentMessage.getText().toString();
                Toast.makeText(addFriend, message,Toast.LENGTH_LONG).show();

                if (TextUtils.isEmpty(message)) {
                    sentMessage.setError("欄位不得為空");
                } else {
                    messageSave(R_ID, message);
                    if(messageIsSent){
                        Toast.makeText(addFriend, "訊息已傳送", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(addFriend, "傳送失敗", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    private void messageSave(String R_ID, String message){
        Message_ID = UUID.randomUUID().toString();
        //current User's friend data
        firestoredb.collection("Users").document(R_ID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DocumentReference documentReference = firestoredb.collection("Users").document(R_ID).collection("Message").document(Message_ID);
                            Map<String,Object> SaveUserProfile = new HashMap<String, Object>();
                            SaveUserProfile.put("messageContent", message);
                            SaveUserProfile.put("messageSender", UserID);
                            Toast.makeText(addFriend, message, Toast.LENGTH_LONG).show();

                            documentReference.set(SaveUserProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        messageIsSent = true;
                                        Log.d("SaveUserProfile","Successful:User Profile is created for " + UserID);
                                    }else {
                                        messageIsSent = false;
                                        Log.w("SaveUserProfile","Fail:",task.getException());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(addFriend, "此用戶不存在!", Toast.LENGTH_LONG).show();
                        }
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
