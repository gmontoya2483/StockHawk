package com.udacity.stockhawk.widget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.udacity.stockhawk.data.Contract;

/**
 * Created by Gabriel on 03/01/2017.
 */

public class StockWidget {

    private Long mWidgetID;
    private String mSymbol;

    public StockWidget (Long widgetID, String Symbol){
        mWidgetID=widgetID;
        mSymbol=Symbol;
    }


    public StockWidget(){
        mWidgetID=null;
        mSymbol=null;

    }


    public StockWidget(Context context, Long widgetID){
        this.mWidgetID=widgetID;
        getWidgetFromDB(context);

    }




    public Long getWidgetID() {
        return mWidgetID;
    }

    public void setWidgetID(Long mWidgetID) {
        this.mWidgetID = mWidgetID;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String mSymbol) {
        this.mSymbol = mSymbol;
    }


    public ContentValues getStockWidgetValues(){
        ContentValues values=new ContentValues();
        values.put(Contract.Widget._ID,this.getWidgetID());
        values.put(Contract.Widget.COLUMN_WIDGET_SYMBOL,this.getSymbol());

        return values;

    }


    public Uri insertWidget(Context context){

        Uri insertedWidgetUri;
        Uri allWidgetUri=Contract.Widget.buildAllWidgetsUri();

        if (verifyAttributes()){
            insertedWidgetUri=context.getContentResolver().insert(allWidgetUri,this.getStockWidgetValues());
        }else{
           insertedWidgetUri=null;
        }

        return insertedWidgetUri;

    }


    private boolean verifyAttributes(){

        boolean isOK=true;

        if (this.getWidgetID()== null){
            isOK=false;
        }

        if (this.getSymbol()== null){
            isOK=false;
        }


        return isOK;
    }




    private void getWidgetFromDB (Context context){
        Cursor cursor;
        Uri uriWidgetByID;

        uriWidgetByID=Contract.Widget.buildWidgetsByWidgetIdUri(this.mWidgetID);
        cursor=context.getContentResolver().query(uriWidgetByID,null,null,null,null);


        if (cursor==null ||cursor.getCount()!=1){
            this.mWidgetID=null;
            this.mSymbol=null;


        }else{
            cursor.moveToFirst();
            this.mWidgetID=cursor.getLong(cursor.getColumnIndex(Contract.Widget._ID));
            this.mSymbol=cursor.getString(cursor.getColumnIndex(Contract.Widget.COLUMN_WIDGET_SYMBOL));
        }

        if (cursor!=null){
            cursor.close();
        }


    }


    public static int DeleteWidget(Context context,Long WidgetId){
        Uri uriWidgetByID;
        uriWidgetByID=Contract.Widget.buildWidgetsByWidgetIdUri(WidgetId);
        return context.getContentResolver().delete(uriWidgetByID,null,null);

    }









}
