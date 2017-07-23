package net.ddns.worldofjarcraft.kappa;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.Pair;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 23.07.2017.
 */

public class MHDCheckerService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private final int mNotificationId =0;
    private final int repeatMillis = 1000*60*60;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    HashMap<Integer,String> schrankListe;
    HashMap<Integer,Pair<Integer,String>> fachListe;
    ArrayList<String[]> ablaufend;
    @Override
    public void onCreate() {
        Toast.makeText(this, R.string.ueberwachung_gestartet, Toast.LENGTH_LONG).show();
        HTTP_Connection clean = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/clean.php");
        clean.execute("");
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
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
                                    if(!output.isEmpty()){
                                        ablaufend = new ArrayList<>();
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

                                                        if(cal.get(Calendar.YEAR)-1970==0&&!text.contains(getResources().getString(R.string.monate))&&cal.get(Calendar.DAY_OF_MONTH)<8){
                                                            String[] angaben = new String[5];
                                                            angaben[0] = werte[1];
                                                            angaben[1] = werte[2];
                                                            angaben[2] = cal.get(Calendar.DAY_OF_MONTH)+" " + getResources().getString(R.string.tage)+".";
                                                            angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                            angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                            ablaufend.add(angaben);
                                                        }
                                                    }
                                                    else {
                                                        String[] angaben = new String[5];
                                                        angaben[0] = werte[1];
                                                        angaben[1] = werte[2];
                                                        angaben[2] = getResources().getString(R.string.abgelaufen);
                                                        angaben[3] = fachListe.get(Integer.valueOf(werte[4])).second;
                                                        angaben[4] = schrankListe.get(fachListe.get(Integer.valueOf(werte[4])).first);
                                                        ablaufend.add(angaben);
                                                    }
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        //Toast.makeText(context,"Es laufen "+ablaufend.size()+" Lebensmittel ab.",Toast.LENGTH_LONG).show();
                                        // prepare intent which is triggered if the
// notification is selected

                                        Intent intent = new Intent(MHDCheckerService.this, AblaufendActivity.class);
                                        Bundle b = new Bundle();
                                        DataHelper helper = new DataHelper(ablaufend);
                                        b.putSerializable("lebensmittel",helper);
                                        intent.putExtras(b);
// use System.currentTimeMillis() to have a unique ID for the pending intent
                                        PendingIntent pIntent = PendingIntent.getActivity(MHDCheckerService.this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
                                        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(MHDCheckerService.this);
                                        mBuilder.setContentTitle(getResources().getString(R.string.titel_notif))
                                                .setContentText(ablaufend.size()+" "+getResources().getString(R.string.inhalt_notif))
                                                .setSmallIcon(R.drawable.food)
                                                .setAutoCancel(true)
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(mBuilder.mContentText));
// Creates an explicit intent for an Activity in your app
                                        Intent resultIntent = new Intent(MHDCheckerService.this, AblaufendActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MHDCheckerService.this);
// Adds the back stack for the Intent (but not the Intent itself)
                                        stackBuilder.addParentStack(AblaufendActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                                        stackBuilder.addNextIntent(intent);
                                        PendingIntent resultPendingIntent =
                                                stackBuilder.getPendingIntent(
                                                        0,
                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                );
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        NotificationManager mNotificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
                                        mNotificationManager.cancel(mNotificationId);
                                        mNotificationManager.notify(mNotificationId, mBuilder.build());
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
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}