package com.practice.android.moments.Activities;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.practice.android.moments.Fragments.Upload_picture;
import com.practice.android.moments.Models.BottomNavigationViewHelper;
import com.practice.android.moments.R;

import java.io.File;

public class BottomNavigation extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    TimelineFragment mTimelineFragment;
    DashboardFragment mDashboardFragment;
    Upload_picture mUpload_pictureFragment;
    FragmentManager mFragmentManager;
    GoogleApiClient googleApiClient;
    BottomNavigationView navigation;
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
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_upload:

                    if (!mUpload_pictureFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mUpload_pictureFragment, "Timeline Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_dashboard:

                    if (!mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mDashboardFragment, "Timeline Fragment");
                        transaction.addToBackStack(null);
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

            default:{
                    if (!mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mTimelineFragment, "Timeline Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_navigation);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);

        mTimelineFragment = new TimelineFragment();
        mDashboardFragment = new DashboardFragment();
        mUpload_pictureFragment = new Upload_picture();
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

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath;
        Uri uri;
        Bitmap thumbnail;
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            assert c != null;
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);
            c.close();

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            picturePath = getRealPathFromURI(selectedImageUri);

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    @Override
    public void onBackPressed() {

        if (!mTimelineFragment.isAdded()) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.content, mTimelineFragment, "Timeline Fragment");
            transaction.addToBackStack(null);
            transaction.commit();

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(BottomNavigation.this);
            builder1.setMessage("You want to exit!!!!");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        dialog.cancel();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    });

            builder1.setNegativeButton(
                    "No",
                    (dialog, id) -> {
                        dialog.cancel();
                        Toast.makeText(BottomNavigation.this, "Thank you for Staying back", Toast.LENGTH_SHORT).show();
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}
