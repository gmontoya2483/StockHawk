package com.udacity.stockhawk.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {


    static final String AUTHORITY = "com.udacity.stockhawk";
    static final String PATH_QUOTE = "quote";
    static final String PATH_QUOTE_WITH_SYMBOL = "quote/*";
    static final String PATH_WIDGET="widget";
    static final String PATH_WIDGET_WITH_ID="widget/#";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private Contract() {
    }

    public static final class Quote implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_QUOTE).build();
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_ABSOLUTE_CHANGE = "absolute_change";
        public static final String COLUMN_PERCENTAGE_CHANGE = "percentage_change";
        public static final String COLUMN_HISTORY = "history";
        public static final int POSITION_ID = 0;
        public static final int POSITION_SYMBOL = 1;
        public static final int POSITION_PRICE = 2;
        public static final int POSITION_ABSOLUTE_CHANGE = 3;
        public static final int POSITION_PERCENTAGE_CHANGE = 4;
        public static final int POSITION_HISTORY = 5;
        public static final String[] QUOTE_COLUMNS = {
                _ID,
                COLUMN_SYMBOL,
                COLUMN_PRICE,
                COLUMN_ABSOLUTE_CHANGE,
                COLUMN_PERCENTAGE_CHANGE,
                COLUMN_HISTORY
        };
        static final String TABLE_NAME = "quotes";

        public static Uri makeUriForStock(String symbol) {
            return URI.buildUpon().appendPath(symbol).build();
        }

        static String getStockFromUri(Uri queryUri) {
            return queryUri.getLastPathSegment();
        }





    }


    public static final class Widget implements BaseColumns{


        //Table Name
        public static final String TABLE_NAME="widgets";

        //Columns
        public static final String COLUMN_WIDGET_SYMBOL="widget_symbol";

        // Create content uri
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_WIDGET)
                .build();

        // create cursor of base type directory for multiples entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_WIDGET;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_WIDGET;


        public static Uri buildWidgetsByWidgetIdUri(Long id){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }


        public static Uri buildAllWidgetsUri(){
            return CONTENT_URI;
        }






    }

}
