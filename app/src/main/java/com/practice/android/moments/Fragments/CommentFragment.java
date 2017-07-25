package com.practice.android.moments.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.Models.Comment;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentFragment extends Fragment {

    RecyclerView recyclerView;
    String name;
    EditText user_comment;
    ImageView send;
    FirebaseUser firebaseUser;
    String user_id;
    String imageName;
    Context context;
    DatabaseReference databaseReference, databaseReference4, data, databaseReference5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comment, container, false);


        Log.e("Comment Fragment", name);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
        } catch (NullPointerException e) {

            e.getMessage();
        }
        user_comment = (EditText) v.findViewById(R.id.comment);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_comment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm_recycle = new LinearLayoutManager(context);
//        lm_recycle.setReverseLayout(true);
        lm_recycle.setStackFromEnd(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);


        user_comment.setText(null);
        send = (ImageView) v.findViewById(R.id.post_comment);
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Comments");

        Log.e("Comment Fragment", name);

        data = databaseReference.child(name).child("Users");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!user_comment.getText().toString().equals("")) {

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


                    DatabaseReference database = data.child(imageName);
                    database.child("user_id").setValue(user_id);

                    database.child("comment").setValue(user_comment.getText().toString());
                    Log.e("usercomments", user_comment.getText().toString());

                    user_comment.setText("");

                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();


                    Log.e("imageName", name);
                    databaseReference4 = FirebaseDatabase.getInstance().getReference().child("User Pictures")
                            .child(name);
                    Log.e("imageName ", name + "database" + databaseReference4);
                    databaseReference4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Blog image = dataSnapshot.getValue(Blog.class);
                            assert image != null;

                            Log.e("info=====", image.getUser_id());


                            databaseReference5 = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(image.getUser_id()).child("Notification");

                            Log.e("imageName ", image.getUser_id() + "database" + databaseReference5);
                            databaseReference5.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    DatabaseReference rootReference = databaseReference5.child(imageName);
                                    rootReference.child("frienduserid").setValue(user_id);
                                    rootReference.child("userimageid").setValue(name);
                                    rootReference.child("status").setValue("Comment");


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


                } else {
                    user_comment.setError("Empty Comment");
                    Toast.makeText(getContext(), "Empty Comment", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }

    public void setImageResourceName(String name) {
        this.name = name;

        Log.e("Comment Fragment Method", name);
    }


    @Override
    public void onResume() {
        super.onResume();
        user_comment.setText("");

        context = getContext();
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // String key = dataSnapshot.getValue(String.class);

                try {
                    if (null == dataSnapshot.getValue(String.class)) {
                        Log.e("dataSnapshotKey", dataSnapshot.getValue() + "Hello");
                        Toast.makeText(context, "NO comments", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<Comment, CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(
                Comment.class,
                R.layout.comment_recycler,
                CommentViewHolder.class,
                data.orderByValue()
        ) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position) {

                String pos = getRef(position).getKey();
                Log.e("POSITION++++++", pos);
                String posvalu = String.valueOf(getRef(position).getDatabase());

                Log.e("Database Values will be", posvalu);

                if (pos.equals("")) {
                    Log.e("NO Comment", pos);
                    Toast.makeText(context, "NO Comment yet", Toast.LENGTH_SHORT).show();

                } else {
                    viewHolder.setuser(context, name, pos);

                }
            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public static String TAG = "Comment TAG";
        TextView getname, user_comment;
        View mView;
        String userid;
        DatabaseReference databaseReference, database, data;
        CircleImageView user_image;

        public CommentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            database = FirebaseDatabase.getInstance().getReference()
                    .child("Users");

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments");
            getname = (TextView) mView.findViewById(R.id.comment_user_name);
            user_comment = (TextView) mView.findViewById(R.id.user_comment);
            user_image = (CircleImageView) mView.findViewById(R.id.user_profile_photo);


        }

        public void setuser(Context context, String ImageName, String pos) {
            data = databaseReference.child(ImageName).child("Users");

            data.child(pos).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Comment comment_user = dataSnapshot.getValue(Comment.class);

                    assert comment_user != null;
                    userid = comment_user.getUser_id();

                    user_comment.setText(comment_user.getComment());


                    database.child(userid).child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                            assert user != null;
                            Log.e(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());
                            getname.setText(user.getName());

                            Glide.with(context).load(user.getThumbnailProfilephoto())
                                    .placeholder(R.drawable.placeholder)
                                    .into(user_image);


                            Log.e(TAG, "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getRelationship() + "    " + user.getAbout());
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


        }
    }


}
