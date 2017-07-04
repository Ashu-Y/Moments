package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.practice.android.moments.R;

public class SettingsActivity extends AppCompatActivity {

    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changePassword = (Button) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
            }
        });
    }
}
