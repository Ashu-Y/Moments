package com.practice.android.moments.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.practice.android.moments.Activities.ChangePassword;
import com.practice.android.moments.R;


/**
 * Created by Ashutosh on 7/10/2017.
 */

public class SettingsFragment extends Fragment {

    private String TAG = getClass().getSimpleName();
    private Button profile, editProfile, changePassword, privacyPolicy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_settings, container, false);

        profile = (Button) v.findViewById(R.id.profile);
        editProfile = (Button) v.findViewById(R.id.EditProfile);
        changePassword = (Button) v.findViewById(R.id.changePassword);
        privacyPolicy = (Button) v.findViewById(R.id.privacyPolicy);

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


        changePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePassword.class));
            }
        });

        privacyPolicy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
                transaction.replace(R.id.content, privacyPolicyFragment, "Edit Profile Fragment");
                transaction.commit();
            }
        });

        return v;
    }
}
