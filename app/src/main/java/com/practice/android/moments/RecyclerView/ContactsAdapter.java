package com.practice.android.moments.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.practice.android.moments.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmgautam5000 on 6/15/2017.
 */




public class ContactsAdapter extends
        RecyclerView.Adapter<ContactsAdapter.ViewHolder> {



    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView ImageViewNo1;
        public  ImageView ImageViewNo2;
        public ImageView ImageViewNo3;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.textview1);
            ImageViewNo1= (ImageView) itemView.findViewById(R.id.imageView1);
            ImageViewNo2= (ImageView) itemView.findViewById(R.id.imageview2);
            ImageViewNo3 = (ImageView) itemView.findViewById(R.id.imageview3);
        }
    }
   {


        // Store a member variable for the contacts
        List<Contact> mContacts;
        // Store the context for easy access
         Context mContext;

        // Pass in the contact array into the constructor
         List<Contact> contacts;
       ContactsAdapter(Context context, mContext, List<Contact> contacts) {
            mContacts = contacts;
            mContext = Context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position


        Contact contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getName());
        ImageView imageview = viewHolder.ImageView;
        ImageView.setText("Message");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mContacts.size();
    }
    }
public class UserListActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(this, contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
}