package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddFriend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FloatingActionButton addNewFriend;
    private String uid;
    SearchView searchView;
    private String FriendID;
    private String userName;
    private String userPhone;
    private String userIdentify;
    private String fName;
    private String fPhone;
    private String fIdentify;
    private TextView btnSetting;

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    List<fModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FriendAdapter adapter;
    ProgressDialog progressDialog;
    FFListAdapter FFadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        searchView = findViewById(R.id.edtID);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycle_view);
        addNewFriend = findViewById(R.id.addNewFriend);
        btnSetting = findViewById(R.id.btnSetting);

        /*---------navigation view and tool bar-------------*/
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
                    startActivity(new Intent(AddFriend.this,MyProfile.class));
                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(AddFriend.this,HomePage.class));
                    return true;
                } else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(AddFriend.this,scheduleList.class));
                    return true;
                }else if(id == R.id.addFriend){
                    startActivity(new Intent(AddFriend.this,AddFriend.class));
                    return true;
                }else if (id == R.id.signOut){
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Toast.makeText(AddFriend.this, "???????????????", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddFriend.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });
        /*---------navigation view and tool bar-------------*/

        progressDialog = new ProgressDialog(AddFriend.this);

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showSettingButton();        //BeCare??????????????????

        showFriendList();

        addNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFriend.this,FriendSetting.class));
            }
        });

    }

    private void showSettingButton() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        progressDialog.setTitle("???????????????...");
        progressDialog.show();
        if(mAuth.getCurrentUser() != null){
            uid = mAuth.getCurrentUser().getUid();
        }

        //???????????????????????????????????????????????????
        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("identify").equals("BeCare")){
                            btnSetting.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    public void showFriendList() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        progressDialog.setTitle("???????????????...");
        progressDialog.show();
        if(mAuth.getCurrentUser() != null){
            uid = mAuth.getCurrentUser().getUid();
        }
        db.collection("Users").document(uid).collection("Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        progressDialog.dismiss();
                        //????????????
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            if(documentSnapshot.getString("friendIdentify").equals("BeCare")){
                                fIdentify = "????????????";
                            }else {
                                fIdentify = "?????????";
                            }
                            fModel model = new fModel(
                                    documentSnapshot.getString("id"),
                                    documentSnapshot.getString("uidFriend"),
                                    documentSnapshot.getString("friendName"),
                                    documentSnapshot.getString("friendPhone"),
                                    fIdentify);
                            modelList.add(model);
                        }
                        //??????
                        adapter = new FriendAdapter(AddFriend.this,modelList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddFriend.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showFFList(String UserID){
        //?????????????????????
        btnSetting.setVisibility(View.INVISIBLE);
        addNewFriend.setVisibility(View.INVISIBLE);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        progressDialog.setTitle("???????????????...");
        progressDialog.show();
        db.collection("Users").document(UserID).collection("Friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        progressDialog.dismiss();
                        //????????????
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            if(documentSnapshot.getString("friendIdentify").equals("BeCare")){
                                fIdentify = "????????????";
                            }else {
                                fIdentify = "?????????";
                            }
                            fModel model = new fModel(
                                    documentSnapshot.getString("id"),
                                    documentSnapshot.getString("uidFriend"),
                                    documentSnapshot.getString("friendName"),
                                    documentSnapshot.getString("friendPhone"),
                                    fIdentify);
                            modelList.add(model);
                        }
                        //??????
                        FFadapter = new FFListAdapter(AddFriend.this, modelList);
                        recyclerView.setAdapter(FFadapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddFriend.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void add(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        Dialog dialog = new Dialog(AddFriend.this);
        dialog.setTitle("????????????????????????");
        dialog.setContentView(R.layout.dialog_add);
        dialog.show();

        EditText edtID = dialog.findViewById(R.id.edtID);

        Button btnSearch = dialog.findViewById(R.id.sch_friend);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone= edtID.getText().toString();
//                Toast.makeText(AddFriend.this, edtID,Toast.LENGTH_LONG).show();

                if(TextUtils.isEmpty(userPhone)){
                    edtID.setError("??????????????????");
                } else{
                    db.collection("Users").whereEqualTo("MyPhoneNumber", userPhone)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()){
                                edtID.setError("???????????????");
                            } else{
                                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                    String uidFriend = documentSnapshot.getId();
                                    if(uid.equals(uidFriend)){
                                        //?????????????????????
                                        edtID.setError("????????????");
                                    }else{
                                        //????????????????????????
                                        db.collection("Users").document(uid)
                                                .collection("Friend").whereEqualTo("friendPhone", userPhone)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if(queryDocumentSnapshots.isEmpty()){
                                                    //???????????????
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable(){

                                                        @Override
                                                        public void run() {

                                                            //???????????????????????????
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);

                                                            builder.setMessage("?????????????????????");
                                                            //???????????????????????????
                                                            builder.setCancelable(false);

                                                            builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    //???????????????????????????
                                                                    dialog.dismiss();
                                                                    checkFriendExist(uidFriend);
                                                                    startActivity(new Intent(AddFriend.this,AddFriend.class));
                                                                }
                                                            });

                                                            builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    //???????????????????????????
                                                                    dialog.dismiss();
                                                                    startActivity(new Intent(AddFriend.this,AddFriend.class));
                                                                }
                                                            });

                                                            AlertDialog alert = builder.create();
                                                            alert.show();

                                                        }}, 1000);
                                                }else {
                                                    //????????????
                                                    edtID.setError("????????????");
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkFriendExist(String uidFriend) {
        FriendID = UUID.randomUUID().toString();
        //current User's friend data
        db.collection("Users").document(uidFriend)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            fName = documentSnapshot.getString("Username");
                            fPhone = documentSnapshot.getString("MyPhoneNumber");
                            fIdentify = documentSnapshot.getString("identify");

                            DocumentReference documentReference = db.collection("Users").document(uid).collection("Friend").document(FriendID);
                            Map<String,Object> SaveUserProfile = new HashMap<String, Object>();
                            SaveUserProfile.put("id",FriendID);
                            SaveUserProfile.put("uidFriend",uidFriend);
                            SaveUserProfile.put("friendName", fName);
                            SaveUserProfile.put("friendPhone", fPhone);
                            SaveUserProfile.put("friendIdentify", fIdentify);

                            documentReference.set(SaveUserProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("SaveUserProfile","Successful:User Profile is created for " + uid);
                                    }else {
                                        Log.w("SaveUserProfile","Fail:",task.getException());
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(AddFriend.this, "??????????????????!", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        //friend's friend data
        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            userName = documentSnapshot.getString("Username");
                            userPhone = documentSnapshot.getString("MyPhoneNumber");
                            userIdentify = documentSnapshot.getString("identify");

                            DocumentReference documentReference_2 = db.collection("Users").document(uidFriend).collection("Friend").document(FriendID);
                            Map<String,Object> SaveFriendProfile = new HashMap<String, Object>();
                            SaveFriendProfile.put("id",FriendID);
                            SaveFriendProfile.put("uidFriend",uid);
                            SaveFriendProfile.put("friendName", userName);
                            SaveFriendProfile.put("friendPhone", userPhone);
                            SaveFriendProfile.put("friendIdentify", userIdentify);

                            documentReference_2.set(SaveFriendProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("SaveFriendProfile","Successful:User Profile is created for " + uid);
                                    }else {
                                        Log.w("SaveFriendProfile","Fail:",task.getException());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddFriend.this, "??????????????????!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        showFriendList();
    }
}