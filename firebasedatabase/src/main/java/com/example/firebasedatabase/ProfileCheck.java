package com.example.firebasedatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileCheck extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_check);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Test Users").child("Test Info")
                .child("images");

        recyclerView = (RecyclerView) findViewById(R.id.blog_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm_recycle=new LinearLayoutManager(this);
        lm_recycle.setReverseLayout(true);
        recyclerView.setLayoutManager(lm_recycle);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.recycler,
                BlogViewHolder.class,
                databaseReference

                ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPhoto(getApplicationContext(),model.getPhoto());


            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editingprofile) {

            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {


        View mView;


        public BlogViewHolder(View itemView) {
            super(itemView);


             mView = itemView;
        }


        public void setTitle(String title) {

            TextView post_title = (TextView) mView.findViewById(R.id.title_image);
            post_title.setText(title);

        }

        public void setDescription(String description) {

            TextView post_descption = (TextView) mView.findViewById(R.id.get_description);
            post_descption.setText(description);

        }


        public void setPhoto(Context context,String photo){

            ImageView imageView = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(context).load(photo).into(imageView);

        }


    }


}
