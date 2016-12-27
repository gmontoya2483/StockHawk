package com.udacity.stockhawk.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;

/**
 * Created by Gabriel on 26/12/2016.
 */

public class StockDetailsFragment extends Fragment{

    String mSymbol;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_stock_details, container, false);
        TextView text= (TextView) rootView.findViewById(R.id.text1);


        getIntentParameters();
        text.setText(mSymbol);



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    //Helper Method to get the Intent information comming from the main Activity
    private void getIntentParameters(){
        Intent intent=getActivity().getIntent();
        if (intent !=null){
            mSymbol=intent.getStringExtra("SYMBOL");

        }else{
            mSymbol="NO DATA";
        }

    }





}
