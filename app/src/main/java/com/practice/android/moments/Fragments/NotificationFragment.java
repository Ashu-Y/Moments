package com.practice.android.moments.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.practice.android.moments.R;


public class NotificationFragment extends Fragment {

    Button friend, likecomment; //buttons for fragment calling

    FrameLayout frameLayout;
    FragmentManager mFragmentManager;
    LikeCommentNotification likeCommentNotification;
    FriendsFragment friends;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        friend = (Button) v.findViewById(R.id.notif_friends);
        likecomment = (Button) v.findViewById(R.id.notify);
        frameLayout = (FrameLayout) v.findViewById(R.id.notif_frame);

        mFragmentManager = getFragmentManager();
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCommentNotification = new LikeCommentNotification();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.notif_frame, likeCommentNotification, "Like comments Fragment");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        likecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friends = new FriendsFragment();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.notif_frame, friends, "friends Fragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;
    }

}
