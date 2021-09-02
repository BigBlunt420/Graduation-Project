package com.example.loginandsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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


import org.jetbrains.annotations.NotNull;

public class HomePage extends AppCompatActivity implements OnMapReadyCallback{

    private static int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    //private ActivityMapsBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;
    private static boolean rLocationGranted = false ;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

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
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    /*
    Run google map, and update the location from user
     */
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try{
                    userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();   //clear the old location marker on the map
                    mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
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

//        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(lastLocation==null){
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            mMap.clear();   //clear the old location marker on the map
//            mMap.addMarker(new MarkerOptions().position(userLatLong).title("Your location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
//        }
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }







//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                        }
//                    }
//                });


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