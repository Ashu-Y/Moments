package com.practice.android.moments.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.android.moments.R;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class ProfileEditingFragment extends Fragment {

    /*
    * Gender not added
    * in xml
    * update it with
    * DEFAULT
        * */


    Spinner spinner;
    String relation;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText About;
    private EditText Date_of_birth;
    private Button Submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        name = (EditText) rootView.findViewById(R.id.editText4);
        email = (EditText) rootView.findViewById(R.id.editText9);
        phone = (EditText) rootView.findViewById(R.id.editText5);
        About = (EditText) rootView.findViewById(R.id.editText541);
        Date_of_birth = (EditText) rootView.findViewById(R.id.editText51);
        Submit = (Button) rootView.findViewById(R.id.submitedit);
        spinner = (Spinner) rootView.findViewById(R.id.Relationship);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.RelationshipStatus, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                relation = (String) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                relation = "Not want to tell";
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String user_id = firebaseUser.getUid();

            DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
            currentuser_db.child("name").setValue(name.getText().toString());
            currentuser_db.child("email").setValue(email.getText().toString());
            currentuser_db.child("phone").setValue(phone.getText().toString());
            currentuser_db.child("Gender").setValue("DEFAULT");
            currentuser_db.child("Relationship").setValue(relation);
            currentuser_db.child("About").setValue(About.getText().toString());
            currentuser_db.child("Date Of Birth").setValue(Date_of_birth.getText().toString());
            currentuser_db.child("photo").setValue("Default");

        }

        return rootView;
    }


}
