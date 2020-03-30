package net.ddns.worldofjarcraft.kappa;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;

import android.util.Pair;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link InhaltWidgetConfigureActivity InhaltWidgetConfigureActivity}
 */
public class InhaltWidget extends AppWidgetProvider {

    public static final String EXTRA_LABEL = "TASK_TEXT";
    public static final String KEY_SCHRANK = "schrank";
    public static final String KEY_FACH = "fach";


    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, InhaltWidget.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(final Context context, Intent urintent) {
        final String action = urintent.getAction();
        if (action != null && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, InhaltWidget.class);
            int[] appWidgetIds = mgr.getAppWidgetIds(cn);
            for (int appWidgetId : mgr.getAppWidgetIds(cn)) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.inhalt_widget);
                // Set up the intent that starts the StackViewService, which will
                // provide the views for this collection.
                Intent intent = new Intent(context, InhaltWidgetRemoteViewsService.class);
                // Add the app widget ID to the intent extras.
                Pair<Integer, Integer> pref = InhaltWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
                if (pref.first!=-1 && pref.second != -1) {
                    views.setTextViewText(R.id.inhaltwidgetTitleLabel, context.getResources().getString(R.string.inhalte) + " " + pref.second + " " + context.getResources().getString(R.string.aus) + " " + pref.first);
                    Bundle keys = new Bundle(2);
                    keys.putInt(KEY_SCHRANK, pref.first);
                    keys.putInt(KEY_FACH, pref.second);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    intent.putExtras(keys);
                    // Instantiate the RemoteViews object for the app widget layout.
                    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.inhalt_widget);
                    rv.setTextViewText(R.id.inhaltwidgetTitleLabel, context.getString(R.string.inhalte) + " " + pref.second + " " + context.getString(R.string.aus) + " " + pref.first);
                    // Set up the RemoteViews object to use a RemoteViews adapter.
                    // This adapter connects
                    // to a RemoteViewsService  through the specified intent.
                    // This is how you populate the data.
                    rv.setRemoteAdapter(R.id.inhaltwidgetListView, intent);

                    // The empty view is displayed when the collection has no items.
                    // It should be in the same layout used to instantiate the RemoteViews
                    // object above.
                    rv.setEmptyView(R.id.inhaltwidgetListView, R.string.network_error);

                    //
                    // Do additional processing specific to this app widget...
                    //
                    mgr.updateAppWidget(appWidgetId, rv);
                    mgr.notifyAppWidgetViewDataChanged(appWidgetId, R.id.inhaltwidgetListView);
                }
            }
        }
        super.onReceive(context, urintent);
    }
}