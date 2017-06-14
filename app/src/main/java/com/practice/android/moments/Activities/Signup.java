package com.practice.android.moments.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.practice.android.moments.R;

public class Signup extends AppCompatActivity {

    EditText name,email,password,conpassword,phone;
    Button sub,backtosign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        Toast.makeText(Signup.this, "Sign Up", Toast.LENGTH_SHORT).show();
    }
}
