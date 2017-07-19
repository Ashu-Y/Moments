package com.practice.android.moments.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practice.android.moments.R;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    FirebaseUser firebaseuser;
    String userid, useremail;

    TextView oldPass, newPass, comfirmNewpass;
    String oldPassword, newPassword, comfirmNewpassword;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

        userid = firebaseuser.getUid();
        useremail = firebaseuser.getEmail();

        oldPass = (TextView) findViewById(R.id.editText);

        newPass = (TextView) findViewById(R.id.editText2);
        comfirmNewpass = (TextView) findViewById(R.id.editText3);

        change = (Button) findViewById(R.id.sumbitchange);

        oldPassword = oldPass.getText().toString().trim();
        newPassword = newPass.getText().toString().trim();
        comfirmNewpassword = comfirmNewpass.getText().toString().trim();


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthCredential credential = EmailAuthProvider
                        .getCredential(useremail, oldPassword);

                if (TextUtils.isEmpty(oldPassword)) {

                    oldPass.setError("Empty");
                    Toast.makeText(getApplicationContext(), "Enter Current Password!", Toast.LENGTH_SHORT).show();
                } else {
// Prompt the user to re-provide their sign-in credentials
                    firebaseuser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (TextUtils.isEmpty(newPassword)) {

                                        newPass.setError("Empty");
                                        Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                                    } else if (TextUtils.isEmpty(comfirmNewpassword)) {

                                        comfirmNewpass.setError("Empty");
                                        Toast.makeText(getApplicationContext(), "Enter Confirm password!", Toast.LENGTH_SHORT).show();
                                    } else
                                        //password length
                                        if (newPassword.length() < 6) {

                                            newPass.setError("Length less than 6 ");
                                            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                        } else if (!Objects.equals(comfirmNewpassword, newPassword)) {
                                            Log.e(TAG, comfirmNewpassword + "   " + newPassword);
                                            newPass.setError("Does not match");
                                            comfirmNewpass.setError("Does not match");
                                            Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                                        } else {
                                            firebaseuser.updatePassword(newPassword)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ChangePassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ChangePassword.this, "Password changed failed", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePassword.this, "Current Password does not match", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });


    }
}
