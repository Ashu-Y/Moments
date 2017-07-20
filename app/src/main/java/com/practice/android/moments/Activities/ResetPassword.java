package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practice.android.moments.R;

public class ResetPassword extends AppCompatActivity {
    private static final String TAG = "Reset Password";
    private Button emailVerfified, passwordreset;
    private EditText emailverify, newpassword, newconfirmpassword;
    private TextView passw;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passw = (TextView) findViewById(R.id.textView5);
        passwordreset = (Button) findViewById(R.id.button5);




        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = passw.getText().toString();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.e(TAG, "Email sent.");
                                }
                            }
                        });
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetPassword.this, MainActivity.class));
    }
}
