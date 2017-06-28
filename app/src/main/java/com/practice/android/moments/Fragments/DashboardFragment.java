package com.practice.android.moments.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.practice.android.moments.Activities.SettingsActivity;
import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;
import com.practice.android.moments.RecyclerView.DashboardRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    TextView settings;
    List<Post> dashboardList;
    RecyclerView mRecyclerView;
    DashboardRecyclerAdapter mDashRecyclerAdapter;
    StaggeredGridLayoutManager staggeredGrid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.dashboard_staggered);
        settings = (TextView) v.findViewById(R.id.settings);
        dashboardList = getListItemData();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.dashboard_staggered);
        mRecyclerView.setHasFixedSize(true);
        staggeredGrid = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(staggeredGrid);
        mDashRecyclerAdapter = new DashboardRecyclerAdapter(getActivity(), dashboardList);
        mRecyclerView.setAdapter(mDashRecyclerAdapter);


        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        return v;
    }


    private List<Post> getListItemData() {
        List<Post> listViewItems = new ArrayList<Post>();
        listViewItems.add(new Post("Home", R.drawable.home));
        listViewItems.add(new Post("Camera", R.drawable.camera));
        listViewItems.add(new Post("Gallery", R.drawable.gallery));
        listViewItems.add(new Post("Editing", R.drawable.editing));
        listViewItems.add(new Post("Photos And Videos", R.drawable.upload));
        listViewItems.add(new Post("Friends", R.drawable.friends));
        listViewItems.add(new Post("Profile", R.drawable.profile));
        listViewItems.add(new Post("Edit Profile", R.drawable.edit_profile));
        listViewItems.add(new Post("Settings", R.drawable.settings));
        listViewItems.add(new Post("Log Out", R.drawable.logout));

        return listViewItems;

    }
}