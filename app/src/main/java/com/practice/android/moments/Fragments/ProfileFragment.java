package com.practice.android.moments.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.practice.android.moments.Helper.ProfileViewHelper;
import com.practice.android.moments.Models.Friends;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.Image;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.practice.android.moments.Activities.BottomNavigation.FTAG;
import static com.practice.android.moments.Activities.BottomNavigation.ZOOMTAG;
import static com.practice.android.moments.Activities.BottomNavigation.size;
import static com.practice.android.moments.Activities.BottomNavigation.thumb;
import static com.practice.android.moments.Activities.BottomNavigation.zoom;


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
    TextView userfriends, userimages;

    View containerA;

    LinearLayout gone;

    ImageView expandedImageView;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    String coveruri;
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

        gone = (LinearLayout) v.findViewById(R.id.gone2);

        containerA = v.findViewById(R.id.container);
        expandedImageView = (ImageView) v.findViewById(R.id.expanded_image);

        userfriends = (TextView) v.findViewById(R.id.friendnumber);
        userimages = (TextView) v.findViewById(R.id.imagenumber);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        context = getContext();

        // Retrieve and cache the system's default "short" animation time.


        RecyclerView.LayoutManager lm_recycle = new GridLayoutManager(getContext(), 3);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);

        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                SettingsFragment settingsFragment = new SettingsFragment();
                FTAG = "Settings Fragment";
                transaction.replace(R.id.content, settingsFragment, "Settings Fragment");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        //thumb1view= coverpic
        coverpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoom = true;
                ZOOMTAG = "Profile Fragment";
                zoomImageFromThumb(coverpic, coveruri);
            }
        });

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

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


        try {


            databaseReference.child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                    assert user != null;
                    try {

                        Log.e(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());

                        try {
                            profilename.setText(user.getName());
                        } catch (Exception e) {
                            e.getMessage();
                        }

                        coveruri = user.getCoverPhoto();
                        Glide.with(getContext()).load(user.getThumbnailProfilephoto())
                                .placeholder(R.drawable.placeholder)
                                .into(profile);

                        Glide.with(getContext()).load(user.getThumbnailCoverPhoto())
                                .placeholder(R.drawable.placeholder)
                                .into(coverpic);

                        Log.e(TAG, "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getPhoto() + "    " + user.getCoverPhoto());
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
        //Friend count

        try {

            databaseReference.child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
                long numberoffriends = 0;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    numberoffriends = dataSnapshot.getChildrenCount();

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        String Friendid = childDataSnapshot.getKey();


                        Log.e("Friends Id", Friendid);
                        try {
                            databaseReference.child("Friends").child(String.valueOf(Friendid)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Friends friends = dataSnapshot.getValue(Friends.class);

                                    assert friends != null;
                                    if (friends.getStatus().equals("Accept")) {
                                        numberoffriends++;
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
            databaseReference.child("User Pictures").addListenerForSingleValueEvent(new ValueEventListener() {
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

        try {
            FirebaseRecyclerAdapter<Image, ProfileViewHelper> firebaseRecyclerAdapter = new
                    FirebaseRecyclerAdapter<Image, ProfileViewHelper>(

                            Image.class,
                            R.layout.res_thumbnail,
                            ProfileViewHelper.class,
                            databaseReference.child("User Pictures").orderByPriority()


                    ) {
                        @Override
                        protected void populateViewHolder(ProfileViewHelper viewHolder, Image model, int position) {

                            PicName = getRef(position).getKey();

                            String picname = PicName;

                            Log.e("Images ", picname);
                            viewHolder.setpic(context, user_id, picname);


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

