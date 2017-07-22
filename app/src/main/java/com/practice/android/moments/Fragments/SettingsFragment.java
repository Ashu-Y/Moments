package com.practice.android.moments.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.practice.android.moments.Activities.ChangePassword;
import com.practice.android.moments.Activities.Login_method;
import com.practice.android.moments.R;

import static com.practice.android.moments.Activities.BottomNavigation.context;
import static com.practice.android.moments.Activities.BottomNavigation.googleApiClient;
import static com.practice.android.moments.Activities.Login_method.isEmail;


/**
 * Created by Ashutosh on 7/10/2017.
 */

public class SettingsFragment extends Fragment {

    //    GoogleApiClient googleApiClient;
    public static Context mContext;
    private String TAG = getClass().getSimpleName();
    private Button profile, editProfile, changePassword, privacyPolicy, Logout_button, ClearHistory, invitebutton;

    @Override
    public void onResume() {
        super.onResume();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_settings, container, false);

        mContext = getContext();

        profile = (Button) v.findViewById(R.id.profile);

        ClearHistory = (Button) v.findViewById(R.id.ClearSearchHistory);

        editProfile = (Button) v.findViewById(R.id.EditProfile);
        changePassword = (Button) v.findViewById(R.id.changePassword);
        privacyPolicy = (Button) v.findViewById(R.id.privacyPolicy);
        invitebutton = (Button) v.findViewById(R.id.Invitebutton);

        Logout_button = (Button) v.findViewById(R.id.Logout);

        profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                ProfileScreenFragment profileScreenFragment = new ProfileScreenFragment();
                transaction.replace(R.id.content, profileScreenFragment, "Profile Fragment");
                transaction.commit();
            }
        });

        editProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                ProfileEditingFragment editProfileFragment = new ProfileEditingFragment();
                transaction.replace(R.id.content, editProfileFragment, "Edit Profile Fragment");
                transaction.commit();
            }
        });

        invitebutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        ClearHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        changePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePassword.class));
            }
        });

        privacyPolicy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Coming Soon", Toast.LENGTH_LONG).show();
//
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
                transaction.replace(R.id.content, privacyPolicyFragment, "Edit Profile Fragment");
                transaction.commit();
            }
        });

        Logout_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmail = false;
                Log.i(TAG, "You clicked onClick Button");
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Auth.GoogleSignInApi.signOut(googleApiClient)
                        .setResultCallback(
                                status -> {
                                    Log.i(TAG, "log off from google sign button");
                                    Toast.makeText(context, "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(context, Login_method.class));
                                });
            }
        });

//        googleApiClient = new GoogleApiClient.Builder(context)
//                .enableAutoManage(getActivity(), connectionResult -> Toast.makeText(context,
//                        "Check ur connection", Toast.LENGTH_SHORT).show()).addApi(Auth.GOOGLE_SIGN_IN_API).build();

        return v;
    }
}
