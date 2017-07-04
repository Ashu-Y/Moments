package com.practice.android.moments.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;
import com.practice.android.moments.RecyclerView.DashboardRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;

public class DashboardFragment extends Fragment {

    List<Post> dashboardList;
    RecyclerView mRecyclerView;
    DashboardRecyclerAdapter mDashRecyclerAdapter;
    StaggeredGridLayoutManager staggeredGrid;
    GridLayoutManager grid;
    DashboardFragment dashboardFragment;

    private String TAG = getClass().getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);



        mRecyclerView = (RecyclerView) v.findViewById(R.id.dashboard_staggered);
        dashboardList = getListItemData();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.dashboard_staggered);
        mRecyclerView.setHasFixedSize(true);
        staggeredGrid = new StaggeredGridLayoutManager(2, 1);
        grid = new GridLayoutManager(getActivity(), 2);
        grid.setOrientation(GridLayoutManager.VERTICAL);
        grid.setAutoMeasureEnabled(true);
        staggeredGrid.setAutoMeasureEnabled(true);

        staggeredGrid.setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//        staggeredGrid.setReverseLayout(true);
//        staggeredGrid.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(grid);

        dashboardFragment = this;
        mDashRecyclerAdapter = new DashboardRecyclerAdapter(getActivity(), dashboardList, dashboardFragment);

        mRecyclerView.setAdapter(mDashRecyclerAdapter);

        return v;
    }


    private List<Post> getListItemData() {
        List<Post> listViewItems = new ArrayList<Post>();
        listViewItems.add(new Post("Choose Image", R.drawable.gallery));
        listViewItems.add(new Post("Editing", R.drawable.editing));
        listViewItems.add(new Post("Upload", R.drawable.upload));
        listViewItems.add(new Post("Friends", R.drawable.friends));
        listViewItems.add(new Post("Profile", R.drawable.profile));
        listViewItems.add(new Post("Edit Profile", R.drawable.edit_profile));
        listViewItems.add(new Post("Settings", R.drawable.settings));
        listViewItems.add(new Post("Log Out", R.drawable.logout));

        return listViewItems;

    }

    public void addProfile() {
        Fragment profFragment = new ProfileScreenFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, profFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addEditProfile() {
        Fragment editProfFragment = new ProfileEditingFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, editProfFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addUpload() {
        Fragment uploadFragment = new Upload_picture();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, uploadFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}