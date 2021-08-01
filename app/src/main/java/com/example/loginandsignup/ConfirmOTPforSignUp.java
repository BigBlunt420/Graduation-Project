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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
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
    private String mVerificationId;
    private String ePhone;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otpfor_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        eUserMobile = findViewById(R.id.UserMobile);
        eButtonofConfirmOTPPage = findViewById(R.id.ButtonofConfirmOTPPage);
        eProgressBar = findViewById(R.id.progressBar);
        eOTPfConfirmOTPPage = findViewById(R.id.OTPfConfirmOTPPage);
        eResendOTP = findViewById(R.id.ResendOTP);
        eBackofConfirmOTPPage = findViewById(R.id.BackofConfirmOTPPage);
        ePhone = getIntent().getStringExtra("PhoneNumber");

        //定義驗證碼
        eOTPid = getIntent().getStringExtra("OTPid");

        //使用者輸入的電話
        eUserMobile.setText(String.format(
                "%s",getIntent().getStringExtra("PhoneNumberDisplay")
        ));

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                //will be invoked when verification was completed
                singInWithPhoneAuthCredential(phoneAuthCredential);
                //jump to home page

            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                //this call back is invoked in an invalid request for varification
                Toast.makeText(ConfirmOTPforSignUp.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String OTPid, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken Token) {
                //call back for otp was sent
                Toast.makeText(ConfirmOTPforSignUp.this,"驗證碼已傳送",Toast.LENGTH_SHORT).show();
                mVerificationId = OTPid;
                forceResendingToken = Token;
            }
        };

        startPhoneNumberVerification(ePhone);
        //驗證
        eButtonofConfirmOTPPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eOTPfConfirmOTPPage.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ConfirmOTPforSignUp.this,"請輸入有效驗證碼",Toast.LENGTH_SHORT).show();
                }else{
                    verifyPhoneNumberWithCodes(mVerificationId, eOTPfConfirmOTPPage.getText().toString().trim());
                }
            }
        });

        //重新傳送驗證碼
        eResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode(ePhone, forceResendingToken);
            }
        });
        

    }

    private void startPhoneNumberVerification(String ePhone) {
        Toast.makeText(ConfirmOTPforSignUp.this,"驗證碼傳送中",Toast.LENGTH_SHORT).show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(ePhone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCodes(String verificationId, String code) {
        Toast.makeText(ConfirmOTPforSignUp.this,"正在驗證驗證碼",Toast.LENGTH_SHORT).show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        singInWithPhoneAuthCredential(credential);
    }

    private void resendCode(String ePhone, PhoneAuthProvider.ForceResendingToken token) {
        Toast.makeText(ConfirmOTPforSignUp.this,"驗證碼傳送中",Toast.LENGTH_SHORT).show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(ePhone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .setForceResendingToken(token)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void singInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Toast.makeText(ConfirmOTPforSignUp.this,"登入中",Toast.LENGTH_SHORT).show();

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(ConfirmOTPforSignUp.this,"已登入",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ConfirmOTPforSignUp.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}