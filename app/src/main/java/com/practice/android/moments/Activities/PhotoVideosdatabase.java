package com.practice.android.moments.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.practice.android.moments.R;

public class PhotoVideosdatabase extends AppCompatActivity {
    private static final int GALLERY_INTENT = 2;
    private static final String TAG = "PhotoVideo";
    ProgressDialog progrees;
    private Button mselectimage;
    private StorageReference mstorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_videosdatabase);
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mselectimage = (Button) findViewById(R.id.selectimage);
        Uri uri;
        progrees = new ProgressDialog(this);
        mselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");

                startActivityForResult(i, GALLERY_INTENT);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            progrees.setMessage("Please wait");
            progrees.show();
            Uri uri = data.getData();
            Log.i(TAG, uri.toString());

            StorageReference filePath = mstorageReference.child("Photos").child(uri.getLastPathSegment());

            filePath.getFile(uri).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    progrees.dismiss();
                    Toast.makeText(PhotoVideosdatabase.this, "File Uploaded", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
