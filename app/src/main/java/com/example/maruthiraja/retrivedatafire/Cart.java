package com.example.maruthiraja.retrivedatafire;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private FirebaseUser cuser;
    private FirebaseDatabase database,mdb;
    private Toolbar toolbar;
    private LinearLayoutManager horizontalLayoutManagaer;
    private DatabaseReference myRef,mref;
    private TextView textView;
    private List<String> listitems;
    String itemprice;
    private float total = 0;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.carttoolbar);
        mList = (RecyclerView) findViewById(R.id.cartlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mList.setHasFixedSize(true);
        button = (Button) findViewById(R.id.cartpaybtn);
        textView = (TextView) findViewById(R.id.totprice);
        mAuth = FirebaseAuth.getInstance();
        cuser = mAuth.getCurrentUser();
        horizontalLayoutManagaer = new LinearLayoutManager(Cart.this, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(horizontalLayoutManagaer);
        listitems = new ArrayList<String>();
        database = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
        myRef = database.getReference("cart").child(cuser.getUid());
        //myRef.keepSynced(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CartPayment.class));
            }
        });
        insetitem();
    }

    private void insetitem() {
        FirebaseRecyclerAdapter<GetCartData, CartHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GetCartData, CartHolder>(
                GetCartData.class,
                R.layout.cartitemrow,
                CartHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(CartHolder viewHolder, GetCartData model, final int position) {
               // Toast.makeText(Cart.this, "view", Toast.LENGTH_SHORT).show();
               // final String value;
                String itemid = model.getItemid();
                String itemname = model.getItemname();
                itemprice = model.getItemprice();
                String itmeimage = model.getImageid();
                viewHolder.setTitleName(itemname);
                viewHolder.setPrice(itemprice);
                //viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), itmeimage);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Toast.makeText(Cart.this, getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                        myRef.child(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.child("itemprice").getValue(String.class);
                                if (value != null)
                                {
                                    total = total - Float.parseFloat(value);
                                    textView.setText(total+"");
                                }
                               // Toast.makeText(Cart.this, value, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                        myRef.child(getRef(position).getKey()).removeValue();

                        //
                        //Toast.makeText(Cart.this, itemprice, Toast.LENGTH_SHORT).show();

                    }
                });
                //viewHolder.setRating(model.);
                total = total + Float.parseFloat(itemprice);
                textView.setText(total+"");
            }

        };
        mList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CartHolder extends RecyclerView.ViewHolder {
        View mview;
        Button button;

        public CartHolder(View itemView) {
            super(itemView);
            mview = itemView;
            button = (Button)mview.findViewById(R.id.cartremovebtn);
        }

        public void setTitleName(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.cartitemname);
            textTitle.setText(title);
        }
        public void setPrice(String price)
        {
            TextView textprice = (TextView) mview.findViewById(R.id.cartprice);
            textprice.setText(price);
        }
        public void setImage(Context applicationContext, String itemimage) {
            ImageView imageView = (ImageView) mview.findViewById(R.id.cartimage);
            Picasso.with(mview.getContext()).load(itemimage).fit().centerCrop().error(R.drawable.ic_broken_image_black_24dp)
                    .placeholder(R.drawable.ic_image_black_24dp).into(imageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
