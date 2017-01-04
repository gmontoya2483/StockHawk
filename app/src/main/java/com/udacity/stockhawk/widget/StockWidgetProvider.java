package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Created by Gabriel on 03/01/2017.
 */

public class StockWidgetProvider extends AppWidgetProvider {




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        context.startService(new Intent(context, StockWidgetIntentService.class));


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        //Check if the data was updated
        if (QuoteSyncJob.ACTION_DATA_UPDATED.equals(intent.getAction())){
            context.startService(new Intent(context,StockWidgetIntentService.class));
        }

        //Check if a widget was deleted
        if (intent.getAction().equals(ACTION_APPWIDGET_UPDATE)){
            int widgetID=getTheAppWidgetID(intent);
            int result=StockWidget.DeleteWidget(context, (long) widgetID);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        context.startService(new Intent(context, StockWidgetIntentService.class));


    }


    private int getTheAppWidgetID(Intent intent){
        int widgetID=INVALID_APPWIDGET_ID;

        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        }

        return widgetID;

    }
}
