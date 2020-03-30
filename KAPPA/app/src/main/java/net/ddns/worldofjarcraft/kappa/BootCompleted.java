package net.ddns.worldofjarcraft.kappa;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.autostart_name;
import static net.ddns.worldofjarcraft.kappa.LaunchActivity.login_name;

// here is the OnRevieve methode which will be called when boot completed
public class BootCompleted extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent) {
        //we double check here for only boot complete event
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            /*context.setTheme(R.style.AppTheme);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.ueberwachung_gestartet);
            builder.setTitle(R.string.ueberwachung_starten);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent serviceIntent = new Intent(context, MHDCheckerService.class);
                    context.startService(serviceIntent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }
            );
            builder.show().show();
            */
            SharedPreferences pref = context.getSharedPreferences(login_name,MODE_PRIVATE);
            if(pref.contains(autostart_name)){
                if(pref.getBoolean(autostart_name,false)){
                    Intent serviceIntent = new Intent(context, MHDCheckerService.class);
                    context.startService(serviceIntent);
                }
            }

        }
    }
}
