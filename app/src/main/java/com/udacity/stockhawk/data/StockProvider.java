package com.udacity.stockhawk.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.stockhawk.R;


public class StockProvider extends ContentProvider {

    private static final String LOG_TAG=StockProvider.class.getSimpleName();

    static final int QUOTE = 100;
    static final int QUOTE_FOR_SYMBOL = 101;

    static final int WIDGET=200;
    static final int WIDGET_WITH_WIDGET_ID=201;





    static UriMatcher uriMatcher = buildUriMatcher();

    private DbHelper dbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_QUOTE, QUOTE);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_QUOTE_WITH_SYMBOL, QUOTE_FOR_SYMBOL);
        matcher.addURI(Contract.AUTHORITY,Contract.PATH_WIDGET,WIDGET);
        matcher.addURI(Contract.AUTHORITY,Contract.PATH_WIDGET_WITH_ID,WIDGET_WITH_WIDGET_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                returnCursor = db.query(
                        Contract.Quote.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case QUOTE_FOR_SYMBOL:
                returnCursor = db.query(
                        Contract.Quote.TABLE_NAME,
                        projection,
                        Contract.Quote.COLUMN_SYMBOL + " = ?",
                        new String[]{Contract.Quote.getStockFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );

                break;
            case WIDGET_WITH_WIDGET_ID:
                String widget_id=String.valueOf(ContentUris.parseId(uri));
                returnCursor=queryWidgetByWidgetID(widget_id);
                break;


            default:
                throw new UnsupportedOperationException(String.format(getContext().getString(R.string.uri_unknown),uri.toString()));
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

//        if (db.isOpen()) {
//            db.close();
//        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                db.insert(
                        Contract.Quote.TABLE_NAME,
                        null,
                        values
                );
                returnUri = Contract.Quote.URI;
                break;

            case WIDGET:
                returnUri=insertWidget(values);
                break;

            default:
                throw new UnsupportedOperationException(String.format(getContext().getString(R.string.uri_unknown),uri.toString()));
        }

        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }
        switch (uriMatcher.match(uri)) {
            case QUOTE:
                rowsDeleted = db.delete(
                        Contract.Quote.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;

            case QUOTE_FOR_SYMBOL:
                String symbol = Contract.Quote.getStockFromUri(uri);
                rowsDeleted = db.delete(
                        Contract.Quote.TABLE_NAME,
                        '"' + symbol + '"' + " =" + Contract.Quote.COLUMN_SYMBOL,
                        selectionArgs
                );
                break;

            case WIDGET_WITH_WIDGET_ID:
                String widgetID=String.valueOf(ContentUris.parseId(uri));
                rowsDeleted=deleteWidget(widgetID);
                break;

            default:
                throw new UnsupportedOperationException(String.format(getContext().getString(R.string.uri_unknown),uri.toString()));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        //if (rowsDeleted != 0) {

        //}
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        db.insert(
                                Contract.Quote.TABLE_NAME,
                                null,
                                value
                        );
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }


    }


    private Cursor queryWidgetByWidgetID(String widgetId){
        Cursor cursor;

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        if (db.isOpen()){

            String SQLStatment="SELECT * from "+Contract.Widget.TABLE_NAME+" WHERE "+Contract.Widget._ID+"="+widgetId;

            cursor=db.rawQuery(SQLStatment,null);

        }else{
            cursor=null;
            Log.e(LOG_TAG,getContext().getString(R.string.provider_db_not_open));
        }


        return cursor;



    }


    private Uri insertWidget(ContentValues values){

        Uri insertedWidgetUri=null;
        Long insertedWidgetId;

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()){
            insertedWidgetId=db.insert(Contract.Widget.TABLE_NAME,null,values);
            if(insertedWidgetId!=-1){
                insertedWidgetUri=Contract.Widget.buildWidgetsByWidgetIdUri(insertedWidgetId);
            }else{
                insertedWidgetUri=null;
                Log.e(LOG_TAG,getContext().getString(R.string.provider_widget_not_inserted));
            }


        }else{
            insertedWidgetUri=null;
            Log.e(LOG_TAG,getContext().getString(R.string.provider_db_not_open));
        }

        return insertedWidgetUri;

    }

    private int deleteWidget(String id){
        int deletedRecords=-1;

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()){

            deletedRecords=db.delete(Contract.Widget.TABLE_NAME,Contract.Widget._ID+"=?",new String[]{id});


            if(deletedRecords!=1){

                Log.e(LOG_TAG,getContext().getString(R.string.provider_widget_delete_problem));
            }
        }else{
            Log.e(LOG_TAG,getContext().getString(R.string.provider_db_not_open));
        }
        return  deletedRecords;

    }




}
