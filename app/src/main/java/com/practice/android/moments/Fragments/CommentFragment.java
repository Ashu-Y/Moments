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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Models.Comment;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import java.util.Calendar;


public class CommentFragment extends Fragment {

    RecyclerView recyclerView;
    String name;
    EditText user_comment;
    ImageView send;
    FirebaseUser firebaseUser;
    String user_id;
    Context context;
    DatabaseReference databaseReference, data;


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
        lm_recycle.setReverseLayout(true);
        lm_recycle.setStackFromEnd(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);


        user_comment.setText("");
        send = (ImageView) v.findViewById(R.id.post_comment);
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Comments");

        Log.e("Comment Fragment", name);

        data = databaseReference.child(name).child("Users");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);
                int seconds = c.get(Calendar.SECOND);
                int milliSeconds = c.get(Calendar.MILLISECOND);


                //Can cause error in uploading
                String imageName = day + "-" + month + "-" + year + "-" + hour + ":" + minutes + ":" + seconds + ":" + milliSeconds + "";
                Log.e("Camera", imageName);


                DatabaseReference database = data.child(imageName);
                database.child("user_id").setValue(user_id);

                database.child("comment").setValue(user_comment.getText().toString());
                Log.e("usercomments", user_comment.getText().toString());

                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();


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

                viewHolder.setuser(context, name, pos);

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

        public CommentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            database = FirebaseDatabase.getInstance().getReference()
                    .child("Users");

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments");
            getname = (TextView) mView.findViewById(R.id.comment_user_name);
            user_comment = (TextView) mView.findViewById(R.id.user_comment);


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
                            Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());
                            getname.setText(user.getName());

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
