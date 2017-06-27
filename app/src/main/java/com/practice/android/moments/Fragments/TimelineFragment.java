package com.practice.android.moments.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;
import com.practice.android.moments.RecyclerView.PostRecyclerAdapter;

import java.util.ArrayList;


/**
 * Created by Ashutosh on 6/27/2017.
 */

public class TimelineFragment extends Fragment {

    RecyclerView mRecyclerView;
    PostRecyclerAdapter mPostRecyclerAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Post> mPostArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_timeline, container, false);

        //Recycler
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mPostArrayList = new ArrayList<>();

        mPostArrayList.add(new Post("gautam", R.drawable.c1));
        mPostArrayList.add(new Post("hitesh", R.drawable.c2));
        mPostArrayList.add(new Post("piyush", R.drawable.c3));
        mPostArrayList.add(new Post("abhya", R.drawable.c4));
        mPostArrayList.add(new Post("mansi", R.drawable.c5));
        mPostArrayList.add(new Post("naman", R.drawable.c6));
        mPostArrayList.add(new Post("rajat", R.drawable.c7));

        mPostRecyclerAdapter = new PostRecyclerAdapter(getActivity(), mPostArrayList);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mPostRecyclerAdapter);


        return v;
    }
}
