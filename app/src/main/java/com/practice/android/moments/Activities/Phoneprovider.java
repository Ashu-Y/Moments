package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.R;

import java.util.concurrent.TimeUnit;

public class Phoneprovider extends AppCompatActivity {
    private static final String TAG = "Phone Auth Provider";
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button verify;
    Button resend;
    private String mVerificationId;
    private EditText phone_number;
    private EditText Verfiy_code;
    private EditText phone_pass;
    private Button EnterIn;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks Callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneprovider);
        phone_number = (EditText) findViewById(R.id.field_phone_number);
        Verfiy_code = (EditText) findViewById(R.id.field_verification_code);
        EnterIn = (Button) findViewById(R.id.button_start_verification);
        verify = (Button) findViewById(R.id.button_verify_phone);
        resend = (Button) findViewById(R.id.button_resend);
//        phone_pass = (EditText) findViewById(R.id.password_field);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users from phone");

        EnterIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber()) {


                } else {
                    EnterIn.setVisibility(View.GONE);
                    verify.setVisibility(View.VISIBLE);
                    resend.setVisibility(View.VISIBLE);
                    startPhoneNumberVerification(phone_number.getText().toString());
                }

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = Verfiy_code.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Verfiy_code.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendVerificationCode(phone_number.getText().toString(), mResendToken);


            }
        });

        CALLBACKMETHOD();


    }

    private void CALLBACKMETHOD() {
        Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(Phoneprovider.this, "Wrong Number Entered", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Phoneprovider.this, new OnCompleteListener<AuthResult>() {
                    @SuppressWarnings({"ConstantConditions", "ThrowableResultOfMethodCallIgnored"})
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = task.getResult().getUser();

                            String user_id = firebaseAuth.getCurrentUser().getUid();

                            DatabaseReference currentuser_db = databaseReference.child(user_id);
                            currentuser_db.child("phone").setValue(phone_number.getText().toString());
//                            currentuser_db.child("Password").setValue(phone_pass.getText().toString());
                            currentuser_db.child("Verification code").setValue(Verfiy_code.getText().toString());
                            startActivity(new Intent(Phoneprovider.this, Timeline.class));

                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Verfiy_code.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                Callbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        if (code.length() > 0) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                Callbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phone_number.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            phone_number.setError("Invalid phone number.");
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(Phoneprovider.this, Timeline.class));
            finish();
        }
        EnterIn.setVisibility(View.VISIBLE);
        verify.setVisibility(View.INVISIBLE);
        resend.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Phoneprovider.this, Login_method.class));
    }
}
