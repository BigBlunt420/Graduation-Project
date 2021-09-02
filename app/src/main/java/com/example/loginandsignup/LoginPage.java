


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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class LoginPage extends AppCompatActivity {


    private TextView eSignUpNow,eForgetPassword;
    private EditText eEmailofLoginPage,ePasswordofLoginPage;
    private Button eButtonofLoginPage;

    private Button button;

    private FirebaseAuth firebaseAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);

        eSignUpNow = findViewById(R.id.SignUpNow);
        eEmailofLoginPage = findViewById(R.id.EmailofLoginPage);
        ePasswordofLoginPage = findViewById(R.id.PasswordofLoginPage);
        eButtonofLoginPage = findViewById(R.id.ButtonofLoginPage);
        eForgetPassword = findViewById(R.id.ForgetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this,HomePage.class);
                startActivity(intent);
            }
        });

        //立即登入
        eSignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,OTP_MessageforSignUp.class));
            }
        });

        //忘記密碼
        eForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,ForgotPassword.class));
            }
        });

        //登入
        eButtonofLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });

    }
    private void UserLogin(){
        String Email = eEmailofLoginPage.getText().toString().trim();
        String Password =ePasswordofLoginPage.getText().toString().trim();

        if(Email.isEmpty()){
            eEmailofLoginPage.setError("此欄不得為空");
            eEmailofLoginPage.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            eEmailofLoginPage.setError("請填入有效信箱");
            eEmailofLoginPage.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            ePasswordofLoginPage.setError("此欄不得為空");
            ePasswordofLoginPage.requestFocus();
            return;
        }

        if(Password.length() < 6){
            ePasswordofLoginPage.setError("密碼需長度為6~12字元");
            ePasswordofLoginPage.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //成功登入
                    startActivity(new Intent(LoginPage.this,HomePage.class));
                }else {
                    Toast.makeText(LoginPage.this,"登入失敗！無法驗證您的信箱或密碼",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}