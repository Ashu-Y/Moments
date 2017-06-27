package com.practice.android.moments.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.practice.android.moments.Fragments.DashboardFragment;
import com.practice.android.moments.Fragments.TimelineFragment;
import com.practice.android.moments.R;

public class BottomNavigation extends AppCompatActivity {

    TimelineFragment mTimelineFragment;
    DashboardFragment mDashboardFragment;
    FragmentManager mFragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mDashboardFragment);
                        transaction.commit();
                    }
                    if (!mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.add(R.id.content, mTimelineFragment, "Timeline Fragment");
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_dashboard:
                    if (mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mTimelineFragment);
                        transaction.commit();
                    }

                    if (!mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.add(R.id.content, mDashboardFragment, "Timeline Fragment");
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


        if (!mTimelineFragment.isAdded()) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.content, mTimelineFragment, "Timeline Fragment");
            transaction.commit();
        }
    }

}
