package com.udacity.stockhawk;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.udacity.stockhawk.data.Contract;

/**
 * Created by Gabriel on 28/12/2016.
 */

public class Stock {


    public static final String NO_SYMBOL="NO_SYMBOL";

    private String mSymbol;
    private double mPrice;
    private double mPorcentage;
    private double mAbasolut;

    public void setSymbol(String mSymbol) {
        this.mSymbol = mSymbol;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public void setPercentage(double mPercentage) {
        this.mPorcentage = mPercentage;
    }

    public void setAbsolute(double mAbasolute) {
        this.mAbasolut = mAbasolute;
    }



    public String getSymbol() {
        return mSymbol;
    }


    public double getPrice() {
        return mPrice;
    }

    public double getPorcentage() {
        return mPorcentage;
    }


    public double getAbasolut() {
        return mAbasolut;
    }


    public Stock (String symbol,double price,double porcentageChange,double absoluteChange){
        this.mSymbol=symbol;
        this.mPrice=price;
        this.mPorcentage=porcentageChange;
        this.mAbasolut=absoluteChange;


    }


    public Stock (Context context, String symbol){

        Cursor cursor;
        Uri uriQuoteSymbol= Contract.Quote.makeUriForStock(symbol);
        System.out.println(uriQuoteSymbol.toString());


        cursor=context.getContentResolver().query(uriQuoteSymbol,Contract.Quote.QUOTE_COLUMNS,null,null,null);

        if (cursor==null ||cursor.getCount()!=1){
            this.mSymbol=Stock.NO_SYMBOL;
            this.mPrice=0.00;
            this.mPorcentage=0.00;
            this.mAbasolut=0.00;
        }else{
            cursor.moveToFirst();
            this.mSymbol=symbol;
            this.mPrice=cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE));
            this.mPorcentage=cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));
            this.mAbasolut=cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));
        }

        if (cursor != null) {
            cursor.close();
        }


    }

    public Stock (){

    }


    public static Cursor getAllStocks(Context context){
        Cursor cursor;

        Uri allQuotesUri=Contract.Quote.BuildAllQuotesUri();
        cursor=context.getContentResolver().query(allQuotesUri,Contract.Quote.QUOTE_COLUMNS,null,null,null);

        return cursor;

    }




}
