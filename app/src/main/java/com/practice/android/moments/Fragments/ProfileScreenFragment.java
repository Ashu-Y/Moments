package com.practice.android.moments.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.practice.android.moments.Models.Profile_model_class;
import com.practice.android.moments.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("deprecation")
public class ProfileScreenFragment extends Fragment {

    private static final int GALLERY_PICTURE = 1;
    private static final int GALLERY_PICTURE_COVER = 2;
    private static final int CAMERA_REQUEST = 3;
    private static final int CAMERA_REQUEST_COVER = 4;

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
    ImageView BackPIC;
    Context context;
    private String userChosenTask;
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

        context = getContext();

        fabGallery = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        profile_pic = (CircleImageView) v.findViewById(R.id.user_profile_photo);

        fabGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fn_Choose_Image(view.getId());
            }
        });

        BackPIC = (ImageView) v.findViewById(R.id.header_cover_image);
        getname = (TextView) v.findViewById(R.id.user_profile_name);
        getemail = (TextView) v.findViewById(R.id.editText15);
        getphone = (TextView) v.findViewById(R.id.TextviewPhone);
        getAbout = (TextView) v.findViewById(R.id.user_profile_short_bio);
        getDate = (TextView) v.findViewById(R.id.dob);
        getrealtion_ship = (TextView) v.findViewById(R.id.editText695);
        getgender = (TextView) v.findViewById(R.id.gendertext);


        BackPIC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                fn_Choose_Image(view.getId());
            }
        });

        return v;
    }

    public void fn_Choose_Image(int id) {

        String title;
        final CharSequence[] items = {"Choose from Library",
                "Take Photo",
                "Cancel"};

        switch (id) {

            case R.id.floatingActionButton:

                title = "Add Profile Picture!";

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Choose from Library")) {

                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_PICTURE);

                        } else if (items[item].equals("Take Photo")) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST);

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

                break;

            case R.id.header_cover_image:

                title = "Add Cover Picture!";

                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Choose from Library")) {

                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_PICTURE_COVER);

                        } else if (items[item].equals("Take Photo")) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST_COVER);

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath;

        Bitmap thumbnail;
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {

            filePath = data.getData();
            Log.i(TAG, filePath.toString());

            uploadFile(requestCode, filePath);


        } else if (requestCode == GALLERY_PICTURE_COVER && resultCode == RESULT_OK) {

            filePath = data.getData();
            Log.i(TAG, filePath.toString());

            uploadFile(requestCode, filePath);


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            thumbnail = (Bitmap) data.getExtras().get("data");
            uri = saveImageBitmap(thumbnail);

            Log.i(TAG, uri.toString());

            try {
                picturePath = uri.toString();
                // thumbnail = (BitmapFactory.decodeFile(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }

            uploadFile(requestCode, uri);


        } else if (requestCode == CAMERA_REQUEST_COVER && resultCode == RESULT_OK) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            uri = saveImageBitmap(thumbnail);

            Log.i(TAG, uri.toString());

            //  picturePath = uristringpic;
            try {
                picturePath = uri.toString();
                // thumbnail = (BitmapFactory.decodeFile(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }

            uploadFile(requestCode, uri);


        }
    }

//    public String getRealPathFromURI(Uri contentUri) {
//        String path = null;
//        String[] proj = {MediaStore.MediaColumns.DATA};
//        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
//        assert cursor != null;
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            path = cursor.getString(column_index);
//        }
//        cursor.close();
//        return path;
//    }

    private void uploadFile(int requestCode, Uri filePath) {
        //if there is a file to upload

        switch (requestCode) {
            case CAMERA_REQUEST:
            case GALLERY_PICTURE: {
                if (filePath != null) {
                    //displaying a progress dialog while upload is going on
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
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
                    Log.i("Camera", imageName);

                    StorageReference riversRef = mstorageReference.child("Photos")
                            .child(firebaseuser.getUid()).child("Profile Photo")
                            .child(imageName);
                    riversRef.putFile(filePath)
                            .addOnSuccessListener(taskSnapshot -> {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                download_uri = taskSnapshot.getDownloadUrl();
                                Picasso.with(getActivity()).load(download_uri).fit().centerCrop().into(profile_pic);


                                String user_id = firebaseuser.getUid();
                                String picture = String.valueOf(download_uri);
                                String thumbpic = picture;

                                DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");

                                currentuser_db.child("photo").setValue(picture);

                                currentuser_db.child("thumbnailProfilephoto").setValue(thumbpic);

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firebaseuser.getDisplayName())
                                        .setPhotoUri(download_uri)
                                        .build();

                                firebaseuser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e("Editing", "User profile updated.");
                                                }
                                            }
                                        });

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
            break;

            case CAMERA_REQUEST_COVER:
            case GALLERY_PICTURE_COVER: {
                if (filePath != null) {
                    //displaying a progress dialog while upload is going on
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();
                    progressDialog.setCancelable(false);

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
                    Log.i("Camera", imageName);

                    StorageReference riversRef = mstorageReference.child("Photos")
                            .child(firebaseuser.getUid()).child("Cover Photo")
                            .child(imageName);
                    riversRef.putFile(filePath)
                            .addOnSuccessListener(taskSnapshot -> {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                download_uri = taskSnapshot.getDownloadUrl();
                                Picasso.with(getActivity()).load(download_uri).fit().centerCrop().into(BackPIC);


                                String user_id = firebaseuser.getUid();
                                String picture = String.valueOf(download_uri);
                                String thumbpic = picture;

                                DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Info");
                                currentuser_db.child("coverPhoto").setValue(picture);
                                currentuser_db.child("thumbnailCoverPhoto").setValue(thumbpic);

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
            break;
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
                Log.e(TAG, "User name: " + user.getName() + ", email " + user.getEmail() + "    " + user.getRelationship() + "    " + user.getAbout());
                getname.setText(user.getName());
                getemail.setText(user.getEmail());
                getphone.setText(user.getPhone());
                getAbout.setText(user.getAbout());
                getDate.setText(user.getDate_of_birth());
                getrealtion_ship.setText(user.getRelationship());
                getgender.setText(user.getGender());

//                Picasso.with(getContext()).load(user.getPhoto()).fit().centerCrop().into(BackPIC);
                Glide.with(getContext()).load(user.getThumbnailProfilephoto())
                        .thumbnail(Glide.with(getContext()).load(R.drawable.giphy))
                        .into(profile_pic);


                Glide.with(getContext()).load(user.getThumbnailCoverPhoto())
                        .thumbnail(Glide.with(getContext()).load(R.drawable.loader))
                        .into(BackPIC);

                Log.e(TAG, "\n" + user.getPhoto() + "        " + user.getGender() + "    " + user.getRelationship() + "    " + user.getAbout());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public Uri saveImageBitmap(Bitmap bitmap) {
        String strDirectoy = context.getFilesDir().getAbsolutePath();


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
        Log.i("Camera", imageName);
        OutputStream fOut = null;
        File file = new File(strDirectoy, imageName);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }


}