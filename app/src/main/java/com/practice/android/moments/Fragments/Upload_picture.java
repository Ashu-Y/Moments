package com.practice.android.moments.Fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.practice.android.moments.R;

@SuppressLint("NewApi")
public class Upload_picture extends Fragment {

    ImageView Uploadimage;
    EditText tittle, description;
    Button choose, upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upload_picture, container, false);


        Uploadimage = (ImageView) v.findViewById(R.id.imageView2);
        tittle = (EditText) v.findViewById(R.id.image_title);
        description = (EditText) v.findViewById(R.id.image_description);
        choose = (Button) v.findViewById(R.id.button_choose);
        upload = (Button) v.findViewById(R.id.button_upload);



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return v;
    }

}
