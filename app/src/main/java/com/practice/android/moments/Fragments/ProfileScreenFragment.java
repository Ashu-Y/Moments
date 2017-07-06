package com.practice.android.moments.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("deprecation")
public class ProfileScreenFragment extends Fragment {

    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    private static final String TAG = "ProfileScreen";
    StorageReference mstorageReference;
    FirebaseUser firebaseuser;
    Uri filePath;
    Uri uri;
    Uri download_uri;
    DatabaseReference databaseReference;
    FloatingActionButton fabGallery;
    String user_id;
    TextView getname;
    TextView getemail;
    TextView getphone;
    TextView getAbout;
    TextView getDate;
    TextView getrealtion_ship, getgender;
    ImageButton BackPIC;
    private CircleImageView profile_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_profile_screen, container, false);
        Log.i("ProfileScreenFrag", "onCreateView");

        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mstorageReference = FirebaseStorage.getInstance().getReference();

        user_id = firebaseuser.getUid();


        fabGallery = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        profile_pic = (CircleImageView) v.findViewById(R.id.user_profile_photo);

        fabGallery.setOnClickListener(v1 -> fn_Choose_Image());

        BackPIC = (ImageButton) v.findViewById(R.id.header_cover_image);
        getname = (TextView) v.findViewById(R.id.user_profile_name);
        getemail = (TextView) v.findViewById(R.id.editText15);
        getphone = (TextView) v.findViewById(R.id.TextviewPhone);
        getAbout = (TextView) v.findViewById(R.id.user_profile_short_bio);
        getDate = (TextView) v.findViewById(R.id.dob);
        getrealtion_ship = (TextView) v.findViewById(R.id.editText695);
        getgender = (TextView) v.findViewById(R.id.gendertext);


        return v;
    }

    public void fn_Choose_Image() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery", (arg0, arg1) -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_PICTURE);
        });

        myAlertDialog.setNegativeButton("Camera", (arg0, arg1) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        });

        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath;

        Bitmap thumbnail;
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
//            Uri selectedImage = data.getData();
//            String[] filePath = {MediaStore.Images.Media.DATA};
//            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
//            assert c != null;
//            c.moveToFirst();
//
//            int columnIndex = c.getColumnIndex(filePath[0]);
//            picturePath = c.getString(columnIndex);
//
//            profile_pic.setImageURI(data.getData());
//
//            c.close();
//
//            try {
//                thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.e("gallery.***********692." + thumbnail, picturePath);
//                uri = Uri.fromFile(new File(picturePath));
//            } catch (Exception e) {
//                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
//                e.printStackTrace();
//            }


            filePath = data.getData();
            Log.i(TAG, filePath.toString());

            uploadFile();


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            picturePath = getRealPathFromURI(filePath);
            uploadFile();
            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
//                profile_pic.setImageURI(filePath);
//                uploadFile();
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }
            Log.i(TAG, filePath.toString());


        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCancelable(false);


            StorageReference riversRef = mstorageReference.child("Photos")
                    .child(firebaseuser.getUid()).child("Profile Photo")
                    .child(filePath.getLastPathSegment());
            riversRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        download_uri = taskSnapshot.getDownloadUrl();
                        Picasso.with(getActivity()).load(download_uri).fit().centerCrop().into(profile_pic);


                        String user_id = firebaseuser.getUid();
                        String picture = String.valueOf(download_uri);
                        DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
                        currentuser_db.child("photo").setValue(picture);


                        //and displaying a success toast
                        Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(exception -> {
                        //if the upload is not successful
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        //and displaying error message
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getActivity(), "File Upload Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        databaseReference.child(user_id).child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile_model_class user = dataSnapshot.getValue(Profile_model_class.class);

                assert user != null;
                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());
                getname.setText(user.getName());
                getemail.setText(user.getEmail());
                getphone.setText(user.getPhone());
                getAbout.setText(user.getAbout());
                getDate.setText(user.getDate_of_birth());
                getrealtion_ship.setText(user.getRelationship());
                getgender.setText(user.getGender());

                Picasso.with(getContext()).load(user.getPhoto()).fit().centerCrop().into(BackPIC);
                Glide.with(getContext()).load(user.getPhoto()).into(profile_pic);

                Log.d(TAG, "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getRelationship() + "    " + user.getAbout());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}