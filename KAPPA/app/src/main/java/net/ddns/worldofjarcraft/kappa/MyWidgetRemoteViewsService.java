package net.ddns.worldofjarcraft.kappa;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Eric on 24.07.2017.
 */

public class MyWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AbgelaufenWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
