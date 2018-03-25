package com.example.maruthiraja.retrivedatafire;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CartPayment extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private FirebaseUser cuser;
    private FirebaseDatabase database;
    private Toolbar toolbar;
    private TextView pricetext,taxtext, totalcarttext ,deliverystr;
    private LinearLayoutManager horizontalLayoutManagaer;
    private DatabaseReference myRef, mref;
    private float total = 0,tax = 0;
    private String lat,log;
    private DatabaseReference mdatabase,mdb;
    private FirebaseUser muser;
    private List<String> listitems;
    private ArrayList<String>  itemname,itemprice,itemid ,itmeimage ;
    private String rat,shopid;
    private RadioButton rrb;
    private int count = 0;
    private RadioGroup rb;

    StringBuilder stBuilder = new StringBuilder();

    private GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cart_payment);
        mList = (RecyclerView) findViewById(R.id.cartpaymentlist);
        itemname =  new ArrayList<String>();
        itemprice =  new ArrayList<String>();
        itemid =  new ArrayList<String>();
        itmeimage =  new ArrayList<String>();
        deliverystr = (TextView) findViewById(R.id.deliveryaddrid);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        mList.setHasFixedSize(true);
        rb = (RadioGroup) findViewById(R.id.deleviryselectgroup);
        pricetext = (TextView) findViewById(R.id.priceidcart);
        taxtext = (TextView) findViewById(R.id.taxamtidcart);
        totalcarttext = (TextView) findViewById(R.id.totalamtidcart);
        mAuth = FirebaseAuth.getInstance();
        cuser = mAuth.getCurrentUser();
        horizontalLayoutManagaer = new LinearLayoutManager(CartPayment.this, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(horizontalLayoutManagaer);
        database = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
        myRef = database.getReference("cart").child(cuser.getUid());
        mdb = FirebaseDatabase.getInstance().getReference().child("ShopkeeperSignup");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("shop_details");
        insetitem();
    }

    private void insetitem() {
        //Toast.makeText(this, "ookok", Toast.LENGTH_SHORT).show();


        FirebaseRecyclerAdapter<CartListGetData, CartListHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartListGetData, CartListHolder>(
                CartListGetData.class,
                R.layout.cartlist,
                CartListHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(CartListHolder viewHolder, CartListGetData model, final int position) {
                // Toast.makeText(CartPayment.this, "view", Toast.LENGTH_SHORT).show();
                // final String value;
                itemid.add(model.getItemid());
                itemname.add(model.getItemname());
                itemprice.add(model.getItemprice());
                itmeimage.add(model.getImageid());
                viewHolder.setTitleName(model.getItemname());
                viewHolder.setPrice(model.getItemprice());
                //viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getImageid());
                total = total + Float.parseFloat(model.getItemprice());
                tax = (total/100)*10;
                taxtext.setText(tax+"");
                pricetext.setText(total+"");
                totalcarttext.setText(String.valueOf(tax+total));
                System.out.println("count : "+count++);
            }

        };
        mList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class CartListHolder extends RecyclerView.ViewHolder {
        View mview;
        Button button;

        public CartListHolder(View itemView) {
            super(itemView);
            mview = itemView;
            //button = (Button)mview.findViewById(R.id.cartremovebtn);
        }

        public void setTitleName(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.itemlistnameid);
            textTitle.setText(title);
        }
        public void setPrice(String price)
        {
            TextView textprice = (TextView) mview.findViewById(R.id.itemlistpriceid);
            textprice.setText(price);
        }
        public void setImage(Context applicationContext, String itemimage) {
            ImageView imageView = (ImageView) mview.findViewById(R.id.itemlistimageit);
            Picasso.with(mview.getContext()).load(itemimage).fit().centerCrop().error(R.drawable.ic_broken_image_black_24dp)
                    .placeholder(R.drawable.ic_image_black_24dp).into(imageView);
        }
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


   public void getshopid(View view) {
       Iterator itr = itemname.iterator();
       Iterator itr1 = itemprice.iterator();
       Iterator itr2 = itmeimage.iterator();
       Iterator itr3 = itemid.iterator();
       while (itr.hasNext() & itr1.hasNext() & itr2.hasNext() & itr3.hasNext()) {
          // System.out.println(itr.next());
           //System.out.println(itr1.next());
           //System.out.println(itr2.next());
           //System.out.println(itr3.next());
           int selectedid = rb.getCheckedRadioButtonId();
           rrb = (RadioButton) findViewById(selectedid);
           if (stBuilder.toString().equals("")) {
               Toast.makeText(this, "Please Select the Delivery Address..!!!", Toast.LENGTH_SHORT).show();
           } else {
               if (rrb != null) {
                   muser = FirebaseAuth.getInstance().getCurrentUser();
                   mdb = FirebaseDatabase.getInstance().getReference().child("Purchased").child(muser.getUid()).push();
                   String itemid = itr3.next().toString();
                   mdb.child("itemid").setValue(itemid);
                   mdb.child("itemname").setValue(itr.next().toString());
                   mdb.child("price").setValue(itr1.next().toString());
                   mdb.child("itemimage").setValue(itr2.next().toString());
                   getrating(itemid,mdb);
                   //mdb.child("itemrating").setValue(getrating(itemid));
                   mdb.child("paymentMode").setValue(rrb.getText());
                   if (rrb.getText().equals("Cash On Delivery") || rrb.getText().equals("Get on Shop")) {
                       mdb.child("amountpayable").setValue(total + "");
                       mdb.child("ispayed").setValue("1");
                       if (rrb.getText().equals("Cash On Delivery")) {
                           Toast.makeText(this, "Cash On Delivery Accepted Your Order will be delivered within 2 hrs", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(this, "Please Collect your Order on the Shop . ", Toast.LENGTH_SHORT).show();
                       }
                   } else {
                       mdb.child("AmountPayable").setValue("0");
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
    }

    public void getrating(String id, final DatabaseReference mdb)
    {

        mdatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rat = (String) dataSnapshot.child("rating").getValue();
                shopid = (String) dataSnapshot.child("id").getValue();
                mdb.child("itemrating").setValue(rat);
                mdb.child("shopid").setValue(shopid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //return rat.get(0);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


}
