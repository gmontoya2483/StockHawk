package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Gabriel on 04/01/2017.
 */

public class DetailWidgetRemoteViewsService extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DetailedWidgetRemoteViewsFactory(getBaseContext(),intent);
    }



}
