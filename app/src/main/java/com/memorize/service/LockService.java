package com.memorize.service;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.memorize.activity.Locker;

/**
 * Created by Tortuvshin on 11/19/2015.
 */
public class LockService extends Service {

    private static final String TAG = "===LockService===";
    private KeyguardManager km = null;
    @SuppressWarnings("deprecation")
    private KeyguardManager.KeyguardLock keylock = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("android.intent.action.SCREEN_OFF")){
                Intent i = new Intent(context, Locker.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        km=(KeyguardManager) this.getSystemService(Activity.KEYGUARD_SERVICE);
        if(km!=null){
            keylock = km.newKeyguardLock("test");
            keylock.disableKeyguard();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Lock Screen Enabled!", Toast.LENGTH_LONG).show();
        IntentFilter filter = new IntentFilter("com.androidhuman.action.isAlive");
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        return Service.START_NOT_STICKY;
    }
    @Override
    public void onDestroy(){
        if(keylock!=null){
            keylock.reenableKeyguard();
        }
        if(mReceiver != null)
            unregisterReceiver(mReceiver);
        Toast.makeText(this, "Lock Screen Disabled!", Toast.LENGTH_LONG).show();
    }
}
