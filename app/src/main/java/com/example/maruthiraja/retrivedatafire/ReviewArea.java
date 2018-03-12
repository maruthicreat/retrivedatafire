package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewArea extends AppCompatActivity {
    private String itemid;
    private TextView review;
    private RatingBar ratingBar;
    private Button button;
    private FirebaseUser muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_area);
        itemid = getIntent().getStringExtra("position");
        Toast.makeText(this, itemid, Toast.LENGTH_SHORT).show();
        ratingBar = (RatingBar) findViewById(R.id.putrating);
        review = (TextView) findViewById(R.id.reviewtext);
        button = (Button) findViewById(R.id.revsubbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rat = ratingBar.getRating();
                muser = FirebaseAuth.getInstance().getCurrentUser();
                //Toast.makeText(ReviewArea.this, rat+"", Toast.LENGTH_SHORT).show();
                String rev = review.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://shopkeeperapp-7d95b.firebaseio.com/");
                DatabaseReference myRef = database.getReference("product_reviews").child(muser.getUid()).child(itemid);
                myRef.child("review").setValue(rev);
                myRef.child("rating").setValue(rat);
                myRef.child("comrate").setValue("-1");
                Toast.makeText(ReviewArea.this, "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
