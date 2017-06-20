package com.practice.android.moments.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practice.android.moments.R;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class BlankFragment extends Fragment {

//    Button Sign_Out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
//        Sign_Out = (Button) rootView.findViewById(R.id.SignOut);
//        //Sign Out
//        Sign_Out.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(getActivity(), "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), Login_method.class));
//            }
//        });


        return rootView;
    }


}
