package com.practice.android.moments.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.practice.android.moments.Activities.AddFriendProfileActivity;
import com.practice.android.moments.Activities.BottomNavigation;
import com.practice.android.moments.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.practice.android.moments.Activities.BottomNavigation.USER_ID;
import static com.practice.android.moments.Activities.BottomNavigation.USER_NAME;
import static com.practice.android.moments.Activities.BottomNavigation.USER_PHOTO;


/**
 * Created by Ashutosh on 7/14/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {


    static ArrayList<HashMap<String, String>> mDataArray = new ArrayList<>();
    Context mContext;


    public SearchAdapter(Context context) {
        mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row_itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_item, parent, false);


        return new MyViewHolder(row_itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HashMap<String, String> searchModel = mDataArray.get(position);

        holder.setData(searchModel, position);

        holder.search_row.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = mDataArray.get(position).get(USER_ID);
                mContext.startActivity(new Intent(mContext, AddFriendProfileActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    public void filter(String charText) {
        String a = charText.toLowerCase(Locale.getDefault());
        mDataArray.clear();

        if (charText.length() == 0) {
            mDataArray.addAll(BottomNavigation.al_appsearch);
        } else {
            for (int i = 0; i < BottomNavigation.al_appsearch.size(); i++) {
                String s = BottomNavigation.al_appsearch.get(i).get(USER_NAME);
                if (s.toLowerCase(Locale.getDefault()).contains(a)) {
                    try {
                        mDataArray.add(BottomNavigation.al_appsearch.get(i));
                    } catch (NullPointerException e) {
                        Log.e("SearchAdapter", e.getMessage());
                    }
                }
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        CircleImageView profilePic;
        LinearLayout search_row;

        public MyViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.name);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            search_row = (LinearLayout) itemView.findViewById(R.id.search_row);

        }

        public void setData(HashMap<String, String> searchModel, int position) {
            username.setText(searchModel.get(USER_NAME));

            Glide.with(mContext)
                    .load(searchModel.get(USER_PHOTO))
                    .thumbnail(Glide.with(mContext).load(R.drawable.loader))
                    .fitCenter()
                    .into(profilePic);

        }
    }

}
