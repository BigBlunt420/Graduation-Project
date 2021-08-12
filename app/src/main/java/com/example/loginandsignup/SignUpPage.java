package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity{

    public static final String USERNAME_KEY = "Username";
    public static final String EMAIL_KEY = "Email";
    public static final String PASSWORD_KEY = "Password";
    public static final String TAG = "ProfileQuote";
    private EditText eUsernameofSignUpPage,eEmailofSignUpPage,ePasswordofSignUpPage,eConfirmPasswordofSignUpPage;
    private Button eButtonofSignUpPage;
    private FirebaseAuth firebaseAuth ;
    private FirebaseFirestore fsdb;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        fsdb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        eUsernameofSignUpPage = findViewById(R.id.UsernameofSignUpPage);
        eEmailofSignUpPage = findViewById(R.id.EmailofSignUpPage);
        ePasswordofSignUpPage = findViewById(R.id.PasswordofSignUpPage);
        eConfirmPasswordofSignUpPage = findViewById(R.id.ConfirmPasswordofSignUpPage);
        eButtonofSignUpPage = findViewById(R.id.ButtonofSignUpPage);

        eButtonofSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile();
            }
        });
    }

    private void UserProfile() {
        String Username = eUsernameofSignUpPage.getText().toString().trim();
        String Email = eEmailofSignUpPage.getText().toString().trim();
        String Password = ePasswordofSignUpPage.getText().toString().trim();
        String ConfirmPassword =eConfirmPasswordofSignUpPage.getText().toString().trim();

        if(Username.isEmpty()){
            eUsernameofSignUpPage.setError("此欄不得為空");
            eUsernameofSignUpPage.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            eEmailofSignUpPage.setError("此欄不得為空");
            eEmailofSignUpPage.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            eEmailofSignUpPage.setError("請填入有效信箱");
            eEmailofSignUpPage.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            ePasswordofSignUpPage.setError("此欄不得為空");
            ePasswordofSignUpPage.requestFocus();
            return;
        }
        if(Password.length() < 6){
            ePasswordofSignUpPage.setError("密碼需長度為6~12字元");
            ePasswordofSignUpPage.requestFocus();
            return;
        }
        if(ConfirmPassword.isEmpty()){
            eConfirmPasswordofSignUpPage.setError("此欄不得為空");
            eConfirmPasswordofSignUpPage.requestFocus();
            return;
        }
        if(!Password.equals(ConfirmPassword)){
            eConfirmPasswordofSignUpPage.setError("請輸入相同密碼");
            eConfirmPasswordofSignUpPage.requestFocus();
            return;
        }

        //將資料寫進firestore
        UserID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fsdb.collection("Users").document(UserID);
        Map<String,Object> SaveUserProfile = new HashMap<String, Object>();
        SaveUserProfile.put(USERNAME_KEY,Username);
        SaveUserProfile.put(EMAIL_KEY,Email);
        SaveUserProfile.put(PASSWORD_KEY,Password);

        documentReference.update(SaveUserProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"Successful:User Profile was been update");
                }else {
                    Log.w(TAG,"Failed:",task.getException());
                }
            }
        });

        AuthCredential credential = EmailAuthProvider.getCredential(Email,Password);


        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("EmailLink","linkWithCredential:success");
                }else {
                    Log.w("EmailLink", "linkWithCredential:failure", task.getException());
                    Toast.makeText(SignUpPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}