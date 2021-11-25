package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class FriendSchedule extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    private Button mySchedule,friSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_schedule);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_toolbar);
        // 用toolbar做為APP的ActionBar
        setSupportActionBar(toolbar);
        /*--navigation drawer menu--*/
        navigationView.bringToFront();
        // 將drawerLayout和toolbar整合，會出現「三」按鈕
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //為NavigationView設置點擊事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);
                // 取得選項id
                int id = item.getItemId();

                // 依照id判斷點了哪個項目並做相應事件
                if(id == R.id.profile){
                    startActivity(new Intent(FriendSchedule.this,MyProfile.class));
                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(FriendSchedule.this,HomePage.class));
                    return true;
                } else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(FriendSchedule.this,FriendSchedule.class));
                    return true;
                }else if(id == R.id.addFriend){
                    startActivity(new Intent(FriendSchedule.this,AddFriend.class));
                    return true;
                } else if (id == R.id.signOut){
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(FriendSchedule.this, "用戶已登出", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FriendSchedule.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });

        mySchedule = findViewById(R.id.mySchedule);
        friSchedule = findViewById(R.id.friSchedule);

        mySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendSchedule.this,scheduleList.class));
            }
        });

        friSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendSchedule.this,FriendListForSchedule.class));
            }
        });


    }
}