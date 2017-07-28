package net.ddns.worldofjarcraft.kappa;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.util.Pair;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link InhaltWidgetConfigureActivity InhaltWidgetConfigureActivity}
 */
public class InhaltWidget extends AppWidgetProvider {

    public static final String EXTRA_LABEL = "TASK_TEXT";
    public static final String KEY_SCHRANK = "schrank";
    public static final String KEY_FACH = "fach";
    /*@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(

                    context.getPackageName(),
                    R.layout.inhalt_widget

            );
            if(!ids.contains(appWidgetId)){
                ids.add(appWidgetId);
            }
            if(!InhaltWidgetConfigureActivity.loadTitlePref(context,appWidgetId).first.isEmpty()&&!InhaltWidgetConfigureActivity.loadTitlePref(context,appWidgetId).second.isEmpty()){
            // click event handler for the title, launches the app when the user clicks on title
            Intent titleIntent = new Intent(context, LaunchActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.inhaltwidgetTitleLabel, titlePendingIntent);
            Pair<String,String> pref = InhaltWidgetConfigureActivity.loadTitlePref(context,appWidgetId);
                views.setTextViewText(R.id.inhaltwidgetTitleLabel,context.getResources().getString(R.string.inhalte)+" "+pref.second+" "+context.getResources().getString(R.string.aus)+" "+pref.first);
            Bundle keys = new Bundle(2);
            zuordnung.put(appWidgetId,pref);
            keys.putString(KEY_SCHRANK,pref.first);
            keys.putString(KEY_FACH,pref.second);
            Intent intent = new Intent(context, InhaltWidgetRemoteViewsService.class);
            intent.putExtras(keys);
            context.startService(intent);
            InhaltWidgetRemoteViewsFactory.schrank=pref.first;
            InhaltWidgetRemoteViewsFactory.fach=pref.second;
            Log.e("REQUEST","Gesendet");
            views.setRemoteAdapter(R.id.inhaltwidgetListView, intent);

            // template to handle the click listener for each item
            Intent clickIntentTemplate = new Intent(context, AblaufendActivity.class);
            Bundle b = new Bundle(1);
            DataHelper helper = new DataHelper(MHDCheckerService.alle_lebensmittel);
            b.putSerializable("lebensmittel", helper);
            clickIntentTemplate.putExtras(b);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.inhaltwidgetListView, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            }
        }
    }

*/
    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, InhaltWidget.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(final Context context, Intent urintent) {
        final String action = urintent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, InhaltWidget.class);
            int[] appWidgetIds = mgr.getAppWidgetIds(cn);
            for(int appWidgetId:mgr.getAppWidgetIds(cn)) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.inhalt_widget);
                // Set up the intent that starts the StackViewService, which will
                // provide the views for this collection.
                Intent intent = new Intent(context, InhaltWidgetRemoteViewsService.class);
                // Add the app widget ID to the intent extras.
                Pair<String, String> pref = InhaltWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
                if (!pref.first.isEmpty() && !pref.second.isEmpty()) {
                    views.setTextViewText(R.id.inhaltwidgetTitleLabel, context.getResources().getString(R.string.inhalte) + " " + pref.second + " " + context.getResources().getString(R.string.aus) + " " + pref.first);
                    Bundle keys = new Bundle(2);
                    keys.putString(KEY_SCHRANK, pref.first);
                    keys.putString(KEY_FACH, pref.second);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    intent.putExtras(keys);
                    // Instantiate the RemoteViews object for the app widget layout.
                    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.inhalt_widget);
                    rv.setTextViewText(R.id.inhaltwidgetTitleLabel,context.getString(R.string.inhalte)+" "+pref.second+" "+context.getString(R.string.aus)+" "+pref.first);
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
                    mgr.notifyAppWidgetViewDataChanged(appWidgetId,R.id.inhaltwidgetListView);
                }
            }
        }
        super.onReceive(context, urintent);
    }
}