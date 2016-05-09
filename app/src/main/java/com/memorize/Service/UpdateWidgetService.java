package com.memorize.Service;
/**
 * Created by Tortuvshin on 11/15/2015.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.memorize.Database.DatabaseHelper;
import com.memorize.Model.RememberWord;
import com.memorize.R;

public class UpdateWidgetService extends Service {

    private static final String TAG = "===WordWidgetUpdate===";
    DatabaseHelper databaseHelper;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.d(TAG, "onStart started");

        Random random = new Random();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        if (appWidgetIds.length > 0) {
            for (int widgetId : appWidgetIds) {
                List<String> qList = getWidgetRememberWords();
                int nextInt = random.nextInt(qList.size());
                RemoteViews remoteViews = new RemoteViews(getPackageName(),	R.layout.word_widget);
                remoteViews.setTextViewText(R.id.widget_textview, qList.get(nextInt));
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
            stopSelf();
        }
        super.onStart(intent, startId);
    }

    public List<String> getWidgetRememberWords(){
        List<String> qList = new ArrayList<String>();
        qList.add("dictionary - noun. толь бичиг");
        List<RememberWord> rememberWords = databaseHelper.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = ""+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+" "+rememberWord.getRememberMongolia();
            qList.add(listWord);
        }
        return qList;
    }
}