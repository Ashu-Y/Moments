package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.practice.android.moments.R;

public class Signup extends AppCompatActivity {

    EditText name, email, password, conpassword, phone;
    Button sub, backtosign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


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

            }
        });








        backtosign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, MainActivity.class));
                finish();
            }
        });

    }
}
