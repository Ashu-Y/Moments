//package com.practice.android.moments.Adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.ViewHolder;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.practice.android.moments.Models.Image;
//import com.practice.android.moments.R;
//
//import java.util.List;
//
///**
// * Created by Ashutosh on 7/12/2017.
// */
//
//public class GalleryAdapter extends RecyclerView.Adapter {
//    private List<Image> images;
//    private Context mContext;
//
//    public GalleryAdapter(List<Image> images, Context context) {
//        this.images = images;
//        mContext = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView gallery_item;
//
//        View mView;
//        DatabaseReference mDatabaseReference;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//
//            mView = itemView;
//            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User Pictures");
//            gallery_item = (ImageView) mView.findViewById(R.id.thumbnail);
//        }
//
//        public void setImages(Context context, String position) {
//
//            mDatabaseReference.child(position).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Image user = dataSnapshot.getValue(Image.class);
//
//                    assert user != null;
//                    Glide.with(context).load(user.getSmall()).placeholder(R.drawable.c1).into(gallery_item);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//
//
//    }
//
//
//}
