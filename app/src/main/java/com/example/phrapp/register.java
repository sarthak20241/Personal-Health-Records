package com.example.phrapp;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    DatabaseReference ref;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(register.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

        // configure google sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageView signupbutton = (ImageView) findViewById(R.id.signup_button);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout name = findViewById(R.id.register_name);
                String nameEntered = name.getEditText().getText().toString();

                TextInputLayout mobile=  findViewById(R.id.register_mobile);
                String mobileEntered = mobile.getEditText().getText().toString();

                TextInputLayout email=  findViewById(R.id.register_email);
                String emailEntered = email.getEditText().getText().toString();

                TextInputLayout password=  findViewById(R.id.register_password);
                String passwordEntered = password.getEditText().getText().toString();

                System.out.println("Entered Details " +nameEntered +mobileEntered +emailEntered + passwordEntered);
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(emailEntered,passwordEntered)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        userdetails newuser = new userdetails(nameEntered,mobileEntered,emailEntered);
                                        //System.out.println(nameEntered+emailEntered+mobileEntered);
                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(register.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    Toast.makeText(register.this,"Failed to Register! DO IT AGAIN.",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }

                        );


                ////


                //alluserslist.add(newuser);

                Toast.makeText(register.this, "User Registered Successful !!!", Toast.LENGTH_SHORT).show();

                Intent myIntent;
                myIntent = new Intent(view.getContext(), login2.class);
                startActivity(myIntent);
            }
        });
        TextView signin = (TextView) findViewById(R.id.alreadyacc);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent;
                myIntent = new Intent(view.getContext(), login2.class);
                startActivity(myIntent);
            }
        });

        LinearLayout linearLayoutgoogle = findViewById(R.id.linearLayoutgoogle);
        linearLayoutgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    int RC_SIGN_IN = 65;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthwithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthwithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "signInWithCredetial:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    userdetails users=new userdetails();

                    assert user != null;
                    users.setEmail(user.getEmail());
                    users.setName(user.getDisplayName());
                    if(user.getPhoneNumber()!=null){
                        users.setMobile(user.getPhoneNumber());
                    }else{
                        users.setMobile("0000000000");
                    }

                    //users.setMobile((user.getPhoneNumber()).toString());
                    database.getReference().child("Users").child(user.getUid()).setValue(users);

                    Intent intent = new Intent(register.this, dashboard.class);
                    startActivity(intent);
                    Toast.makeText(register.this, "Sign in with Google", Toast.LENGTH_SHORT).show();

                } else {
                    Log.w("TAG", "signInWithCredetial:failure", task.getException());
                    Toast.makeText(register.this, "signInWithCredetial:failure", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
