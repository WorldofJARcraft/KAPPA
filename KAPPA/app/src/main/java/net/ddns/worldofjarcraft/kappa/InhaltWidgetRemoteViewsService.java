package net.ddns.worldofjarcraft.kappa;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by Eric on 24.07.2017.
 */

public class InhaltWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e("Übergeben",intent.getStringExtra(InhaltWidget.KEY_SCHRANK)+";"+intent.getStringExtra(InhaltWidget.KEY_FACH));
        return new InhaltWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
