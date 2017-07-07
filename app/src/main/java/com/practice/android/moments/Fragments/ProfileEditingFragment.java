package com.practice.android.moments.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class ProfileEditingFragment extends Fragment {


    Spinner spinner;
    String relation;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    Calendar myCalendar;
    ProgressDialog progressDialog;
    String user_id;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText About;
    private EditText Date_of_birth;
    private ImageView selectCal;
    private Button Submit;
    private RadioGroup genderGroup;
    private RadioButton gender;
    private RadioButton male;
    private RadioButton female;
    private RadioButton defaultGender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        progressDialog = new ProgressDialog(getActivity());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = firebaseUser.getUid();

        name = (EditText) rootView.findViewById(R.id.editText4);
        email = (EditText) rootView.findViewById(R.id.editText9);
        phone = (EditText) rootView.findViewById(R.id.editText5);
        About = (EditText) rootView.findViewById(R.id.editText541);
        Date_of_birth = (EditText) rootView.findViewById(R.id.edit_date);
        selectCal = (ImageView) rootView.findViewById(R.id.select_cal);
        Submit = (Button) rootView.findViewById(R.id.submitedit);
        spinner = (Spinner) rootView.findViewById(R.id.Relationship);
        genderGroup = (RadioGroup) rootView.findViewById(R.id.radio_gender);
        defaultGender = (RadioButton) rootView.findViewById(R.id.gender_prefer_not_to_tell);
        male = (RadioButton) rootView.findViewById(R.id.gender_male);
        female = (RadioButton) rootView.findViewById(R.id.gender_female);
        defaultGender.setChecked(true);
        myCalendar = Calendar.getInstance();

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(10);
        phone.setFilters(filterArray);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        selectCal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.RelationshipStatus, R.layout.spinner_item);
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


        Submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Uploading");
                progressDialog.show();
                int flag = 1;
                if (firebaseUser != null) {
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    user_id = firebaseUser.getUid();
                    String defa = "DEFAULT";


                    int selectedId = genderGroup.getCheckedRadioButtonId();
                    gender = (RadioButton) rootView.findViewById(selectedId);

                    String code = name.getText().toString();
                    String code1 = email.getText().toString();
                    String code2 = phone.getText().toString();
                    String code3 = About.getText().toString();
                    String code4 = Date_of_birth.getText().toString();
                    String code5 = gender.getText().toString();

                    if (TextUtils.isEmpty(code)) {
                        name.setError("Cannot be empty.");
                        flag = -1;
                        progressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty(code2) || code2.length() != 10) {
//                        Toast.makeText(getContext(), String.valueOf(code2.length()), Toast.LENGTH_SHORT).show();
                        phone.setError("Phone length cannot be less than 10");
                        flag = -1;
                        progressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty(code3)) {
                        About.setError("Cannot be empty.");
                        flag = -1;
                        progressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty(code4)) {
                        Date_of_birth.setError("Cannot be empty.");
                        flag = -1;
                        progressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty(code5)) {
                        Toast.makeText(getContext(), "Please enter the gender", Toast.LENGTH_SHORT).show();
                        flag = -1;
                        progressDialog.dismiss();
                        return;
                    }

                    if (flag != -1) {

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name.getText().toString())
                                .build();

                        firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("Editing", "User profile updated.");
                                        }
                                    }
                                });

                        DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
                        currentuser_db.child("name").setValue(name.getText().toString());

                        if (!Objects.equals(firebaseUser.getEmail(), defa)) {
                            currentuser_db.child("email").setValue(firebaseUser.getEmail());
                            email.setText(firebaseUser.getEmail());
                        } else {
                            currentuser_db.child("email").setValue(email.getText().toString());
                        }
//                        if (!Objects.equals(firebaseUser.getEmail(), defa)) {
//                            currentuser_db.child("phone").setValue(firebaseUser.getPhoneNumber());
//                            email.setText(firebaseUser.getPhoneNumber());
//                        } else {
                        currentuser_db.child("phone").setValue(phone.getText().toString());

//                        }

                        currentuser_db.child("gender").setValue(code5);
                        currentuser_db.child("relationship").setValue(relation);
                        currentuser_db.child("about").setValue(About.getText().toString());
                        currentuser_db.child("date_of_birth").setValue(Date_of_birth.getText().toString());


                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }

        });


        return rootView;
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, getActivity().getResources().getConfiguration().locale);

        Date_of_birth.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onResume() {
        super.onResume();


        databaseReference.child(user_id).child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                assert user != null;
                Log.d("Editing Activity", "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());
                name.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                About.setText(user.getAbout());
                Date_of_birth.setText(user.getDate_of_birth());

                String userGender = user.getGender();

                if (userGender.equals("Male")) {
                    male.setChecked(true);
                } else if (userGender.equals("Female")) {
                    female.setChecked(true);
                }

                Log.d("Editing Activity", "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getRelationship() + "    " + user.getAbout());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
