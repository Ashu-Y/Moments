package com.practice.android.moments.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.R;

import java.util.Objects;

public class Signup extends AppCompatActivity {
    private static final String TAG = "Sign up";
    //variables
    private EditText name, email, password, conpassword, phone;
    private Button sub, backtosign;
    private ProgressDialog mProgressDialog;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
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


                final String user_name = name.getText().toString().trim();
                final String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                String user_confirmpassword = conpassword.getText().toString().trim();
                final String user_phone = phone.getText().toString().trim();


                //Email  check
                if (TextUtils.isEmpty(user_email)) {
                    updateUI(null);
                    Toast.makeText(getApplicationContext(), "Enter Email address!", Toast.LENGTH_SHORT).show();
                } else
                    //phone number check
                    if (TextUtils.isEmpty(user_phone)) {
                        updateUI(null);
                        Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    } else

                        //password check
                        if (TextUtils.isEmpty(user_password)) {
                            updateUI(null);
                            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        } else
                            //name check
                            if (TextUtils.isEmpty(user_name)) {
                                updateUI(null);
                                Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                            } else
                                //confirm password check
                                if (TextUtils.isEmpty(user_confirmpassword)) {
                                    updateUI(null);
                                    Toast.makeText(getApplicationContext(), "Enter Confirm password!", Toast.LENGTH_SHORT).show();
                                } else
                                    //password length
                                    if (user_password.length() < 6) {
                                        updateUI(null);
                                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                    } else
                                        //check if both pass are same
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            if (!Objects.equals(user_confirmpassword, user_password)) {
                                                updateUI(null);
                                                Log.d(TAG, user_confirmpassword + "   " + user_password);
                                                Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                                            } else {
                                                firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).
                                                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @SuppressWarnings("ConstantConditions")
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                                if (!task.isSuccessful()) {
                                                                    // there was an error
                                                                    if (password.length() < 6) {
                                                                        password.setError(getString(R.string.minimum_password));
                                                                    } else {
                                                                        Toast.makeText(Signup.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                                    }
                                                                } else {


                                                                    String user_id;

                                                                        user_id = firebaseUser.getUid();

                                                                        DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
                                                                        currentuser_db.child("name").setValue(user_name);
                                                                        currentuser_db.child("email").setValue(user_email);
                                                                        currentuser_db.child("phone").setValue(user_phone);
                                                                        currentuser_db.child("photo").setValue("Default");
                                                                        currentuser_db.child("Gender").setValue("Default");
                                                                        currentuser_db.child("Relationship").setValue("Default");
                                                                        currentuser_db.child("About Who you are").setValue("Default");
                                                                        currentuser_db.child("Date Of Birth").setValue("Default");

                                                                        updateUI(firebaseUser);


                                                                    startActivity(new Intent(Signup.this, Timeline.class));
                                                                    finish();
                                                                }
                                                                hideProgressDialog();
                                                            }
                                                        });
                                            }
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
