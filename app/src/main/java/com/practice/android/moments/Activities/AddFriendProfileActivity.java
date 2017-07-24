package com.practice.android.moments.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.practice.android.moments.Activities.BottomNavigation.context;
import static com.practice.android.moments.Activities.BottomNavigation.size;
import static com.practice.android.moments.Activities.BottomNavigation.thumb;
import static com.practice.android.moments.Activities.BottomNavigation.zoom;


public class AddFriendProfileActivity extends AppCompatActivity {


    private final String TAG = getClass().getSimpleName();
    String imagename_from_bottom, userid_from_search;

    ImageView expandedImageView;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    String coveruri = null;
    View containerA;
    LinearLayout gone;

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

        gone = (LinearLayout) findViewById(R.id.gone2);
        expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        containerA = findViewById(R.id.container);

        Log.e("data check", imagename_from_bottom + "\t" + userid_from_search);

        RecyclerView.LayoutManager lm_recycle = new GridLayoutManager(getApplicationContext(), 3);
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

        try {
            coverpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomNavigation.zoom = true;
                    BottomNavigation.ZOOMTAG = "Profile Fragment";
                    if (coveruri != null) {
                        zoomImageFromThumb(coverpic, coveruri);

                    } else {
                        Toast.makeText(AddFriendProfileActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
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
                    coveruri = user.getCoverPhoto();
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

                            viewHolder.profile.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String Name = getRef(viewHolder.getAdapterPosition()).getKey();

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                            .child("User Pictures").child(Name);

                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Blog image = dataSnapshot.getValue(Blog.class);

                                            assert image != null;
                                            image.getMedium();

                                            zoomImageFromThumb(viewHolder.profile, image.getMedium());


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

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

    private void zoomImageFromThumb(final View thumbView, String imageResId) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        thumb = thumbView;

        Glide.with(context).load(imageResId)
                .skipMemoryCache(false)
                .placeholder(R.drawable.coffee1)
                .into(expandedImageView);

//        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        containerA.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        gone.setVisibility(View.GONE);

        containerA.setMinimumHeight(size.y);
        containerA.getLayoutParams().height = size.y;
        containerA.requestLayout();

        expandedImageView.setVisibility(View.VISIBLE);


        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;

        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                zoom = false;

                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        gone.setVisibility(View.VISIBLE);
                        containerA.setMinimumHeight(0);
                        containerA.getLayoutParams().height = 0;
                        containerA.requestLayout();
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        gone.setVisibility(View.VISIBLE);
                        containerA.setMinimumHeight(0);
                        containerA.getLayoutParams().height = 0;
                        containerA.requestLayout();
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


}
