package net.ddns.worldofjarcraft.kappa;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Lebensmittel;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static net.ddns.worldofjarcraft.kappa.LaunchActivity.login_name;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_password;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_preference;

/**
 * Created by Eric on 23.07.2017.
 */

public class MHDCheckerService extends Service {
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
    public static final ArrayList<String[]> ablaufend = new ArrayList<>();
    public static ArrayList<String[] > alle_lebensmittel;
    public Handler handler2=null;
    public Runnable runnable2=null;
    private String mail,pw;
    public void init() {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                ablaufend.clear();
                SharedPreferences login = context.getSharedPreferences(login_name, MODE_PRIVATE);
                if ((login.contains(user_preference) && login.contains(user_password)) || (data.mail != null && data.pw != null)) {
                    System.out.println("Melde mich mit gespeiucherten Daten an!");
                    mail = login.getString(user_preference, null);
                    pw = login.getString(user_password, null);
                    //prüfen, ob Daten gültig sind
                } else
                    mail = pw = null;

                HTTP_Connection conn = null;
                conn = new HTTP_Connection(Constants.Server_Adress + "/lebensmittel/getAll", 2);
                conn.delegate = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url) {
                        boolean fehler = false;
                        for (Lebensmittel lm : new Gson().fromJson(output, Lebensmittel[].class)) {
                            try {
                                long mhd = lm.getHaltbarkeitsdatum();
                                if (mhd > 0) {
                                    long rest = mhd - Calendar.getInstance().getTimeInMillis();
                                    if (rest > 0) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTimeInMillis(rest);
                                        String text = "";
                                        long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = Calendar.DAY_OF_MONTH;
                                        System.out.println(year + "," + month + "," + day);

                                        if (cal.get(Calendar.YEAR) - 1970 == 0 && cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DAY_OF_MONTH) < 8) {
                                            String[] angaben = new String[5];
                                            angaben[0] = lm.getName();
                                            angaben[1] = lm.getAnzahl();
                                            angaben[2] = cal.get(Calendar.DAY_OF_MONTH) + " " + getResources().getString(R.string.tage) + ".";
                                            angaben[3] = lm.getFach().getName();
                                            angaben[4] = lm.getFach().getKuehlschrank().getName();
                                            ablaufend.add(angaben);

                                        }
                                    }
                                    } else {
                                        String[] angaben = new String[5];
                                        angaben[0] = lm.getName();
                                        angaben[1] = lm.getAnzahl();
                                        angaben[2] = getResources().getString(R.string.abgelaufen);
                                        angaben[3] = lm.getFach().getName();
                                        angaben[4] = lm.getFach().getKuehlschrank().getName();
                                        ablaufend.add(angaben);
                                    }
                            } catch (Exception e) {
                                e.printStackTrace();
                                fehler = true;
                            }
                        }
                        //Toast.makeText(context,"Es laufen "+ablaufend.size()+" Lebensmittel ab.",Toast.LENGTH_LONG).show();
                        // prepare intent which is triggered if the
                        // notification is selected
                        if (!fehler) {
                            Intent intent = new Intent(MHDCheckerService.this, AblaufendActivity.class);
                            Bundle b = new Bundle();
                            DataHelper helper = new DataHelper(ablaufend);
                            CollectionAppWidgetProvider.sendRefreshBroadcast(MHDCheckerService.this);
                            //InhaltWidget.sendRefreshBroadcast(MHDCheckerService.this);
                            b.putSerializable("lebensmittel", helper);
                            intent.putExtras(b);
                            // use System.currentTimeMillis() to have a unique ID for the pending intent
                            PendingIntent pIntent = PendingIntent.getActivity(MHDCheckerService.this, (int) System.currentTimeMillis(), intent, 0);

                            // build notification
                            // the addAction re-use the same intent to keep the example short
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MHDCheckerService.this);
                            mBuilder.setContentTitle(getResources().getString(R.string.titel_notif))
                                    .setContentText(ablaufend.size() + " " + getResources().getString(R.string.inhalt_notif))
                                    .setSmallIcon(R.mipmap.schnee)
                                    .setAutoCancel(false)
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(ablaufend.size() + " " + getResources().getString(R.string.inhalt_notif)));
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
            mail=login.getString(user_preference,null);
            pw=login.getString(user_password,null);
            //prüfen, ob Daten gültig sind
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/check_Passwort.php?mail="+mail+"&pw="+pw);
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
        //Toast.makeText(this, R.string.ueberwachung_laeuft, Toast.LENGTH_LONG).show();
    }
}