package com.example.loginandsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpPage extends AppCompatActivity {

    private ImageView eBackofSignUpPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        eBackofSignUpPage = findViewById(R.id.BackofSignUpPage);

        eBackofSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SignUpPage.this,OTP_MessageforSignUp.class);
                startActivity(intent1);
            }
        });


    }
}