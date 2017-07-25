package com.practice.android.moments.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Activities.BottomNavigation;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.Models.notification;
import com.practice.android.moments.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikeCommentNotification extends Fragment {

    static String noLikecomment = "No Notification Yet";
    DatabaseReference databaseReference;
    String user_id;
    FirebaseUser firebaseUser;
    Context context;
    RecyclerView recyclerView;
    TextView onliketext;
    LinearLayoutManager lm_recycle;
    NotificationImage notificationImage;
    android.support.v4.app.FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_likenotify, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        user_id = firebaseUser.getUid();
        recyclerView = (RecyclerView) v.findViewById(R.id.likecommentn);
        onliketext = (TextView) v.findViewById(R.id.onliketext);
        context = getActivity();


        recyclerView.setHasFixedSize(true);
        lm_recycle = new LinearLayoutManager(context);
        lm_recycle.setReverseLayout(true);
        lm_recycle.setStackFromEnd(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);

        notificationImage = new NotificationImage();
        mFragmentManager = getFragmentManager();

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(user_id).child("Notification");
        } catch (Exception e) {
            e.getMessage();
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // String key = dataSnapshot.getValue(String.class);

                try {
                    if (null == dataSnapshot.getValue(String.class)) {
                        Log.e("dataSnapshotKey", dataSnapshot.getValue() + noLikecomment);
                        onliketext.setText(noLikecomment);
                        onliketext.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        onliketext.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {

            FirebaseRecyclerAdapter<notification, Notification> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<notification, Notification>(
                    notification.class,
                    R.layout.notify_recycler,
                    Notification.class,
                    databaseReference.orderByValue()
            ) {
                @Override
                protected void populateViewHolder(Notification viewHolder, notification model, int position) {
                    String pos = getRef(position).getKey();
                    Log.e("POSITION++++++", pos);
                    String posvalu = String.valueOf(getRef(position).getDatabase());
                    onliketext.setVisibility(View.GONE);
                    Log.e("Database Values will be", posvalu);

                    viewHolder.getnotify(context, pos);


                    viewHolder.gotoimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Name = getRef(viewHolder.getAdapterPosition()).getKey();


                            notificationImage.setImageResourceName(viewHolder.userImageID, Name);

                            Log.e("Pic Name", viewHolder.userImageID + "\n" + Name);
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.remove(notificationImage);
                            transaction.add(R.id.content, notificationImage, "NotificationImage Fragment");
                            BottomNavigation.FTAG = "Like Fragment";
                            transaction.addToBackStack(null);
                            transaction.commit();


                        }
                    });

                }


            };

            firebaseRecyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(firebaseRecyclerAdapter);


        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static class Notification extends RecyclerView.ViewHolder {

        View mview;


        CircleImageView image;
        TextView user, notify;
        DatabaseReference mdatabaseReference;
        String userid;
        FirebaseUser firebaseUser;
        String userImage;
        String friendUsernameID, userImageID;
        String iflike = "Liked Your Photo";
        String ifcomment = "Commented Your Photo";
        CardView gotoimage;

        public Notification(View itemView) {
            super(itemView);

            mview = itemView;
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            userid = firebaseUser.getUid();
            image = (CircleImageView) mview.findViewById(R.id.like_comment_photo);
            user = (TextView) mview.findViewById(R.id.user_comment_name);
            notify = (TextView) mview.findViewById(R.id.Notification);
            mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

            gotoimage = (CardView) mview.findViewById(R.id.clicktoopen);
        }

        public void getnotify(Context context, String position) {


            mdatabaseReference.child(userid).child("Notification").child(position).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    com.practice.android.moments.Models.notification notification = dataSnapshot.getValue(com.practice.android.moments.Models.notification.class);
                    assert notification != null;

                    userImageID = notification.getUserimageid();

                    friendUsernameID = notification.getFrienduserid();

                    try {
                        mdatabaseReference.child(userid).child("User Pictures").child(userImageID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Blog images = dataSnapshot.getValue(Blog.class);

                                assert images != null;

                                userImage = images.getMedium();
                                Glide.with(context).load(userImage)
                                        .skipMemoryCache(false)
                                        .placeholder(R.drawable.placeholder)
                                        .into(image);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    try {


                        mdatabaseReference.child(friendUsernameID).child("User Info").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Profile_model_class userinfo = dataSnapshot.getValue(Profile_model_class.class);


                                assert userinfo != null;
                                Log.e("user name", userinfo.getName());
                                user.setText(userinfo.getName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    try {

                        switch (notification.getStatus()) {
                            case "Like": {
                                notify.setText(iflike);
                                break;
                            }
                            case "Comment": {
                                notify.setText(ifcomment);
                                break;
                            }
                        }


                    } catch (Exception e) {
                        e.getMessage();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


}
