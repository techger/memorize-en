package com.memorize.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.memorize.Activity.Settings;
import com.memorize.Service.DesktopButtonService;

/**
 * Created by Tortuvshin on 11/21/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "===BootReceiver===";
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences(Settings.PREF, Context.MODE_PRIVATE);

        boolean serviceEnabled = preferences.getBoolean(Settings.BUTTON_DISPLAYED, false);

        if (serviceEnabled) {
            Intent floatingService = new Intent(context, DesktopButtonService.class);
            context.startService(floatingService);
        }
    }
}