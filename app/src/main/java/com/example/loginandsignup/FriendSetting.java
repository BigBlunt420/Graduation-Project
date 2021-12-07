package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FriendSetting extends AppCompatActivity {
    private Button btnBack;
    private Button editSetting;
    private Button updateSetting,cancelSetting;
    private EditText inputPeriod;
    private TextView textViewPeriod;
    private Spinner inputStatus;
    private static int period;  //須在幾分鐘內確認訊息

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_setting);

        btnBack = findViewById(R.id.btnBacktoFriendList);
        editSetting = findViewById(R.id.EditFriendSettingButton);
        textViewPeriod = findViewById(R.id.period);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //從Firestore取period的值
        uid = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            period = documentSnapshot.getLong("CheckPeriod").intValue();
                        }
                    }
                });
        textViewPeriod.setText(String.valueOf(period));

        editSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSetting();
            }
        });

        //返回上一頁,好友列表
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static int getPeriod(){
        Log.d("TAG", "getPeriod is " + period);
        return period;
    }

    private void friendSetting() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(FriendSetting.this);
        LayoutInflater inflater = LayoutInflater.from(FriendSetting.this);

        View myView = inflater.inflate(R.layout.friend_setting,null);
        builder.setView(myView);

        inputStatus = myView.findViewById(R.id.inputStatus);
        inputPeriod = myView.findViewById(R.id.inputPeriod);
        updateSetting = myView.findViewById(R.id.updateSetting);
        cancelSetting = myView.findViewById(R.id.cancelSetting);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.status
                , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputStatus.setAdapter(adapter);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();

        updateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    period = Integer.parseInt(inputPeriod.getText().toString().trim());

                    //更新至Firestore
                    updatePeriod(period);

                    Toast.makeText(FriendSetting.this, "已設定訊息須在"+period+"分鐘內確認", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                    //更新、顯示period
                    textViewPeriod.setText(String.valueOf(period));
                }
                catch (NumberFormatException e)
                {
                    // handle the exception
                    inputPeriod.setError("請輸入整數");
                }
            }
        });

        cancelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updatePeriod(int newPeriod) {
        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        DocumentReference documentReference = db.collection("Users").document(uid);
                        Map<String,Object> SaveUserProfile = new HashMap<>();
                        SaveUserProfile.put("CheckPeriod",newPeriod);

                        documentReference.set(SaveUserProfile, SetOptions.merge())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d("SaveUserProfile","Successful:User CheckPeriod is updated for " + uid);
                                        }else {
                                            Log.w("SaveUserProfile","Fail:",task.getException());
                                        }
                                    }
                                });
                    }
                });
    }
}