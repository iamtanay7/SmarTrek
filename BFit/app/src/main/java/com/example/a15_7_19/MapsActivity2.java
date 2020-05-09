package com.example.a15_7_19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
    private String trekname;
    private GoogleMap mMap;
    private double lat_glob = 0,long_glob = 0;
private MaterialButton postbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {

            trekname=bundle.getString("STRID");
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
postbtn=findViewById(R.id.post_btn);

postbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Intent intent = new Intent(MapsActivity2.this,PostActivity.class);
        String strid = "STRID";
        intent.putExtra(strid,trekname);
        startActivity(intent);
    }
});

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            DatabaseReference db_uid = db.child("Blogzone");
            DatabaseReference db_loc = db_uid.child(trekname).child("location");


            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        double latitude = ds.child("latitude").getValue(Double.class);
                        double longitude = ds.child("longitude").getValue(Double.class);
                        Log.d("TAG", latitude + " / " +  longitude);

                        if(lat_glob != 0 && long_glob != 0)
                        {
                            Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(lat_glob,long_glob),
                                    new LatLng(latitude,longitude)).color(Color.BLUE).width(8));
                        }
                        else {
                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                    .position(new LatLng(latitude, longitude))
                                    .title(""));
                        }

                        lat_glob = latitude;
                        long_glob = longitude;

                    }
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .position(new LatLng(lat_glob, long_glob))
                            .title(""));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat_glob,long_glob)));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };
            db_loc.addListenerForSingleValueEvent(eventListener);
        } else {
            Toast.makeText(MapsActivity2.this,"Authentication failed",Toast.LENGTH_SHORT).show();
        }
    }}//);
    //}
//}



