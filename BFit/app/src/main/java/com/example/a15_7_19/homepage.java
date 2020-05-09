package com.example.a15_7_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class homepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        loadFragment(new tafragment());
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment!=null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.nav_ta:
                fragment= new tafragment();
                break;

            case R.id.nav_m:
                fragment= new mfragment();
                break;


            case R.id.nav_e:
                fragment= new efragment();
                break;
        }
        return loadFragment(fragment);
    }

    public void musicfunc(View view)
    {

        Fragment fragment=null;
        fragment = new mfragment();
        loadFragment(fragment);
    }

    public void explorefunc(View view)
    {
        Fragment fragment=null;
        fragment = new efragment();
        loadFragment(fragment);
    }
    public void gofortrek(View view)
    {
        Intent intent = new Intent(homepage.this,dummypage.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(homepage.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
