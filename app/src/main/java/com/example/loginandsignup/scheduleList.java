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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class scheduleList extends AppCompatActivity {

    List<Model> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    String UserID;
    UserAdapter adapter;
    ProgressDialog progressDialog;
    FloatingActionButton addNewSchedule;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        recyclerView = findViewById(R.id.recycle_view);
        addNewSchedule = findViewById(R.id.addNewSchedule);

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

                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(scheduleList.this,HomePage.class));
                    return true;
                } else if(id == R.id.joinedGroup){

                    return true;
                }else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(scheduleList.this,scheduleList.class));
                    return true;
                }else if (id == R.id.signOut){
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(scheduleList.this, "用戶已登出", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(scheduleList.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });

        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(scheduleList.this);

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showScheduleList();

        addNewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(scheduleList.this,HomePage.class));
            }
        });
    }

    public void showScheduleList() {
        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog.setTitle("資料載入中...");
        progressDialog.show();
        UserID = firebaseAuth.getCurrentUser().getUid();
        firestoredb.collection("Users").document(UserID).collection("Schedule")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        progressDialog.dismiss();
                        //顯示資料
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            Model model = new Model(
                                    documentSnapshot.getString("id"),
                                    documentSnapshot.getString("Title"),
                                    documentSnapshot.getString("Describe"),
                                    documentSnapshot.getString("StartTime"),
                                    documentSnapshot.getString("EndTime"),
                                    documentSnapshot.getString("Location"),
                                    documentSnapshot.getString("Date"));
                            modelList.add(model);
                        }
                        //連接
                        adapter = new UserAdapter(scheduleList.this,modelList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(scheduleList.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
//    public void deleteData(int position){
//        firebaseAuth = FirebaseAuth.getInstance();
//        firestoredb = FirebaseFirestore.getInstance();
//        UserID = firebaseAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = firestoredb.collection("Users").document(UserID);
//                documentReference.collection("Schedule").document(modelList.get(position).getId());
//        documentReference.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        //showScheduleList();
//                        Log.d("DeleteDetailSchedule","Successful:User Profile is deleted for " + UserID);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Log.w("DeleteDetailSchedule","Fail:"+e.getMessage());
//            }
//        });
//    }

}