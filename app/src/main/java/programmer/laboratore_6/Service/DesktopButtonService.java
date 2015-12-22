package programmer.laboratore_6.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.RememberWord;
import programmer.laboratore_6.R;
import programmer.laboratore_6.SettingsFragment;

/**
 * Created by Toroo on 11/21/2015.
 */
public class DesktopButtonService extends Service {

    private static final String TAG = "=DesktopButtonService=";

    WindowManager windowManager;
    ImageButton floatingButton;
    MyDbHandler myDbHandler;
    Context context;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (floatingButton != null)
                floatingButton.performClick();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        registerReceiver(receiver, new IntentFilter("LOCK"));

        Intent i = new Intent(this, SettingsFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        Intent b = new Intent("Цээжлэх үгээ харах");
        PendingIntent bpIntent = PendingIntent.getBroadcast(this, 0, b, 0);

        startForeground(android.os.Process.myPid(), new NotificationCompat.Builder(this)
                .setContentTitle("Англи үг цээжлэх")
                .setContentText("Англи үг цээжлэх програмд нэвтрэх")
                .setSmallIcon(R.drawable.programmer)
                .setContentIntent(pIntent)
                .addAction(R.drawable.programmer, "Lock", bpIntent)
                .build());
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        myDbHandler = new MyDbHandler(getApplicationContext());
        floatingButton = new ImageButton(this);
        floatingButton.setLayoutParams(new ViewGroup.LayoutParams(48, 48));
        floatingButton.setBackgroundResource(R.drawable.circle_button);
        floatingButton.setImageResource(R.drawable.programmer);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                100, 100, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(floatingButton, params);

        floatingButton.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean moved = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moved = false;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!moved)
                            floatingButton.performClick();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        moved = true;
                        params.x = initialX - (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingButton, params);
                        return true;
                }
                return false;
            }
        });
        final List<String> remember = new ArrayList<String>();
        //qList.add("");
        List<RememberWord> rememberWords = myDbHandler.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = "\n"+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+
                    " "+rememberWord.getRememberMongolia()+"";
            remember.add(listWord);
        }
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Цээжлэх үгийн жагсаалт"+remember.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (floatingButton != null)
            windowManager.removeView(floatingButton);
    }
    public List<String> getRememberWords(){
        List<String> qList = new ArrayList<String>();
        qList.add("dictionary - noun. толь бичиг");
        List<RememberWord> rememberWords = myDbHandler.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = ""+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+" "+rememberWord.getRememberMongolia();
            qList.add(listWord);
        }
        return qList;
    }
}