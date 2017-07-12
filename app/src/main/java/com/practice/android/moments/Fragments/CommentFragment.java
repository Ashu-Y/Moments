package com.practice.android.moments.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.R;

import java.util.Calendar;


public class CommentFragment extends Fragment {

    public static TextView imagename;
    String name;
    EditText user_comment;
    ImageView send;
    FirebaseUser firebaseUser;
    String user_id;
    DatabaseReference databaseReference, data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comment, container, false);


        imagename = (TextView) v.findViewById(R.id.name);

//        name = imagename.getText().toString();
        imagename.setText(name);
        Log.e("Comment Fragment", name);

        imagename.setVisibility(View.GONE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            assert firebaseUser != null;
            user_id = firebaseUser.getUid();
        } catch (NullPointerException e) {

            e.getMessage();
        }
        user_comment = (EditText) v.findViewById(R.id.comment);

        user_comment.setText("");
        send = (ImageView) v.findViewById(R.id.post_comment);
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Comments");

        Log.e("Comment Fragment", name);

        data = databaseReference.child(name).child("Users");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);
                int seconds = c.get(Calendar.SECOND);
                int milliSeconds = c.get(Calendar.MILLISECOND);


                //Can cause error in uploading
                String imageName = day + "-" + month + "-" + year + "-" + hour + ":" + minutes + ":" + seconds + ":" + milliSeconds + "";
                Log.e("Camera", imageName);


                DatabaseReference database = data.child(imageName);
                database.child("user_id").setValue(user_id);

                database.child("comment").setValue(user_comment.getText().toString());
                Log.e("usercomments", user_comment.getText().toString());

                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();


            }
        });

        return v;
    }

    public void setImageResourceName(String name) {
        this.name = name;

        Log.e("Comment Fragment Method", name);
    }


    @Override
    public void onResume() {
        super.onResume();
        user_comment.setText("");




    }
}
