package com.udacity.stockhawk;

/**
 * Created by Gabriel on 27/12/2016.
 */

public class YahooChart {

    private static final String CHART_BASE_URL="http://chart.finance.yahoo.com/z?s=";
    private static final String CHART_OTHER_PARAMETERS_URL="&t=6m&q=l&l=on&z=s&p=m50,m200";


    /**
     * This method retrieves the url to generate the Yahoo stock chart
     */

    public static String getChartUrl(String symbol, boolean fullUrl){
        String url=CHART_BASE_URL+symbol;
        if (fullUrl){
            url=url+CHART_OTHER_PARAMETERS_URL;
        }

        return url;
    }



}
