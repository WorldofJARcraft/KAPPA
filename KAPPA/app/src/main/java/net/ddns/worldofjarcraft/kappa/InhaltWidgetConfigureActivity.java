package net.ddns.worldofjarcraft.kappa;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ScrollingTabContainerView;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Fach;
import net.ddns.worldofjarcraft.kappa.Model.Kuehlschrank;

import java.util.ArrayList;
import java.util.List;

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
            saveTitlePref(context, mAppWidgetId, kuehlschranks[s1.getSelectedItemPosition()],results[s2.getSelectedItemPosition()]);
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
    static void saveTitlePref(Context context, int appWidgetId, Kuehlschrank schrank, Fach fach) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, schrank.getLaufNummer());
        prefs.putInt(PREF_FACH_KEY+appWidgetId, fach.getlNummer());
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Pair<Integer,Integer> loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int titleValue = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
        int fach = prefs.getInt(PREF_FACH_KEY+appWidgetId,-1);
        if (titleValue != -1 && fach!=-1) {
            return new Pair<>(titleValue,fach);
        } else {
            return new Pair<>(-1,-1);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
    List<String> liste;
    List<String> faecher;
    Kuehlschrank[] kuehlschranks;
    Fach[] results;
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
        HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress+"/schrank/getAll",2);
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                kuehlschranks = new Gson().fromJson(output, Kuehlschrank[].class) == null? new Kuehlschrank[0]: new Gson().fromJson(output, Kuehlschrank[].class);
                liste = new ArrayList<>();
                for(Kuehlschrank schrank : kuehlschranks){
                    liste.add(schrank.getName());
                }
                Spinner schrank = findViewById(R.id.schrankSpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(InhaltWidgetConfigureActivity.this,R.layout.simple_spinner_item,liste);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                schrank.setAdapter(adapter);
                schrank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String url = Constants.Server_Adress+ "/schrank/"+ kuehlschranks[adapterView.getSelectedItemPosition()].getLaufNummer()+"/getAll";
                        HTTP_Connection conn = new HTTP_Connection(url,1);
                        conn.delegate = new AsyncResponse() {
                            @Override
                            public void processFinish(String output, String url) {
                                faecher = new ArrayList<>();
                                results = new Gson().fromJson(output, Fach[].class);
                                for(Fach fach:results){
                                    faecher.add(fach.getName());
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

