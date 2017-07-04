package com.practice.android.moments.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.android.moments.Activities.SettingsActivity;
import com.practice.android.moments.Editing.EditingActivity;
import com.practice.android.moments.Fragments.DashboardFragment;
import com.practice.android.moments.Models.Post;
import com.practice.android.moments.R;

import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ashutosh on 6/28/2017.
 */

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashViewHolder> {

    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int GALLERY_PICTURE = 1;
    private static final int CAMERA_REQUEST = 0;
    DashboardFragment dashFragment;
    private List<Post> itemList;
    private Context mContext;

    public DashboardRecyclerAdapter(Context context, List<Post> itemList, DashboardFragment dashFragment) {
        this.itemList = itemList;
        mContext = context;
        this.dashFragment = dashFragment;

    }

    @Override
    public DashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dash_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item, parent, false);
        DashViewHolder holder = new DashViewHolder(dash_layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(DashViewHolder holder, int position) {
//        holder.setData(itemList.get(position), position);

        holder.dashText.setText(itemList.get(position).getUsername());

//        Picasso.with(mContext)
//                .load(itemList.get(position).getImageId())
//                .into(holder.dashImage);

//        Picasso.with(mContext)
//                .load(itemList.get(position).getImageId())
//
//                .into(holder.dashImage);
//
//          holder.dashImage.setImageResource(itemList.get(position).getImageId());

        holder.dashCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Post value = itemList.get(position);
                Toast.makeText(mContext, value.getUsername(), Toast.LENGTH_SHORT).show();
                switch (value.getUsername().toUpperCase()) {
                    case "CHOOSE IMAGE":
                        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {

                            fn_Choose_Image();
                        } else {
                            isStoragePermissionGranted();
                            fn_Choose_Image();
                        }
                        break;

                    case "EDITING":
                        mContext.startActivity(new Intent(mContext, EditingActivity.class));
                        break;

                    case "UPLOAD":
                        dashFragment.addUpload();
                        break;

                    case "PROFILE":
                        dashFragment.addProfile();
                        break;

                    case "EDIT PROFILE":
                        dashFragment.addEditProfile();
                        break;

                    case "SETTINGS":
                        mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                        break;


                    case "LOG OUT":

                        break;

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void fn_Choose_Image() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(mContext);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                dashFragment.startActivityForResult(intent, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton("Camera", (arg0, arg1) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            dashFragment.startActivityForResult(intent, CAMERA_REQUEST);
        });

        myAlertDialog.show();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                Log.v("dsf", "Permission is granted");
                return true;

            } else {
                Log.v("wef", "Permission is revoked");

                ActivityCompat.requestPermissions(dashFragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);

                return false;
            }
        } else {
            Log.v("f", "Permission is granted");
            return true;
        }

    }






    public class DashViewHolder extends RecyclerView.ViewHolder {

        CardView dashCard;
        TextView dashText;
        ImageView dashImage;

        public DashViewHolder(View itemView) {
            super(itemView);

            dashCard = (CardView) itemView.findViewById(R.id.dash_card);
            dashText = (TextView) itemView.findViewById(R.id.dash_text);
            dashImage = (ImageView) itemView.findViewById(R.id.dash_image);

            /*itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Post value = itemList.get(pos);
                    Toast.makeText(mContext, value.getUsername(), Toast.LENGTH_SHORT).show();
                    switch(value.getUsername().toUpperCase()) {
                        case "HOME":
                            mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                            break;

                        case "PROFILE":
                            dashFragment.addProfile();
                            break;

                        case "SETTINGS":
                            mContext.startActivity(new Intent(mContext, SettingsActivity.class));
                            break;

                        case "CHANGE PASSWORD":
                            mContext.startActivity(new Intent(mContext, ChangePassword.class));
                            break;

                    }

                }
            });*/
        }

        public void setData(Post post, int position) {
            dashText.setText(post.getUsername());
            dashImage.setImageResource(post.getImageId());
        }


    }
}
