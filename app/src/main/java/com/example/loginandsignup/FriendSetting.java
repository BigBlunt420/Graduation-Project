package com.example.loginandsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FriendSetting extends AppCompatActivity {
    private Button btnBack;
    private Button editSetting;
    private Button updateSetting,cancelSetting;
    private EditText inputPeriod;
    private static int period = 1;  //須在幾分鐘內確認訊息,default一分鐘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_setting);

        btnBack = findViewById(R.id.btnBacktoFriendList);
        editSetting = findViewById(R.id.EditFriendSettingButton);

        editSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSetting();
            }
        });

        //返回上一頁,好友列表
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static int getPeriod(){
        return period;
    }

    private void friendSetting() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(FriendSetting.this);
        LayoutInflater inflater = LayoutInflater.from(FriendSetting.this);

        View myView = inflater.inflate(R.layout.friend_setting,null);
        builder.setView(myView);

        inputPeriod = myView.findViewById(R.id.inputPeriod);
        updateSetting = myView.findViewById(R.id.updateSetting);
        cancelSetting = myView.findViewById(R.id.cancelSetting);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();

        updateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    period = Integer.parseInt(inputPeriod.getText().toString().trim());

                    Toast.makeText(FriendSetting.this, "已設定訊息須在"+period+"分鐘內確認", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    throw new NumberFormatException("period should be integer!");
                }
                catch (NumberFormatException e)
                {
                    // handle the exception
                    inputPeriod.setError("請輸入整數");
                }
            }
        });

        cancelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}