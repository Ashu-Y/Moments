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
    Button emailVerfified, passwordreset;
    EditText emailverify, newpassword, newconfirmpassword;
    TextView passw;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

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

                /*passwordreset.setVisibility(View.VISIBLE);
                newpassword.setVisibility(View.VISIBLE);
                newconfirmpassword.setVisibility(View.VISIBLE);
                emailVerfified.setVisibility(View.GONE);
                passw.setVisibility(View.VISIBLE);*/



            }
        });


/*
        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email = emailverify.getText().toString().trim();
                final String NewPassword = newpassword.getText().toString().trim();
                final String NewConfirmPassword = newconfirmpassword.getText().toString().trim();

                Log.i(Tag, "THis is the " + Email + "\n" + NewPassword + "\n" + NewConfirmPassword);

                AuthCredential credential = EmailAuthProvider.getCredential(Email, "password1234");
                Log.i(Tag, "THis is the " + credential);
                user = FirebaseAuth.getInstance().getCurrentUser();
                //confirm password check

                if (TextUtils.isEmpty(NewConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter Confirm password!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(NewPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter Confirm password!", Toast.LENGTH_SHORT).show();
                } else
                    //password length
                    if (NewConfirmPassword.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                           } else if (NewPassword.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    } else
                        //check if both pass are same
                        if (TextUtils.isEmpty(NewConfirmPassword) != TextUtils.isEmpty(NewPassword)) {
                            Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                        } else {

// Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(NewPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(Tag, "Password updated");
                                                        } else {
                                                            Log.d(Tag, "Error password not updated");
                                                        }
                                                    }
                                                });
                                    } else {
                                        Log.d(Tag, "Error auth failed");
                                    }
                                }
                            });
                        }
            }
        });
*/
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResetPassword.this, MainActivity.class));
    }
}
