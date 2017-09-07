package net.ddns.worldofjarcraft.kappa;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.content.Context.MODE_PRIVATE;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.login_name;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_password;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_preference;


/**
 * Created by Eric on 24.07.2017.
 */

public class InhaltWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    public List<String[]> daten;
    public String schrank=null, fach=null;
    private String mail,pw;
    public InhaltWidgetRemoteViewsFactory(final Context context, Intent intent) {
        mContext = context;
        Bundle extras = intent.getExtras();
        schrank = extras.getString(InhaltWidget.KEY_SCHRANK);
        fach = extras.getString(InhaltWidget.KEY_FACH);
        Log.e("Werte", schrank + "," + fach);
        SharedPreferences login = context.getSharedPreferences(login_name, MODE_PRIVATE);
        if ((login.contains(user_preference) && login.contains(user_password)) || (data.mail != null && data.pw != null)) {
            System.out.println("Melde mich mit gespeiucherten Daten an!");
            mail = login.getString(user_preference, null);
            pw = login.getString(user_password, null);
            //prüfen, ob Daten gültig sind
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/check_Passwort.php?mail=" + mail + "&pw=" + pw);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    if (output.equals("true")) {
                        System.out.println("Daten korrekt!");
                    } else {
                        Toast.makeText(context, R.string.kann_nicht_aktualisieren, Toast.LENGTH_LONG).show();
                    }
                }
            };
            conn.execute("params");
        }
        else
        mail=pw=null;
    }
    android.os.Handler handler2;
    Runnable runnable2;
    @Override
    public void onCreate() {
        handler2 = new android.os.Handler();
        runnable2 = new Runnable() {
            public void run() {
                Log.e("SCHRANKSERVICE","LAUFE...");
                HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/getSchrank.php?mail="+data.mail+"&pw="+data.pw);
                conn.delegate = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url) {
                        schrankListe = new HashMap<>();
                        fachListe = new HashMap<>();
                        if(!output.isEmpty()){

                            final String[] schranenke = output.split("\\|");

                            for(String schrank:schranenke){
                                String[] attribute = schrank.split(";");
                                Integer key = -1;
                                try {
                                    key = new Integer(attribute[0]);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                if(key>=0){
                                    schrankListe.put(key,attribute[1]);
                                    HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/get_Fach.php?mail="+data.mail+"&pw="+data.pw+"&schrank="+attribute[1]);
                                    conn.delegate = new AsyncResponse() {
                                        @Override
                                        public void processFinish(String output, String url) {
                                            if(!output.isEmpty()){
                                                String[] faecher = output.split("\\|");
                                                for(String fach:faecher){
                                                    final String[] attribute = fach.split(";");
                                                    Integer key = -1;
                                                    Integer schrank = -1;
                                                    try {
                                                        if(attribute.length>2){
                                                        key = new Integer(attribute[0]);
                                                        schrank = Integer.valueOf(attribute[2]);
                                                        }
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                    if(key>=0&&schrank>=0){
                                                        Pair<Integer,String> paar = new Pair<>(schrank,attribute[1]);
                                                        fachListe.put(key,paar);
                                                    }
                                                }
                                            }
                                        }
                                    };
                                    conn.execute("");
                                }
                            }
                            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/search.php?mail="+data.mail+"&pw="+data.pw);
                            conn.delegate = new AsyncResponse() {
                                @Override
                                public void processFinish(String output, String url) {
                                    boolean fehler = false;
                                    if(!output.isEmpty()){
                                        daten = new ArrayList<>();
                                        String[] lebensmittel = output.split("\\|");
                                        for(String lm:lebensmittel){
                                            String[] werte = lm.split(";");
                                            if(werte.length>4){
                                            try {
                                                Long mhd = new Long(werte[3]);
                                                if(mhd>0){
                                                    long rest = mhd - Calendar.getInstance().getTimeInMillis();
                                                    if(rest>0){
                                                        Calendar cal = Calendar.getInstance();
                                                        cal.setTimeInMillis(rest);
                                                        String text="";
                                                        long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day= Calendar.DAY_OF_MONTH;
                                                        System.out.println(year+","+month+","+day);

                                                        String[] angaben = new String[5];
                                                        angaben[0] = werte[1];
                                                        angaben[1] = werte[2];
                                                        angaben[2] = cal.get(Calendar.YEAR)-1970+" "+mContext.getString(R.string.jahre)+", "+cal.get(Calendar.MONTH)+" "+mContext.getString(R.string.monate)+", "+cal.get(Calendar.DAY_OF_MONTH)+" " + mContext.getResources().getString(R.string.tage)+".";
                                                        angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                        angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                        daten.add(angaben);
                                                    }
                                                    else{
                                                        String[] angaben = new String[5];
                                                        angaben[0] = werte[1];
                                                        angaben[1] = werte[2];
                                                        angaben[2] = mContext.getResources().getString(R.string.abgelaufen);
                                                        angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                        angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                        daten.add(angaben);
                                                    }

                                                }
                                                else {
                                                    String[] angaben = new String[5];
                                                    angaben[0] = werte[1];
                                                    angaben[1] = werte[2];
                                                    angaben[2] = mContext.getResources().getString(R.string.keine_Angabe);
                                                    angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                    angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                    daten.add(angaben);
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                fehler=true;
                                            }
                                        }
                                        else fehler=true;
                                        }
                                        //Toast.makeText(context,"Es laufen "+ablaufend.size()+" Lebensmittel ab.",Toast.LENGTH_LONG).show();
                                        // prepare intent which is triggered if the
// notification is selected
                                        if(!fehler) {
                                            Log.e("RELOADING","Sending Broadcast");
                                            for(String[] lm: daten)
                                                System.out.println(lm[0]);
                                            InhaltWidget.sendRefreshBroadcast(mContext);
                                        }
                                    }
                                }
                            };
                            conn.execute("params");
                        }
                    }
                };
                conn.execute("params");
                handler2.postDelayed(runnable2, 30000);
            }
        };

        handler2.postDelayed(runnable2, 1000);

        if(daten==null) {
            daten = new ArrayList<>();
            daten.add(new String[]{"Nichts anzuzeigen"});
        }
        daten = getWerte();
    }

    HashMap<Integer,String> schrankListe;
    HashMap<Integer,Pair<Integer,String>> fachListe;
    private ArrayList<String[]> getWerte(){
        ArrayList<String[]> werte = new ArrayList();
        for(String[] attr: daten){
            if(attr.length>4){
                if(attr[4].equals(schrank)&&attr[3].equals(fach))
                    werte.add(attr);
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
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.inhalt_widget_list_item);
        if(i<0||daten==null||i>=daten.size())
            return rv;
        else {
            if(daten!=null ){
                if(daten.size()>i) {
                    daten = getWerte();
                    System.out.println("Alle lm-onCreate");
                /*for (String[] data:MHDCheckerService.alle_lebensmittel){
                    System.out.println(data);
                }*/
                    rv.setTextViewText(R.id.inhaltwidgetItemTaskNameLabel, daten.get(i)[0] + " (" + mContext.getResources().getString(R.string.haltbarkeit) + ": " + daten.get(i)[2] + ")");
                    Intent fillInIntent = new Intent();
                    fillInIntent.putExtra(CollectionAppWidgetProvider.EXTRA_LABEL, daten.get(i)[0] + " (" + mContext.getResources().getString(R.string.haltbarkeit) + ": " + daten.get(i)[2] + ")");
                    rv.setOnClickFillInIntent(R.id.inhaltwidgetItemContainer, fillInIntent);
                }
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