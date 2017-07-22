package com.practice.android.moments.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.practice.android.moments.Models.notification;
import com.practice.android.moments.R;

public class LikeCommentNotification extends Fragment {

    DatabaseReference databaseReference;
    String userid;
    FirebaseUser firebaseUser;
    Context context;
    RecyclerView recyclerView;
    TextView onliketext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_likenotify, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid = firebaseUser.getUid();
        recyclerView = (RecyclerView) v.findViewById(R.id.likecommentn);
        onliketext = (TextView) v.findViewById(R.id.onliketext);
        context = getActivity();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(userid).child("Notification");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // String key = dataSnapshot.getValue(String.class);

                try {
                    if (null == dataSnapshot.getValue(String.class)) {
                        Log.e("dataSnapshotKey", dataSnapshot.getValue() + "Hello");
                        Toast.makeText(context, "NO comments", Toast.LENGTH_LONG).show();
                        recyclerView.setVisibility(View.GONE);
                    } else {

                        recyclerView();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void recyclerView() {

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

                Log.e("Database Values will be", posvalu);


                viewHolder.getnotify(pos);
            }


        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }


    public static class Notification extends RecyclerView.ViewHolder {

        View mview;

        public Notification(View itemView) {
            super(itemView);

            mview = itemView;
        }

        public void getnotify(String position) {

        }
    }


}
