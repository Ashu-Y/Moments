package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.R;

public class MainActivity extends AppCompatActivity {

    //variables
    EditText login, pass; // login and password edittext
    Button signin, signup; // sign in  and sign up button
//    GoogleSignInOptions gso;//google sign button

    //    ProgressDialog progressDialog; //dialog variable
    FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// giving data from xml to variables
        login = (EditText) findViewById(R.id.Emailogin);
        pass = (EditText) findViewById(R.id.passlogin);

        signin = (Button) findViewById(R.id.button);
        signup = (Button) findViewById(R.id.button2);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, Timeline.class));
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };


// signin button
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Signinserver();

            }
        });


        //Intent from login to new user signup page
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Signup.class));

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //
    public void Signinserver() {
        String strLogin = login.getText().toString().trim();
        String strpassword = pass.getText().toString().trim();


        if (TextUtils.isEmpty(strLogin)) {
            //email empty
            Toast.makeText(MainActivity.this, "Email  Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(strpassword)) {
            //password empty
            Toast.makeText(MainActivity.this, "Password Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(strLogin, strpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Problem in Signin", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}
