
package com.example.phrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class login2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(login2.this);
        progressDialog.setTitle("Login into Account");
        progressDialog.setMessage("Feteching your Account");

        // configure google sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageView signinbutton = (ImageView) findViewById(R.id.signinbutton);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check for login
                TextInputLayout email= (TextInputLayout)findViewById(R.id.emailLayout) ;
                String emailEntered = email.getEditText().getText().toString();

                TextInputLayout password= (TextInputLayout)findViewById(R.id.passLayout) ;
                String passwordEntered = password.getEditText().getText().toString();

                progressDialog.show();

                mAuth.signInWithEmailAndPassword(emailEntered,passwordEntered).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login2.this,"Logged in Successfully!",Toast.LENGTH_LONG).show();
                            String user  = FirebaseAuth.getInstance().getUid();
                            System.out.println("User data : "+user);
                            if (user!=null) {
                                Toast.makeText(login2.this, user, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(login2.this, "NO USER DATA", Toast.LENGTH_SHORT).show();
                            }


                            ref =  database.getReference("Users/"+FirebaseAuth.getInstance().getUid());
                            DatabaseReference usersRef = ref.child("hobbies details");
//                            Map<String, String> users = new HashMap<>();
//                            users.put("hobbie 1", "football");
//                            users.put("hobbie 2", "TT");
//                            users.put("hobbie 3", "badminton");
//                            users.put("hobbie 4", "tennis");
//                            usersRef.setValue(users);

                            Intent myIntent;
                            myIntent = new Intent(view.getContext(),option_login.class);
                            startActivity(myIntent);




                        }
                        else{
                            Toast.makeText(login2.this,"Failed to Login! DO IT AGAIN.",Toast.LENGTH_LONG).show();
                        }
                    }
            });
            }
        });

        TextView signup = (TextView) findViewById(R.id.signuplink1);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent;
                myIntent = new Intent(view.getContext(),register.class);
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


                    Intent intent = new Intent(login2.this, option_login.class);
                    startActivity(intent);
                    Toast.makeText(login2.this, "Sign in with Google", Toast.LENGTH_SHORT).show();

                } else {
                    Log.w("TAG", "signInWithCredetial:failure", task.getException());

                }
            }
        });

    }




}

