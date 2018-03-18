package com.example.maruthiraja.retrivedatafire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SelectedItem extends AppCompatActivity {

    Toolbar toolbar;
    String itemid;
    private DatabaseReference mdatabase,mdb;
    private FirebaseUser muser;
    Button del,mod;
    ImageView imageView;
    TextView des,name,title,noofitem;
    TextView price;
    RatingBar rb;
    private String description,image,pristr,rating,titlestr,itemno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);
        itemid = getIntent().getStringExtra("position");
        del = (Button) findViewById(R.id.deletebtn);
        mod = (Button) findViewById(R.id.modifybtn);
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        imageView = (ImageView) findViewById(R.id.imageView2);
        des = (TextView) findViewById(R.id.selectDescription);
        price = (TextView) findViewById(R.id.selectPrice);
        noofitem = (TextView) findViewById(R.id.noofitemtext);
        title = (TextView) findViewById(R.id.selectTitle);
        rb = (RatingBar) findViewById(R.id.ratingBar2);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("shop_details");
        mdatabase.child(itemid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    description = (String) dataSnapshot.child("description").getValue();
                    image = (String) dataSnapshot.child("image").getValue();
                    pristr = (String) dataSnapshot.child("price").getValue();
                    rating = (String) dataSnapshot.child("rating").getValue();
                    titlestr = (String) dataSnapshot.child("title").getValue();
                    itemno = (String) dataSnapshot.child("quantity").getValue();
                    price.setText(pristr);
                    noofitem.setText(itemno);
                    title.setText(titlestr);
                    des.setText(description);
                    rb.setRating(Float.parseFloat(rating));
                    Picasso.with(getApplicationContext()).load(image).fit().centerCrop().error(R.drawable.ic_broken_image_black_24dp)
                            .placeholder(R.drawable.ic_image_black_24dp).into(imageView);
                }catch (Exception e)
                {
                    finish();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectedItem.this, "click Cart", Toast.LENGTH_SHORT).show();
                muser = FirebaseAuth.getInstance().getCurrentUser();
                mdb = FirebaseDatabase.getInstance().getReference().child("cart").child(muser.getUid()).push();
                mdb.child("itemid").setValue(itemid);
                mdb.child("imageid").setValue(image);
                mdb.child("itemname").setValue(titlestr);
                mdb.child("itemprice").setValue(pristr);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectedItem.this, "click Buy", Toast.LENGTH_SHORT).show();
                Intent selint = new Intent(SelectedItem.this,TestActivity.class);
                Intent intent = selint.putExtra("itemid", itemid);
                startActivity(intent);
                //startActivity(new Intent(SelectedItem.this,BuyPage.class));
            }
        });
        setSupportActionBar(toolbar);
        Toast.makeText(this, itemid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
