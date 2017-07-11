package com.practice.android.moments.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.practice.android.moments.R;


public class ProfileFragment extends Fragment {

    ImageView settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        settings = (ImageView) v.findViewById(R.id.settings);

        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                SettingsFragment settingsFragment = new SettingsFragment();

                transaction.replace(R.id.content, settingsFragment, "Settings Fragment");
                transaction.commit();

            }

        });

        return v;
    }


}
