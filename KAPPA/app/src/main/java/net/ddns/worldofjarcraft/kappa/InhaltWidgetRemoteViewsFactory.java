package net.ddns.worldofjarcraft.kappa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Lebensmittel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Eric on 24.07.2017.
 */

public class InhaltWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<String[]> daten = new ArrayList<>();
    public int schrank=-1, fach=-1;
    private String mail,pw;
    InhaltWidgetRemoteViewsFactory(final Context context, Intent intent) {
        mContext = context;
        Bundle extras = intent.getExtras();
        if(extras == null){
            return;
        }
        schrank = extras.getInt(InhaltWidget.KEY_SCHRANK);
        fach = extras.getInt(InhaltWidget.KEY_FACH);
        Log.e("Werte", schrank + "," + fach);

    }
    private android.os.Handler handler2;
    private Runnable runnable2;
    @Override
    public void onCreate() {
        handler2 = new android.os.Handler();
        runnable2 = new Runnable() {
            public void run() {
                Log.e("SCHRANKSERVICE", "LAUFE...");
                HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + schrank + "/" + fach + "/getAll");
                conn.delegate = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url) {
                        Lebensmittel[] results = new Gson().fromJson(output,Lebensmittel[].class);
                        //Toast.makeText(context,"Es laufen "+ablaufend.size()+" Lebensmittel ab.",Toast.LENGTH_LONG).show();
                        // prepare intent which is triggered if the
                        // notification is selected
                        if(results == null){
                            results = new Lebensmittel[0];
                        }
                        daten = new ArrayList<>();
                        for(Lebensmittel lm : results){
                            String[] element = new String[4];
                            element[0]=lm.getName();
                            element[1] = lm.getAnzahl();
                            try{
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(lm.getEingelagert());
                                long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = Calendar.DAY_OF_MONTH;
                                element[2]=day+"."+month+"."+year;

                            }
                            catch (Exception e){
                                element[2]="";
                                e.printStackTrace();
                            }
                            long mhd = lm.getHaltbarkeitsdatum();
                            if (mhd > 0) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(mhd);
                                long rest = mhd - Calendar.getInstance().getTimeInMillis();
                                if (rest > 0) {

                                    cal.setTimeInMillis(rest);
                                    String text = "";
                                    long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = Calendar.DAY_OF_MONTH;
                                    System.out.println(year + "," + month + "," + day);
                                    if (cal.get(Calendar.YEAR) - 1970 > 0)
                                        text += (cal.get(Calendar.YEAR) - 1970) + " " + mContext.getResources().getString(R.string.jahre) + ", ";
                                    if (cal.get(Calendar.MONTH) > 0)
                                        text += cal.get(Calendar.MONTH) + " " + mContext.getResources().getString(R.string.monate) + ", ";

                                    text += cal.get(Calendar.DAY_OF_MONTH) + " " + mContext.getResources().getString(R.string.tage) + ".";
                                    element[3]=(text);
                                } else {
                                    element[3]= mContext.getResources().getString(R.string.abgelaufen);
                                }
                            }
                            daten.add(element);
                        }
                        if (output !=  null) {
                            Log.e("RELOADING", "Sending Broadcast");
                            for (String[] lm : daten)
                                System.out.println(lm[0]);
                            InhaltWidget.sendRefreshBroadcast(mContext);
                        }
                    }
                };
                conn.execute("params");
                handler2.postDelayed(runnable2, 30000);

                if (daten == null) {
                    daten = new ArrayList<>();
                    daten.add(new String[]{"Nichts anzuzeigen"});
                }
                daten = getWerte();

            }
        };
        handler2.postDelayed(runnable2, 1000);
    }


    private ArrayList<String[]> getWerte(){
        return new ArrayList<>(daten);
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
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.inhalt_widget_list_item);
        if(i<0||daten==null||i>=daten.size())
            return rv;
        else {
            daten = getWerte();
            System.out.println("Alle lm-onCreate");
            /*for (String[^] data:MHDCheckerService.alle_lebensmittel){
                System.out.println(data);
            }*/
            rv.setTextViewText(R.id.inhaltwidgetItemTaskNameLabel, daten.get(i)[0] + " (" + mContext.getResources().getString(R.string.haltbarkeit) + ": " + daten.get(i)[2] + ")");
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(CollectionAppWidgetProvider.EXTRA_LABEL, daten.get(i)[0] + " (" + mContext.getResources().getString(R.string.haltbarkeit) + ": " + daten.get(i)[2] + ")");
            rv.setOnClickFillInIntent(R.id.inhaltwidgetItemContainer, fillInIntent);
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