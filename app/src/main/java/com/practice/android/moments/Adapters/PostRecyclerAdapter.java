package com.practice.android.moments.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;

import java.util.ArrayList;

/**
 * Created by Ashutosh on 6/15/2017.
 */

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.MyViewHolder> {
    ArrayList<Post> mPosts;
    Context mContext;

    public PostRecyclerAdapter(Context context, ArrayList<Post> posts) {
        mPosts = posts;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row_itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(row_itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = mPosts.get(position);

        holder.setData(post, position);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        ImageView image;
        ImageView like, comment;

        public MyViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            image = (ImageView) itemView.findViewById(R.id.image);
//            like = (ImageView) itemView.findViewById(R.id.like_btn);
//            comment = (ImageView) itemView.findViewById(R.id.comment_btn);
        }

        public void setData(Post post, int position) {
            username.setText(post.getUsername());

            Glide.with(mContext)
                    .load(post.getImageId())
                    .placeholder(R.drawable.placeholder)
                    .fitCenter()
                    .into(image);

        }
    }
}
