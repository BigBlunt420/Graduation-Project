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
import com.google.firebase.auth.PhoneAuthCredential;
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
                    Toast.makeText(OTP_MessageforSignUp.this,"請輸入有效電話號碼",Toast.LENGTH_SHORT).show();
                    return;
                }
                //Button消失，進度條出現
                eProgressBar.setVisibility(View.VISIBLE);
                eButtonofOTPPage.setVisibility(View.INVISIBLE);


                /*getInstance()類似於new，
                getInstance()不一定要再次创建，它可以把一个已存在的引用给你使用
                getInstance()只定義一次，但可被其他類別使用
                new 一定要生成一个新对象，分配内存*/
                //傳送驗證碼
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+886"+ePhoneNumberofOTPPage.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        OTP_MessageforSignUp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                eProgressBar.setVisibility(View.GONE);
                                eButtonofOTPPage.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                eProgressBar.setVisibility(View.GONE);
                                eButtonofOTPPage.setVisibility(View.VISIBLE);
                                Toast.makeText(OTP_MessageforSignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull @NotNull String OTPid, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                eProgressBar.setVisibility(View.GONE);
                                eButtonofOTPPage.setVisibility(View.VISIBLE);
                                /*getApplicationContext():獲取當前Activity所在的應用的Context對象
                                https://spicyboyd.blogspot.com/2018/04/appcontext.html*/
                                Intent intent =new Intent(getApplicationContext(),ConfirmOTPforSignUp.class);
                                /*putExtra:不同Activity間傳遞數據
                                https://ithelp.ithome.com.tw/articles/10232005*/
                                intent.putExtra("PhoneNumber",ePhoneNumberofOTPPage.getText().toString());
                                intent.putExtra("OTPid",OTPid);
                                startActivity(intent);
                            }
                        }
                );
            }
        });

        /*立即登入：將頁面轉至登入畫面*/
        eLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OTP_MessageforSignUp.this, LoginPage.class);
                startActivity(intent1);
            }
        });

        /*返回鍵頭：將頁面轉至首頁畫面*/
        eBackofOTPPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(OTP_MessageforSignUp.this, MainActivity.class);
                startActivity(intent2);
            }
        });
    }
}