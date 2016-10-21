package com.cse.dsiparkingpayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    String TAG = "test";
    Button logout;
    TextView profile, name, code, mob, amount, rfid;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressDialog = new ProgressDialog(this);

        name = (TextView) findViewById(R.id.usr);
        mob = (TextView) findViewById(R.id.mob);
        code = (TextView) findViewById(R.id.code);
        rfid = (TextView) findViewById(R.id.rfid);
        amount =(TextView) findViewById(R.id.amount);
        profile = (TextView) findViewById(R.id.profileView);
        logout = (Button) findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

        }

    }

    public void click4(View view) {
        progressDialog.setMessage("Signing Out...");
        progressDialog.show();
        firebaseAuth.signOut();
        progressDialog.dismiss();

        Intent i4 = new Intent(UserProfile.this, Login.class);
        finish();
        startActivity(i4);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference mUserReference = mRootReference.child("users");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            final String userId = user.getUid();
              mUserReference.child(userId).addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      Log.d("TAG",dataSnapshot.getValue().toString());
                      HashMap<String,String> hm = (HashMap<String,String>) dataSnapshot.getValue();
                      String hname = hm.get("name");
                      String hphone = hm.get("phone");
                      String hcode = hm.get("ID");
                      String hrfid = hm.get("RFID");
                      String hamount = hm.get("Amount");
                      //HashMap<String,Integer> map = (HashMap<String, Integer>) dataSnapshot.getValue();
                      //Integer amt = map.get("Amount");
                      //double amt = dataSnapshot.getValue(double.class);
                      //String hamount = Integer.toString(amt);
                      profile.setText("Welcome " + hname);
                      name.setText(hname);
                      mob.setText(hphone);
                      code.setText(hcode);
                      rfid.setText(hrfid);
                      amount.setText("Rs." + hamount);
                      Log.d("TAG", "onDataChange() called with: " + "dataSnapshot = [" + dataSnapshot + "] "+hphone);
                      Log.d("TAG", "onDataChange() called with: " + "dataSnapshot = [" + dataSnapshot + "] "+hname);
                      //Log.d("TAG", "onDataChange() called with: " + "dataSnapshot = [" + dataSnapshot + "] "+hamount);

                  }
                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
        }
    }

}