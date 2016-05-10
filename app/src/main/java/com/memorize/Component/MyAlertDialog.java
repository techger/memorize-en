package com.memorize.Component;

/**
 * Created by Tortuvshin on 10/18/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.memorize.R;

public class MyAlertDialog {
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if(status != null)
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
            alertDialog.setButton("Тийм", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}