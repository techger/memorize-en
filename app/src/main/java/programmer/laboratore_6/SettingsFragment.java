package programmer.laboratore_6;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import programmer.laboratore_6.Receiver.Admin;
import programmer.laboratore_6.Service.DesktopButtonService;
import programmer.laboratore_6.Service.LockService;

public class SettingsFragment extends Fragment {

    private static final String TAG = "===Settings===";
    static final int RESULT_ENABLE = 1;
    public static final String PREF = "Settings";
    public static final String ADMIN_ENABLED = "admin_enabled";
    public static final String BUTTON_DISPLAYED = "button_displayed";
    public static final String LOCKSCREEN_ENABLE = "lock_screen_enable";
    View rootView;

    private ComponentName componentName;
    private ToggleButton enableAdmin;
    private ToggleButton displaySwitch;
    private ToggleButton lockScreenEnable;
    private TextView textView;
    private SharedPreferences preferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        preferences = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        textView = (TextView) rootView.findViewById(R.id.textView);
        TextView adminEnabledLabel = (TextView) rootView.findViewById(R.id.admin_enabled_label);
        TextView displayLockLabel = (TextView) rootView.findViewById(R.id.display_lock_label);

        componentName = new ComponentName(getActivity(), Admin.class);

        enableAdmin = (ToggleButton) rootView.findViewById(R.id.admin_enabled);
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
                        getActivity().stopService(new Intent(getActivity(), DesktopButtonService.class));
                        preferences.edit().putBoolean(BUTTON_DISPLAYED, false).commit();
                    }
                    preferences.edit().putBoolean(ADMIN_ENABLED, false).commit();
                    textView.setText(R.string.initial_message);
                }
            }
        });

        displaySwitch = (ToggleButton) rootView.findViewById(R.id.display_lock);
        displaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (enableAdmin != null && enableAdmin.isChecked()) {
                        getActivity().startService(new Intent(getActivity(), DesktopButtonService.class));
                        preferences.edit().putBoolean(BUTTON_DISPLAYED, true).commit();
                    }
                } else {
                    getActivity().stopService(new Intent(getActivity(), DesktopButtonService.class));
                    preferences.edit().putBoolean(BUTTON_DISPLAYED, false).commit();
                }
            }
        });
        lockScreenEnable = (ToggleButton)rootView.findViewById(R.id.lockScreenEnable);
        lockScreenEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    getActivity().startService(new Intent(getActivity(), LockService.class));
                    preferences.edit().putBoolean(LOCKSCREEN_ENABLE, true).commit();
                } else {
                    getActivity().stopService(new Intent(getActivity(), LockService.class));
                    preferences.edit().putBoolean(LOCKSCREEN_ENABLE, false).commit();
                }

            }
        });
        return rootView;
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
