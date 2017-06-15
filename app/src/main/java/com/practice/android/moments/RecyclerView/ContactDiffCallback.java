package com.practice.android.moments.RecyclerView;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by gmgautam5000 on 6/15/2017.
 */

public class ContactDiffCallback extends DiffUtil.Callback {

    private List<Contact> mOldList;
    private List<Contact> mNewList;

    public ContactDiffCallback(List<Contact> oldList, List<Contact> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // add a unique ID property on Contact and expose a getId() method
        return mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Contact oldContact = mOldList.get(oldItemPosition);
        Contact newContact = mNewList.get(newItemPosition);

        if (oldContact.getName() == newContact.getName() && oldContact.isOnline() == newContact.isOnline()) {
            return true;
        }
        return false;
    }
}