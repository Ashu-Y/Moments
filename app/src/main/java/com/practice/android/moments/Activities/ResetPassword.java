package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.R;

public class ResetPassword extends AppCompatActivity {
    private static final String TAG = "Reset Password";
    String emailAddress = null;
    FirebaseAuth auth;
    private Button passwordReset;
    private EditText emailReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailReset = (EditText) findViewById(R.id.emailreset);
        passwordReset = (Button) findViewById(R.id.reset_button);


        passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth = FirebaseAuth.getInstance();
                emailAddress = emailReset.getText().toString();

                try {
                    if (emailAddress != null) {
                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.e(TAG, "Email sent.");
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ResetPassword.this, "Please enter the valid email.....  ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Reset Password:", e.getMessage());
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetPassword.this, MainActivity.class));
    }
}
