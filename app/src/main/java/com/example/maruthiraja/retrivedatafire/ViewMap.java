package com.example.maruthiraja.retrivedatafire;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference myRef;
    private static final int LOCATION_REQUEST = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
        myRef = database.getReference("ShopkeeperSignup");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Toast.makeText(this, "hello map!!!!", Toast.LENGTH_SHORT).show();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //System.out.println(value);
                Toast.makeText(ViewMap.this, "Map entry", Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String Uid = ds.getKey().toString();
                    System.out.println("userid = "+Uid);
                    String shopName = ds.child("shop_name").getValue().toString();
                    double lat = Double.parseDouble(ds.child("latitude").getValue().toString());
                    double log = Double.parseDouble(ds.child("longitude").getValue().toString());
                    System.out.println("latitude  = "+ds.child("latitude").getValue().toString());
                    System.out.println("longitude  = "+ds.child("longitude").getValue().toString());
                    LatLng tamil = new LatLng(lat, log);
                    mMap.addMarker(new MarkerOptions().position(tamil).title(shopName));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(tamil));
                }
                //double latitude = Double.parseDouble(value);
               // Toast.makeText(ViewMap.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.w(TAG, "Failed to read value.", error.toException());
                System.out.println(error.toException());
            }
        });


        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
