package com.practice.android.moments.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Activities.AddFriendProfileActivity;
import com.practice.android.moments.Activities.BottomNavigation;
import com.practice.android.moments.Helper.ServiceHandler;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.practice.android.moments.Activities.BottomNavigation.description;
import static com.practice.android.moments.Activities.BottomNavigation.deviceToken;
import static com.practice.android.moments.Activities.BottomNavigation.google_key;
import static com.practice.android.moments.Activities.BottomNavigation.heading;
import static com.practice.android.moments.Activities.BottomNavigation.image;
import static com.practice.android.moments.Activities.BottomNavigation.imageusertoken;
import static com.practice.android.moments.Activities.BottomNavigation.jsonStr;
import static com.practice.android.moments.Activities.BottomNavigation.mServiceHandler;


public class NotificationImage extends Fragment {
    public final String TAG = getClass().getSimpleName();
    String mName = null; //notification id
    String imageName = null; //imageid
    DatabaseReference databaseReference, mdatabaseReference, databaseReference3, database, databaseReference5, databaseReference4;
    CircleImageView profile;
    ImageView comment;
    Long i;
    ImageView imageView, expandImage;

    ImageView Like;
    FirebaseUser firebaseUser;
    TextView Numberlike;
    TextView Blog_user_name, Blog_title, Blog_description;

    LikeFragment mLikeFragment;
    CommentFragment mCommentFragment;
    FragmentManager mFragmentManager;

    Boolean picLike = false;
    String user_id;
    Context context;

    public NotificationImage() {
        // Required empty public constructor
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_image, container, false);

        profile = (CircleImageView) view.findViewById(R.id.user_profile_photo);


        context = getContext();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Likes");
        database = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Pictures");

        mLikeFragment = new LikeFragment();
        mCommentFragment = new CommentFragment();
        mFragmentManager = getFragmentManager();

        imageView = (ImageView) view.findViewById(R.id.image);
        expandImage = (ImageView) view.findViewById(R.id.expanded_image);

        Like = (ImageView) view.findViewById(R.id.like_btn);
        comment = (ImageView) view.findViewById(R.id.comment_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        user_id = firebaseUser.getUid();

        Numberlike = (TextView) view.findViewById(R.id.likes);

        Blog_user_name = (TextView) view.findViewById(R.id.username);
        Blog_title = (TextView) view.findViewById(R.id._title);

        Blog_description = (TextView) view.findViewById(R.id._description);


        if (imageName != null && mName != null) {
            Log.e("From LIKE COMMENT NOTIFICATION inside ", imageName + "\n" + mName);
            try {
                databaseReference.child(imageName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Blog image = dataSnapshot.getValue(Blog.class);
                        assert image != null;
                        setLike(imageName);
                        setNumberLike(imageName);

                        setTitle(image.getTitle());
                        setDescription(image.getDescription());

//                        setPic(getContext(), image.getMedium());


                        database.child(image.getUser_id()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                                assert user != null;
                                setUsername(user.getName());
//                                setProfilepic(getContext(), user.getThumbnailProfilephoto());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.getMessage();
            }
        } else if (mName == null && imageName != null) {

            Log.e("From BOTTOM NOTIFICATION inside ", "\n" + imageName);
            try {
                databaseReference.child(imageName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Blog image = dataSnapshot.getValue(Blog.class);
                        assert image != null;
                        setLike(imageName);
                        setNumberLike(imageName);

                        setTitle(image.getTitle());
                        setDescription(image.getDescription());

                        setPic(context, image.getMedium());
                        setUsername(image.getUserName());
                        Log.e("image user name", image.getUser_id());

                        database.child(image.getUser_id()).child("User Info").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                                assert user != null;
                                Log.e("image user name", user.getName());
                                setUsername(user.getName());
                                setProfilepic(context, user.getThumbnailProfilephoto());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                e.getMessage();
            }

        }

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        int milliSeconds = c.get(Calendar.MILLISECOND);


        //Can cause error in uploading
        imageName = day + "-" + month + "-" + year + "-" + hour + ":" + minutes + ":" + seconds + ":" + milliSeconds + "" + user_id;
        Log.e("Camera", imageName);
        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picLike = true;

                DatabaseReference currentuser_db = mdatabaseReference;

                currentuser_db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (picLike) {

                            if (dataSnapshot.child(imageName).child("Users").hasChild(firebaseUser.getUid())) {
                                i = dataSnapshot.child(imageName).child("Users").getChildrenCount();
                                Log.e("No. of likes", String.valueOf(i));
                                currentuser_db.child(imageName).child("Users").child(firebaseUser.getUid()).removeValue();
                                picLike = false;
                                if (i > 0) {
                                    i--;
                                    String like = String.valueOf(i);
                                    currentuser_db.child(imageName).child("Likes").setValue(like);
                                } else {
                                    String like = String.valueOf(i);
                                    currentuser_db.child(imageName).child("Likes").setValue(like);
                                }
                            } else {
                                i = dataSnapshot.child(imageName).child("Users").getChildrenCount();
                                Log.e("No. of likes", String.valueOf(i));
                                i++;
                                String like = String.valueOf(i);
                                currentuser_db.child(imageName).child("Users").child(firebaseUser.getUid()).setValue(firebaseUser.getDisplayName());
                                currentuser_db.child(imageName).child("Likes").setValue(like);
                                Log.e("Likes=====", like);
                                picLike = false;
                                //Post method

                                databaseReference5 = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(user_id).child("Notification");

                                databaseReference3 = FirebaseDatabase.getInstance().getReference()
                                        .child("User Pictures").child(imageName);


                                databaseReference3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Log.e("numberof elements", String.valueOf(dataSnapshot.getChildrenCount()));
                                        Blog user = dataSnapshot.getValue(Blog.class);

                                        assert user != null;
                                        Log.e("User name: ", user.getPicName() + "\n" + user.getThumbnail_pic());

                                        BottomNavigation.imageurl = user.getPic();

                                        String userid = user.getUser_id();

                                        databaseReference4 = FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(userid).child("User Info");
                                        databaseReference5 = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(userid).child("Notification");
                                        //notification sender
                                        databaseReference5.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {




                                                DatabaseReference rootReference = databaseReference5.child(imageName);
                                                rootReference.child("frienduserid").setValue(user_id);
                                                rootReference.child("userimageid").setValue(imageName);
                                                rootReference.child("status").setValue("Like");


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        //notification sender


                                        databaseReference4.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Profile_model_class userinfo = dataSnapshot.getValue(Profile_model_class.class);

                                                assert userinfo != null;
                                                imageusertoken = userinfo.getUserToken();

                                                Log.e(TAG, "User Token        \n" + imageusertoken);

                                                BottomNavigation.setValues(userinfo.getUserToken(), user.getPic());
                                                new SendAsync().execute();

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                        Log.e("image details", imageusertoken + "\n" + BottomNavigation.imageurl);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        Blog_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity(), AddFriendProfileActivity.class);
                i.putExtra("imagename", imageName);
                startActivity(i);


            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Name", "Comment Fragment Called");


                mCommentFragment.setImageResourceName(imageName);


                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.remove(mCommentFragment);
                transaction.add(R.id.content, mCommentFragment, "Comment Fragment");
                BottomNavigation.FTAG = "Comment Fragment";
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        Numberlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mLikeFragment.setImageResourceName(imageName);

                Log.e("Pic Name", imageName);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.remove(mLikeFragment);
                transaction.add(R.id.content, mLikeFragment, "Like Fragment");
                BottomNavigation.FTAG = "Like Fragment";
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        return view;
    }


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

        Blog_user_name.setText(username);

        Log.e("Username =======", Blog_user_name.getText().toString());
    }

    public void setTitle(String title) {

        Blog_title.setText(title);

        Log.e("Title =======", Blog_title.getText().toString());
    }

    public void setDescription(String description) {

        Blog_description.setText(description);

        Log.e("Description =======", Blog_description.getText().toString());
    }


    public void setPic(Context context, String photo) {

        Glide.with(getContext()).load(photo)
                .skipMemoryCache(false)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        Log.e("Image URl =======", photo);

    }

    public void setProfilepic(Context context, String image) {

        Log.e("USER ID", image);

        Glide.with(context).load(image)
                .placeholder(R.drawable.placeholder)
                .into(profile);
        Log.e("Image URl =======", image);
    }


    @SuppressLint("LongLogTag")
    public void setImageResourceName(String imagename, String name) {
        this.imageName = imagename;
        this.mName = name;

        Log.e("From LIKE COMMENT NOTIFICATION outside", imageName + "\n" + mName);
    }


    @SuppressLint("LongLogTag")
    public void setImageResource(String imagename) {

        this.imageName = imagename;

        Log.e("From BOTTOM NOTIFICATION outside", imageName);
    }

    public class SendAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {


            mServiceHandler = new ServiceHandler(getActivity());

            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("google_key", google_key));
            nameValuePairs.add(new BasicNameValuePair("deviceToken", deviceToken));
            nameValuePairs.add(new BasicNameValuePair("heading", heading));
            nameValuePairs.add(new BasicNameValuePair("description", description));
            nameValuePairs.add(new BasicNameValuePair("image", image));

            jsonStr = mServiceHandler.makeServiceCall("http://appzynga.com/projects/trainee/and_notifications/notification.php/",
                    ServiceHandler.POST,
                    nameValuePairs);

            Log.e("Notifyyyy", jsonStr);


            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }
}
