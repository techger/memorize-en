package com.memorize;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.memorize.Database.DatabaseHelper;
import com.memorize.Database.RememberWordsAdapter;
import com.memorize.Model.RememberWord;

public class LockScreen extends AppCompatActivity{

    private static final String TAG = "===LockScreenAc===";
    ScrollView background;
    RememberWordsAdapter rememberWordsAdapter;
    private TextView words;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        rememberWordsAdapter = new RememberWordsAdapter(this);
        words = (TextView)findViewById(R.id.lockScreenWord);
        background = (ScrollView)findViewById(R.id.lockScreenView);
        List<String> remember = new ArrayList<String>();
        //qList.add("");
        Random random = new Random();
        List<RememberWord> rememberWords = rememberWordsAdapter.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = "\n"+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+
                    " "+rememberWord.getRememberMongolia()+"";
            remember.add(listWord);
        }
        final String item = remember.get(random.nextInt(remember.size()));

        words.setText(item);
        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });
        BroadcastReceiver mybroadcast = new BroadcastReceiver() {

            //When Event is published, onReceive method is called
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.i("[BroadcastReceiver]", "MyReceiver");

                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    words.setText(item);
                    Log.i("[BroadcastReceiver]", "Screen ON onCreate");
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    Log.i("[BroadcastReceiver]", "Screen OFF");
                    finish();
                }

            }
        };

        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return super.onTouchEvent(event);
    }

}
