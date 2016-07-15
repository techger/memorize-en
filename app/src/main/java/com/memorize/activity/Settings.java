package com.memorize.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.memorize.R;
import com.memorize.lockscreen.*;
import com.memorize.lockscreen.LockScreen;
import com.memorize.receiver.Admin;
import com.memorize.service.DesktopButtonService;
import com.memorize.service.LockService;

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

    private SwitchCompat mSwitchd = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;

        SharedPreferencesUtil.init(mContext);

        mSwitchd = (SwitchCompat) this.findViewById(R.id.switch_locksetting);
        mSwitchd.setTextOn("yes");
        mSwitchd.setTextOff("no");
        boolean lockState = SharedPreferencesUtil.get(com.memorize.lockscreen.LockScreen.ISLOCK);
        if (lockState) {
            mSwitchd.setChecked(true);

        } else {
            mSwitchd.setChecked(false);

        }

        mSwitchd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(LockScreen.ISLOCK, true);
                    LockScreen.getInstance(mContext).startLockscreenService();
                } else {
                    SharedPreferencesUtil.setBoolean(LockScreen.ISLOCK, false);
                    LockScreen.getInstance(mContext).stopLockscreenService();
                }

            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();

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
}
