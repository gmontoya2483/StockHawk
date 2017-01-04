package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.Stock;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;

/**
 * Created by Gabriel on 03/01/2017.
 */

public class StockWidgetIntentService extends IntentService{
    public StockWidgetIntentService() {
        super("StockWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockWidgetProvider.class));

        for (int appWidgetId: appWidgetIds){

            //String symbol=intent.getStringExtra(Contract.Quote.COLUMN_SYMBOL);

            StockWidget stockWidget=new StockWidget(getApplicationContext(), (long) appWidgetId);

            RemoteViews views=new RemoteViews(getPackageName(), R.layout.widget_stock);
            Stock stock=new Stock(this,stockWidget.getSymbol());



            if (stock.getSymbol()!=Stock.NO_SYMBOL){

                views.setViewVisibility(R.id.widget, View.VISIBLE);
                views.setViewVisibility(R.id.widget_no_info,View.GONE);
                views.setTextViewText(R.id.widgetSymbol,stock.getSymbol());
                views.setTextViewText(R.id.widgetPrice,String.format(this.getString(R.string.formatted_Price),stock.getPrice()));

                // Create an Intent to launch MainActivity
                Intent launchIntent = new Intent(this, MainActivity.class);
                //launchIntent.putExtra(Contract.Quote.COLUMN_SYMBOL,stock.getSymbol());
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            }else{
                views.setViewVisibility(R.id.widget, View.GONE);
                views.setViewVisibility(R.id.widget_no_info,View.VISIBLE);

            }


            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }





    }


}
