package com.practice.android.moments.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

public class SearchFragment extends Fragment {

    Context context;
    DatabaseReference databaseReference1, databaseReference;

//    ArrayList<HashMap<String, String>> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

//        userList = new ArrayList<>();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getContext();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

//            int i = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int number = (int) dataSnapshot.getChildrenCount();


                Log.e("number of users", String.valueOf(number));

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    String nodeKey = childDataSnapshot.getKey();
                    Log.e("Key Node", "" + nodeKey);


                    assert nodeKey != null;
                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(nodeKey).child("User Info");

                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int number1 = (int) dataSnapshot.getChildrenCount();
                            Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);
                            assert user != null;
                            Log.e("Key Node", "" + nodeKey);
                            Log.e("number of OBJECTS", String.valueOf(number1) + "\t" + user.getName());

//                            HashMap<String, String> item = new HashMap<String, String>();
//                            item.put(nodeKey, user.getName());
//
//                            userList.add(item);
//
//                            HashMap<String, String> useritem = userList.get(i);
//
//                            i++;
//
//                            Log.e("Key ", "" + useritem.get(nodeKey));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


//    public void Arraylist(String UserId) {
//        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId).child("User Info");
//
//
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int number1 = (int) dataSnapshot.getChildrenCount();
//
//                Log.e("number of OBJECTS", String.valueOf(number1));
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }


}
