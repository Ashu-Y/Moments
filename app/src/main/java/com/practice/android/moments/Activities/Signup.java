package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.practice.android.moments.R;

public class Signup extends AppCompatActivity {
    //variables
    EditText name, email, password, conpassword, phone;
    Button sub, backtosign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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


                String user_name = name.getText().toString().trim();
                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                String user_confirmpassword = conpassword.getText().toString().trim();
                String user_phone = phone.getText().toString().trim();


                if (TextUtils.isEmpty(user_email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(user_password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(user_name)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(user_confirmpassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (user_password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(user_confirmpassword) != TextUtils.isEmpty(user_password)) {
                    Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                    return;
                }



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
