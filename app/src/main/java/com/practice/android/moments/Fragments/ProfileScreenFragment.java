package com.practice.android.moments.Fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.practice.android.moments.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileScreenFragment extends Fragment {

    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    private FloatingActionButton fabGallery;
    private TextView prof_logout;
    private CircleImageView profile_pic;
    private Uri uri;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_profile_screen, container, false);
        Log.i("ProfileScreenFrag", "onCreateView");

        fabGallery = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        prof_logout = (TextView) v.findViewById(R.id.prof_logout);
        profile_pic = (CircleImageView) v.findViewById(R.id.user_profile_photo);

        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn_Choose_Image();
            }
        });

//        prof_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("ProfileScreenFragment", "You clicked onClick Button");
//                FirebaseAuth.getInstance().signOut();
//                LoginManager.getInstance().logOut();
//                Auth.GoogleSignInApi.signOut(googleApiClient)
//                        .setResultCallback(
//                                new ResultCallback<Status>() {
//                                    @Override
//                                    public void onResult(Status status) {
////                                        Log.i(TAG, "log off from google sign button");
//                                        Toast.makeText(getActivity(), "You have Successfully Sign off", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(getActivity(), Login_method.class));
//                                    }
//                                });
//            }
//        });

        return v;
    }

    public void fn_Choose_Image() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picturePath;

        Bitmap thumbnail;
        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            assert c != null;
            c.moveToFirst();

            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);

            profile_pic.setImageURI(data.getData());

            c.close();

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            profile_pic.setImageURI(selectedImageUri);

            picturePath = getRealPathFromURI(selectedImageUri);

            try {
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("gallery.***********692." + thumbnail, picturePath);
                uri = Uri.fromFile(new File(picturePath));
            } catch (Exception e) {
                Log.e("gallery***********692.", "Exception==========Exception==============Exception");
                e.printStackTrace();
            }

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
}