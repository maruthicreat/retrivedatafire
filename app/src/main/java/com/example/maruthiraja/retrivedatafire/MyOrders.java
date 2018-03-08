package com.example.maruthiraja.retrivedatafire;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private FirebaseUser cuser;
    private MaterialSearchView searchView;
    private FirebaseDatabase database,mdb;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef,mref;
    boolean doubleBackToExitPressedOnce = false;
    StaggeredGridLayoutManager gridLayoutManager;
    LinearLayoutManager horizontalLayoutManagaer;
    private List<String> listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        mList = (RecyclerView) findViewById(R.id.purchaseditems);
        mAuth = FirebaseAuth.getInstance();
        cuser = mAuth.getCurrentUser();
        toolbar = (Toolbar) findViewById(R.id.purchasetool);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mList.setHasFixedSize(true);
        horizontalLayoutManagaer = new LinearLayoutManager(MyOrders.this, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(horizontalLayoutManagaer);
        listitems = new ArrayList<String>();
        database = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
        myRef = database.getReference("Purchased").child(cuser.getUid());
        myRef.keepSynced(true);
        insetitem();
    }

    private void insetitem() {
        FirebaseRecyclerAdapter<GetPurchaseData, purchaseHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GetPurchaseData, purchaseHolder>(
                GetPurchaseData.class,
                R.layout.purchaseitemsrow,
                purchaseHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(purchaseHolder viewHolder, GetPurchaseData model, final int position) {
                Toast.makeText(MyOrders.this, "view", Toast.LENGTH_SHORT).show();
                String itemid = model.getItemid();
                String itemname = model.getItemname();
                String itemtime = model.getPurchaseTime();
                String itmerating = model.getItemrating();
               // mdb = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
                //mref = database.getReference("shop_details").child(itemid);
                viewHolder.setTitleName(itemname);
                viewHolder.setTiming(itemtime);
                viewHolder.setRating(itmerating);
                //viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getItemimage());
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(MyOrders.this, "review clicked :"+getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(MyOrders.this,ReviewArea.class));
                        Intent selint = new Intent(getApplicationContext(),ReviewArea.class);
                        Intent intent = selint.putExtra("position", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                //viewHolder.setRating(model.);
            }

        };
        mList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class purchaseHolder extends RecyclerView.ViewHolder {
        View mview;
        Button button;

        public purchaseHolder(View itemView) {
            super(itemView);
            mview = itemView;
            button = (Button)mview.findViewById(R.id.reviewbtn);
        }

        public void setTitleName(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.ItemTitleid);
            textTitle.setText(title);
        }
        public void setTiming(String timing)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.DelivredTime);
            textTitle.setText(timing);
        }
        public void setRating(String rating)
        {
            //TextView textTitle = (TextView) mview.findViewById(R.id.DelivredTime);
            //textTitle.setText(rating);
            RatingBar rat = (RatingBar) mview.findViewById(R.id.setratingid);
            rat.setRating(Float.parseFloat(rating));
        }

        public void setImage(Context applicationContext, String itemimage) {
            ImageView imageView = (ImageView) mview.findViewById(R.id.imageviewid);
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
