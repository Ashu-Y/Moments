package com.practice.android.moments.RecyclerView;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by gmgautam5000 on 6/15/2017.
 */

public class Contact {
        private String mName;
        private boolean mOnline;

        public Contact(String name, boolean online) {
            mName = name;
            mOnline = online;
        }

        public String getName() {
            return mName;
        }

        public boolean isOnline() {
            return mOnline;
        }

        private static int lastContactId = 0;

        public static ArrayList<Contact> createContactsList(int numContacts) {
            ArrayList<Contact> contacts = new ArrayList<Contact>();

            for (int i = 1; i <= numContacts; i++) {
                contacts.add(new Contact("Person " + ++lastContactId, i <= numContacts / 2));
            }

            return contacts;
        }


    public Object getId() {
        return id;
    }
}