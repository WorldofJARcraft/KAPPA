package net.ddns.worldofjarcraft.kappa;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Eric on 24.07.2017.
 */

public class InhaltWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new InhaltWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
