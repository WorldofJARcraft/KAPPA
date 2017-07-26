package net.ddns.worldofjarcraft.kappa;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Pair;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Eric on 24.07.2017.
 */

public class InhaltWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    public List<String[]> daten;
    public String schrank = null;
    public String fach = null;
    public int RELOAD_MILLIS = 10000;
    public InhaltWidgetRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        schrank = intent.getStringExtra(InhaltWidget.KEY_SCHRANK);
        fach = intent.getStringExtra(InhaltWidget.KEY_FACH);
    }
    android.os.Handler handler;

    Pair<String,String> params;
    @Override
    public void onCreate() {
        daten = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        fillDaten();
        if(daten!=null)
        return daten.size();
        else return  0;
    }
    private void fillDaten(){
        daten = new ArrayList<>();
        if(data.alle_lebensmittel!=null){
            for(String[] lm:data.alle_lebensmittel){
                if(lm.length>4&&lm[4].equals(schrank)&&lm[3].equals(fach))
                    daten.add(lm);
            }
        }
    }
    @Override
    public RemoteViews getViewAt(int i) {
        fillDaten();
        if(i<0||daten==null||i>=daten.size())
        return null;
        else {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.inhalt_widget_list_item);
            rv.setTextViewText(R.id.inhaltwidgetItemTaskNameLabel,daten.get(i)[0]+"("+mContext.getResources().getString(R.string.haltbarkeit)+": "+daten.get(i)[2]+")");
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(InhaltWidget.EXTRA_LABEL, daten.get(i)[0]+"("+mContext.getResources().getString(R.string.haltbarkeit)+": "+daten.get(i)[2]+")");
            rv.setOnClickFillInIntent(R.id.inhaltwidgetItemContainer, fillInIntent);
            return rv;}
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
