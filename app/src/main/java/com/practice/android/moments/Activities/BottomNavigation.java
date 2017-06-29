package com.practice.android.moments.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.Fragments.DashboardFragment;
import com.practice.android.moments.Fragments.TimelineFragment;
import com.practice.android.moments.R;

public class BottomNavigation extends AppCompatActivity {

    TimelineFragment mTimelineFragment;
    DashboardFragment mDashboardFragment;
    FragmentManager mFragmentManager;
    GoogleApiClient googleApiClient;
    private String TAG = getClass().getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (!mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mTimelineFragment, "Timeline Fragment");
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_dashboard:

                    if (!mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mDashboardFragment, "Timeline Fragment");
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mTimelineFragment);
                        transaction.commit();
                    }
                    if (mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mDashboardFragment);
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_logout:
                    if (mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mTimelineFragment);
                        transaction.commit();
                    }
                    if (mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mDashboardFragment);
                        transaction.commit();
                    }
                    LogoutButton();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTimelineFragment = new TimelineFragment();
        mDashboardFragment = new DashboardFragment();
        mFragmentManager = getSupportFragmentManager();

        // Google API CLIENT
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Toast.makeText(this,
                        "Check ur connection", Toast.LENGTH_SHORT).show()).addApi(Auth.GOOGLE_SIGN_IN_API).build();


        if (!mTimelineFragment.isAdded()) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.content, mTimelineFragment, "Timeline Fragment");
            transaction.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void LogoutButton() {
        Log.i(TAG, "You clicked onClick Button");
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(
                        status -> {
                            Log.i(TAG, "log off from google sign button");
                            Toast.makeText(this, "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Login_method.class));
                        });
    }
}
