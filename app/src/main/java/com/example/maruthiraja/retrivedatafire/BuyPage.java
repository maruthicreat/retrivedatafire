package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuyPage extends AppCompatActivity {

    TextView deliverystr,itemnamestr,sellernamestr,qtynostr,itempricestr,pricestr,deliverypricestr,totalamtstr;
    Button pickaddbtn;
    ImageView itemimg;
    String itemid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);
        itemid = getIntent().getStringExtra("itemid");
        Toast.makeText(this, itemid, Toast.LENGTH_SHORT).show();
        deliverystr = (TextView) findViewById(R.id.deliveryaddrid);
        itemnamestr = (TextView) findViewById(R.id.itemnameid);
        sellernamestr = (TextView) findViewById(R.id.sellernameid);
        qtynostr = (TextView) findViewById(R.id.qtynoid);
        itempricestr = (TextView) findViewById(R.id.itempriceid);
        pricestr = (TextView) findViewById(R.id.priceid);
        deliverypricestr = (TextView) findViewById(R.id.deliverypriceid);
        totalamtstr = (TextView) findViewById(R.id.totalamtid);
    }
}
