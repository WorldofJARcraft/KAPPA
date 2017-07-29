package net.ddns.worldofjarcraft.kappa;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.ddns.worldofjarcraft.kappa.LaunchActivity.login_name;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_password;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.user_preference;

/**
 * The configuration screen for the {@link InhaltWidget InhaltWidget} AppWidget.
 */
public class InhaltWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "net.ddns.worldofjarcraft.kappa.InhaltWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_FACH_KEY="fach";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = InhaltWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            //String widgetText = mAppWidgetText.getText().toString();
            //saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            Spinner s1 = findViewById(R.id.schrankSpinner);
            Spinner s2 = findViewById(R.id.FachSpinner);
            saveTitlePref(context, mAppWidgetId, s1.getSelectedItem().toString(),s2.getSelectedItem().toString());
            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.inhalt_widget);
// This is equivalent to your ChecksWidgetProvider.updateAppWidget()
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
            InhaltWidget.sendRefreshBroadcast(context);
// Destroy activity
            finish();
        }
    };

    public InhaltWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String schrank, String fach) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, schrank);
        prefs.putString(PREF_FACH_KEY+appWidgetId, fach);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Pair<String,String> loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        String fach = prefs.getString(PREF_FACH_KEY+appWidgetId,null);
        if (titleValue != null && fach!=null) {
            return new Pair<>(titleValue,fach);
        } else {
            return new Pair<>("","");
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
    List<String> liste;
    List<String> faecher;
    String mail,pw;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.inhalt_widget_configure);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        SharedPreferences login = getSharedPreferences(login_name, MODE_PRIVATE);
        if ((login.contains(user_preference) && login.contains(user_password))) {
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
                        Toast.makeText(getApplicationContext(), R.string.kann_nicht_aktualisieren, Toast.LENGTH_LONG).show();
                    }
                }
            };
            conn.execute("params");
        }
        else
            mail=pw=null;
        HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/getSchrank.php?mail="+mail+"&pw="+pw,2);
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                liste = new ArrayList<>();
                if(!output.isEmpty()){
                    String[] boxen = output.split("\\|");
                    for(String box:boxen){
                        String[] attribute = box.split(";");
                        if(attribute.length>1)
                        liste.add(attribute[1]);
                        else {
                            Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_LONG).show();
                        }
                    }
                }
            Spinner schrank = findViewById(R.id.schrankSpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(InhaltWidgetConfigureActivity.this,R.layout.simple_spinner_item,liste);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                schrank.setAdapter(adapter);
                schrank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String url = "https://worldofjarcraft.ddns.net/kappa/get_Fach.php?mail="+mail+"&pw="+pw+"&schrank="+liste.get(adapterView.getSelectedItemPosition());
                        HTTP_Connection conn = new HTTP_Connection(url,1);
                        conn.delegate = new AsyncResponse() {
                            @Override
                            public void processFinish(String output, String url) {
                                String[] neuefaecher = output.split("\\|");
                                faecher = new ArrayList<>();
                                for(String fach:neuefaecher){
                                    String[] werte = fach.split(";");
                                    if(werte.length>=2){
                                        faecher.add(werte[1]);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_LONG).show();
                                    }
                                }
                            Spinner fachSpinner = findViewById(R.id.FachSpinner);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(InhaltWidgetConfigureActivity.this,R.layout.simple_spinner_item,faecher);
                                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                fachSpinner.setAdapter(adapter);
                            }
                        };
                        conn.execute("params");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                schrank.setSelection(0);
            }
        };
        conn.execute("params");
        //mAppWidgetText.setText(loadTitlePref(InhaltWidgetConfigureActivity.this, mAppWidgetId));


    }
}

