package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddFriend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdd;
    private String uid;

    public static final String TAG = "ProfileQuote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        goMain();
    }

    private void goMain(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        Dialog dialog = new Dialog(AddFriend.this);
        dialog.setTitle("輸入用戶電話號碼");
        dialog.setContentView(R.layout.dialog_add);
        dialog.show();

//        final EditText edtID = dialog.findViewById(R.id.edtID);
        final EditText edtID = dialog.findViewById(R.id.edtID);

        Button btnSearch = dialog.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone= edtID.getText().toString();
                if(TextUtils.isEmpty(userPhone)){
                    edtID.setError("欄位不得為空");
                } else{
                    db.collection("Users").whereEqualTo("MyPhoneNumber", userPhone)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()){
                                edtID.setError("找不到用戶");
                            } else{
                                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                    String uidFriend = documentSnapshot.getId();
                                    if(uid.equals(uidFriend)){
                                        //不能加自己好友
                                        edtID.setError("錯誤號碼");
                                    }else{
                                        checkFriendExist(uidFriend);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
//        fabAdd = findViewById(R.id.fabAdd);
//        fabAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void checkFriendExist(String uidFriend) {
        db.collection("user").document(uid).collection("friend").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(!documentSnapshot.exists()){
                        DocumentReference documentReference_1 = db.collection("Users").document(uid);
                        DocumentReference documentReference_2 = db.collection("Users").document(uidFriend);
                        Map<String,Object> SaveUserProfile = new HashMap<String, Object>();
                        SaveUserProfile.put("friend", uidFriend);
                        SaveUserProfile.put("friend", uid);

                        documentReference_1.update(SaveUserProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG,"Successful:User Profile was been update");
                                }else {
                                    Log.w(TAG,"Failed:",task.getException());
                                }
                            }
                        });

                        documentReference_2.update(SaveUserProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG,"Successful:User Profile was been update");
                                }else {
                                    Log.w(TAG,"Failed:",task.getException());
                                }
                            }
                        });
                    } else{

                    }
                }
            }
        });
    }
}