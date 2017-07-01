package com.practice.android.moments.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.android.moments.Activities.ChangePassword;
import com.practice.android.moments.Activities.PhotoVideosdatabase;
import com.practice.android.moments.Activities.SettingsActivity;
import com.practice.android.moments.Editing.EditingActivity;
import com.practice.android.moments.Fragments.DashboardFragment;
import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ashutosh on 6/28/2017.
 */

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashViewHolder> {

    DashboardFragment dashFragment;
    private List<Post> itemList;
    private Context mContext;

    public DashboardRecyclerAdapter(Context context, List<Post> itemList, DashboardFragment dashFragment) {
        this.itemList = itemList;
        mContext = context;
        this.dashFragment = dashFragment;

    }

    @Override
    public DashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dash_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item, parent, false);
        DashViewHolder holder = new DashViewHolder(dash_layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(DashViewHolder holder, int position) {
//        holder.setData(itemList.get(position), position);

        holder.dashText.setText(itemList.get(position).getUsername());

//        Picasso.with(mContext)
//                .load(itemList.get(position).getImageId())
//                .into(holder.dashImage);

        Picasso.with(mContext)
                .load(itemList.get(position).getImageId())

                .into(holder.dashImage);
//
//          holder.dashImage.setImageResource(itemList.get(position).getImageId());

        holder.dashCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Post value = itemList.get(position);
                Toast.makeText(mContext, value.getUsername(), Toast.LENGTH_SHORT).show();
                switch (value.getUsername().toUpperCase()) {
                    case "EDITING":
                        mContext.startActivity(new Intent(mContext, EditingActivity.class));
                        break;

                    case "UPLOAD":
                        mContext.startActivity(new Intent(mContext, PhotoVideosdatabase.class));
                        break;

                    case "PROFILE":
                        dashFragment.addProfile();
                        break;

                    case "EDIT PROFILE":
                        dashFragment.addEditProfile();
                        break;

                    case "SETTINGS":
                        mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                        break;

                    case "CHANGE PASSWORD":
                        mContext.startActivity(new Intent(mContext, ChangePassword.class));
                        break;

                    case "LOG OUT":

                        break;

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class DashViewHolder extends RecyclerView.ViewHolder {

        CardView dashCard;
        TextView dashText;
        ImageView dashImage;

        public DashViewHolder(View itemView) {
            super(itemView);

            dashCard = (CardView) itemView.findViewById(R.id.dash_card);
            dashText = (TextView) itemView.findViewById(R.id.dash_text);
            dashImage = (ImageView) itemView.findViewById(R.id.dash_image);

            /*itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Post value = itemList.get(pos);
                    Toast.makeText(mContext, value.getUsername(), Toast.LENGTH_SHORT).show();
                    switch(value.getUsername().toUpperCase()) {
                        case "HOME":
                            mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                            break;

                        case "PROFILE":
                            dashFragment.addProfile();
                            break;

                        case "SETTINGS":
                            mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                            break;

                        case "CHANGE PASSWORD":
                            mContext.startActivity(new Intent(mContext, ChangePassword.class));
                            break;

                    }

                }
            });*/
        }

        public void setData(Post post, int position) {
            dashText.setText(post.getUsername());
            dashImage.setImageResource(post.getImageId());
        }


    }
}
