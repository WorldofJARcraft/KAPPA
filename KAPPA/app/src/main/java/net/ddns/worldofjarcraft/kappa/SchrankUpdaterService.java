package net.ddns.worldofjarcraft.kappa;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.Pair;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.login_name;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_password;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_preference;
/**
 * Created by Eric on 26.07.2017.
 */

public class SchrankUpdaterService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private final int mNotificationId =0;
    private final int repeatMillis = 1000*60;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    HashMap<Integer,String> schrankListe;
    HashMap<Integer,Pair<Integer,String>> fachListe;
    public static ArrayList<String[]> alle_lebensmittel;

    public void init(){
        handler = new Handler();
        runnable = new Runnable() {
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
                                                        key = new Integer(attribute[0]);
                                                        schrank = Integer.valueOf(attribute[2]);
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
                                        alle_lebensmittel = new ArrayList<>();
                                        String[] lebensmittel = output.split("\\|");
                                        for(String lm:lebensmittel){
                                            String[] werte = lm.split(";");
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
                                                            angaben[2] = cal.get(Calendar.DAY_OF_MONTH)+" " + getResources().getString(R.string.tage)+".";
                                                            angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                            angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                            alle_lebensmittel.add(angaben);
                                                    }
                                                    else{
                                                                String[] angaben = new String[5];
                                                                angaben[0] = werte[1];
                                                                angaben[1] = werte[2];
                                                                angaben[2] = getResources().getString(R.string.abgelaufen);
                                                                angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                                angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                                alle_lebensmittel.add(angaben);
                                                            }

                                                }
                                                else {
                                                            String[] angaben = new String[5];
                                                            angaben[0] = werte[1];
                                                            angaben[1] = werte[2];
                                                            angaben[2] = getResources().getString(R.string.keine_Angabe);
                                                            angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                            angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                            alle_lebensmittel.add(angaben);
                                                        }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                fehler=true;
                                            }
                                        }
                                        //Toast.makeText(context,"Es laufen "+ablaufend.size()+" Lebensmittel ab.",Toast.LENGTH_LONG).show();
                                        // prepare intent which is triggered if the
// notification is selected
                                        if(!fehler) {
                                            Log.e("RELOADING","Sending Broadcast");
                                            MHDCheckerService.alle_lebensmittel = alle_lebensmittel;
                                            for(String[] lm: alle_lebensmittel)
                                                System.out.println(lm[0]);
                                            InhaltWidget.sendRefreshBroadcast(context);
                                        }
                                    }
                                }
                            };
                            conn.execute("params");
                        }
                    }
                };
                conn.execute("params");
                handler.postDelayed(runnable, repeatMillis);
            }
        };

        handler.postDelayed(runnable, 5000);
    }
    @Override
    public void onCreate() {
        HTTP_Connection clean = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/clean.php");
        clean.execute("");
        SharedPreferences login = context.getSharedPreferences(login_name,MODE_PRIVATE);
        if((login.contains(user_preference)&&login.contains(user_password))||(data.mail!=null&&data.pw!=null)){
            System.out.println("Melde mich mit gespeiucherten Daten an!");
            data.mail=login.getString(user_preference,null);
            data.pw=login.getString(user_password,null);
            //prüfen, ob Daten gültig sind
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/check_Passwort.php?mail="+data.mail+"&pw="+data.pw);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    if(output.equals("true")){
                        System.out.println("Daten korrekt!");
                        init();
                    }
                    else{
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.unauth_titel);
                        builder.setMessage(R.string.unauth);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.startActivity(new Intent(context,LoginActivity.class));
                            }
                        });
                        builder.show();*/
                    }
                }
            };
            conn.execute("params");
        }

        else{
            System.out.println("Frage Nutzerdaten an!");
            context.startActivity(new Intent(context,LoginActivity.class));
        }


    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
    }
}
