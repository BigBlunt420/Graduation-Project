package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPassword extends AppCompatActivity {

    private EditText eEmailofForgotPassword;
    private Button eButtonofForgotPassword;
    private ImageView eBackofForgotPassword;
    private FirebaseAuth firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        eEmailofForgotPassword = findViewById(R.id.EmailofForgotPassword);
        eButtonofForgotPassword = findViewById(R.id.ButtonofForgotPassword);
        eBackofForgotPassword = findViewById(R.id.BackofForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        //返回箭頭
        eBackofForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,LoginPage.class));
            }
        });

        //傳送確認信
        eButtonofForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String Email = eEmailofForgotPassword.getText().toString().trim();

        if(Email.isEmpty()){
            eEmailofForgotPassword.setError("此欄不得為空");
            eEmailofForgotPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            eEmailofForgotPassword.setError("請填入有效信箱");
            eEmailofForgotPassword.requestFocus();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"請至信箱確認收信並重設密碼!",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgotPassword.this,"發生錯誤，請重新嘗試！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}