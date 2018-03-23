package com.example.maruthiraja.retrivedatafire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
        implements NavigationView.OnNavigationItemSelectedListener ,PriceFragment.OnFragmentInteractionListener
        ,NameFragment.OnFragmentInteractionListener,RatingFragment.OnFragmentInteractionListener{

    Fragment fragment = null;
    Class fragmentClass = null;

    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private MaterialSearchView searchView;
    public RatingBar ratingBar;
    private ProgressDialog progress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    boolean doubleBackToExitPressedOnce = false;
    StaggeredGridLayoutManager gridLayoutManager;
    private List<String> listitems ;
    private static FirebaseRecyclerAdapter<Getdata,Holder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_view_item);
         // Make to run your application only in portrait mode
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Make to run your application only in LANDSCAPE mode
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner productname_spinner =(Spinner) findViewById(R.id.spinner2);
       // Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
        mList =(RecyclerView)findViewById(R.id.itemRecycler);
        mList.setHasFixedSize(true);
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mList.setLayoutManager(gridLayoutManager);
       // mList.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        listitems = new ArrayList<String>();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //startActivity(new Intent(viewItem.this,loginactivity.class));
                    finish();
                }
            }
        };


        fragmentClass = PriceFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


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
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.w(TAG, "Failed to read value.", error.toException());
                System.out.println(error.toException());
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(viewItem.this, "change", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(viewItem.this,SearchItem.class);
                intent.putExtra("message",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                //Toast.makeText(shopkeeperfirstpage.this, "change", Toast.LENGTH_SHORT).show();
                return false;
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

        productname_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        Object item = parent.getItemAtPosition(pos);
                       // Toast.makeText(viewItem.this, item.toString(), Toast.LENGTH_SHORT).show();
                        if (item.toString().equals("Sort by Name"))
                        {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            NameFragment fragment = new NameFragment();
                            fragmentTransaction.replace(R.id.fragmentarea, fragment);
                            //fragmentTransaction.addToBackStack(fragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        }
                        else if (item.toString().equals("Sort by Price"))
                        {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            PriceFragment fragment = new PriceFragment();
                            fragmentTransaction.replace(R.id.fragmentarea, fragment);
                            //fragmentTransaction.addToBackStack(fragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        }
                        else if (item.toString().equals("Sort by Ratings"))
                        {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            RatingFragment fragment = new RatingFragment();
                            fragmentTransaction.replace(R.id.fragmentarea, fragment);
                            //fragmentTransaction.addToBackStack(fragment.toString());
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.commit();
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setsuggestion(List<String> listitems) {
        String listarr[] = listitems.toArray(new String[listitems.size()]);
        // Toast.makeText(this, listarr[0], Toast.LENGTH_SHORT).show();
        searchView.setSuggestions(listarr);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                    Toast.makeText(mview.getContext(), "you clicked me!!", Toast.LENGTH_SHORT).show();
                    mClickListener.onItemClick(view,getAdapterPosition());
                }
            });
            mview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                     Toast.makeText(mview.getContext(), "you Long clicked me!!", Toast.LENGTH_SHORT).show();
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
            Picasso.with(ctx).load(image).fit().centerCrop().error(R.drawable.ic_broken_image_black_24dp)
                    .placeholder(R.drawable.ic_image_black_24dp).into(img);
          //  Picasso.with(ctx).load(image).into(img);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                // Toast.makeText(this, "finish all activity", Toast.LENGTH_SHORT).show();
                finishAffinity();
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

        } else if (id == R.id.cart) {
            startActivity(new Intent(viewItem.this,Cart.class));

        } else if (id == R.id.orders) {
            startActivity(new Intent(viewItem.this,MyOrders.class));

        } else if (id == R.id.account) {
            startActivity(new Intent(viewItem.this,MyAccount.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
