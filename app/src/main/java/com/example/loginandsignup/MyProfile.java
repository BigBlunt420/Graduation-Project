package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {

    private TextView eName,eMyMobile,eMyEmail,eContactMobileOne,eContactMobileTwo;
    private Button eUpdateContactMobileButton;

    private FirebaseFirestore firestoredb;
    private FirebaseAuth firebaseAuth;
    String UserID;
    String dbName,dbMyPhoneNumber,dbMyEmail,dbContactOne,dbContactTwo;
    ProgressDialog progressDialog;

    //toolbar&navigation
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //dialog元件
    private EditText eUpdateContactPersonOne,eUpdateContactPersonTwo;
    private Button eCancelButton,eokButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        eName = findViewById(R.id.Name);
        eMyMobile = findViewById(R.id.MyMobile);
        eMyEmail = findViewById(R.id.MyEmail);
        eContactMobileOne = findViewById(R.id.ContactMobileOne);
        eContactMobileTwo = findViewById(R.id.ContactMobileTwo);
        eUpdateContactMobileButton = findViewById(R.id.UpdateContactMobileButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();


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
                    startActivity(new Intent(MyProfile.this,MyProfile.class));
                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(MyProfile.this,HomePage.class));
                    return true;
                } else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(MyProfile.this,scheduleList.class));
                    return true;
                }else if (id == R.id.signOut){
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(MyProfile.this, "用戶已登出", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyProfile.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });
        /*---------navigation view and tool bar-------------*/

        progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setTitle("資料載入中...");
        progressDialog.show();

        UserID = firebaseAuth.getCurrentUser().getUid();
        firestoredb.collection("Users").document(UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressDialog.dismiss();
                        if(documentSnapshot.exists()){
                            dbName = documentSnapshot.getString("Username");
                            dbMyPhoneNumber = documentSnapshot.getString("MyPhoneNumber");
                            dbMyEmail = documentSnapshot.getString("Email");
                            dbContactOne = documentSnapshot.getString("ContactPersonOne");
                            dbContactTwo = documentSnapshot.getString("ContactPersonTwo");

                            eName.setText(dbName);
                            eMyMobile.setText(dbMyPhoneNumber);
                            eMyEmail.setText(dbMyEmail);
                            eContactMobileOne.setText(dbContactOne);
                            eContactMobileTwo.setText(dbContactTwo);
                        }else{
                            Toast.makeText(MyProfile.this,"此用戶不存在!",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MyProfile.this,"Fail:"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        eUpdateContactMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateContactMobile();
            }
        });


    }

    private void UpdateContactMobile() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MyProfile.this);
        LayoutInflater inflater = LayoutInflater.from(MyProfile.this);

        View myView = inflater.inflate(R.layout.update_contact_mobile,null);
        builder.setView(myView);

        eUpdateContactPersonOne = myView.findViewById(R.id.UpdateContactPersonOne);
        eUpdateContactPersonTwo = myView.findViewById(R.id.UpdateContactPersonTwo);
        eokButton = myView.findViewById(R.id.okButton);
        eCancelButton = myView.findViewById(R.id.cancelButton);

        eUpdateContactPersonOne.setText(dbContactOne);
        eUpdateContactPersonTwo.setText(dbContactTwo);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();

        eokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String one = eUpdateContactPersonOne.getText().toString().trim();
                String two = eUpdateContactPersonTwo.getText().toString().trim();

                if(one.isEmpty()){
                    eUpdateContactPersonOne.setError("此欄不得為空");
                    eUpdateContactPersonOne.requestFocus();
                    return;
                }
                if (one.length() != 10){
                    eUpdateContactPersonOne.setError("請填入10位手機號碼");
                    eUpdateContactPersonOne.requestFocus();
                    return;
                }
                if(two.isEmpty()){
                    eUpdateContactPersonTwo.setError("此欄不得為空");
                    eUpdateContactPersonTwo.requestFocus();
                    return;
                }
                if (two.length() != 10){
                    eUpdateContactPersonTwo.setError("請填入10位手機號碼");
                    eUpdateContactPersonTwo.requestFocus();
                    return;
                }
                UserID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firestoredb.collection("Users").document(UserID);
                Map<String,Object> UpdateContactPerson = new HashMap<String, Object>();
                UpdateContactPerson.put("ContactPersonOne",one);
                UpdateContactPerson.put("ContactPersonTwo",two);

                documentReference.update(UpdateContactPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("UpdateContactPerson","Successful:User Profile was been update");

                        }else {
                            Log.w("UpdateContactPerson","Failed:"+task.getException());
                        }
                    }
                });
                dialog.dismiss();
                startActivity(new Intent(MyProfile.this,MyProfile.class));
            }
        });
        eCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}