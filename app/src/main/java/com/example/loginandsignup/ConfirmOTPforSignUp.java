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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ConfirmOTPforSignUp extends AppCompatActivity {

    private TextView eUserMobile;
    private Button eButtonofConfirmOTPPage;
    private EditText eOTPfConfirmOTPPage;
    private ProgressBar eProgressBar;
    private String eOTPid;
    private TextView eResendOTP;
    private ImageView eBackofConfirmOTPPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otpfor_sign_up);

        eUserMobile = findViewById(R.id.UserMobile);
        eButtonofConfirmOTPPage = findViewById(R.id.ButtonofConfirmOTPPage);
        eProgressBar = findViewById(R.id.progressBar);
        eOTPfConfirmOTPPage = findViewById(R.id.OTPfConfirmOTPPage);
        eResendOTP = findViewById(R.id.ResendOTP);
        eBackofConfirmOTPPage = findViewById(R.id.BackofConfirmOTPPage);

        //定義驗證碼
        eOTPid = getIntent().getStringExtra("OTPid");

        //使用者輸入的電話
        eUserMobile.setText(String.format(
                "%s",getIntent().getStringExtra("PhoneNumber")
        ));

        //驗證
        eButtonofConfirmOTPPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eOTPfConfirmOTPPage.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ConfirmOTPforSignUp.this,"請輸入有效驗證碼",Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = eOTPfConfirmOTPPage.getText().toString().trim();

                if(eOTPid != null)
                {
                    eProgressBar.setVisibility(View.VISIBLE);
                    eButtonofConfirmOTPPage.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            eOTPid,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    eProgressBar.setVisibility(View.GONE);
                                    eButtonofConfirmOTPPage .setVisibility(View.VISIBLE);
                                    if(task.isSuccessful())
                                    {
                                        Intent intent = new Intent(getApplicationContext(),SignUpPage.class);
                                        /*setFlags用法
                                        FLAG_ACTIVITY_CLEAR_TASK和FLAG_ACTIVITY_NEW_TASK通常連用
                                        http://dbhills.blogspot.com/2015/01/androidactivity.html*/
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(ConfirmOTPforSignUp.this,"驗證碼錯誤",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        
        //再次傳送驗證碼
        eResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+886"+getIntent().getStringExtra("PhoneNumber"),
                        60,
                        TimeUnit.SECONDS,
                         ConfirmOTPforSignUp .this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                Toast.makeText(ConfirmOTPforSignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull @NotNull String NewOTPid, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                eOTPid = NewOTPid;
                                Toast.makeText(ConfirmOTPforSignUp.this,"驗證碼已再次發送",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

        eBackofConfirmOTPPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ConfirmOTPforSignUp.this,OTP_MessageforSignUp.class);
                startActivity(intent1);
            }
        });
    }
}