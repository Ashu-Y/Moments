package com.practice.android.moments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {

    //variables
    EditText login, pass; // login and password edittext
    Button signin, signup; // sign in  and sign up button
    GoogleSignInOptions gso;//google sign button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// giving data from xml to variables
        login = (EditText) findViewById(R.id.Emailogin);
        pass = (EditText) findViewById(R.id.passlogin);

        signin = (Button) findViewById(R.id.button);
        signup = (Button) findViewById(R.id.button2);








    }
}
