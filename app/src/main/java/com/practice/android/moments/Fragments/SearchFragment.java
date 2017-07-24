package com.practice.android.moments.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.practice.android.moments.Adapters.SearchAdapter;
import com.practice.android.moments.R;

import java.util.Locale;

public class SearchFragment extends Fragment {

    Context context;
    DatabaseReference databaseReference1, databaseReference;
    EditText edt_search;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    SearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        edt_search = (EditText) v.findViewById(R.id.editText_search);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_search);
        adapter = new SearchAdapter(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
//                adapter.filter(text);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
                mRecyclerView.setAdapter(adapter);

            }
        });


        return v;
    }


}

