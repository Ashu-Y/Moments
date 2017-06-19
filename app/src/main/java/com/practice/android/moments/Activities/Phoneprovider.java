package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.R;

public class Phoneprovider extends AppCompatActivity {
    private static final String TAG = "Phone Auth Provider";
    EditText phone_number, Verfiy_code, phone_pass;
    Button EnterIn, Verify, Resend;

    FirebaseAuth firebaseAuth;
//    String mVerificationId;
//    private PhoneAuthProvider.ForceResendingToken mResendToken;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks Callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneprovider);
        phone_number = (EditText) findViewById(R.id.field_phone_number);
        Verfiy_code = (EditText) findViewById(R.id.field_verification_code);
        EnterIn = (Button) findViewById(R.id.button_start_verification);
        Verify = (Button) findViewById(R.id.button_verify_phone);
        Resend = (Button) findViewById(R.id.button_resend);
        phone_pass = (EditText) findViewById(R.id.password_field);


        EnterIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Verify.setVisibility(View.VISIBLE);
                Verfiy_code.setVisibility(View.VISIBLE);
                Resend.setVisibility(View.VISIBLE);
                phone_pass.setVisibility(View.GONE);
                EnterIn.setVisibility(View.GONE);


            }
        });
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        firebaseAuth.getInstance();
//        Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                Log.w(TAG, "onVerificationFailed", e);
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    mPhoneNumberField.setError("Invalid phone number.");
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCodeSent(String verificationId,
//                                   PhoneAuthProvider.ForceResendingToken token) {
//                Log.d(TAG, "onCodeSent:" + verificationId);
//                mVerificationId = verificationId;
//                mResendToken = token;
//            }
//        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        Verify.setVisibility(View.GONE);
        Verfiy_code.setVisibility(View.GONE);
        Resend.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Phoneprovider.this, Login_method.class));
    }
}
