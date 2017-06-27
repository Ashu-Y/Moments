package com.practice.android.moments.Activities;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.Editing.EditingActivity;
import com.practice.android.moments.Fragments.ProfileEditingFragment;
import com.practice.android.moments.Fragments.ProfileScreenFragment;
import com.practice.android.moments.Fragments.getProfiel;
import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;
import com.practice.android.moments.RecyclerView.PostRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;

public class Timeline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TimeLine";
    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    private static final int REQUEST_WRITE_STORAGE = 1;
    FragmentManager fragmentManager;
    ProfileScreenFragment profFragment;
    ProfileEditingFragment editProfFragment;
    GoogleApiClient googleApiClient;
    RecyclerView mRecyclerView;
    PostRecyclerAdapter mPostRecyclerAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Post> mPostArrayList;

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getFragmentManager();
        profFragment = new ProfileScreenFragment();
        editProfFragment = new ProfileEditingFragment();


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Google API CLIENT
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Toast.makeText(Timeline.this, "Check ur connection", Toast.LENGTH_SHORT).show()).addApi(Auth.GOOGLE_SIGN_IN_API).build();


        //Recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mPostArrayList = new ArrayList<>();

        mPostArrayList.add(new Post("gautam", R.drawable.c1));
        mPostArrayList.add(new Post("hitesh", R.drawable.c2));
        mPostArrayList.add(new Post("piyush", R.drawable.c3));
        mPostArrayList.add(new Post("abhya", R.drawable.c4));
        mPostArrayList.add(new Post("mansi", R.drawable.c5));
        mPostArrayList.add(new Post("naman", R.drawable.c6));
        mPostArrayList.add(new Post("rajat", R.drawable.c7));

        mPostRecyclerAdapter = new PostRecyclerAdapter(getApplicationContext(), mPostArrayList);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mPostRecyclerAdapter);

    }

    /*
    If navigation drawer is opened and Back Button is pressed then it
    will take it to Timeline Activity
    and Back Button is pressed it will open a dialog box written You want to exit!!!!
    if pressed yes then it will exit
    else it remain the same
          */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Timeline.this);
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
                        Toast.makeText(Timeline.this, "Thank you for Staying back", Toast.LENGTH_SHORT).show();
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            if (profFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(profFragment);
                fm.commit();
            }
            startActivity(new Intent(this, Timeline.class));


        } else if (id == R.id.nav_camera) {
            if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {

                fn_Choose_Image();
            } else {
                isStoragePermissionGranted();
                fn_Choose_Image();
            }
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/external/images/media/"));
            startActivity(i);
        } else if (id == R.id.nav_profile) {

            if (!profFragment.isAdded()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.containerA, profFragment, "profile Fragment");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_slideshow) {
            if (profFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(profFragment);
                fm.commit();
            }
            if (editProfFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(editProfFragment);
                fm.commit();
            }
            startActivity(new Intent(this, PhotoVideosdatabase.class));
        } else if (id == R.id.nav_Friends) {


            getProfiel fragment1 = new getProfiel();
            if (!fragment1.isAdded()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.containerA, fragment1, "profile Fragment");
                fragmentTransaction.commit();
            }

        } else if (id == R.id.nav_editing) {
            if (profFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(profFragment);
                fm.commit();
            }
            if (editProfFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(editProfFragment);
                fm.commit();
            }
            startActivity(new Intent(Timeline.this, EditingActivity.class));
        } else if (id == R.id.nav_bottomNavigation) {
            if (profFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(profFragment);
                fm.commit();
            }
            if (editProfFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(editProfFragment);
                fm.commit();
            }
            startActivity(new Intent(Timeline.this, BottomNavigation.class));
        } else if (id == R.id.nav_Logout) {
            LogoutButton();
        } else if (id == R.id.profile_edit) {
            if (profFragment.isAdded()) {
                FragmentTransaction fm = fragmentManager.beginTransaction();
                fm.remove(profFragment);
                fm.commit();
            }
            if (!editProfFragment.isAdded()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.containerA, editProfFragment, "profile Fragment");
                fragmentTransaction.commit();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void fn_Choose_Image() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Timeline.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton("Camera", (arg0, arg1) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        });

        myAlertDialog.show();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                Log.v("dsf", "Permission is granted");
                return true;

            } else {
                Log.v("wef", "Permission is revoked");

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);

                return false;
            }
        } else {
            Log.v("f", "Permission is granted");
            return true;
        }

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


    public void LogoutButton() {
        Log.i(TAG, "You clicked onClick Button");
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(
                        status -> {
                            Log.i(TAG, "log off from google sign button");
                            Toast.makeText(Timeline.this, "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Timeline.this, Login_method.class));
                        });
    }
}
