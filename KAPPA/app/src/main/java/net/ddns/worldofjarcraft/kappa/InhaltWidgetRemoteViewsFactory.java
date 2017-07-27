package net.ddns.worldofjarcraft.kappa;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.AdapterView;
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
    private String schrank=null, fach=null;
    public InhaltWidgetRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        Bundle extras = intent.getExtras();
        schrank = extras.getString(InhaltWidget.KEY_SCHRANK);
        fach=extras.getString(InhaltWidget.KEY_FACH);
        Log.e("Werte",schrank+","+fach);
    }
    @Override
    public void onCreate() {


        if(daten==null) {
            daten = new ArrayList<>();
            daten.add(new String[]{"Nichts anzuzeigen"});
        }
        daten = getWerte();
    }
    private ArrayList<String[]> getWerte(){
        ArrayList<String[]> werte = new ArrayList();
        if(SchrankUpdaterService.alle_lebensmittel!=null){
        for(String[] attr: SchrankUpdaterService.alle_lebensmittel){
            if(attr.length>4){
                if(attr[4].equals(schrank)&&attr[3].equals(fach))
                    werte.add(attr);
            }
        }
        }
        return werte;
    }
    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        daten = getWerte();
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
        if(i<0||i>=daten.size())
            return null;
        else {
            if(daten!=null){
                System.out.println("Alle lm-onCreate");
                System.out.println("ISt null: "+MHDCheckerService.alle_lebensmittel==null);
                /*for (String[] data:MHDCheckerService.alle_lebensmittel){
                    System.out.println(data);
                }*/
                RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.inhalt_widget_list_item);
                rv.setTextViewText(R.id.inhaltwidgetItemTaskNameLabel,daten.get(i)[0]+"("+mContext.getResources().getString(R.string.haltbarkeit)+": "+daten.get(i)[2]+")");
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(CollectionAppWidgetProvider.EXTRA_LABEL, daten.get(i)[0]+" ("+mContext.getResources().getString(R.string.haltbarkeit)+": "+daten.get(i)[2]+")");
                rv.setOnClickFillInIntent(R.id.inhaltwidgetItemContainer, fillInIntent);
                return rv;}
            else {
                Log.e("NICHTS DA","ALLES LEER");
                RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.inhalt_widget_list_item);
                rv.setTextViewText(R.id.inhaltwidgetItemTaskNameLabel, mContext.getResources().getString(R.string.service_aus));
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(InhaltWidget.EXTRA_LABEL, "");
                rv.setOnClickFillInIntent(R.id.inhaltwidgetItemContainer, fillInIntent);
                return rv;
            }
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