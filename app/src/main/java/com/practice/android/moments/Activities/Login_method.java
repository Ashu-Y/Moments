package com.practice.android.moments.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.practice.android.moments.R;

import static java.lang.System.exit;

public class Login_method extends AppCompatActivity {

    Button ViaEmail;
    Button Viaphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_method);

        ViaEmail = (Button) findViewById(R.id.Emailact);
        Viaphone = (Button) findViewById(R.id.Phoneact);


        ViaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_method.this, MainActivity.class));
                finish();

            }
        });

        Viaphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_method.this, PhoneAuth.class));
                finish();
            }
        });


    }

    /*When Back Button is pressed it will open a dialog box written You want to exit!!!!
      if pressed yes then it will exit
      else it remain the same
      */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Login_method.this);
        builder1.setMessage("You want to exit!!!!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        exit(0);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
