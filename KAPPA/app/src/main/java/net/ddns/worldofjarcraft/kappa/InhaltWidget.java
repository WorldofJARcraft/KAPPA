package net.ddns.worldofjarcraft.kappa;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link InhaltWidgetConfigureActivity InhaltWidgetConfigureActivity}
 */
public class InhaltWidget extends AppWidgetProvider {
    public static final String KEY_SCHRANK="key_schrank";
    public static final String KEY_FACH = "key_fach";
    public static final String EXTRA_LABEL = "TASK_TEXT";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Pair<String,String> widgetText = InhaltWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.inhalt_widget);
        views.setTextViewText(R.id.inhaltwidgetTitleLabel,context.getResources().getString(R.string.inhalte)+" "+widgetText.second+" "+context.getResources().getString(R.string.aus)+" "+widgetText.first);
        // Instruct the widget manager to update the widget
        Intent intent = new Intent(context,InhaltWidgetRemoteViewsService.class);
        Bundle b = new Bundle(2);
        b.putString(KEY_SCHRANK,widgetText.first);
        b.putString(KEY_FACH,widgetText.second);
        Intent serv = new Intent(context,SchrankUpdaterService.class);
        serv.putExtras(b);
        context.startService(serv);
        intent.putExtras(b);
        views.setRemoteAdapter(R.id.inhaltwidgetListView, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            InhaltWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

