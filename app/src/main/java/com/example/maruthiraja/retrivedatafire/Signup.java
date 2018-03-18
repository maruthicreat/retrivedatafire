package com.example.maruthiraja.retrivedatafire;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Signup extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText repass,emailId,password,nameid,phoneno;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        repass = (EditText)findViewById(R.id.Cretypepass);
        emailId = (EditText) findViewById(R.id.CEmail);
        nameid = (EditText) findViewById(R.id.cusnametext);
        phoneno = (EditText) findViewById(R.id.cusphonetext);
        password = (EditText) findViewById(R.id.CPass);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("CustomerSignup");
    }

    public void storeUserData(View view)
    {

        final String rep = repass.getText().toString().trim();
        final String emailid = emailId.getText().toString().trim();
        final String passwordid = password.getText().toString().trim();
        final String name = nameid.getText().toString().trim();
        final String phone = phoneno.getText().toString().trim();
        if (TextUtils.isEmpty(name)){

            nameid.setError( "User Name is required!");

        }else if(TextUtils.isEmpty(phone))
        {
            phoneno.setError( "Phone Number is required!");
        }
        else if(TextUtils.isEmpty(emailid))
        {
            emailId.setError( "Email id is required!");
        }
        else if(TextUtils.isEmpty(passwordid))
        {
            password.setError( "Password is required!");
        }
        else if(TextUtils.isEmpty(rep))
        {
            repass.setError( "Re-password is required!");
        }
        else if(passwordid.length() < 8)
        {
            password.setError( "Password must Contain 8 Character!");
        }
        else {
            if (rep.equals(passwordid))
            {
                progressDialog.setMessage("Signing Up...!!!");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailid, passwordid).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        makeText(Signup.this, "Verification email sent to your Email id..!", LENGTH_SHORT).show();
                                    }
                                    else {
                                        makeText(Signup.this, "Failed to sent the Email Verification..! ", LENGTH_SHORT).show();
                                    }
                                }
                            });
                            String u_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference c_uid = mDatabase.child(u_id);
                            c_uid.child("user_id").setValue("1");
                            c_uid.child("mail_id").setValue(emailid);
                            c_uid.child("password").setValue(passwordid);
                            progressDialog.dismiss();
                            makeText(Signup.this, "You are Successfully Signed up...!!!", LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                makeText(Signup.this, "User with this email already exist.", LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else {
                                makeText(Signup.this, "Please Enter the valid mail id.", LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
            }
            else {
                repass.setError( "Re-password is miss match!");
            }
        }
    }
}
