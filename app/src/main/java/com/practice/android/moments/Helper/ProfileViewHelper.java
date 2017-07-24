package com.practice.android.moments.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Models.Image;
import com.practice.android.moments.R;

/**
 * Created by Hitesh Goel on 12-07-2017.
 * Made by Hitesh Goel
 */

public class ProfileViewHelper extends RecyclerView.ViewHolder {


    View mView;
    DatabaseReference database;
    public ImageView profile;

    public ProfileViewHelper(View itemView) {
        super(itemView);
        database = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        mView = itemView;

        profile = (ImageView) mView.findViewById(R.id.thumbnail);
    }

    public void setpic(Context context, String user_id, String picName) {


        database.child(user_id).child("User Pictures").child(picName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Image user = dataSnapshot.getValue(Image.class);

                assert user != null;

                Glide.with(context).load(user.getMedium())
                        .thumbnail(Glide.with(context).load(R.drawable.loader))
                        .into(profile);

                Log.e("PROFILE PIC", "\n" + user.getMedium());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
