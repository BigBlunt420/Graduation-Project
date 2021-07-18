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

    //定義UI上的elements
    private Button eLogin;
    private Button eSignUp;
    /*
    private FirebaseAuth mAuth;
    //private FirebaseAnalytics mFirebaseAnalytics;

    //初始化firebase身份驗證
    mAuth = FirebaseAuth.getInstance();
    */



    /* onCreate是程式初始化時呼叫的
        通常情況下，我們需要在onCreate()中呼叫setContentView(int)函式填充螢幕的UI
        子類在重寫onCreate()方法的時候必須呼叫父類的onCreate()方法，即super.onCreate()，否則會丟擲異常。
        findViewById(int)返回xml中定義的檢視或元件的ID
        https://codertw.com/android-%E9%96%8B%E7%99%BC/346421/
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 綁定到xml中的按鈕 */
        eLogin= findViewById(R.id.LoginButton);
        eSignUp= findViewById(R.id.SignUpButton);

        /*
        //開啟程式時監看使用者的登入狀態
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {

                }
                else
                {

                }
            }
        }
        */


        /*Button要呼叫onClick(點擊)事件需要透過OnClickListener來監聽
          多個Button監聽寫法：https://ithelp.ithome.com.tw/articles/10242545*/
        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent代表使用者與應用程式的互動
                由原本的ainActivity轉換到下一個的Activity，使用Intent來實現
                https://litotom.com/ch5-2-intent/
                */
                Intent intent1 = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent1);
            }
        });

        eSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, OTP_MessageforSignUp.class);
                startActivity(intent2);
            }
        });
    }


}