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
        Log.e("Übergeben",intent.getIntExtra(InhaltWidget.KEY_SCHRANK,-1)+";"+intent.getIntExtra(InhaltWidget.KEY_FACH,-1));
        return new InhaltWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
