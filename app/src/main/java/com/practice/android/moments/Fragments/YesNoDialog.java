package com.practice.android.moments.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

/**
 * Created by Ashutosh on 6/22/2017.
 */

public class YesNoDialog extends DialogFragment {
    public static final String ARG_TITLE = "YesNoDialog.Title";
    public static final String ARG_MESSAGE = "YesNoDialog.Message";
    private String userChosenTask;

    public YesNoDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        String title = args.getString(ARG_TITLE);
//        String message = args.getString(ARG_MESSAGE);

        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        return new AlertDialog.Builder(getActivity())
                .setItems(items, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (items[i].equals("Choose from Library")) {
                            userChosenTask = "Choose from Library";

                            //define

                        } else if (items[i].equals("Cancel")) {
                            dismiss();
                        }
                    }
                })
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
//                        dialog.dismiss();
//                    }
//                })
                .create();
    }
}