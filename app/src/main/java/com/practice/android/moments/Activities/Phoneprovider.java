package com.practice.android.moments.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.R;

public class Phoneprovider extends AppCompatActivity {
    EditText phone_number, Verfi_code;
    Button EnterIn, Verify, Resend;

    FirebaseAuth firebaseAuth;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneprovider);
        phone_number = (EditText) findViewById(R.id.field_phone_number);
        Verfi_code = (EditText) findViewById(R.id.field_verification_code);
        EnterIn = (Button) findViewById(R.id.button_start_verification);
        Verify = (Button) findViewById(R.id.button_verify_phone);
        Resend = (Button) findViewById(R.id.button_resend);

//
    }


}
