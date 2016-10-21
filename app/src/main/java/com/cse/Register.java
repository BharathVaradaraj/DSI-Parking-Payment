package com.cse.dsiparkingpayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity  implements View.OnClickListener{

    EditText ContactName,Pass,cpass,Phno,email,usn;
    Button register;
    ProgressDialog progressDialog;
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersReference = mRootReference.child("users");
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        register = (Button) findViewById(R.id.register);
        ContactName = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.mail);
        Pass = (EditText) findViewById(R.id.password);
        cpass = (EditText) findViewById(R.id.cpassword);
        Phno = (EditText) findViewById(R.id.phone);
        usn = (EditText) findViewById(R.id.code);

        register.setOnClickListener(Register.this);

    }
    @Override
    public void onClick(View v) {
            String name = ContactName.getText().toString();
            String mail = email.getText().toString();
            String pas = Pass.getText().toString();
            String cpas = cpass.getText().toString();
            String phno = Phno.getText().toString();
            String ID = usn.getText().toString();
            ID = ID.toUpperCase();
        if(v == register) {

            if (TextUtils.isEmpty(mail) && TextUtils.isEmpty(name) && TextUtils.isEmpty(phno)) {
                Toast.makeText(this, "Please enter all the fields...", Toast.LENGTH_SHORT).show();

                return;
            }
            if (pas.equals(cpas)) {

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(mail, cpas)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Register.this, "Registered Sucessfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Register.this, "Error Please Try Again Later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    String amt = "0";
                    String rf = "Not Registered";
                    writeNewUser(userId, name, mail, phno,ID,rf,amt);
                    Intent x = new Intent(Register.this, UserProfile.class);
                    finish();
                    startActivity(x);
                }

            }
            else{
                Toast.makeText(this, "Incorrect Confirm Password", Toast.LENGTH_SHORT).show();

            }

        }

    }


    @IgnoreExtraProperties
        public class Users {

            public String name;
            public String email;
            public String phone;
            public String ID;
            public String RFID;
            public String Amount;


            public Users() {
                // Default constructor required for calls to DataSnapshot.getValue(User.class)
            }

            public Users(String name, String email,String phone,String ID,String RFID,String Amount) {
                this.name = name;
                this.email = email;
                this.phone = phone;
                this.ID = ID;
                this.RFID = RFID;
                this.Amount = Amount;
            }

    }
    private void writeNewUser(String userId,String name, String mail, String phone,String ID,String RFID,String Amount){
        Users user = new Users(name ,mail,phone,ID,RFID,Amount);
        mUsersReference.child(userId).setValue(user);
    }
}
