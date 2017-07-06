package com.practice.android.moments.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.Fragments.DashboardFragment;
import com.practice.android.moments.Fragments.TimelineFragment;
import com.practice.android.moments.Fragments.Upload_picture;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.BottomNavigationViewHelper;
import com.practice.android.moments.R;
import com.squareup.picasso.Picasso;

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
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FrameLayout fl;
    String user_id;
    String user_name;
    Display display;
    Point size;
    FirebaseUser firebaseUser;
    BottomNavigationView navigation;
    private String TAG = getClass().getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fl.getLayoutParams().height = 0;
                    fl.requestLayout();

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

                    if (mUpload_pictureFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mUpload_pictureFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

//                    if (!mTimelineFragment.isAdded()) {
//                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//                        transaction.replace(R.id.content, mTimelineFragment, "Timeline Fragment");
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                    }


//                    fl.getLayoutParams().height = 0;
//                    fl.requestLayout();

                    return true;

                case R.id.navigation_upload:

                    if (!mUpload_pictureFragment.isAdded()) {

                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();


                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mUpload_pictureFragment, "Timeline Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_dashboard:

                    fl.setMinimumHeight(size.y);
                    fl.getLayoutParams().height = size.y;
                    fl.requestLayout();

                    if (!mDashboardFragment.isAdded()) {
                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();

                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mDashboardFragment, "Timeline Fragment");
                        transaction.addToBackStack("Timeline");
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_notifications:

                    fl.setMinimumHeight(size.y);
                    fl.getLayoutParams().height = size.y;
                    fl.requestLayout();

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

                    if (mUpload_pictureFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mUpload_pictureFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    return true;

                case R.id.navigation_logout:



                    if (mTimelineFragment.isAdded()) {
                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();

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

                default: {
//                    fl.getLayoutParams().height = 0;
//                    fl.requestLayout();

                }

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_navigation);


        fl = (FrameLayout) findViewById(R.id.content);
        display = getWindowManager().getDefaultDisplay();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        size = new Point();
        display.getSize(size);

        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id)
                    .child("User Pictures");
        } catch (NullPointerException e) {
            Log.i("TimelineFrag", e.getMessage());
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm_recycle = new LinearLayoutManager(this);
//        lm_recycle.setReverseLayout(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);


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


//        if (!mTimelineFragment.isAdded()) {
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            transaction.add(R.id.content, mTimelineFragment, "Timeline Fragment");
//            transaction.commit();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.replace(R.id.content, mTimelineFragment, "Timeline Fragment");
//        transaction.addToBackStack("Timeline");
//        transaction.commit();

    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.row_item,
                BlogViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPic(getApplicationContext(), model.getPic());


            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

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


    public static class BlogViewHolder extends RecyclerView.ViewHolder {


        View mView;


        public BlogViewHolder(View itemView) {
            super(itemView);


            mView = itemView;
        }


        public void setTitle(String title) {

            TextView Blog_title = (TextView) mView.findViewById(R.id.username);
            Blog_title.setText(title);

        }

        public void setDescription(String description) {

            TextView Blog_descption = (TextView) mView.findViewById(R.id._description);
            Blog_descption.setText(description);

        }


        public void setPic(Context context, String photo) {

            ImageView imageView = (ImageView) mView.findViewById(R.id.image);
            Picasso.with(context).load(photo).into(imageView);

        }


    }
}

