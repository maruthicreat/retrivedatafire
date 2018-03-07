package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private FirebaseUser cuser;
    private MaterialSearchView searchView;
    private FirebaseDatabase database,mdb;
    public RatingBar ratingBar;
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
        mList.setHasFixedSize(true);
        horizontalLayoutManagaer = new LinearLayoutManager(MyOrders.this, LinearLayoutManager.HORIZONTAL, false);
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
            protected void populateViewHolder(purchaseHolder viewHolder, GetPurchaseData model, int position) {
                String itemid = model.getItemid();
                mdb = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
                mref = database.getReference("shop_details").child(itemid);
                viewHolder.setTitleName(model.getAmount_payable());
                viewHolder.setTiming(model.getDescription());
                //viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setRating(model.getRating());
            }

        };
    }

    public static class purchaseHolder extends RecyclerView.ViewHolder {
        View mview;
        public purchaseHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }
        public void setTitleName(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.ItemTitleid);
            textTitle.setText(title);
        }
        public void setTiming(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.ItemTitleid);
            textTitle.setText(title);
        }
        public void setRating(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.ItemTitleid);
            textTitle.setText(title);
        }
        public void setImage(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.ItemTitleid);
            textTitle.setText(title);
        }
    }

}
