package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化firebase身份驗證
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //開啟程式時監看使用者的登入狀態
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            //已登入過
            startActivity(new Intent(MainActivity.this,HomePage.class));
        }
        else {
            //未登入
            startActivity(new Intent(MainActivity.this,LoginPage.class));
        }
    }
}