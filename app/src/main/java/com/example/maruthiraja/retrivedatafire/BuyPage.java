package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BuyPage extends AppCompatActivity {

    TextView deliverystr,itemnamestr,sellernamestr,qtynostr,itempricestr,pricestr,deliverypricestr,
            totalamtstr,phonestr,shopaddstr;
    Button pickaddbtn;
    ImageView itemimg;
    private DatabaseReference mdatabase,mdb;
    private FirebaseUser muser;
    String itemid;
    String shopid;

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
        phonestr = (TextView) findViewById(R.id.phoneid);
        shopaddstr = (TextView) findViewById(R.id.shopaddressid);
        itemimg = (ImageView) findViewById(R.id.itemimageit);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("shop_details");
        mdb = FirebaseDatabase.getInstance().getReference().child("ShopkeeperSignup");
        mdatabase.child(itemid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    //String description = (String) dataSnapshot.child("description").getValue();
                    String image = (String) dataSnapshot.child("image").getValue();
                    String pristr = (String) dataSnapshot.child("price").getValue();
                    //String rating = (String) dataSnapshot.child("rating").getValue();
                    String titlestr = (String) dataSnapshot.child("title").getValue();
                    String itemno = (String) dataSnapshot.child("quantity").getValue();
                    shopid = (String) dataSnapshot.child("id").getValue();
                    pricestr.setText(pristr);
                    qtynostr.setText(itemno);
                    itemnamestr.setText(titlestr);
                    //des.setText(description);
                   // rb.setRating(Float.parseFloat(rating));
                    Picasso.with(getApplicationContext()).load(image).centerCrop().resize(itemimg.getMeasuredWidth(),itemimg.getMeasuredHeight()).error(R.drawable.ic_broken_image_black_24dp)
                            .placeholder(R.drawable.ic_image_black_24dp).into(itemimg);
                }catch (Exception e)
                {
                    Toast.makeText(BuyPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdb.child(shopid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Address = (String) dataSnapshot.child("Address").getValue();
                String name = (String) dataSnapshot.child("name").getValue();
                String phone = (String) dataSnapshot.child("phone").getValue();
                shopaddstr.setText(Address);
                sellernamestr.setText(Address);
                phonestr.setText(Address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
