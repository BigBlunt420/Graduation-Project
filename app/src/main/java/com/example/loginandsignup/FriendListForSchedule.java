package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class Friend {
    String id,name;

    Friend(String id,String name) {
        this.name = name;
        this.id = id;
    }
}

public class FriendListForSchedule extends AppCompatActivity {
    /*--navigation drawer menu--*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    /*--navigation drawer menu--*/

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog;
    FloatingActionButton refresh;
    String UserID;
    FriendScheduleAdapter adapter;

    private List<Friend> Friend= new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_for_schedule);
        /*--navigation drawer menu--*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_toolbar);
        // ???toolbar??????APP???ActionBar
        setSupportActionBar(toolbar);
        /*--navigation drawer menu--*/
        navigationView.bringToFront();
        // ???drawerLayout???toolbar?????????????????????????????????
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //???NavigationView??????????????????
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                // ?????????????????????
                drawerLayout.closeDrawer(GravityCompat.START);
                // ????????????id
                int id = item.getItemId();

                // ??????id??????????????????????????????????????????
                if(id == R.id.profile){
                    startActivity(new Intent(FriendListForSchedule.this,MyProfile.class));
                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(FriendListForSchedule.this,HomePage.class));
                    return true;
                } else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(FriendListForSchedule.this,FriendSchedule.class));
                    return true;
                }else if(id == R.id.addFriend){
                    startActivity(new Intent(FriendListForSchedule.this,AddFriend.class));
                    return true;
                } else if (id == R.id.signOut){
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(FriendListForSchedule.this, "???????????????", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FriendListForSchedule.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });
        /*--navigation drawer menu--*/



        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(FriendListForSchedule.this);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refresh = findViewById(R.id.refresh);

        showFriendList();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendListForSchedule.this,FriendListForSchedule.class));
            }
        });
    }

    private void showFriendList() {
        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog.setTitle("???????????????...");
        progressDialog.show();
        UserID = firebaseAuth.getCurrentUser().getUid();

        firestoredb.collection("Users").document(UserID).collection("Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        Friend.clear();
                        //????????????
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            String id = documentSnapshot.getString("uidFriend");
                            String ffName = documentSnapshot.getString("friendName");
                            Friend.add(new Friend(id,ffName));
                        }
                        adapter = new FriendScheduleAdapter(FriendListForSchedule.this,Friend);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });
    }
}