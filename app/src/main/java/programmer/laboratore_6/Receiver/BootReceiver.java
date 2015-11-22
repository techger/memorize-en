package programmer.laboratore_6.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import programmer.laboratore_6.Service.DesktopButtonService;
import programmer.laboratore_6.SettingsFragment;

/**
 * Created by Byambaa on 11/21/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "===BootReceiver===";
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences(SettingsFragment.PREF, Context.MODE_PRIVATE);

        boolean serviceEnabled = preferences.getBoolean(SettingsFragment.BUTTON_DISPLAYED, false);

        if (serviceEnabled) {
            Intent floatingService = new Intent(context, DesktopButtonService.class);
            context.startService(floatingService);
        }
    }
}