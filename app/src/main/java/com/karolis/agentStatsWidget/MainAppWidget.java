package com.karolis.agentStatsWidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

public class MainAppWidget extends AppWidgetProvider {
    private static final String TAG = "MainAppWidget";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId,
                WidgetConfigure.loadApiKey(context, appWidgetId)
            );
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                String apiKey) {
        Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId + " apiKey=" + apiKey);

        RemoteViews views = new RemoteViews(
            context.getPackageName(),
            com.karolis.agentStatsWidget.R.layout.widget
        );

        views.setTextViewText(com.karolis.agentStatsWidget.R.id.apiKey, apiKey);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
