package com.practice.android.moments.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Activities.AddFriendProfileActivity;
import com.practice.android.moments.Models.Friends;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import static android.support.v7.appcompat.R.color.accent_material_dark;
import static com.practice.android.moments.Fragments.LikeCommentNotification.nonotifactiontext;
import static com.practice.android.moments.R.color.Request_send;
import static com.practice.android.moments.R.color.foreground_material_dark;


public class FriendsFragment extends Fragment {

    public static TextView ifnofriend;
    static String noFriend = "No Friends Yet";
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;
    LinearLayoutManager lm_recycle;
    String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        nonotifactiontext.setVisibility(View.GONE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Friends").child("All");

        Log.e("friends===", databaseReference.toString());
        recyclerView = (RecyclerView) view.findViewById(R.id.reycler);
        ifnofriend = (TextView) view.findViewById(R.id.onfriendtext);

        recyclerView.setHasFixedSize(true);
        lm_recycle = new LinearLayoutManager(getContext());
        lm_recycle.setReverseLayout(true);
        lm_recycle.setStackFromEnd(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);

        ifnofriend.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        return view;
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
                        Log.e("dataSnapshotKey", dataSnapshot.getValue() + noFriend);
                        ifnofriend.setText(noFriend);
                        ifnofriend.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<Friends, FriendsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsHolder>(
                Friends.class,
                R.layout.friend_recycler,
                FriendsHolder.class,
                databaseReference.orderByValue()
        ) {
            @Override
            protected void populateViewHolder(FriendsHolder viewHolder, Friends model, int position) {
                ifnofriend.setVisibility(View.GONE);
                String user_id = getRef(position).getKey();

                Log.e("user requested", user_id);
                viewHolder.setStatus(user_id);


                viewHolder.friendname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(getContext(), AddFriendProfileActivity.class);
                        i.putExtra("User_id", user_id);
                        getContext().startActivity(i);
                    }
                });

                viewHolder.friendstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        viewHolder.friend = true;
                        try {
                            viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends")
                                    .child("All").addValueEventListener(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (viewHolder.friend) {
                                        if (dataSnapshot.hasChild(user_id)) {
                                            Friends friends = dataSnapshot.child(user_id).getValue(Friends.class);

                                            assert friends != null;
                                            String Status = friends.getStatus();
                                            switch (Status) {
                                                case "Request send":

                                                    viewHolder.friendstatus.setClickable(false);

                                                    break;
                                                case "Requested":

                                                    viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends").child("All")
                                                            .child(user_id).child("status").setValue("Accept");

                                                    viewHolder.mdatabaseReference.child(user_id).child("Friends").child("All").child(viewHolder.currentuser_id)
                                                            .child("status").setValue("Accept");

                                                    viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends").child("Accepted Friends")
                                                            .child(user_id).child("status").setValue("Accept");

                                                    viewHolder.mdatabaseReference.child(user_id).child("Friends").child("Accepted Friends").child(viewHolder.currentuser_id)
                                                            .child("status").setValue("Accept");

                                                    viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends").child("Accepted Friends")
                                                            .child(user_id).child("userName").setValue(viewHolder.Friendname);


                                                    viewHolder.mdatabaseReference.child(user_id).child("Friends").child("Accepted Friends")
                                                            .child(viewHolder.currentuser_id).child("userName").setValue(viewHolder.myname);


                                                    viewHolder.friendstatus.setText("Friends");

                                                    break;
                                                case "Accept":
                                                    viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends")
                                                            .child("All").child(user_id).removeValue();
                                                    viewHolder.mdatabaseReference.child(user_id).child("Friends")
                                                            .child("All").child(viewHolder.currentuser_id).removeValue();

                                                    viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends").child("Accepted Friends")
                                                            .child(user_id).removeValue();
                                                    viewHolder.mdatabaseReference.child(user_id).child("Friends").child("Accepted Friends")
                                                            .child(viewHolder.currentuser_id).removeValue();


                                                    viewHolder.friendstatus.setText("Add Friend");

                                                    break;
                                            }


                                            viewHolder.friend = false;
                                        } else {

                                            viewHolder.mdatabaseReference.child(viewHolder.currentuser_id)
                                                    .child("User Info").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);
                                                    assert user != null;

                                                    viewHolder.myname = user.getName();
                                                    Log.e("My name is ===", viewHolder.myname);

                                                    viewHolder.mdatabaseReference.child(user_id).child("User Info").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);
                                                            assert user != null;

                                                            viewHolder.Friendname = user.getName();
                                                            Log.e("My Friend name is ===", viewHolder.Friendname);

                                                            viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends").child("All")
                                                                    .child(user_id).child("userName").setValue(viewHolder.Friendname);

                                                            viewHolder.mdatabaseReference.child(viewHolder.currentuser_id).child("Friends").child("All")
                                                                    .child(user_id).child("status").setValue("Request send");

                                                            viewHolder.mdatabaseReference.child(user_id).child("Friends").child("All")
                                                                    .child(viewHolder.currentuser_id).child("userName").setValue(viewHolder.myname);

                                                            viewHolder.mdatabaseReference.child(user_id).child("Friends").child("All")
                                                                    .child(viewHolder.currentuser_id).child("status").setValue("Requested");
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
                                            viewHolder.friend = false;
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } catch (NullPointerException e) {
                            e.getMessage();
                        }
                    }
                });
            }
        };


        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }


    public static class FriendsHolder extends RecyclerView.ViewHolder {

        TextView friendname;
        Button friendstatus;
        DatabaseReference mdatabaseReference;
        FirebaseUser firebaseUser;
        String Friendname = null;
        String myname = null;
        String currentuser_id;
        Boolean friend = false;

        public FriendsHolder(View itemView) {
            super(itemView);
            mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            friendname = (TextView) itemView.findViewById(R.id.friendusername);
            friendstatus = (Button) itemView.findViewById(R.id.frienduserstatus);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            currentuser_id = firebaseUser.getUid();
            myname = firebaseUser.getDisplayName();
        }


        public void setStatus(String user_id) {


            mdatabaseReference.child(currentuser_id).child("Friends").child("All").addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.hasChild(user_id)) {
                        Friends friends = dataSnapshot.child(user_id).getValue(Friends.class);

                        assert friends != null;
                        String Status = friends.getStatus();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(user_id).child("User Info");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                                assert user != null;
                                friendname.setText(user.getName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        try {
                            switch (Status) {
                                case "Requested": {
                                    friendstatus.setBackgroundResource(foreground_material_dark);
                                    friendstatus.setText("Requested");
                                    break;
                                }
                                case "Accept": {
                                    friendstatus.setBackgroundResource(accent_material_dark);
                                    friendstatus.setText("Friends");
                                    break;
                                }
                                case "Request send": {
                                    friendstatus.setBackgroundResource(Request_send);
                                    friendstatus.setText("Request Send");
                                    break;
                                }
                                default: {
                                    friendstatus.setText("Add Friend");
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}

