package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartPayment extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RecyclerView mList;
    private FirebaseUser cuser;
    private FirebaseDatabase database,mdb;
    private Toolbar toolbar;
    private LinearLayoutManager horizontalLayoutManagaer;
    private DatabaseReference myRef,mref;
    private TextView textView;
    private List<String> listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_payment);
        mList = (RecyclerView) findViewById(R.id.cartpaymentlist);
        mList.setHasFixedSize(true);
    }
}
