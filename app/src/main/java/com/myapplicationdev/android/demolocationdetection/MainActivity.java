package com.myapplicationdev.android.demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
Button btnGetLastLocation,btngetLocationupdate,btnRemoveLocationupdate;
    FusedLocationProviderClient client ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnGetLastLocation = (Button) findViewById(R.id.buttonGetLastLocation);
        btngetLocationupdate = findViewById(R.id.buttonGetLocationUpdate);
        btnRemoveLocationupdate = findViewById(R.id.buttonRemoveLocationupdate);
        client = LocationServices.getFusedLocationProviderClient(this);
        final LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);
        final LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    String msg = "new loc Detected\n" + "Lat:" + data.getLatitude() +"," +
                            "Lng:" + data.getLongitude();

                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();


                }
            };
        };


        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        if (checkPermission() == true){
            Task<Location> task = client.getLastLocation() ;
            task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        String msg = "lat:" + location.getLatitude() +
                                "Log:" + location.getLongitude();
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();

                    }else {
                        String msg = "no last known location found";
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

            }
        });
        btngetLocationupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission() == true){
                    client.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
                }
            }
        });
        btnRemoveLocationupdate.setOnClickListener(new View.OnClickListener() {
       @Override
              public void onClick(View view) {
        client.removeLocationUpdates(mLocationCallback);
                }
          });



    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            String msg = "permission not granted";
            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();

            return false;
        }
    }
}