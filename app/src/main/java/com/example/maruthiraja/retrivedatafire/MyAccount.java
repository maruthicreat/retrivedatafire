package com.example.maruthiraja.retrivedatafire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccount extends AppCompatActivity {
    TextView use,mob,mail;
    private DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        use = (TextView)findViewById(R.id.usernametext);
        mob = (TextView)findViewById(R.id.mobiletext);
        mail = (TextView)findViewById(R.id.mailidtext);
       // Toast.makeText(getApplicationContext(), "okok", Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        System.out.println("user phone number : "+user.getPhoneNumber());
        System.out.println("user name : "+user.getDisplayName());

        //Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("CustomerSignup");
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String key = ds.getKey();
                    String checkmail = ds.child("mail_id").getValue().toString();
                    if (email.equals(checkmail)) {
                        String na = ds.child("name_id").getValue().toString();
                        String ph = ds.child("phone_id").getValue().toString();
                        use.setText(na);
                        mob.setText(ph);
                        mail.setText(email);
                       // Toast.makeText(getApplicationContext(), na, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
