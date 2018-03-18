package com.example.maruthiraja.retrivedatafire;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchItem extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView itemlist;
    private DatabaseReference mdatabase;
    StaggeredGridLayoutManager gridLayoutManager;
    private List<String> listitems ;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        message = intent.getStringExtra("message");
       // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setrecycler();
    }

    private void setrecycler() {
        mdatabase = FirebaseDatabase.getInstance().getReference().child("shop_details");
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mdatabase.keepSynced(true);
        //Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        itemlist = (RecyclerView) findViewById(R.id.item_list);
        itemlist.setHasFixedSize(true);
        itemlist.setLayoutManager(gridLayoutManager);
        listitems = new ArrayList<String>();
        FirebaseRecyclerAdapter<Getdata,viewItem.Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Getdata, viewItem.Holder>(
                Getdata.class,
                R.layout.itemrow,
                viewItem.Holder.class,
                mdatabase.orderByChild("title").equalTo(message)
        ) {

            @Override
            protected void populateViewHolder(viewItem.Holder viewHolder, Getdata model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setRating(model.getRating());
            }

            @Override
            public viewItem.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                viewItem.Holder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new viewItem.Holder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
                        Intent selint = new Intent(getApplicationContext(),SelectedItem.class);
                        Intent intent = selint.putExtra("position", getRef(position).getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder;
            }

        };

        itemlist.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
