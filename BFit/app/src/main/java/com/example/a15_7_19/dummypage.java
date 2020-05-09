package com.example.a15_7_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class dummypage extends AppCompatActivity {
private Button but;
private EditText trekname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummypage);
         but = (Button)findViewById(R.id.button3);
        trekname=(EditText)findViewById(R.id.trekname);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dummypage.this,MapsActivity.class);
                String trekn = trekname.getText().toString();
                String strid = "STRID";
                intent.putExtra(strid,trekn);
                startActivity(intent);

            }
        });
    }

}
