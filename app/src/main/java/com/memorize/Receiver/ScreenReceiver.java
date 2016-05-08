package com.memorize.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tortuvshin on 11/30/2015.
 */

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn = true;
        }
    }

}