package net.ddns.worldofjarcraft.kappa;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.worldofjarcraft.kappa.R.drawable.ablaufende;

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
        daten = MHDCheckerService.ablaufend;
        if(daten==null) {
            daten = new ArrayList<>();
            daten.add(new String[]{"Nichts anzuzeigen"});
        }
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        daten = MHDCheckerService.ablaufend;
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
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        if(i<0||i>=daten.size())
        return rv;
        else {
            if(daten!=null&&daten.size()>i){
            rv.setTextViewText(R.id.widgetItemTaskNameLabel,daten.get(i)[0]+", "+mContext.getResources().getString(R.string.haltbarkeit)+daten.get(i)[2]+"("+mContext.getResources().getString(R.string.haltbarkeit)+": "+daten.get(i)[3]+")");
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(CollectionAppWidgetProvider.EXTRA_LABEL, daten.get(i)[0]+" ("+mContext.getResources().getString(R.string.haltbarkeit)+": "+daten.get(i)[2]+")");
            rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);}
        else {
            rv.setTextViewText(R.id.inhaltwidgetItemTaskNameLabel, mContext.getResources().getString(R.string.service_aus));
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(InhaltWidget.EXTRA_LABEL, "");
            rv.setOnClickFillInIntent(R.id.inhaltwidgetItemContainer, fillInIntent);
        }
        }
        return rv;
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
