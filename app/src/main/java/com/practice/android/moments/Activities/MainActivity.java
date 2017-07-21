package com.practice.android.moments.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    //variables
    private EditText login, pass; // login and password edittext
    private Button signin, signup; // sign in  and sign up button
    private TextView Reset;
    private ProgressDialog mProgressDialog; //dialog variable
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// giving data from xml to variables
        login = (EditText) findViewById(R.id.Emailogin);
        pass = (EditText) findViewById(R.id.passlogin);

        signin = (Button) findViewById(R.id.button);
        signup = (Button) findViewById(R.id.button2);
        Reset = (TextView) findViewById(R.id.passreset);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, BottomNavigation.class));
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

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetPassword.class));
                finish();
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, BottomNavigation.class));

        } else {
            Toast.makeText(MainActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void Signinserver() {
        String strLogin = login.getText().toString().trim();
        String strpassword = pass.getText().toString().trim();

        showProgressDialog();

        if (TextUtils.isEmpty(strLogin)) {
            //email empty
            Toast.makeText(MainActivity.this, "Email  Empty", Toast.LENGTH_SHORT).show();
            updateUI(null);
            return;
        }
        if (TextUtils.isEmpty(strpassword)) {
            //password empty
            Toast.makeText(MainActivity.this, "Password Empty", Toast.LENGTH_SHORT).show();
            updateUI(null);
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(strLogin, strpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            updateUI(null);
                        } else {
                            if (!currentUser.isEmailVerified()) {
                                currentUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e(TAG, "Email sent.");
                                                    Toast.makeText(MainActivity.this, "Please verify your email....", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                hideProgressDialog();
                                Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, BottomNavigation.class));

                            }
                        }
                    }
                });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please Wait");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, Login_method.class));
    }
}