package com.practice.android.moments.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.Models.Blog;
import com.practice.android.moments.R;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;


public class TimelineFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    String user_id;
    String user_name;
    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_timeline, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id)
                    .child("User Pictures");
        } catch (NullPointerException e) {
            Log.i("TimelineFrag", e.getMessage());
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm_recycle = new LinearLayoutManager(getActivity());
        lm_recycle.setReverseLayout(true);
        recyclerView.setLayoutManager(lm_recycle);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
            user_name = firebaseUser.getDisplayName();
        } catch (NullPointerException e) {
            Log.e(e.getMessage(), "Error");
        }
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
//
//                Blog.class,
//                R.layout.row_item,
//                BlogViewHolder.class,
//                databaseReference
//
//        ) {
//            @Override
//            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
//
////                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDescription(model.getDescription());
//                viewHolder.setPic(getApplicationContext(), model.getPic());
//
//
//            }
//        };
//
//
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.row_item,
                BlogViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

//                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPic(getApplicationContext(), model.getPic());


            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {


        View mView;


        public BlogViewHolder(View itemView) {
            super(itemView);


            mView = itemView;
        }


        public void setTitle(String title) {

            TextView Blog_title = (TextView) mView.findViewById(R.id.username);
            Blog_title.setText(title);

        }

        public void setDescription(String description) {

            TextView Blog_descption = (TextView) mView.findViewById(R.id._description);
            Blog_descption.setText(description);

        }


        public void setPic(Context context, String photo) {

            ImageView imageView = (ImageView) mView.findViewById(R.id.image);
            Picasso.with(context).load(photo).into(imageView);

        }


    }
}
