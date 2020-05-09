package com.example.a15_7_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private EditText username,pwd;
private MaterialButton login_button;
private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
if(mAuth.getCurrentUser()!=null){
    Intent intent = new Intent(MainActivity.this,homepage.class);
    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    Toast.makeText(MainActivity.this,"Logged in as "+email,Toast.LENGTH_LONG).show();
    startActivity(intent);
}
        username =  findViewById(R.id.editText2);
        pwd =  findViewById(R.id.editText3);
        login_button =  findViewById(R.id.button);
progressBar=(ProgressBar)findViewById(R.id.progressBar2);
progressBar.setVisibility(View.INVISIBLE);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String user = username.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                if (TextUtils.isEmpty(user)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this,"Login done",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(),homepage.class);
                                startActivity(intent);

                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(MainActivity.this,"Login not done",Toast.LENGTH_LONG).show();

                            }
                    }
                });
            }
        });


    }
    public void callSignup(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Signup.class);
        startActivity(intent);
    }


}
