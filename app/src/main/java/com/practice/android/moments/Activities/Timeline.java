package com.practice.android.moments.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.Fragments.BlankFragment;
import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;
import com.practice.android.moments.RecyclerView.PostRecyclerAdapter;

import java.util.ArrayList;

public class Timeline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TimeLine";
    FragmentTransaction fragmentTransaction;
    Button Sign_Out;
    GoogleApiClient googleApiClient;
    private RecyclerView mRecyclerView;
    private PostRecyclerAdapter mPostRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Post> mPostArrayList;

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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Timeline.this, "Check ur connection", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API).build();

        Sign_Out = (Button) findViewById(R.id.SignOut);
        Sign_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "You clicked onClick Button");
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        Log.i(TAG, "log off from google sign button");
                                        Toast.makeText(Timeline.this, "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Timeline.this, Login_method.class));
                                    }
                                });

            }
        });


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
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
//                            finish();
                            System.exit(0);
                        }


                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(Timeline.this, "Thank you for Staying back", Toast.LENGTH_SHORT).show();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else
        if (id == R.id.nav_profile) {

            Fragment fragmentA = new BlankFragment();


            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.timeline, fragmentA).addToBackStack(null).commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
