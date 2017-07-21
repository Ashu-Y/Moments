package com.practice.android.moments.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Helper.ProfileViewHelper;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.Image;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;


public class AddFriendProfileActivity extends AppCompatActivity {


    private final String TAG = getClass().getSimpleName();
    String imagename_from_bottom, userid_from_search;

    String PicName;
    ImageView profile, coverpic;
    TextView profilename, userfriends, userimages;
    DatabaseReference databaseReference, mdatabaseReference;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_profile);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Pictures");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        imagename_from_bottom = getIntent().getExtras().getString("imageName");
        userid_from_search = getIntent().getExtras().getString("User id");

        recyclerView = (RecyclerView) findViewById(R.id.grid_recycler);
        profile = (ImageView) findViewById(R.id.user_profile_photo);
        coverpic = (ImageView) findViewById(R.id.header_cover_image);
        profilename = (TextView) findViewById(R.id.user_profile);

        userfriends = (TextView) findViewById(R.id.friendnumber);
        userimages = (TextView) findViewById(R.id.imagenumber);
        Log.e("data check", imagename_from_bottom + "\t" + userid_from_search);


        RecyclerView.LayoutManager lm_recycle = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);


        if (imagename_from_bottom != null) {

            databaseReference.child(imagename_from_bottom).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Blog image = dataSnapshot.getValue(Blog.class);

                    assert image != null;
                    profilename.setText(image.getUserName());
                    Userinformation(image.getUser_id());
                    Log.e(TAG, image.getUser_id());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (userid_from_search != null) {
            Userinformation(userid_from_search);
        }
    }


    public void Userinformation(String userid) {

        mdatabaseReference.child(userid).child("User Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                assert user != null;
                try {

                    Log.e(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getThumbnailProfilephoto());

                    try {
                        profilename.setText(user.getName());
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Glide.with(getApplicationContext()).load(user.getThumbnailProfilephoto())
                            .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.giphy))
                            .into(profile);

                    Glide.with(getApplicationContext()).load(user.getThumbnailCoverPhoto())
                            .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.loader))
                            .into(coverpic);

                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //Friend count
        try {
            mdatabaseReference.child(userid).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long numberoffriends = dataSnapshot.getChildrenCount();

                    if (numberoffriends > 0) {
                        userfriends.setText(String.valueOf(numberoffriends));
                    } else {
                        userfriends.setText("0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }

        //Image count
        try {
            mdatabaseReference.child(userid).child("User Pictures").addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long numberofimages = dataSnapshot.getChildrenCount();
                    if (numberofimages > 0) {
                        userimages.setText(String.valueOf(numberofimages));
                    } else {
                        userimages.setText("0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }

        // Recycler adapter

        try {
            Log.e(TAG, "recycler adapter started ");
            FirebaseRecyclerAdapter<Image, ProfileViewHelper> firebaseRecyclerAdapter = new
                    FirebaseRecyclerAdapter<Image, ProfileViewHelper>(

                            Image.class,
                            R.layout.res_thumbnail,
                            ProfileViewHelper.class,
                            mdatabaseReference.child(userid).child("User Pictures").orderByPriority()


                    ) {
                        @Override
                        protected void populateViewHolder(ProfileViewHelper viewHolder, Image model, int position) {

                            PicName = getRef(position).getKey();

                            String picname = PicName;

                            Log.e(TAG, "recycler adapter called " + picname);

                            viewHolder.setpic(getApplicationContext(), userid, picname);


                        }
                    };

            firebaseRecyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(firebaseRecyclerAdapter);

        } catch (Exception e) {
            e.getMessage();
        }
    }


}
