package com.example.maruthiraja.retrivedatafire;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.data.DataHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class viewItem extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private MaterialSearchView searchView;
    public RatingBar ratingBar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;
    boolean doubleBackToExitPressedOnce = false;
    private List<String> listitems ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mList =(RecyclerView)findViewById(R.id.itemRecycler);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        listitems = new ArrayList<String>();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(viewItem.this,loginactivity.class));
                    finish();
                }
            }
        };

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
        myRef = database.getReference("shop_details");
        myRef.keepSynced(true);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String key = ds.getKey();
                    listitems.add(ds.child("title").getValue().toString());
                    setsuggestion(listitems);
                    //String description = ds.child("description").getValue().toString();
                    //Toast.makeText(viewItem.this, description, Toast.LENGTH_SHORT).show();
                }

                //String value = dataSnapshot.getValue(String.class);
                //System.out.println(value);
               // Toast.makeText(viewItem.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.w(TAG, "Failed to read value.", error.toException());
                System.out.println(error.toException());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewItem.this,ViewMap.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Getdata,Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Getdata, Holder>(
                Getdata.class,
                R.layout.itemrow,
                Holder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(Holder viewHolder, Getdata model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setRating(model.getRating());
            }

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                Holder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new Holder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
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

        mList.setAdapter(firebaseRecyclerAdapter);

    }

    public void setsuggestion(List<String> listitems) {
        String listarr[] = listitems.toArray(new String[listitems.size()]);
        // Toast.makeText(this, listarr[0], Toast.LENGTH_SHORT).show();
        searchView.setSuggestions(listarr);
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        View mview;

        public Holder(View itemView) {
            super(itemView);
            mview = itemView;
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mview.getContext(), "you clicked me!!", Toast.LENGTH_SHORT).show();
                    mClickListener.onItemClick(view,getAdapterPosition());
                }
            });
            mview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Toast.makeText(mview.getContext(), "you Long clicked me!!", Toast.LENGTH_SHORT).show();
                    mClickListener.onItemLongClick(view,getAdapterPosition());
                    return true;
                }
            });
        }

        private Holder.ClickListener mClickListener;


        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(Holder.ClickListener clickListener){
            mClickListener = clickListener;
        }

        public void setTitle(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.itemtitle);
            textTitle.setText(title);
        }
        public void setDescription(String desc)
        {
            TextView textdesc = (TextView) mview.findViewById(R.id.itemdescription);
            textdesc.setText(desc);
        }
        public void setPrice(String price)
        {
            TextView textprice = (TextView) mview.findViewById(R.id.itemprice);
            textprice.setText(price);
        }
        public void setImage(Context ctx,String image)
        {
            ImageView img = (ImageView) mview.findViewById(R.id.postimage);
            Picasso.with(ctx).load(image).into(img);
        }
        public void setRating(String rating)
        {
            RatingBar ratingBar = (RatingBar) mview.findViewById(R.id.ratingBar);
            ratingBar.setFocusable(false);
            ratingBar.setRating(Float.parseFloat(rating));
            System.out.println(rating);
        }
        public void setReview()
        {

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                // Toast.makeText(this, "finish all activity", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mAuth.signOut();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.cart) {

        } else if (id == R.id.orders) {

        } else if (id == R.id.account) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
