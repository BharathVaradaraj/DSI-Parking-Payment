package com.cse.dsiparkingpayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Administrator on 10-08-2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "EmailPassword";
    Button sign_in, mregister;
    EditText email, password;
    TextView forgotpwd;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i1 = new Intent(Login.this,UserProfile.class);
                    finish();
                    startActivity(i1);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.sign_in);
        mregister = (Button) findViewById(R.id.mregister);
        forgotpwd = (TextView) findViewById((R.id.textView3)) ;

        sign_in.setOnClickListener(this);
        mregister.setOnClickListener(this);
        forgotpwd.setOnClickListener(this);

    }
        @Override
        public void onStart() {
            super.onStart();
            firebaseAuth.addAuthStateListener(mAuthListener);

        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                firebaseAuth.removeAuthStateListener(mAuthListener);
            }
        }

    @Override
    public void onClick(View v) {

        String mail = email.getText().toString();
        String pwd = password.getText().toString();

        if (v == sign_in){

            if(TextUtils.isEmpty(mail)){
                Toast.makeText(this, "Enter Correct Email", Toast.LENGTH_SHORT).show();

                return;
            }
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(this, "Enter Correct Password ", Toast.LENGTH_SHORT).show();

                return;
            }
            progressDialog.setMessage("Signing In Please Wait...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()){
                                Intent i1 = new Intent(Login.this,UserProfile.class);
                                finish();
                                startActivity(i1);
                            }
                        }
                    });


        }

        if(v == mregister){
            Intent i2 = new Intent(Login.this, Register.class);
            finish();
            startActivity(i2);

        }
        if(v == forgotpwd){
            if(TextUtils.isEmpty(mail)){
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();

                return;
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }
}