package com.practice.android.moments.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.practice.android.moments.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class EditingFragment extends Fragment {

    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;
    static final int REQ_CODE_GALLERY_PICKER = 20;
    public final String TAG = getClass().getSimpleName();
    ImageView content;
    Bitmap bitmap;
    File myDir;

    private ImageView mEditedImageView;
    private Button mOpenGalleryButton;
    private Button mLaunchImageEditorButton;
    private Button save;
    private ImageView mSelectedImageView;
    private Uri mSelectedImageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_editing, container, false);

        /* 2) Find the layout's ImageView by ID */
        mEditedImageView = (ImageView) v.findViewById(R.id.editedImageView);


        mOpenGalleryButton = (Button) v.findViewById(R.id.openGalleryButton);
        mLaunchImageEditorButton = (Button) v.findViewById(R.id.launchImageEditorButton);
        save = (Button) v.findViewById(R.id.save);

        mSelectedImageView = (ImageView) v.findViewById(R.id.editedImageView);


//        /* 1) Make a new Uri object (Replace this with a real image on your device) */
//        Uri imageUri = Uri.parse("content://media/external/images/media/1166");
//
//        /* 2) Create a new Intent */
//        Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
//                .setData(imageUri)
//                .build();
//        /* 3) Start the Image Editor with request code 1 */
//        startActivityForResult(imageEditorIntent, 1);

        mOpenGalleryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryPickerIntent = new Intent();
                galleryPickerIntent.setType("image/*");
                galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryPickerIntent, "Select an image"), REQ_CODE_GALLERY_PICKER);
            }
        });

        mLaunchImageEditorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedImageUri != null) {
                    Intent imageEditorIntent = new AdobeImageIntent.Builder(getActivity())
                            .setData(mSelectedImageUri)
                            .build();

                    startActivityForResult(imageEditorIntent, REQ_CODE_CSDK_IMAGE_EDITOR);
                } else {
                    Toast.makeText(getActivity(), "Select an image from the Gallery", Toast.LENGTH_LONG).show();
                }
            }
        });

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        myDir = new File(root.toString() + "/Moments");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                content = (ImageView) v.findViewById(R.id.editedImageView);
//                content.destroyDrawingCache();
                saveImage();
                content.destroyDrawingCache();

            }
        });


        return v;
    }


    public void saveImage() {


        content.setDrawingCacheEnabled(true);
        Bitmap bmp1 = ((BitmapDrawable) content.getDrawable()).getBitmap();


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater(getArguments());
        final View dialogView = inflater.inflate(R.layout.save_as_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText img_name = (EditText) dialogView.findViewById(R.id.img_name);

        dialogBuilder.setTitle("Save As...");
        dialogBuilder.setMessage("Enter image name:");
        dialogBuilder.setIcon(R.drawable.like);

        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int whichButton) {

                String[] fileNames = myDir.list();
                for (int i = 0; i < fileNames.length; i++) {

                    if (fileNames[i].equals(img_name.getText().toString() + ".jpg")) {


                        Toast.makeText(getActivity(), "Image with same name already exists in the Moments folder",
                                Toast.LENGTH_SHORT).show();
                        imageExistsPopup();
                        return;

                    }
                }

                try {

                    File cachePath = new File(myDir + "/" + img_name.getText().toString() + ".jpg");
                    cachePath.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(cachePath);

                    bmp1.compress(CompressFormat.JPEG, 100, ostream);

                    ostream.close();


                    Toast.makeText(getActivity(), "Image saved with name: " + img_name.getText().toString(), Toast.LENGTH_SHORT).show();

                    Log.i("Editing: ", myDir.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        if (mSelectedImageUri != null) {
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        } else {
            Toast.makeText(getActivity(), "Select an image first", Toast.LENGTH_SHORT).show();
        }


    }


    public void imageExistsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Error");
        builder.setMessage("Image with same name Exists");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                dialog.dismiss();
                saveImage();
            }
        });

        builder.show();
    }

    /* 3) Handle the results */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQ_CODE_GALLERY_PICKER:
                    mSelectedImageUri = data.getData();
                    mSelectedImageView.setImageURI(mSelectedImageUri);

                    break;

                case REQ_CODE_CSDK_IMAGE_EDITOR:

                    /* Set the image! */
                    Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                    mSelectedImageView.setImageURI(editedImageUri);

                    break;

            }
        }
    }
}
