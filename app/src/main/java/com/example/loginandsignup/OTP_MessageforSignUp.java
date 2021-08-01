package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class OTP_MessageforSignUp extends AppCompatActivity {

    //定義UI上的elements
    private TextView eLoginNow;
    private ImageView eBackofOTPPage;
    private EditText ePhoneNumberofOTPPage;
    private Button eButtonofOTPPage;
    private ProgressBar eProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_messagefor_sign_up);



        /* 綁定到xml中的按鈕 */
        eLoginNow = findViewById(R.id.LoginNow);
        eBackofOTPPage = findViewById(R.id.BackofOTPPage);
        ePhoneNumberofOTPPage = findViewById(R.id.PhoneNumberofOTPPage);
        eButtonofOTPPage = findViewById(R.id.ButtonofOTPPage);
        eProgressBar = findViewById(R.id.progressBar);

        /*按下取得驗證碼*/
        eButtonofOTPPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ePhoneNumberofOTPPage.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(OTP_MessageforSignUp.this,"請輸入電話號碼",Toast.LENGTH_SHORT).show();
                }else{
                    /*getApplicationContext():獲取當前Activity所在的應用的Context對象
                    https://spicyboyd.blogspot.com/2018/04/appcontext.html*/
                    Intent intent =new Intent(getApplicationContext(),ConfirmOTPforSignUp.class);
                    /*putExtra:不同Activity間傳遞數據
                    https://ithelp.ithome.com.tw/articles/10232005*/
                    intent.putExtra("PhoneNumberDisplay",ePhoneNumberofOTPPage.getText().toString());
                    intent.putExtra("PhoneNumber","+886"+ePhoneNumberofOTPPage.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}