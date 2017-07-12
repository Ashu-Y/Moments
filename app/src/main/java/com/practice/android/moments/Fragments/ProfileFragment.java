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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Models.Profile_model_class;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);

        settings = (ImageView) v.findViewById(R.id.settings);
        coverpic = (ImageView) v.findViewById(R.id.header_cover_image);
        photosNum = (LinearLayout) v.findViewById(R.id.photosNum);
        recyclerView = (RecyclerView) v.findViewById(R.id.grid_recycler);
        scrollProfile = (ScrollView) v.findViewById(R.id.scrollProfile);
//        editProfile = (Button) v.findViewById(R.id.editProfile);
        profile = (CircleImageView) v.findViewById(R.id.user_profile_photo);
        profilename = (TextView) v.findViewById(R.id.user_profile_name);


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

//        editProfile.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                ProfileEditingFragment editProfFragment = new ProfileEditingFragment();
//
//                transaction.replace(R.id.content, editProfFragment, "Edit Profile Fragment");
//                transaction.commit();
//
//            }
//        });

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


//    @Override
//    public void onStart() {
//        super.onStart();
//        context = getContext();
//
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        assert firebaseUser != null;
//        String user_id = firebaseUser.getUid();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        databaseReference.child(user_id).child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);
//
//                assert user != null;
//                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());
//
////                Picasso.with(getContext()).load(user.getPhoto()).fit().centerCrop().into(BackPIC);
//                Glide.with(getContext()).load(user.getPhoto()).placeholder(R.drawable.c1).into(profile);
//                Glide.with(getContext()).load(user.getCoverPhoto()).placeholder(R.drawable.c1).into(coverpic);
//
//                Log.d(TAG, "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getRelationship() + "    " + user.getAbout());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//
////
////
////        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("User Pictures");
////
////        FirebaseRecyclerAdapter<Image, GalleryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Image, GalleryViewHolder>(
////                Image.class,
////                R.layout.res_thumbnail,
////                GalleryViewHolder.class,
////                database
////
////
////        ) { @Override
////        protected void populateViewHolder(GalleryViewHolder viewHolder, Image model, int position) {
////
////            String pos = getRef(position).getKey();
////
////            Log.e("position", pos +model.getSmall());
////
//////                viewHolder.setImages(context, model.getSmall());
////
////        }
////        };
////
////
////        firebaseRecyclerAdapter.notifyDataSetChanged();
////        recyclerView.setAdapter(firebaseRecyclerAdapter);
//    }

    @Override
    public void onResume() {
        super.onResume();
        context = getContext();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String user_id = firebaseUser.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.child(user_id).child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                assert user != null;
                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());

//                Picasso.with(getContext()).load(user.getPhoto()).fit().centerCrop().into(BackPIC);
                Glide.with(getContext()).load(user.getPhoto()).placeholder(R.drawable.c1).into(profile);
                Glide.with(getContext()).load(user.getCoverPhoto()).placeholder(R.drawable.c1).into(coverpic);

                profilename.setText(user.getName());

                Log.d(TAG, "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getRelationship());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("User Pictures");
//
//        FirebaseRecyclerAdapter<Image, GalleryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Image, GalleryViewHolder>(
//                Image.class,
//                R.layout.res_thumbnail,
//                GalleryViewHolder.class,
//                database
//
//
//        ) { @Override
//            protected void populateViewHolder(GalleryViewHolder viewHolder, Image model, int position) {
//
//                String pos = getRef(position).getKey();
//
//                Log.e("position", pos +model.getSmall());
//
////                viewHolder.setImages(context, model.getSmall());
//
//            }
//        };
//
//
//        firebaseRecyclerAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        DatabaseReference mDatabaseReference;

        ImageView gallery_item;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User Pictures");
            gallery_item = (ImageView) mView.findViewById(R.id.thumbnail);
        }

        public void setImages(Context context, String position) {
//            mDatabaseReference.child(position).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Image user = dataSnapshot.getValue(Image.class);

//                    assert user != null;
            Glide.with(context).load(position).placeholder(R.drawable.c1).into(gallery_item);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
        }


    }


}

