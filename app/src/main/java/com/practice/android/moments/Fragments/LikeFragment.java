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
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class LikeFragment extends Fragment {


    public static String mname;
    Context context;
    DatabaseReference mdatabaseReference, databaseReference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    TextView textname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_like, container, false);

//        textname = (TextView) v.findViewById(R.id.image_name_id);
//        textname.setText(mname);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_like);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm_recycle = new LinearLayoutManager(context);
        lm_recycle.setReverseLayout(true);
        lm_recycle.setStackFromEnd(true);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(lm_recycle);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(mname).child("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");


        return v;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        context = getContext();
//        FirebaseRecyclerAdapter<String, LikeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, LikeViewHolder>(
//                String.class,
//                R.layout.likeuserrow,
//                LikeViewHolder.class,
//                mdatabaseReference
//        ) {
//            @Override
//            protected void populateViewHolder(LikeViewHolder viewHolder, String model, int position) {
//
//                String pos = getRef(position).getKey();
//                Log.e("POSITION++++++", pos);
//
//                viewHolder.setpic(context, pos);
//            }
//        };
//
//        firebaseRecyclerAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//
//
//    }

    @Override
    public void onResume() {
        super.onResume();

        context = getContext();
        FirebaseRecyclerAdapter<String, LikeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, LikeViewHolder>(
                String.class,
                R.layout.likeuserrow,
                LikeViewHolder.class,
                mdatabaseReference
        ) {
            @Override
            protected void populateViewHolder(LikeViewHolder viewHolder, String model, int position) {

                String pos = getRef(position).getKey();
                Log.e("POSITION++++++", pos);

                viewHolder.setpic(context, pos);
            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public void setImageResourceName(String name) {
        this.mname = name;
    }


    public static class LikeViewHolder extends RecyclerView.ViewHolder {


        View mView;
        DatabaseReference database, databaseReference;
        CircleImageView profile;
        TextView Username;
        String user_id = null;
        FirebaseUser firebaseUser;
        ArrayList<Iterable<DataSnapshot>> uid;

        public LikeViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            database = FirebaseDatabase.getInstance().getReference()
                    .child("Users");
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Likes");
            profile = (CircleImageView) mView.findViewById(R.id.like_user_profile_photo);
            Username = (TextView) mView.findViewById(R.id.Like_user_name);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            uid = new ArrayList<>();
        }


        public void setpic(Context context, String mname) {

            database.child(String.valueOf(mname)).child("User Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                    assert user != null;

                    Username.setText(user.getName());


                    Glide.with(context).load(user.getPhoto())
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.c1).into(profile);

                    Log.e("PROFILE PIC", "\n" + user.getPhoto());
                    Log.e("PROFILE Name", "\n" + user.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }


}
