package com.example.editing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    /* 1) Add a member variable for our Image View */
    private ImageView mEditedImageView;

    public static final String TAG = MainActivity.class.getSimpleName();
    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;
    static final int REQ_CODE_GALLERY_PICKER = 20;

    private Button mOpenGalleryButton;
    private Button mLaunchImageEditorButton;
    private Button save;
    private ImageView mSelectedImageView;

    private Uri mSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 2) Find the layout's ImageView by ID */
        mEditedImageView = (ImageView) findViewById(R.id.editedImageView);


        mOpenGalleryButton = (Button) findViewById(R.id.openGalleryButton);
        mLaunchImageEditorButton = (Button) findViewById(R.id.launchImageEditorButton);
        save = (Button) findViewById(R.id.save);

        mSelectedImageView = (ImageView) findViewById(R.id.editedImageView);



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
                if(mSelectedImageUri != null){
                    Intent imageEditorIntent = new AdobeImageIntent.Builder(MainActivity.this)
                            .setData(mSelectedImageUri)
                            .build();

                    startActivityForResult(imageEditorIntent, REQ_CODE_CSDK_IMAGE_EDITOR);
                }else {
                    Toast.makeText(MainActivity.this, "Select an image from the Gallery", Toast.LENGTH_LONG).show();
                }
            }
        });

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File myDir = new File(root.toString() + "/Moments");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


        View content = findViewById(R.id.editedImageView);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();

        File cachePath = new File(myDir + "/moments_10000.jpg");
        try {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(CompressFormat.JPEG, 100, ostream);
            ostream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

            }
        });

    }

    /* 3) Handle the results */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
