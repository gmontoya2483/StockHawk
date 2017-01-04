package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class StockWidgetConfigure extends Activity {

    private EditText mConfSymbol;
    private Button mConfAddStock;
    private int mAppWidgetId;
    private String mSymbol;

    private static final String LOG_TAG = StockWidgetConfigure.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_widget_configure);

        setResult(RESULT_CANCELED);

        //1 - get the App Widget ID from the Intent that launched the activity
        mAppWidgetId=getTheAppWidgetID();

        initViews();

    }


    private void initViews(){


        mConfSymbol=(EditText) findViewById(R.id.ConfSymbol);

        mConfAddStock=(Button) findViewById(R.id.ConfButton);
        mConfAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 2- Perform the Widget configuration
                handleConfAddStockButton();

            }
        });

    }


    private void handleConfAddStockButton(){

        mSymbol=mConfSymbol.getText().toString();
        StockWidget stockWidget=new StockWidget((long) mAppWidgetId,mSymbol);
        stockWidget.insertWidget(this);

        showStockWidget();

    }




    private int getTheAppWidgetID(){
        int widgetID=INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        }

        return widgetID;

    }



    private void showStockWidget() {

        if (mAppWidgetId!=INVALID_APPWIDGET_ID){

            // 3 When the configuration is complete, get an instance of the AppWidgetManager by calling getInstance(Context):
            //AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetInfo(mAppWidgetId);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());


            //4 Update the App Widget with a RemoteViews layout by calling updateAppWidget(int, RemoteViews):
            RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(),R.layout.widget_stock);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);


            Intent resultIntent = new Intent(StockWidgetConfigure.this, StockWidgetIntentService.class);
            resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultIntent);
            startService(resultIntent);

            finish();


            /*Intent startService = new Intent(StockWidgetConfigure.this, StockWidgetIntentService.class);
            startService.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId);
            //startService.putExtra(Contract.Quote.COLUMN_SYMBOL,mSymbol);
            startService.setAction("FROM CONFIGURATION ACTIVITY");
            setResult(RESULT_OK, startService);
            startService(startService);

            finish();*/
        }else{
            Log.i(LOG_TAG, getApplicationContext().getString(R.string.conf_invalid_app_widget_id));
            finish();
        }

    }



}
