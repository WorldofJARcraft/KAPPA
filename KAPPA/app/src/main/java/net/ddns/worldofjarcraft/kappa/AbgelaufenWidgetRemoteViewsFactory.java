package net.ddns.worldofjarcraft.kappa;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 24.07.2017.
 */

public class AbgelaufenWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    public List<String[]> daten;
    public AbgelaufenWidgetRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
    }
    @Override
    public void onCreate() {
        daten = data.ablaufende;
        if(daten==null) {
            daten = new ArrayList<>();
            daten.add(new String[]{"Nichts anzuzeigen"});
        }
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        daten = data.ablaufende;
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(daten!=null)
        return daten.size();
        else return  0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if(i<0||daten==null||i>=daten.size())
        return null;
        else {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
            rv.setTextViewText(R.id.widgetItemTaskNameLabel,daten.get(i)[0]);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(CollectionAppWidgetProvider.EXTRA_LABEL, daten.get(i)[0]);
            rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);
            return rv;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
