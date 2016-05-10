package com.memorize.Activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.memorize.R;
import com.memorize.Receiver.Admin;
import com.memorize.Service.DesktopButtonService;
import com.memorize.Service.LockService;

public class Settings extends AppCompatActivity {

    private static final String TAG = "===Settings===";
    static final int RESULT_ENABLE = 1;
    public static final String PREF = "Settings";
    public static final String ADMIN_ENABLED = "admin_enabled";
    public static final String BUTTON_DISPLAYED = "button_displayed";
    public static final String LOCKSCREEN_ENABLE = "lock_screen_enable";

    private ComponentName componentName;
    private ToggleButton enableAdmin;
    private ToggleButton displaySwitch;
    private ToggleButton lockScreenEnable;
    private TextView enableAdminText;
    private TextView displayButton;
    private TextView lockScreen;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        enableAdminText = (TextView)findViewById(R.id.admin_enabled_label);
        TextView adminEnabledLabel = (TextView)findViewById(R.id.admin_enabled_label);
        TextView displayLockLabel = (TextView)findViewById(R.id.display_lock_label);

        componentName = new ComponentName(this, Admin.class);

        enableAdmin = (ToggleButton)findViewById(R.id.admin_enabled);
        enableAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startEnableAdminIntent();
                    displaySwitch.setEnabled(true);
                } else {
                    displaySwitch.setEnabled(false);
                    if (displaySwitch != null && displaySwitch.isChecked()) {
                        displaySwitch.setChecked(false);
                        stopService(new Intent(Settings.this, DesktopButtonService.class));
                        preferences.edit().putBoolean(BUTTON_DISPLAYED, false).commit();
                    }
                    preferences.edit().putBoolean(ADMIN_ENABLED, false).commit();

                }
            }
        });

        displaySwitch = (ToggleButton)findViewById(R.id.display_lock);
        displaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (enableAdmin != null && enableAdmin.isChecked()) {
                        startService(new Intent(Settings.this, DesktopButtonService.class));
                        preferences.edit().putBoolean(BUTTON_DISPLAYED, true).commit();
                    }
                } else {
                    stopService(new Intent(Settings.this, DesktopButtonService.class));
                    preferences.edit().putBoolean(BUTTON_DISPLAYED, false).commit();
                }
            }
        });
        lockScreenEnable = (ToggleButton)findViewById(R.id.lockScreenEnable);
        lockScreenEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(Settings.this, LockService.class));
                    preferences.edit().putBoolean(LOCKSCREEN_ENABLE, true).commit();
                } else {
                    stopService(new Intent(Settings.this, LockService.class));
                    preferences.edit().putBoolean(LOCKSCREEN_ENABLE, false).commit();
                }

            }
        });
    }


    private void startEnableAdminIntent() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Дэлгэцэн дээр шинэ товч гаргах");
        startActivityForResult(intent, RESULT_ENABLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "Admin enabled!");
                    preferences.edit().putBoolean(ADMIN_ENABLED, true).commit();
                } else {
                    Log.i(TAG, "Admin enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
