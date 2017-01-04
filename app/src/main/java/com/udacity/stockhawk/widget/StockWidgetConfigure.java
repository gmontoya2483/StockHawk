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






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_widget_configure);


        setResult(RESULT_CANCELED);

        mAppWidgetId=getTheAppWidgetID();

        initViews();


    }


    private void initViews(){


        mConfSymbol=(EditText) findViewById(R.id.ConfSymbol);

        mConfAddStock=(Button) findViewById(R.id.ConfButton);
        mConfAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetInfo(mAppWidgetId);



            Intent startService = new Intent(StockWidgetConfigure.this, StockWidgetIntentService.class);
            startService.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId);
            //startService.putExtra(Contract.Quote.COLUMN_SYMBOL,mSymbol);
            startService.setAction("FROM CONFIGURATION ACTIVITY");
            setResult(RESULT_OK, startService);
            startService(startService);

            finish();
        }else{


            Log.i("I am invalid", "I am invalid");
            finish();
        }

    }



}
