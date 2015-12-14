package programmer.laboratore_6;

/**
 * Created by Byambaa on 11/15/2015.
 */
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import programmer.laboratore_6.Database.MyDbHandler;
import programmer.laboratore_6.Model.RecitationWord;
import programmer.laboratore_6.Service.RecritionWordService;
import programmer.laboratore_6.Service.UpdateWidgetService;

public class WordWidget extends AppWidgetProvider {
    Button recitation;
    Context context;
    MyDbHandler myDbHandler = new MyDbHandler(context);
    RemoteViews remoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.word_widget);
        Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
        Intent intentword = new Intent(context.getApplicationContext(), RecritionWordService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getService(
                context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getService(
                context.getApplicationContext(), 0, intentword, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.recitation, ButtonClicked(context));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        context.startService(intent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    public static PendingIntent ButtonClicked(Context context) {
        RemoteViews text = new RemoteViews(context.getPackageName(), R.id.widget_textview);
        Intent intent = new Intent(text.toString());
        Log.d("AAAAAAAAAA", text.toString());
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}