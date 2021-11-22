package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddFriend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdd;
    private String uid;
    private SearchView searchView;
    private String schFriend;
    private String FriendID_1;
    private String FriendID_2;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public static final String TAG = "ProfileQuote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        searchView = findViewById(R.id.edtID);
//        searchView = findViewById(R.id.sch_friend);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        /*---------navigation view and tool bar-------------*/
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
                    startActivity(new Intent(AddFriend.this,MyProfile.class));
                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(AddFriend.this,HomePage.class));
                    return true;
                } else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(AddFriend.this,scheduleList.class));
                    return true;
                }else if (id == R.id.signOut){
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Toast.makeText(AddFriend.this, "用戶已登出", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddFriend.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });
        /*---------navigation view and tool bar-------------*/

        goMain();


    }

//    private void Find(){
//        //search view to search locations
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                schFriend = searchView.getQuery().toString();
//                if(schFriend != null || !schFriend.equals("")){
////                    geocoder = new Geocoder(HomePage.this);
////                    try{
////                        addressList = geocoder.getFromLocationName(shLocation, 1);
////                    }catch (IOException e){
////                        e.printStackTrace();
////                    }
////                    Address address = addressList.get(0);
////                    addresLatLng = new LatLng(address.getLatitude(), address.getLongitude());
////                    mMap.addMarker(new MarkerOptions()
////                            .position(addresLatLng).title("Searched location"));
////                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addresLatLng,zoomLevel));
////
////                    if(identify == "BeCare"){
////                        //add schedule
////                    }
//
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable(){
//
//                        @Override
//                        public void run() {
//
//                            //過四秒後要做的事情
//                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);
//
//                            builder.setMessage("是否加入好友？");
//                            //點選空白處不會返回
//                            builder.setCancelable(false);
//
//                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //按下是之後要做的事
//                                    dialog.dismiss();
//
//                                    addFriend(schFriend);
//                                }
//                            });
//
//                            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //按下否之後要做的事
//                                    dialog.dismiss();
//                                }
//                            });
//
//                            AlertDialog alert = builder.create();
//                            alert.show();
//
//                        }}, 1000);
//
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }
//
//    private void addFriend(String schFriend) {
////        FriendID_1 = UUID.randomUUID().toString();
////        FriendID_2 = UUID.randomUUID().toString();
////        db.collection("Users").document(uid).collection("Friend").document(FriendID_1);
////        db.collection("Users").document(schFriend).collection("Friend").document(FriendID_2);
////        Map<String,Object> SaveDetailSchedule_1 = new HashMap<String, Object>();
////        Map<String,Object> SaveDetailSchedule_2 = new HashMap<String, Object>();
////        SaveDetailSchedule_1.put("friend", schFriend);
////        SaveDetailSchedule_2.put("friend", uid);
//    }

//    private void sendFriendRequest(String schFriend) {
//
//
//
//        db.collection("Users").document(uid).collection("Friend").document().set(schFriend).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Log.d("schFriend","Successful:Friend Request is sent");
//                }else {
//                    Log.w("schFriend","Fail:",task.getException());
//                }
//            }
//        });
//
////                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////            @Override
////            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                if(queryDocumentSnapshots.isEmpty()){
////                    edtID.setError("找不到用戶");
////                } else{
////                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
////                        String uidFriend = documentSnapshot.getId();
////                        if(uid.equals(uidFriend)){
////                            //不能加自己好友
////                            edtID.setError("錯誤號碼");
////                        }else{
////                            checkFriendExist(uidFriend);
////                        }
////                    }
////                }
////            }
////        });
//    }
//
    private void goMain(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        Dialog dialog = new Dialog(AddFriend.this);
        dialog.setTitle("輸入用戶電話號碼");
        dialog.setContentView(R.layout.dialog_add);
        dialog.show();

//        final EditText edtID = dialog.findViewById(R.id.edtID);
        EditText edtID = dialog.findViewById(R.id.edtID);

        Button btnSearch = dialog.findViewById(R.id.sch_friend);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone= edtID.getText().toString();
//                Toast.makeText(AddFriend.this, edtID,Toast.LENGTH_LONG).show();

                if(TextUtils.isEmpty(userPhone)){
                    edtID.setError("欄位不得為空");
                } else{
                    db.collection("Users").whereEqualTo("MyPhoneNumber", userPhone)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()){
                                edtID.setError("找不到用戶");
                            } else{
                                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                    String uidFriend = documentSnapshot.getId();
                                    if(uid.equals(uidFriend)){
                                        //不能加自己好友
                                        edtID.setError("錯誤號碼");
                                    }else{
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable(){

                                            @Override
                                            public void run() {

                                                //過四秒後要做的事情
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);

                                                builder.setMessage("是否加入好友？");
                                                //點選空白處不會返回
                                                builder.setCancelable(false);

                                                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //按下是之後要做的事
                                                        dialog.dismiss();
                                                        checkFriendExist(uidFriend);
                                                    }
                                                });

                                                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //按下否之後要做的事
                                                        dialog.dismiss();
                                                    }
                                                });

                                                AlertDialog alert = builder.create();
                                                alert.show();

                                            }}, 1000);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
        startActivity(new Intent(AddFriend.this,AddFriend.class));
    }

//    private void checkFriendExist(String uidFriend){
//        addFriend(uidFriend);
////        db.collection("Users").document(uid).collection("Friend").document().set(schFriend).addOnCompleteListener(new OnCompleteListener<Void>() {
////            @Override
////            public void onComplete(@NonNull @NotNull Task<Void> task) {
////                if(task.isSuccessful()){
////                    Log.d("schFriend","Successful:Friend Request is sent");
////                }else {
////                    Log.w("schFriend","Fail:",task.getException());
////                }
////            }
////        });
//    }
    private void checkFriendExist(String uidFriend) {
        FriendID_1 = UUID.randomUUID().toString();
        FriendID_2 = UUID.randomUUID().toString();
        DocumentReference documentReference = db.collection("Users").document(uid).collection("Friend").document(FriendID_1);
        Map<String,Object> SaveUserProfile = new HashMap<String, Object>();
        SaveUserProfile.put("id",uidFriend);

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

        DocumentReference documentReference_2 = db.collection("Users").document(uidFriend).collection("Friend").document(FriendID_2);
        Map<String,Object> SaveFriendProfile = new HashMap<String, Object>();
        SaveFriendProfile.put("id",uid);

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
    }
//        db.collection("user").document(uid).collection("friend").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    if(!documentSnapshot.exists()){
//
//                        });
//
//                        documentReference_2.update(SaveFriendProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    Log.d(TAG,"Successful:Friend Profile was been update");
//                                }else {
//                                    Log.w(TAG,"Failed:",task.getException());
//                                }
//                            }
//                        });
//                    }
//                }
//            }

}