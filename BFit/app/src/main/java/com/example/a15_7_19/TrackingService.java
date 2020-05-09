package com.example.a15_7_19;
import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.Notification;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.content.Context;
import android.content.ContextWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackingService extends Service{
    private NotificationManager mManager;
    FusedLocationProviderClient client;
    LocationCallback callback;
    public static final String ANDROID_CHANNEL_ID = "com.example.a15_7_19.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    private FirebaseAuth mAuth;
    private String trekname;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = TrackingService.class.getSimpleName();

    public TrackingService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        trekname = intent.getExtras().getString("STRID");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Blogzone");
        final DatabaseReference newPost = databaseRef.child(trekname);
        newPost.child("imageUrl").setValue("notsetyet");
        newPost.child("title").setValue("PostTitle");
        newPost.child("desc").setValue("PostDes");
        newPost.child("uid").setValue("Username");
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // buildNotification();
        loginToFirebase();
    }
    @Override
    public void onDestroy()
    {
        client.removeLocationUpdates(callback);
        stopSelf();
    }
    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

////Unregister the BroadcastReceiver when the notification is tapped//
//
            unregisterReceiver(stopReceiver);
//
////Stop the Service//
            stopSelf();
        }
    };

    private void loginToFirebase() {

//Authenticate with Firebase, using the email and password we created earlier//

        mAuth = FirebaseAuth.getInstance();


        if(mAuth!=null)
            requestLocationUpdates();


//Call OnCompleteListener if the user is signed in successfully//



    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(5000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "location";
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = null,location_prev = null;
                    DatabaseReference ref = null;
                    String uid = null;

//Get a reference to the database, so your app can perform read and write operations//

                    if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        uid = user.getUid();
                        ref = FirebaseDatabase.getInstance().getReference();
                        location = locationResult.getLastLocation();
                    }

                    if (location != null) {
//Save the location data to the database//
                       // Toast.makeText(TrackingService.this,"recently add to DB",Toast.LENGTH_SHORT).show();
                        ref.child("Blogzone").child(trekname).child(path).push().setValue(location);

                    }
                }
            }, null);





        }
    }
}