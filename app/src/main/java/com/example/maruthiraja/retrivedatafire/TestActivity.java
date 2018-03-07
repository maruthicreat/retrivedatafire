package com.example.maruthiraja.retrivedatafire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    TextView deliverystr,itemnamestr,sellernamestr,qtynostr,itempricestr,pricestr,taxamtstr,
            totalamtstr,phonestr,shopaddstr;
    Button pickaddbtn;
    ImageView itemimg;
    private DatabaseReference mdatabase,mdb;
    private FirebaseUser muser;
    String itemid;
    String shopid;
    String totalamt;
    String lat,log;
    RadioGroup rb;
    String address;
    RadioButton rrb;
    StringBuilder stBuilder = new StringBuilder();

    private GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
       // setContentView(R.layout.activity_buy_page);
        itemid = getIntent().getStringExtra("itemid");

        //Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        deliverystr = (TextView) findViewById(R.id.deliveryaddrid);
        itemnamestr = (TextView) findViewById(R.id.itemnameid);
        sellernamestr = (TextView) findViewById(R.id.sellernameid);
        qtynostr = (TextView) findViewById(R.id.qtynoid);
        itempricestr = (TextView) findViewById(R.id.itempriceid);
        pricestr = (TextView) findViewById(R.id.priceid);
        taxamtstr = (TextView) findViewById(R.id.taxamtid);
        totalamtstr = (TextView) findViewById(R.id.totalamtid);
        phonestr = (TextView) findViewById(R.id.phoneid);
        shopaddstr = (TextView) findViewById(R.id.shopaddressid);
        itemimg = (ImageView) findViewById(R.id.itemimageit);
        rb = (RadioGroup) findViewById(R.id.paymentgroup);
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
                    qtynostr.setText("1");
                    itemnamestr.setText(titlestr);
                    getshopid1();
                    itempricestr.setText(pristr);
                    taxamtstr.setText( ""+((Float.parseFloat(pristr)/100)*10));
                    totalamt = ""+(((Float.parseFloat(pristr)/100)*10)+(Float.parseFloat(pristr)));
                    totalamtstr.setText(""+(((Float.parseFloat(pristr)/100)*10)+(Float.parseFloat(pristr))));
                    //des.setText(description);
                    // rb.setRating(Float.parseFloat(rating));
                    Picasso.with(getApplicationContext()).load(image).centerCrop().resize(itemimg.getMeasuredWidth(),itemimg.getMeasuredHeight()).error(R.drawable.ic_broken_image_black_24dp)
                            .placeholder(R.drawable.ic_image_black_24dp).into(itemimg);
                }catch (Exception e)
                {
                    //Toast.makeText(BuyPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void placepickerfun(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String placename = String.format("%s", place.getName());
                lat = String.valueOf(place.getLatLng().latitude);
                log = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                /*stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");*/
                // stBuilder.append("Address: ");
                stBuilder.append(" "+address);
                deliverystr.setText(stBuilder.toString());
            }
        }
    }


    public void getshopid1()
    {
        //Toast.makeText(this, shopid, Toast.LENGTH_SHORT).show();
        mdb.child(shopid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String phone = (String) dataSnapshot.child("phone").getValue();
                address = (String) dataSnapshot.child("Address").getValue();
                Toast.makeText(TestActivity.this, name+phone+address, Toast.LENGTH_SHORT).show();
                sellernamestr.setText(name);
                phonestr.setText("Phone : "+phone);
                shopaddstr.setText("Address :"+address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getshopid(View view) {
        //Toast.makeText(this, shopid, Toast.LENGTH_SHORT).show();
        int selectedid = rb.getCheckedRadioButtonId();
        rrb = (RadioButton) findViewById(selectedid);
        if (stBuilder.toString().equals("")) {
            Toast.makeText(this, "Please Select the Delivery Address..!!!", Toast.LENGTH_SHORT).show();
        } else {
            if (rrb != null) {
                muser = FirebaseAuth.getInstance().getCurrentUser();
                mdb = FirebaseDatabase.getInstance().getReference().child("Purchased").child(muser.getUid()).push();
                mdb.child("itemid").setValue(itemid);

                mdb.child("paymentMode").setValue(rrb.getText());
                if (rrb.getText().equals("Cash On Delivery") || rrb.getText().equals("Get on Shop")) {
                    mdb.child("Amount_payable").setValue(totalamt);
                    mdb.child("ispayed").setValue("1");
                    if (rrb.getText().equals("Cash On Delivery")) {
                        Toast.makeText(this, "Cash On Delivery Accepted Your Order will be delivered within 2 hrs", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please Collect your Order to this Shop address : " + address, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mdb.child("Amount_payable").setValue("0");
                    mdb.child("ispayed").setValue("1");
                    Toast.makeText(this, "Online Payment Successful Your Order will be delivered within 2 hrs", Toast.LENGTH_SHORT).show();
                }
                mdb.child("orderat").setValue(stBuilder.toString());
                mdb.child("lat").setValue(lat);
                mdb.child("long").setValue(log);
                mdb.child("purchaseTime").setValue(Calendar.getInstance().getTime().toString());
                finish();
            } else {
                Toast.makeText(this, "Please Select your Payment Option...!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
