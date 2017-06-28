package com.practice.android.moments.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;

import java.util.List;

/**
 * Created by Ashutosh on 6/28/2017.
 */

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashViewHolder> {

    private List<Post> itemList;
    private Context mContext;

    public DashboardRecyclerAdapter(Context context, List<Post> itemList) {
        this.itemList = itemList;
        mContext = context;
    }

    @Override
    public DashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dash_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item, parent, false);
        DashViewHolder holder = new DashViewHolder(dash_layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(DashViewHolder holder, int position) {
        holder.setData(itemList.get(position), position);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class DashViewHolder extends RecyclerView.ViewHolder {

        TextView dashText;
        ImageView dashImage;

        public DashViewHolder(View itemView) {
            super(itemView);

            dashText = (TextView) itemView.findViewById(R.id.dash_text);
            dashImage = (ImageView) itemView.findViewById(R.id.dash_image);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Post value = itemList.get(pos);
                    Toast.makeText(mContext, value.getUsername(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(Post post, int position) {
            dashText.setText(post.getUsername());
            dashImage.setImageResource(post.getImageId());
        }
    }
}
