package com.practice.android.moments.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Editing.EditingActivity;
import com.practice.android.moments.Fragments.DashboardFragment;
import com.practice.android.moments.Fragments.ProfileFragment;
import com.practice.android.moments.Fragments.SearchFragment;
import com.practice.android.moments.Fragments.TimelineFragment;
import com.practice.android.moments.Fragments.Upload_picture;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.BottomNavigationViewHelper;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomNavigation extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    TimelineFragment mTimelineFragment;
    DashboardFragment mDashboardFragment;
    SearchFragment mSearchFragment;
    ProfileFragment mProfileFragment;
    Upload_picture mUpload_pictureFragment;
    FragmentManager mFragmentManager;
    CommentFragment mCommentFragment;
    LikeFragment mLikeFragment;
    GoogleApiClient googleApiClient;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FrameLayout fl;
    String user_id;
    String user_name;
    Display display;
    Context context;
    Point size;
    FirebaseAuth firebaseAuth;
    Boolean picLike;
    FirebaseUser firebaseUser;
    BottomNavigationView navigation;
    String PicName;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    private String TAG = getClass().getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fl.getLayoutParams().height = 0;
                    fl.requestLayout();

                    if (mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mTimelineFragment);
                        transaction.commit();
                    }
                    if (mProfileFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mProfileFragment);
                        transaction.commit();
                    }

                    if (mSearchFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mSearchFragment);
                        transaction.commit();
                    }

                    if (mUpload_pictureFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mUpload_pictureFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

//
                    return true;

                case R.id.navigation_upload:

                    if (!mUpload_pictureFragment.isAdded()) {

                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();


                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mUpload_pictureFragment, "Upload Picture Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_search:

                    fl.setMinimumHeight(size.y);
                    fl.getLayoutParams().height = size.y;
                    fl.requestLayout();

//                    if (!mDashboardFragment.isAdded()) {
//                        fl.setMinimumHeight(size.y);
//                        fl.getLayoutParams().height = size.y;
//                        fl.requestLayout();
//
//                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//                        transaction.replace(R.id.content, mDashboardFragment, "Timeline Fragment");
//                        transaction.addToBackStack("Timeline");
//                        transaction.commit();
//                    }

                    if (!mSearchFragment.isAdded()) {
                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();

                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mSearchFragment, "Search Fragment");
                        transaction.commit();

                    }
                    LogoutButton();


                    return true;
                case R.id.navigation_editing:

                    fl.setMinimumHeight(size.y);
                    fl.getLayoutParams().height = size.y;
                    fl.requestLayout();

                    if (mTimelineFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mTimelineFragment);
                        transaction.commit();
                    }
                    if (mDashboardFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mDashboardFragment);
                        transaction.commit();
                    }

                    if (mUpload_pictureFragment.isAdded()) {
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.remove(mUpload_pictureFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    startActivity(new Intent(BottomNavigation.this, EditingActivity.class));

                    return true;

                case R.id.navigation_profile:

                    if (!mProfileFragment.isAdded()) {

                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();


                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mProfileFragment, "Profile Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;


            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_navigation);

        picLike = false;
        fl = (FrameLayout) findViewById(R.id.content);
        display = getWindowManager().getDefaultDisplay();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        size = new Point();
        display.getSize(size);

        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("User Pictures");
        } catch (NullPointerException e) {
            Log.i("TimelineFrag", e.getMessage());
        }

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm_recycle = new LinearLayoutManager(this);
        lm_recycle.setReverseLayout(true);
        lm_recycle.setStackFromEnd(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);

        mTimelineFragment = new TimelineFragment();
        mDashboardFragment = new DashboardFragment();
        mSearchFragment = new SearchFragment();
        mCommentFragment = new CommentFragment();
        mLikeFragment = new LikeFragment();
        mProfileFragment = new ProfileFragment();
        mUpload_pictureFragment = new Upload_picture();
        mFragmentManager = getSupportFragmentManager();

        // Google API CLIENT
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Toast.makeText(this,
                        "Check ur connection", Toast.LENGTH_SHORT).show()).addApi(Auth.GOOGLE_SIGN_IN_API).build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

        context = getApplicationContext();
        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.row_item,
                BlogViewHolder.class,
                databaseReference.orderByPriority()

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                PicName = getRef(position).getKey();

                String picname = PicName;

                viewHolder.setUsername(model.getUserName());
                viewHolder.setTitle(model.getTitle());
//                viewHolder.setComment(context);
                viewHolder.setProfilepic(context, model.getUser_id());
                viewHolder.setNumberLike(picname);
                viewHolder.setLike(picname);
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPic(getApplicationContext(), model.getPic());
//                viewHolder.setPic(getApplicationContext(), model.getThumbnail_pic());
                Log.e("PIC KEY AND NAME", PicName + "    Position:" + position);

                viewHolder.comment.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                viewHolder.Like.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        picLike = true;

                        DatabaseReference currentuser_db = viewHolder.mdatabaseReference;

                        currentuser_db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (picLike) {

                                    if (dataSnapshot.child(picname).child("Users").hasChild(firebaseUser.getUid())) {
                                        viewHolder.i = dataSnapshot.child(picname).child("Users").getChildrenCount();
                                        Log.e("No. of likes", String.valueOf(viewHolder.i));
                                        currentuser_db.child(picname).child("Users").child(firebaseUser.getUid()).removeValue();
                                        picLike = false;
                                        if (viewHolder.i > 0) {
                                            viewHolder.i--;
                                            String like = String.valueOf(viewHolder.i);
                                            currentuser_db.child(picname).child("Likes").setValue(like);
                                        } else {
                                            String like = String.valueOf(viewHolder.i);
                                            currentuser_db.child(picname).child("Likes").setValue(like);
                                        }
                                    } else {
                                        viewHolder.i = dataSnapshot.child(picname).child("Users").getChildrenCount();
                                        Log.e("No. of likes", String.valueOf(viewHolder.i));
                                        viewHolder.i++;
                                        String like = String.valueOf(viewHolder.i);
                                        currentuser_db.child(picname).child("Users").child(firebaseUser.getUid()).setValue(firebaseUser.getDisplayName());
                                        currentuser_db.child(picname).child("Likes").setValue(like);
                                        Log.e("Likes=====", like);
                                        picLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();


                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mCommentFragment, "Comment Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });


                viewHolder.Numberlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fl.setMinimumHeight(size.y);
                        fl.getLayoutParams().height = size.y;
                        fl.requestLayout();


                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        transaction.replace(R.id.content, mLikeFragment, "Like Fragment");
                        transaction.addToBackStack(null);
                        transaction.commit();


                    }
                });

                viewHolder.imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Image = model.getPic();
                        zoomImageFromThumb(viewHolder.imageView, Image );
                    }
                });

                mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    private void zoomImageFromThumb(View thumbView, String imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);


//        expandedImageView.setImageResource(imageResId);

        Glide.with(context).load(imageResId)
                .skipMemoryCache(false)
                .placeholder(R.drawable.c1).into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.containerB).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
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
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);


        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();

        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
//                super.onAnimationCancel(animation);
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
                mCurrentAnimator = null;
            }
        });

        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;

        expandedImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }


        });
    }


    public void LogoutButton() {
        Log.i(TAG, "You clicked onClick Button");
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(
                        status -> {
                            Log.i(TAG, "log off from google sign button");
                            Toast.makeText(this, "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Login_method.class));
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath;
        Uri uri;
        Bitmap thumbnail;
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            assert c != null;
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);
            c.close();

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            picturePath = getRealPathFromURI(selectedImageUri);

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    @Override
    public void onBackPressed() {

        if (navigation.getSelectedItemId() != R.id.navigation_home) {
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(BottomNavigation.this);
            builder1.setMessage("You want to exit!!!!");
            builder1.setCancelable(true);


            builder1.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        dialog.cancel();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    });

            builder1.setNegativeButton(
                    "No",
                    (dialog, id) -> {
                        dialog.cancel();
                        Toast.makeText(BottomNavigation.this, "Thank you for Staying back", Toast.LENGTH_SHORT).show();
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder {


        View mView;
        DatabaseReference mdatabaseReference, database;
        CircleImageView profile;
        ImageView comment;
        Long i;

        ImageView imageView, expandImage;

        ImageView Like;
        ImageView imageView;
        FirebaseUser firebaseUser;
        TextView Numberlike;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mdatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Likes");
            database = FirebaseDatabase.getInstance().getReference()
                    .child("Users");

            profile = (CircleImageView) mView.findViewById(R.id.user_profile_photo);
            imageView = (ImageView) mView.findViewById(R.id.image);

            imageView = (ImageView) mView.findViewById(R.id.image);
            expandImage = (ImageView) mView.findViewById(R.id.expanded_image);

            Like = (ImageView) mView.findViewById(R.id.like_btn);
            comment = (ImageView) mView.findViewById(R.id.comment_btn);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Numberlike = (TextView) mView.findViewById(R.id.likes);

        }

//        public void setComment(Context context) {
//            ImageButton comment = (ImageButton) mView.findViewById(R.id.comment_btn);
//
//            comment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "Comment Fragment to open", Toast.LENGTH_SHORT).show();
//                    Log.e("Comment part", "   Comment ");
//
//
////                    CommentFragment fragment2 = new CommentFragment();
////
////
////                    fl.setMinimumHeight(size.y);
////                    fl.getLayoutParams().height = size.y;
////                    fl.requestLayout();
////
////                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
////                    transaction.replace(R.id.content, fragment2, "Comment Fragment");
////                    transaction.addToBackStack("Comment");
////                    transaction.commit();
//                }
//
//            });
//        }

        public void setLike(String ImageName) {
            DatabaseReference currentuser_db = mdatabaseReference;

            currentuser_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(ImageName).child("Users").hasChild(firebaseUser.getUid())) {

                        Like.setImageResource(R.drawable.like2);

                    } else {
                        Like.setImageResource(R.drawable.like);
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setNumberLike(String ImageName) {
            DatabaseReference currentuser_db = mdatabaseReference;

            currentuser_db.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Long i = dataSnapshot.child(ImageName).child("Users").getChildrenCount();

                    if (i == 1 || i == 0) {
                        Numberlike.setText(i + " friend liked your post");
                    } else if (i > 1) {
                        Numberlike.setText(i + " friends liked your post");
                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        public void setUsername(String username) {

            TextView Blog_title = (TextView) mView.findViewById(R.id.username);
            Blog_title.setText(username);

            Log.e("Username =======", Blog_title.getText().toString());
        }

        public void setTitle(String title) {

            TextView Blog_title = (TextView) mView.findViewById(R.id._title);
            Blog_title.setText(title);

            Log.e("Title =======", Blog_title.getText().toString());
        }

        public void setDescription(String description) {

            TextView Blog_description = (TextView) mView.findViewById(R.id._description);
            Blog_description.setText(description);

            Log.e("Description =======", Blog_description.getText().toString());
        }


        public void setPic(Context context, String photo) {

            Glide.with(context).load(photo)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.c1).into(imageView);
//            Picasso.with(context).load(photo).placeholder(R.drawable.c1).into(imageView);

            Log.e("Image URl =======", photo);

        }


        public void setProfilepic(Context context, String user_id) {

            Log.e("USER ID", user_id);

            database.child(user_id).child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                    assert user != null;

                    Glide.with(context).load(user.getThumbnailProfilephoto()).placeholder(R.drawable.c1).into(profile);

                    Log.e("PROFILE PIC", "\n" + user.getThumbnailProfilephoto());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


//            Glide.with(context).load(photo)
//                    .skipMemoryCache(false)
//                    .placeholder(R.drawable.c1).into(profile);

        }
    }
}

