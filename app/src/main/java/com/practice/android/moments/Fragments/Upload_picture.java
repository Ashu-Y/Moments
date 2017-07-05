package com.practice.android.moments.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.practice.android.moments.Activities.BottomNavigation;
import com.practice.android.moments.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NewApi")
public class Upload_picture extends Fragment {
    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    private static final String TAG = "Upload picture";
    ImageView uploadImage;
    EditText tittle, description;
    Button upload;
    StorageReference mstorageReference;
    FirebaseUser firebaseuser;
    Uri selectedImage = null;
    Uri uri;
    Uri download_uri;
    Context context;
    String user_id;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upload_picture, container, false);

        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mstorageReference = FirebaseStorage.getInstance().getReference();

        user_id = firebaseuser.getUid();
        context = getContext();

        uploadImage = (ImageView) v.findViewById(R.id.imageView2);
        tittle = (EditText) v.findViewById(R.id.image_title);
        description = (EditText) v.findViewById(R.id.image_description);
        upload = (Button) v.findViewById(R.id.button_upload);


        uploadImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fn_Choose_Image();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage != null) {
                    Log.i(TAG, selectedImage.toString());

                    uploadFile();
                } else {
                    Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath;

        Bitmap thumbnail;
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            assert c != null;
            c.moveToFirst();

            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);

            uploadImage.setImageURI(data.getData());

            c.close();

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            uri = saveImageBitmap(thumbnail);
            selectedImage = uri;
            uploadImage.setImageURI(uri);
            //  picturePath = uristringpic;
            try {
                picturePath = uri.toString();
                // thumbnail = (BitmapFactory.decodeFile(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }

        }
    }

    public Uri saveImageBitmap(Bitmap bitmap) {
        String strDirectoy = context.getFilesDir().getAbsolutePath();
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int hour = c.get(Calendar.MILLISECOND);
        String imageName = hour + "image.JPEG";
        OutputStream fOut = null;
        File file = new File(strDirectoy, imageName);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
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


    private void uploadFile() {
        //if there is a file to upload
        if (selectedImage != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);


            StorageReference riversRef = mstorageReference.child("Photos")
                    .child(firebaseuser.getUid()).child("User Photo")
                    .child(selectedImage.getLastPathSegment());
            riversRef.putFile(selectedImage)
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();


                        download_uri = taskSnapshot.getDownloadUrl();
                        String user_id = firebaseuser.getUid();
                        String picture = String.valueOf(download_uri);
                        DatabaseReference currentuser_db = databaseReference.child(user_id).child("User Pictures");
                        currentuser_db.child(selectedImage.getLastPathSegment());
                        DatabaseReference currentuser = currentuser_db.child(selectedImage.getLastPathSegment());
                        currentuser.child("pic").setValue(picture);
                        currentuser.child("tittle").setValue(tittle.getText().toString());
                        currentuser.child("description").setValue(description.getText().toString());


                        //and displaying a success toast
                        Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getActivity(), BottomNavigation.class));
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


}