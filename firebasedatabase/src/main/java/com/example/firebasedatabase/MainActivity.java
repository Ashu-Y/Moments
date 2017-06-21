package com.example.firebasedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private static final int Gallery_intent = 2;
    private Button mselectimage;
    private StorageReference mstorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mstorageReference = FirebaseStorage.getInstance().getReference();
        mselectimage = (Button) findViewById(R.id.selectimage);

        mselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image");


                startActivityForResult(i, Gallery_intent);
            }
        });

    }


}
