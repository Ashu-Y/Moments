package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practice.android.moments.R;

public class ResetPassword extends AppCompatActivity {
    private static final String Tag = "Reset Password";
    private Button emailVerfified, passwordreset;
    private EditText emailverify, newpassword, newconfirmpassword;
    private TextView passw;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        passwordreset.setVisibility(View.GONE);
        newpassword.setVisibility(View.GONE);
        newconfirmpassword.setVisibility(View.GONE);
        passw.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passw = (TextView) findViewById(R.id.textView5);
        emailverify = (EditText) findViewById(R.id.emailreset);
        newpassword = (EditText) findViewById(R.id.pass1);
        newconfirmpassword = (EditText) findViewById(R.id.pass2);
        emailVerfified = (Button) findViewById(R.id.verifyemail);
        passwordreset = (Button) findViewById(R.id.button5);


        emailVerfified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordreset.setVisibility(View.VISIBLE);
                newpassword.setVisibility(View.VISIBLE);
                newconfirmpassword.setVisibility(View.VISIBLE);
                emailVerfified.setVisibility(View.GONE);
                passw.setVisibility(View.VISIBLE);
            }
        });


        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetPassword.this, MainActivity.class));
    }
}
