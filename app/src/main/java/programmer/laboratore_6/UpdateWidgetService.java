package programmer.laboratore_6;

/**
 * Created by Byambaa on 11/15/2015.
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

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.RememberWord;

public class UpdateWidgetService extends Service {
    private static final String TAG = UpdateWidgetService.class.getSimpleName();
    MyDbHandler myDbHandler;
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
        myDbHandler = new MyDbHandler(getApplicationContext());
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
        //qList.add("");
        List<RememberWord> rememberWords = myDbHandler.getAllRememberWords();
        for (RememberWord rememberWord : rememberWords){
            String listWord = ""+rememberWord.getRememberEnglish() +
                    " - "+rememberWord.getRememberType()+" "+rememberWord.getRememberMongolia();
            qList.add(listWord);
        }
        return qList;
    }
}
