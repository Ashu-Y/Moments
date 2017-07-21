package com.practice.android.moments.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.practice.android.moments.Activities.BottomNavigation;
import com.practice.android.moments.BuildConfig;
import com.practice.android.moments.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.practice.android.moments.Activities.BottomNavigation.usertoken;


public class EditingFragment extends Fragment {

    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;
    static final int REQ_CODE_GALLERY_PICKER = 20;
    private static final int REQ_CODE_CAMERA_REQUEST = 1;

    public final String TAG = getClass().getSimpleName();
    ImageView content;
    Bitmap bitmap;
    File myDir;
    Uri tempURI;
    Uri mediumpic = null;
    Uri thumbnailpic = null;
    Uri download_uri;
    DatabaseReference databaseReference, mdatabaseReference;
    StorageReference mstorageReference;
    File mImageFile;
    FirebaseUser firebaseuser;
    ProgressDialog progressDialog;
    ImageView mEditedImageView;
    ImageView mOpenGalleryButton;
    FloatingActionButton mLaunchImageEditorButton;
    FloatingActionButton save, Uploadimage;
    ImageView mSelectedImageView;
    Uri mSelectedImageUri;
    Uri selectedImage = null;
    String imageName, imageTitle, imageDescription;
    String user_id;
    double progress1, progress2, progress3, progress;
    String thumbpic, picture, thumbmed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_editing, container, false);

        /* 2) Find the layout's ImageView by ID */
        mEditedImageView = (ImageView) v.findViewById(R.id.editedImageView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mstorageReference = FirebaseStorage.getInstance().getReference();

        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();


        mOpenGalleryButton = (ImageView) v.findViewById(R.id.openGalleryButton);
        mLaunchImageEditorButton = (FloatingActionButton) v.findViewById(R.id.launchImageEditorButton);
        save = (FloatingActionButton) v.findViewById(R.id.saveButton);

        Uploadimage = (FloatingActionButton) v.findViewById(R.id.button_upload);
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
//                Intent galleryPickerIntent = new Intent();
//                galleryPickerIntent.setType("image/*");
//                galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);

//                startActivityForResult(Intent.createChooser(galleryPickerIntent, "Select an image"), REQ_CODE_GALLERY_PICKER);

                fn_Choose_Image();
            }
        });

        mLaunchImageEditorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedImageUri != null) {
                    Intent imageEditorIntent = new AdobeImageIntent.Builder(getActivity())
                            .setData(mSelectedImageUri)
                            .withVibrationEnabled(true)
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


        Uploadimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String title;
                final CharSequence[] items = {"Choose from Library",
                        "Cancel"};

                title = "Select an option!";

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Choose from Library")) {



                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


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

    public void fn_Choose_Image() {

        String title;
        final CharSequence[] items = {"Choose from Library",
                "Take Photo",
                "Cancel"};

        title = "Select an option!";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Choose from Library")) {

                    Intent galleryPickerIntent = new Intent();
                    galleryPickerIntent.setType("image/*");
                    galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(galleryPickerIntent, "Select an image"), REQ_CODE_GALLERY_PICKER);
//
//                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            startActivityForResult(intent, GALLERY_PICTURE);

                } else if (items[item].equals("Take Photo")) {

                    Calendar c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH) + 1;
                    int year = c.get(Calendar.YEAR);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minutes = c.get(Calendar.MINUTE);
                    int seconds = c.get(Calendar.SECOND);
                    int milliSeconds = c.get(Calendar.MILLISECOND);


                    //Can cause error in uploading
                    String temp = day + "-" + month + "-" + year + "-" + hour + ":" + minutes + ":" + seconds + ":" + milliSeconds + ".png";


                    mImageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + temp);


//            Uri tempURI = Uri.parse(mImageFile.getAbsolutePath());

                    tempURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",
                            mImageFile);

                    Log.e("tempURI: ", tempURI.toString());

                    Toast.makeText(getActivity(), tempURI.toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempURI);
                    startActivityForResult(intent, REQ_CODE_CAMERA_REQUEST);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    /* 3) Handle the results */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String file;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQ_CODE_GALLERY_PICKER:
                    mSelectedImageUri = data.getData();
                    mSelectedImageView.setImageURI(mSelectedImageUri);

                    file = compressImage(String.valueOf(data.getData()));
                    thumbnailpic = Uri.fromFile(new File(file));

                    mediumpic = Uri.fromFile(new File(file));

                    break;

                case REQ_CODE_CAMERA_REQUEST:


                    mSelectedImageUri = Uri.fromFile(mImageFile);
//

                    file = compressImage(String.valueOf(mSelectedImageUri));
                    thumbnailpic = Uri.fromFile(new File(file));

                    mediumpic = Uri.fromFile(new File(file));
                    Log.d("ImagePath", "Image saved to path : " + mImageFile.getAbsolutePath());

                    Toast.makeText(getActivity(), "Path: " + mImageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    mSelectedImageView.setImageURI(Uri.parse(mImageFile.getAbsolutePath()));

                    mSelectedImageView.setImageURI(tempURI);
                    tempURI = null;

                    break;


                case REQ_CODE_CSDK_IMAGE_EDITOR:

                    /* Set the image! */
                    Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                    mSelectedImageView.setImageURI(editedImageUri);

                    file = compressImage(String.valueOf(editedImageUri));
                    thumbnailpic = Uri.fromFile(new File(file));

                    mediumpic = Uri.fromFile(new File(file));

                    break;


            }
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);


//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("name ======= ", filename);
        return filename;

    }

    public String getFilename() {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File file = new File(root.toString() + "/Moments");

        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        Log.e("name location ", uriSting);

        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        @SuppressLint("Recycle")
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        Log.e("SIze ", String.valueOf(inSampleSize));
        return inSampleSize;

    }

    //Uploading File
    private void uploadFile() {
        //if there is a file to upload
        if (selectedImage != null) {
            //displaying a progress dialog while upload is going on
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);


            StorageReference riversRef = mstorageReference.child("Photos")
                    .child(firebaseuser.getUid()).child("User Photo")
                    .child(selectedImage.getLastPathSegment());

            riversRef.child("picture").putFile(selectedImage)
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog

                        download_uri = taskSnapshot.getDownloadUrl();
                        user_id = firebaseuser.getUid();
                        picture = String.valueOf(download_uri);


                        riversRef.child("thumbnail_medium").putFile(mediumpic).addOnSuccessListener(taskSnapshot2 -> {
                            thumbmed = String.valueOf(taskSnapshot2.getDownloadUrl());
                            riversRef.child("thumbnail").putFile(thumbnailpic).addOnSuccessListener(taskSnapshot1 -> {

                                progressDialog.dismiss();


                                thumbpic = String.valueOf(taskSnapshot1.getDownloadUrl());


                                Calendar c = Calendar.getInstance();
                                int day = c.get(Calendar.DAY_OF_MONTH);
                                int month = c.get(Calendar.MONTH) + 1;
                                int year = c.get(Calendar.YEAR);
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                int minutes = c.get(Calendar.MINUTE);
                                int seconds = c.get(Calendar.SECOND);
                                int milliSeconds = c.get(Calendar.MILLISECOND);


                                //Can cause error in uploading
                                imageName = day + "-" + month + "-" + year + "-" + hour + ":" + minutes + ":" + seconds + ":" + milliSeconds + "";
                                Log.e("Camera", imageName);


                                DatabaseReference currentuser_db = databaseReference.child("User Pictures");
                                currentuser_db.child(imageName).orderByPriority();
                                DatabaseReference currentuser = currentuser_db.child(imageName);
                                currentuser.child("pic").setValue(picture);
                                currentuser.child("picName").setValue(imageName);
                                currentuser.child("userName").setValue(firebaseuser.getDisplayName());
                                currentuser.child("user_id").setValue(user_id);
                                currentuser.child("thumbnail_pic").setValue(thumbpic);
                                currentuser.child("medium").setValue(thumbmed);
                                currentuser.child("userToken").setValue(usertoken);
//                                currentuser.child("title").setValue(title.getText().toString());
//                                currentuser.child("description").setValue(description.getText().toString());


                                DatabaseReference user_db = mdatabaseReference.child(user_id).child("User Pictures");
                                user_db.child(imageName).orderByPriority();
                                DatabaseReference user = user_db.child(imageName);
                                user.child("pic").setValue(picture);
                                user.child("picName").setValue(imageName);
                                user.child("userName").setValue(firebaseuser.getDisplayName());
                                user.child("user_id").setValue(user_id);
                                user.child("userToken").setValue(usertoken);
                                user.child("medium").setValue(thumbmed);
                                user.child("thumbnail_pic").setValue(thumbpic);

//                                user.child("title").setValue(title.getText().toString());
//                                user.child("description").setValue(description.getText().toString());


                                //and displaying a success toast
                                Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(getActivity(), BottomNavigation.class));


                            }).addOnProgressListener(taskSnapshot1 -> {
                                //calculating progress percentage
                                progress3 = (100.0 * taskSnapshot1.getBytesTransferred()) / taskSnapshot1.getTotalByteCount();
                                Log.e("Process3", String.valueOf(progress3));

                                progress = (progress1 + progress2 + progress3) / 3;

                                Log.e("Process", String.valueOf(progress));

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

                            });

                        }).addOnProgressListener(taskSnapshot2 -> {
                            //calculating progress percentage
                            progress2 = (100.0 * taskSnapshot2.getBytesTransferred()) / taskSnapshot2.getTotalByteCount();
                            Log.e("Process2", String.valueOf(progress2));
                        });
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
                        progress1 = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.e("Process1", String.valueOf(progress1));

//                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress1) + "%...");
                    });

        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getActivity(), "File Upload Failed", Toast.LENGTH_SHORT).show();
        }
    }

}
