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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practice.android.moments.R;

public class Signup extends AppCompatActivity {
    private static final String TAG = "Sign up";
    //variables
    EditText name, email, password, conpassword, phone;
    Button sub, backtosign;
    ProgressDialog mProgressDialog;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
// giving data from xml to variables

        name = (EditText) findViewById(R.id.nameca);
        email = (EditText) findViewById(R.id.emailuser);
        password = (EditText) findViewById(R.id.passworduser);
        conpassword = (EditText) findViewById(R.id.conpassworduser);
        phone = (EditText) findViewById(R.id.phoneuser);

        sub = (Button) findViewById(R.id.newaccount);
        backtosign = (Button) findViewById(R.id.loginuser);


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();


                String user_name = name.getText().toString().trim();
                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                String user_confirmpassword = conpassword.getText().toString().trim();
                String user_phone = phone.getText().toString().trim();


                //Email  check
                if (TextUtils.isEmpty(user_email)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Enter Email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //phone number check
                if (TextUtils.isEmpty(user_phone)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //password check
                if (TextUtils.isEmpty(user_password)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                    return;
                }

                //name check
                if (TextUtils.isEmpty(user_name)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //confirm password check
                if (TextUtils.isEmpty(user_confirmpassword)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Enter Confirm password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //password length
                if (user_password.length() < 6) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if both pass are same
                if (TextUtils.isEmpty(user_confirmpassword) != TextUtils.isEmpty(user_password)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideProgressDialog();
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            password.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(Signup.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        startActivity(new Intent(Signup.this, Timeline.class));
                                        finish();
                                    }

                                }
                            });
                }
            }
        });

//back button to sign in page
        backtosign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, MainActivity.class));
                finish();
            }
        });

    }


    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            startActivity(new Intent(Signup.this, Timeline.class));
        } else {
            Log.w(TAG, "No Authenticated user found");
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("PLease wait");
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
        startActivity(new Intent(Signup.this, Login_method.class));
    }
}
