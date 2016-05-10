package com.memorize.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * Created by Tortuvshin on 11/21/2015.
 */
public class Admin extends DeviceAdminReceiver {


    private static final String TAG = "===Admin===";

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "Floating Lock Button: enabled");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "Floating Lock Button: disabled");
    }
}

