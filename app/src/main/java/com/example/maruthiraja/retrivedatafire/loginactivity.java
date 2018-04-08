package com.example.maruthiraja.retrivedatafire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginactivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private TextView textView;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_loginactivity);
        mAuth = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.usertext);
        password = (EditText) findViewById(R.id.passtext);
        textView = (TextView) findViewById(R.id.textView4);
        progressDialog = new ProgressDialog(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(loginactivity.this,viewItem.class));
                    //finish();
                }
            }
        };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginactivity.this,ForgetPassword.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void testpass(View v){

        String uname = username.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(uname))
        {
            username.setError( "User Name is required!");
        }
        else if(TextUtils.isEmpty(pass))
        {
            password.setError( "Password is required!" );
        }
        else {
            progressDialog.setMessage("Signing In");
            progressDialog.show();
            // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            mAuth.signInWithEmailAndPassword(uname, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(loginactivity.this, "Incorrect User Id or Password..!!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }


    public void callSignup(View view){
        startActivity(new Intent(loginactivity.this, Signup.class));
    }



}

