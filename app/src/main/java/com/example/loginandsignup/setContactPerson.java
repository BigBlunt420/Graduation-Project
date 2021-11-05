package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class setContactPerson extends AppCompatActivity {


    private ImageView eBackofSetContactPerson;
    private EditText eContactPersonOne,eContactPersonTwo;
    private Button efinishSignUpButton;
    private FirebaseFirestore firestoredb;
    private FirebaseAuth firebaseAuth;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contact_person);

        eBackofSetContactPerson = findViewById(R.id.BackofSetContactPerson);
        eContactPersonOne = findViewById(R.id.ContactPersonOne);
        eContactPersonTwo = findViewById(R.id.ContactPersonTwo);
        efinishSignUpButton = findViewById(R.id.finishSignUpButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();

        eBackofSetContactPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(setContactPerson.this,SignUpPage.class));
            }
        });
        efinishSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String one = eContactPersonOne.getText().toString().trim();
                String two = eContactPersonTwo.getText().toString().trim();

                if(one.isEmpty()){
                    eContactPersonOne.setError("此欄不得為空");
                    eContactPersonOne.requestFocus();
                    return;
                }
                if (one.length() != 10){
                    eContactPersonOne.setError("請填入10位手機號碼");
                    eContactPersonOne.requestFocus();
                    return;
                }
                if(two.isEmpty()){
                    eContactPersonTwo.setError("此欄不得為空");
                    eContactPersonTwo.requestFocus();
                    return;
                }
                if (two.length() != 10){
                    eContactPersonTwo.setError("請填入10位手機號碼");
                    eContactPersonTwo.requestFocus();
                    return;
                }
                UserID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firestoredb.collection("Users").document(UserID);
                Map<String,Object> SaveContactPerson = new HashMap<String, Object>();
                SaveContactPerson.put("ContactPersonOne",one);
                SaveContactPerson.put("ContactPersonTwo",two);

                documentReference.update(SaveContactPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("SaveContactPerson","Successful:User Profile was been update");
                        }else {
                            Log.w("SaveContactPerson","Failed:"+task.getException());
                        }
                    }
                });

            }
        });
    }
}