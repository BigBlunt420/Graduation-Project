package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

//import com.example.loginandsignup.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class HomePage extends AppCompatActivity implements OnMapReadyCallback{

    private static int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private SearchView searchView;
    private Geocoder geocoder;

    private float zoomLevel = 16.0f;
    private String shLocation;
    private List<Address> addressList = null;
    //private ActivityMapsBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;
    LatLng addresLatLng;
    int move = 1;
    private FloatingActionButton reloadButton;
    private static boolean rLocationGranted = false ;
    private int count = 0;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // 用toolbar做為APP的ActionBar
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.sch_location);
        reloadButton = findViewById(R.id.reload);

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
                // 取得選項id
                int id = item.getItemId();

                // 依照id判斷點了哪個項目並做相應事件
                if(id == R.id.profile){

                    return true;
                }else if(id == R.id.mappage){

                    return true;
                } else if(id == R.id.joinedGroup){

                    return true;
                }else if(id == R.id.setTimeAndLocation){
                    //replaceFragment(new setTimeAndLocationFragment());
                    startActivity(new Intent(HomePage.this,setTimeAndLocation.class));
                    return true;
                }else if (id == R.id.signOut){
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(HomePage.this, "用戶已登出", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomePage.this,LoginPage.class));
                    return true;
                }
                return false;
            }
        });



        //relead the activity
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




//        if(count == 0){
//            count=1;
//            if(Build.VERSION.SDK_INT >= 11){
//                recreate();
//            }else{
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
//            }
//        }

    }

//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout,fragment);
//        fragmentTransaction.commit();
//    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }





    /*
    Checking if the version is able to use google map api
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

    /*
    Asking user to permit the permission to share the location
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

//        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(lastLocation == null){
//            if(Build.VERSION.SDK_INT >= 11){
//                recreate();
//            }else{
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
//            }
//        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        rLocationGranted = false;
//        if(requestCode == 1001){
//
//            if(grantResults.length > 0){
//                for(int i = 0 ;  i < grantResults.length ; i++){
//                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
//                        rLocationGranted = false;
//                        return;
//                    }
//                    rLocationGranted = true;
//                }
//            }
//        }
//    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile){

        }else if(id == R.id.joinedGroup){

        }else if(id == R.id.signOut){

        }
        //return super.onOptionsItemSelected(item);
        return true;
    }

*/
    /*
    Run google map, and update the location from user
     */
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;




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



        /////location change
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try{
                    userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();   //clear the old location marker on the map
                    mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your location"));
                    if(addresLatLng != null){
                        mMap.addMarker(new MarkerOptions()
                                .position(addresLatLng).title("Searched location"));
                    }
                    if(move == 1 ){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong,zoomLevel));
                    }

                }catch (SecurityException e){
                    e.printStackTrace();
                }

            }




//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(@NonNull String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(@NonNull String provider) {
//
//            }
//
//
//
        };

        //get current location at the first time when the app was opened
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try{
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
//            userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            if(userLatLong == null){
//                if(Build.VERSION.SDK_INT >= 11){
//                    recreate();
//                }else{
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                }
//            }
            mMap.clear();   //clear the old location marker on the map
            mMap.addMarker(new MarkerOptions().position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())).title("Your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), zoomLevel));
        }catch (SecurityException e){
            e.printStackTrace();
        }



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

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




//        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//        mMap.clear();   //clear the old location marker on the map
//        mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
        // Add a marker in Sydney and move the camera
        ////LatLng sydney = new LatLng(-34, 151);
        ////mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //// mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}
