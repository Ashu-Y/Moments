package com.practice.android.moments.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.Helper.ProfileViewHelper;
import com.practice.android.moments.Models.Image;
import com.practice.android.moments.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    ImageView settings, coverpic;
    LinearLayout photosNum;
    RecyclerView recyclerView;
    ScrollView scrollProfile;
    CircleImageView profile;
    Context context;
    TextView profilename;
    String PicName;

    FirebaseUser firebaseUser;
    String user_id, user_name;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);

        settings = (ImageView) v.findViewById(R.id.settings);
        coverpic = (ImageView) v.findViewById(R.id.header_cover_image);
        photosNum = (LinearLayout) v.findViewById(R.id.photosNum);
        recyclerView = (RecyclerView) v.findViewById(R.id.grid_recycler);
        scrollProfile = (ScrollView) v.findViewById(R.id.scrollProfile);
        profile = (CircleImageView) v.findViewById(R.id.user_profile_photo);
        profilename = (TextView) v.findViewById(R.id.user_profile_name);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        context = getContext();



        RecyclerView.LayoutManager lm_recycle = new GridLayoutManager(getContext(), 2);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);




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


        profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                ProfileScreenFragment profFragment = new ProfileScreenFragment();

                transaction.replace(R.id.content, profFragment, "Profile Screen Fragment");
                transaction.commit();

            }
        });

        photosNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollProfile.requestChildFocus(recyclerView, recyclerView);
            }
        });


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        context = getContext();

        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }


        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id).child("User Pictures");


        FirebaseRecyclerAdapter<Image, ProfileViewHelper> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Image, ProfileViewHelper>(

                        Image.class,
                        R.layout.res_thumbnail,
                        ProfileViewHelper.class,
                        databaseReference.orderByPriority()


                ) {
                    @Override
                    protected void populateViewHolder(ProfileViewHelper viewHolder, Image model, int position) {

                        PicName = getRef(position).getKey();

                        String picname = PicName;

                        Log.e("Images ", picname);
                        viewHolder.setpic(context, user_id, picname);


                    }
                };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


}

