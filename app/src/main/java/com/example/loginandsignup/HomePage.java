package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.example.loginandsignup.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.firebase.firestore.QuerySnapshot;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomePage extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private static int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private SearchView searchView;
    private Geocoder geocoder;

    private float zoomLevel = 16.0f;
    private String shLocation;
    private List<Address> addressList = null;
    //private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLatLong;
    private LatLng addresLatLng;
    int move = 1; //to check if the user is moving the map
    private FloatingActionButton reloadButton;
    private static boolean rLocationGranted = false ;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //Dialog的元件
    private TextView inputStartTime,inputEndTime;
    private Spinner inputParameter;
    private int starthour,startminute,endhour,endminute;
    int setYear,setMonth,setDay,month;
    private TextView inputDate;
    private DatePickerDialog datePickerDialog;
    private Button addDetail,cancelDetail;
    private FirebaseFirestore firestoredb;
    private String UserID; //data ID of current on firebase
    private EditText inputTile,inputDescribe;
    private String date;
    private String ScheduleID;
    private String setStartTime,setEndTime;
    int choice;
    private String identify;

    //variables for getting the times and date of the searched schedule
    private String getDate;
    private String getStTime;
    private String getEndTime;

    //variables for send text msg
    private String dbContactOne;
    private String dbContactTwo;
    private String target_name;
    private String target_phone;
    int max_msgsize=0;
    int send_hourofday=0;
    int send_min=0;


    private String Identify;
    private TextView Text;
    private TextView ContactMobileOne;
    private TextView ContactMobileTwo;
    private Button UpdateContactMobileButton;
//    private ConstraintLayout contactPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.main_toolbar);
        // 用toolbar做為APP的ActionBar
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.sch_location);
        reloadButton = findViewById(R.id.reload);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        firebaseAuth = FirebaseAuth.getInstance();
        UserID = firebaseAuth.getCurrentUser().getUid();

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

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
//                    contactPeople = findViewById(R.id.contactPeople);
                    Text = findViewById(R.id.Text);
                    ContactMobileOne = findViewById(R.id.ContactMobileOne);
                    ContactMobileTwo = findViewById(R.id.ContactMobileTwo);
                    UpdateContactMobileButton = findViewById(R.id.UpdateContactMobileButton);
                    firestoredb.collection("Users").document(UserID)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        identify = documentSnapshot.getString("identify");
                                        if(identify == "TakeCare"){
                                            //照顧者不需顯示緊急聯絡人
//                                            contactPeople.setVisibility(View.GONE);
                                            Text.setVisibility(View.GONE);
                                            ContactMobileOne.setVisibility(View.GONE);
                                            ContactMobileTwo.setVisibility(View.GONE);
                                            UpdateContactMobileButton.setVisibility(View.GONE);
                                        }
                                    }else{
                                        Toast.makeText(HomePage.this,"此用戶不存在!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(HomePage.this,"Fail:"
                                            +e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                    startActivity(new Intent(HomePage.this,MyProfile.class));
                    return true;
                }else if(id == R.id.mappage){
                    startActivity(new Intent(HomePage.this,HomePage.class));
                    return true;
                }else if(id == R.id.setTimeAndLocation){
                    startActivity(new Intent(HomePage.this,scheduleList.class));
                    return true;
                }else if(id == R.id.addFriend){
                    startActivity(new Intent(HomePage.this, AddFriend.class));
                } else if (id == R.id.signOut){
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(HomePage.this, "用戶已登出", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomePage.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });



        //reload the activity
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 11){
                    recreate();
                }else{
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        //when the user is moving the map, move = 0
        if(MotionEvent.ACTION_DOWN == 0){
            move = 0;
        }

        //checking if the version of device is able to use google map api
        if(checkPlayService()==true){
            initialMap();       //ask for permission
        }

        if(rLocationGranted == true){
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map1);
            mapFragment.getMapAsync(this);
        }





    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    /**
     * Checks if the version is able to use google map api
     * @return if the version is able to use google map api
     */
    private boolean checkPlayService(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomePage.this);
        if(available == ConnectionResult.SUCCESS){
            return true;
        }else{
            Toast.makeText(this,"版本不符合，無法執行程式", Toast.LENGTH_LONG);
            return false;
        }
    }

    /**
     * Asks user to permit the permission to share the location
     */
    private void initialMap(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        //ask for location permission
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){  //check if the android version == 10
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1])
                    == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])
                            == PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(this.getApplicationContext(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){
                //able to get fine_location
                rLocationGranted = true;

            }else{
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1])
                    == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])
                            == PackageManager.PERMISSION_GRANTED){
                //able to get fine_location
                rLocationGranted = true;

            }else{
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }

    }


    /**
     * Run google map, and update the location from user
     * @param googleMap the GoogleMap the is showing on homepage
     */
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


//        firestoredb.collection("Users").document(UserID)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            identify = documentSnapshot.getString("identify");
//                        }else{
//                            Toast.makeText(HomePage.this,"此用戶不存在!",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Toast.makeText(HomePage.this,"Fail:"
//                                +e.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                });

        //ask for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        //location change
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try{
                    userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();   //clear the old location marker on the map
                    MarkerOptions options = new MarkerOptions().position(userLatLong).title("Your location");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.addMarker(options);

                    stayInRange(location.getLatitude(), location.getLongitude());

                    if(addresLatLng != null){
                        mMap.addMarker(new MarkerOptions()
                                .position(addresLatLng).title("Searched location"));
                    }
                    //if the user is not moving the map then move the camera
                    if(move == 1 ){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong,zoomLevel));
                    }

                }catch (SecurityException e){
                    e.printStackTrace();
                }

            }

        };

        //get current location at the first time when the app was opened
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try{
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            mMap.clear();   //clear the old location marker on the map
            MarkerOptions options = new MarkerOptions().position(new LatLng(lastLocation.getLatitude(),
                    lastLocation.getLongitude())).title("Your location");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), zoomLevel));
        }catch (SecurityException e){
            e.printStackTrace();
        }

        //search view to search locations
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                shLocation = searchView.getQuery().toString();
                addressList = null;
                if(shLocation != null || !shLocation.equals("")){
                    geocoder = new Geocoder(HomePage.this);
                    try{
                        addressList = geocoder.getFromLocationName(shLocation, 1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    addresLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(addresLatLng).title("Searched location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addresLatLng,zoomLevel));

                    if(identify == "BeCare"){
                        //add schedule
                    }


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){

                        @Override
                        public void run() {

                            //過四秒後要做的事情
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);


                            builder.setMessage("是否要將此位置 "+shLocation+" 加入行程中？");
                            //點選空白處不會返回
                            builder.setCancelable(false);

                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //按下是之後要做的事
                                    dialog.dismiss();
                                    setDetailSchedule(address.getLatitude(), address.getLongitude());
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

                        }}, 4000);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    /**
     * 確認要位置要加入行程後，跳出建立詳細行程的Dialog
     * @param Latitude the user's current latitude
     * @param Longitude the user's current longitude
     */
    private void setDetailSchedule(double Latitude, double Longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        LayoutInflater inflater = LayoutInflater.from(HomePage.this);

        View myView = inflater.inflate(R.layout.input_detail_schedule,null);
        builder.setView(myView);


        inputTile = myView.findViewById(R.id.inputTile);
        inputDescribe = myView.findViewById(R.id.inputDescribe);
        inputStartTime = myView.findViewById(R.id.inputStartTime);
        inputEndTime = myView.findViewById(R.id.inputEndTime);
        inputDate = myView.findViewById(R.id.inputDate);
        addDetail = myView.findViewById(R.id.addDetail);
        cancelDetail = myView.findViewById(R.id.cancelDetail);
        inputParameter = myView.findViewById(R.id.inputParameter);

        //取得當日日期時間
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute =calendar.get(Calendar.MINUTE);
        date = makeDateString(year,month,day);
        setStartTime = makeTimeString(hour,minute);
        setEndTime = makeTimeString(hour,minute);
        inputDate.setText(makeDateString(year,month,day));
        inputStartTime.setText(String.format("%02d:%02d",hour,minute));
        inputEndTime.setText(String.format("%02d:%02d",hour,minute));

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();

        //設定範圍
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputParameter.setAdapter(adapter);
        inputParameter.setOnItemClickListener(this);

        //設定起始時間
        inputStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        setStartTime = makeTimeString(setHour,setMinute);
                        inputStartTime.setText(setStartTime);
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int setStarthour = calendar.get(Calendar.HOUR_OF_DAY);
                int setStartminute =calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        HomePage.this, android.app.AlertDialog.THEME_HOLO_LIGHT,
                        onTimeSetListener,setStarthour,setStartminute,true);

                timePickerDialog.show();
            }
        });

        //設定結束時間
        inputEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        setEndTime = makeTimeString(setHour,setMinute);
                        inputEndTime.setText(setEndTime);
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int setEndHour = calendar.get(Calendar.HOUR_OF_DAY);
                int setEndMinute =calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        HomePage.this, android.app.AlertDialog.THEME_HOLO_LIGHT,
                        onTimeSetListener,setEndHour,setEndMinute,true);

                timePickerDialog.show();
            }
        });

        //設定日期
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        date = makeDateString(year,month,day);
                        inputDate.setText(date);
                    }
                };

                Calendar calendar = Calendar.getInstance();
                setYear = calendar.get(Calendar.YEAR);
                setMonth = calendar.get(Calendar.MONTH);
                setDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(HomePage.this, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener,setYear,setMonth,setDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

                datePickerDialog.show();
            }
        });

        addDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Tile = inputTile.getText().toString().trim();
                String Describe = inputDescribe.getText().toString().trim();
                //檢查起始時間和結束時間
                while(year==setYear && month==setMonth&& day==setDay) {
                    if (starthour < hour || (starthour == hour && startminute < minute)) {
                        inputStartTime.requestFocus();
                        inputStartTime.setError("起始時間已過");
                        return;
                    }
                }
                if(starthour>endhour){
                    inputEndTime.requestFocus();
                    inputEndTime.setError("結束時間不得比起始時間早");
                    return;
                }
                if(starthour==endhour && startminute>endminute){
                    inputEndTime.requestFocus();
                    inputEndTime.setError("結束時間不得比起始時間早");
                    return;
                }

                //將資料加進firestore
                firestoredb = FirebaseFirestore.getInstance();
                firebaseAuth = FirebaseAuth.getInstance();
                ScheduleID = UUID.randomUUID().toString();
                DocumentReference documentReference = firestoredb.collection("Users").document(UserID).collection("Schedule").document(ScheduleID);
                Map<String,Object> SaveDetailSchedule = new HashMap<String, Object>();
                SaveDetailSchedule.put("id",ScheduleID);
                SaveDetailSchedule.put("Title",Tile);
                SaveDetailSchedule.put("Describe",Describe);
                SaveDetailSchedule.put("Location",shLocation);
                SaveDetailSchedule.put("Date",date);
                SaveDetailSchedule.put("StartTime",setStartTime);
                SaveDetailSchedule.put("EndTime",setEndTime);
                SaveDetailSchedule.put("Latitude",Double.toString(Latitude));
                SaveDetailSchedule.put("Longitude",Double.toString(Longitude));
                SaveDetailSchedule.put("Parameter",choice);

                documentReference.set(SaveDetailSchedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("SaveDetailSchedule","Successful:User Profile is created for " + UserID);
                        }else {
                            Log.w("SaveDetailSchedule","Fail:",task.getException());
                        }
                    }
                });

                dialog.dismiss();
            }
        });
        cancelDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * checks if the user is staying in range
     * and send text msg if user's out of range
     * @param Latitude the user's Latitude
     * @param Longitude the user's longitude
     */
    private void stayInRange(double Latitude, double Longitude){

        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firestoredb.collection("Users").document(UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            dbContactOne = documentSnapshot.getString("ContactPersonOne");
                            dbContactTwo = documentSnapshot.getString("ContactPersonTwo");
                            target_name  = documentSnapshot.getString("Username");
                            target_phone = documentSnapshot.getString("MyPhonenumber");
                        }else{
                            Toast.makeText(HomePage.this,"此用戶不存在!",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(HomePage.this,"Fail:"
                                +e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        firestoredb.collection("Users").document(UserID).collection("Schedule")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        int year = 0;
                        int day = 0;
                        int month = 0;
                        int stHr = 0;
                        int stMin = 0;
                        int endHr = 0;
                        int endMin = 0;
                        double rLat ;
                        double rLong;
                        final int R = 6371; // Radius of the earth
                        double latDistance;
                        double lonDistance;
                        double a, c, distance, height;


                        Calendar calendar = Calendar.getInstance();
                        //顯示資料
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            getDate = documentSnapshot.getString("Date");
                            rLat = Double.valueOf(
                                    documentSnapshot.getString("Latitude"));
                            rLong = Double.valueOf(
                                    documentSnapshot.getString("Longitude"));
                            //get the date from a schedule
                            for(int i = 0 ; i < getDate.length() ; i++){
                                if(getDate.charAt(i) == '年'){
                                    year = Integer.valueOf(getDate.substring(0,i));
                                    month = i+1;
                                }

                                if(getDate.charAt(i) == '月'){
                                    month = Integer.valueOf(getDate.substring(month,i));
                                    day = i+1;
                                }

                                if(getDate.charAt(i) == '日'){
                                    day = Integer.valueOf(getDate.substring(day,i));

                                }
                            }

                            //檢查資料的日期是否與當日相同
                            if(year == calendar.get(Calendar.YEAR)
                                    && month == calendar.get(Calendar.MONTH)+1
                                    && day == calendar.get(Calendar.DAY_OF_MONTH)){
                                getStTime = documentSnapshot.getString("StartTime");
                                getEndTime = documentSnapshot.getString("EndTime");


                                for(int j = 0 ; j < getStTime.length() ; j++){

                                    if(getStTime.charAt(j) == ':'){
                                        //get the hour of the start time
                                        stHr = Integer.valueOf(getStTime.substring(0, j));
                                        //get the minuit of the start time
                                        stMin = Integer.valueOf(getStTime.substring(j+1,
                                                getStTime.length()));
                                    }
                                }

                                for(int l = 0 ; l < getEndTime.length() ; l++){
                                    if(getEndTime.charAt(l) == ':'){
                                        //get the hour of the end time
                                        endHr = Integer.valueOf(getEndTime.substring(0, l));
                                        //get the minuit of the end time
                                        endMin = Integer.valueOf(getEndTime.substring(l+1,
                                                getEndTime.length()));
                                    }
                                }

                                //check if the time is already started
                                if(stHr == calendar.get(Calendar.HOUR_OF_DAY) &&
                                        stMin <= calendar.get(Calendar.MINUTE)
                                        || stHr <calendar.get(Calendar.HOUR_OF_DAY)){

                                    //check if the time wasn't ended yet
                                    if(endHr == calendar.get(Calendar.HOUR_OF_DAY) &&
                                            endMin >= calendar.get(Calendar.MINUTE)
                                            || endHr > calendar.get(Calendar.HOUR_OF_DAY)){
                                        latDistance = Math.toRadians(Latitude
                                                - rLat);
                                        lonDistance = Math.toRadians(Longitude
                                                - rLong);
                                        a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                                                + Math.cos(Math.toRadians(rLat)) * Math.cos(Math.toRadians(Latitude))
                                                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                                        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                                        distance = R * c * 1000; // convert to meters

                                        if(distance > 200){
                                            if(calendar.get(Calendar.HOUR_OF_DAY)>send_hourofday){
                                                max_msgsize++;
                                                sendMassage(max_msgsize, Latitude, Longitude
                                                        , target_name, dbContactOne, dbContactTwo);
                                                send_hourofday=calendar.get(Calendar.HOUR_OF_DAY);
                                                send_min=calendar.get(Calendar.MINUTE);
                                            }
                                            else if((calendar.get(Calendar.HOUR_OF_DAY)==send_hourofday)
                                                    && (calendar.get(Calendar.MINUTE)-send_min>=5)){
                                                max_msgsize++;
                                                sendMassage(max_msgsize, Latitude, Longitude
                                                        , target_name, dbContactOne, dbContactTwo);
                                                send_hourofday=calendar.get(Calendar.HOUR_OF_DAY);
                                                send_min=calendar.get(Calendar.MINUTE);
                                            }

                                        }
                                    }
                                }




                            }

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(HomePage.this,"Fail:"
                                +e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * send out the text massage from user
     * @param max_msgsize the time of massage has been sent
     * @param latitude the user's latitude
     * @param longitude the user's longitude
     * @param target_name the current user's name
     * @param dbContactOne the phone number of first contact
     * @param dbContactTwo the phone number of second contact
     */
    private void sendMassage(int max_msgsize, double latitude, double longitude,
                             String target_name,String dbContactOne, String dbContactTwo){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(dbContactOne,null,"目前傳送簡訊數量: "+max_msgsize+
                ".\n目前" +target_name+"已偏離一定距離，請盡快與其聯繫。"
                + "\n如要關閉提醒請刪除此行程。\n使用者位置：緯度"+latitude+"經度"+longitude
                ,null,null);
        smsManager.sendTextMessage(dbContactTwo,null,"目前傳送簡訊數量: "+max_msgsize+
                ".\n目前"+target_name+"已偏離一定距離，請盡快與其聯繫。"+
                "\n如要關閉提醒請刪除此行程。\n使用者位置：緯度"+latitude+"經度"+longitude
                ,null,null);
    }

    /**
     * 設定當日日期顯示樣式供設定Schedule日期時使用
     * @param year the year of the current day
     * @param month the month of the current month
     * @param day the day of the current day
     * @return 日期的String格式
     */
    private String makeDateString(int year, int month, int day) {
        return year + "年" + month + "月" + day + "日";
    }

    /**
     * 設定當日時間的格式供設定Schedule時間時使用
     * @param hour the current hour of the time
     * @param minute the current minute of the time
     * @return 時間的String格式
     */
    private String makeTimeString(int hour,int minute){
        return hour+":"+minute;
    }

    //選擇範圍
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        choice = (int) parent.getItemAtPosition(position);
    }
}