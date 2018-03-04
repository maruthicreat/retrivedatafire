package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyPage extends AppCompatActivity {

    TextView deliveraddid,itemnameid,sellernameid,qtynoid,itempriceid,priceid,deliverypriceid,totalamtid;
    Button pickadd;
    ImageView itemimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);
    }
}
