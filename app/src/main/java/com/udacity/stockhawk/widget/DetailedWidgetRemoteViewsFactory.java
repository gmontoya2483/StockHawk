package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.Stock;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel on 04/01/2017.
 */

public class DetailedWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private final String LOG_TAG = DetailedWidgetRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private int mAppWidgetId;
    private Cursor mCursor;




    public DetailedWidgetRemoteViewsFactory(Context context, Intent intent){
        mContext=context;
        mAppWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }



    @Override
    public void onCreate() {
        //Nothing to do

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission
        final long identityToken = Binder.clearCallingIdentity();

        mCursor=Stock.getAllStocks(mContext);

        Binder.restoreCallingIdentity(identityToken);


    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }


    // Given the position (index) of a WidgetItem in the array, use the item's text value in
    // combination with the app widget item XML file to construct a RemoteViews object.
    @Override
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.

        // Construct a RemoteViews item based on the app widget item XML file, and set the
        // text based on the position.
        //RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_detail_list_item);
        //rv.setTextViewText(R.id.widgetSymbol, mWidgetItems.get(position).getSymbol());
        //rv.setTextViewText(R.id.widgetPrice,String.format(mContext.getString(R.string.formatted_Price),mWidgetItems.get(position).getPrice()));




        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_detail_list_item);
        Stock stock=new Stock();


        stock.setSymbol(mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL)));
        stock.setPrice(mCursor.getDouble(mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
        stock.setAbsolute(mCursor.getDouble(mCursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE)));
        stock.setPercentage(mCursor.getDouble(mCursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE)));


        views.setTextViewText(R.id.widgetSymbol, stock.getSymbol());
        views.setContentDescription(R.id.widgetSymbol,String.format(mContext.getString(R.string.a11y_stock_symbol),stock.getSymbol()));

        views.setTextViewText(R.id.widgetPrice,String.format(mContext.getString(R.string.formatted_Price),stock.getPrice()));
        views.setContentDescription(R.id.widgetPrice,String.format(mContext.getString(R.string.a11y_price),stock.getPrice()));

        views.setTextViewText(R.id.widgetChange,String.format(mContext.getString(R.string.formatted_Porcentage),stock.getPorcentage()));
        views.setTextViewText(R.id.widgetAbsolut,String.format(mContext.getString(R.string.formatted_Absolut),stock.getAbasolut()));


        //Setting change views background on the basis of price change
        if (stock.getAbasolut()>=0){
            views.setInt(R.id.widgetChange, "setBackgroundResource", R.drawable.percent_change_pill_green);
            views.setInt(R.id.widgetAbsolut, "setBackgroundResource", R.drawable.percent_change_pill_green);
            views.setContentDescription(R.id.widgetChange,String.format(mContext.getString(R.string.a11y_variation_percentage),stock.getPorcentage()));
            views.setContentDescription(R.id.widgetChange,String.format(mContext.getString(R.string.a11y_variation_$),stock.getAbasolut()));


        }else{
            views.setInt(R.id.widgetChange, "setBackgroundResource", R.drawable.percent_change_pill_red);
            views.setInt(R.id.widgetAbsolut, "setBackgroundResource", R.drawable.percent_change_pill_red);
            views.setContentDescription(R.id.widgetChange,String.format(mContext.getString(R.string.a11y_variation_percentage_minus),stock.getPorcentage()));
            views.setContentDescription(R.id.widgetChange,String.format(mContext.getString(R.string.a11y_variation_$_minus),stock.getAbasolut()));

        }



        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Contract.Quote.COLUMN_SYMBOL, stock.getSymbol());
        views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return views;


    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_detail_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor.moveToPosition(position)) {
            return mCursor.getLong(mCursor.getColumnIndex(Contract.Quote._ID));
        }
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
