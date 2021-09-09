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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class OTP_MessageforSignUp extends AppCompatActivity{

    //定義UI上的elements
    private TextView eLoginNow;
    private Button eButtonofOTPPage;
    private ProgressBar eProgressBar;
    private EditText ePhoneNumberofOTPPage;
    private FirebaseFirestore firestoredb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_messagefor_sign_up);


        eLoginNow = findViewById(R.id.LoginNow);
        ePhoneNumberofOTPPage = findViewById(R.id.PhoneNumberofOTPPage);
        eButtonofOTPPage = findViewById(R.id.ButtonofOTPPage);
        eProgressBar = findViewById(R.id.progressBar);


        firestoredb = FirebaseFirestore.getInstance();


        /*按下取得驗證碼*/
        eButtonofOTPPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPhoneNumber();
            }
        });

        //立即登入
        eLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTP_MessageforSignUp.this,LoginPage.class));
            }
        });
    }

    private void CheckPhoneNumber() {
        String Mobile = ePhoneNumberofOTPPage.getText().toString().trim();

        if (Mobile.isEmpty()){
            ePhoneNumberofOTPPage.setError("此欄不得為空");
            ePhoneNumberofOTPPage.requestFocus();
            return;
        }

        if (Mobile.length() != 9){
            ePhoneNumberofOTPPage.setError("請填入9位手機號碼");
            ePhoneNumberofOTPPage.requestFocus();
            return;
        }

        firestoredb.collection("Users")
                .whereEqualTo("NinePhoneNumber",Mobile)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Toast.makeText(OTP_MessageforSignUp.this,"此號碼已註冊，請立即登入!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(OTP_MessageforSignUp.this,LoginPage.class));
                    }
                });

        Intent intent =new Intent(getApplicationContext(),ConfirmOTPforSignUp.class);
        intent.putExtra("PhoneNumberDisplay",ePhoneNumberofOTPPage.getText().toString());
        intent.putExtra("PhoneNumber","+886"+ePhoneNumberofOTPPage.getText().toString());
        startActivity(intent);

    }

}