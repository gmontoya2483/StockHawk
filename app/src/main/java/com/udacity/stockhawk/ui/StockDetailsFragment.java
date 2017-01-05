package com.udacity.stockhawk.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.Stock;
import com.udacity.stockhawk.Utils;
import com.udacity.stockhawk.YahooChart;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by Gabriel on 26/12/2016.
 */

public class StockDetailsFragment extends Fragment{



    private Context context;

    private View mRootView;
    private TextView mSymbolView;
    private TextView mPriceView;
    private TextView mAbsolutView;
    private TextView mPorcentageView;
    private TextView mErrorView;

    private WebView mWebView;


    private String mSymbol;

    private Stock mStock;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context= getActivity().getApplicationContext();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_stock_details, container, false);

        getIntentParameters();
        setLayoutObjects();
        setLayoutValues();


        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    //Helper Method to get the Intent information coming from the main Activity
    private void getIntentParameters(){
        Intent intent=getActivity().getIntent();
        if (intent !=null){
            mSymbol=intent.getStringExtra(Contract.Quote.COLUMN_SYMBOL);

        }else{
            mSymbol=Stock.NO_SYMBOL;
        }

    }

    //Helper method to localize the views
    private void setLayoutObjects(){
        mWebView = (WebView) mRootView.findViewById(R.id.webView);
        mSymbolView=(TextView) mRootView.findViewById(R.id.CardSymbol);
        mPriceView=(TextView)mRootView.findViewById(R.id.CardPrice);
        mAbsolutView=(TextView)mRootView.findViewById(R.id.CardAbsolut);
        mPorcentageView=(TextView)mRootView.findViewById(R.id.CardChange);
        mErrorView=(TextView) mRootView.findViewById(R.id.errorText);

    }


    private void setLayoutValues(){

        mStock=new Stock(context,mSymbol);

        //Set the YahooChart
        if (Utils.NetworkUp(context)){
            mWebView.loadUrl(YahooChart.getChartUrl(mSymbol,true));
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.setContentDescription(null);
            mWebView.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);

        }else{
            mWebView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
        }


        //Set the SymbolView
        mSymbolView.setText(mStock.getSymbol());
        mSymbolView.setContentDescription(String.format(context.getString(R.string.a11y_stock_symbol),mStock.getSymbol()));

        //Set the PriceView
        mPriceView.setText(String.format(context.getString(R.string.formatted_Price), mStock.getPrice()));
        mPriceView.setContentDescription(String.format(context.getString(R.string.a11y_price), mStock.getPrice()));


        //Set the Absolute change
        mAbsolutView.setText(String.format(context.getString(R.string.formatted_Absolut), mStock.getAbasolut()));
        if (mStock.getAbasolut()>=0){
            mAbsolutView.setBackground(context.getDrawable(R.drawable.percent_change_pill_green));
            mAbsolutView.setContentDescription(String.format(context.getString(R.string.a11y_variation_$), mStock.getAbasolut()));

        }else{
            mAbsolutView.setBackground(context.getDrawable(R.drawable.percent_change_pill_red));
            mAbsolutView.setContentDescription(String.format(context.getString(R.string.a11y_variation_$_minus), mStock.getAbasolut()));
        }



        //Set the Percentage change
        mPorcentageView.setText(String.format(context.getString(R.string.formatted_Porcentage), mStock.getPorcentage()));
        if (mStock.getPorcentage()>=0){
            mPorcentageView.setBackground(context.getDrawable(R.drawable.percent_change_pill_green));
            mPorcentageView.setContentDescription(String.format(context.getString(R.string.a11y_variation_percentage), mStock.getPorcentage()));

        }else{
            mPorcentageView.setBackground(context.getDrawable(R.drawable.percent_change_pill_red));
            mPorcentageView.setContentDescription(String.format(context.getString(R.string.a11y_variation_percentage_minus), mStock.getPorcentage()));
        }

        mSymbolView.requestFocus();




    }


}
