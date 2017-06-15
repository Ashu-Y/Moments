package com.practice.android.moments.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.R;

public class PhoneAuth extends AppCompatActivity {

    EditText phone_number, Verfi_code;
    Button EnterIn, Verify, Resend;

    FirebaseAuth firebaseAuth;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        phone_number = (EditText) findViewById(R.id.field_phone_number);
        Verfi_code = (EditText) findViewById(R.id.field_verification_code);
        EnterIn = (Button) findViewById(R.id.button_start_verification);
        Verify = (Button) findViewById(R.id.button_verify_phone);
        Resend = (Button) findViewById(R.id.button_resend);

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number,
//                60,TimeUnit.SECONDS,this,mCallbacks);

    }


}
