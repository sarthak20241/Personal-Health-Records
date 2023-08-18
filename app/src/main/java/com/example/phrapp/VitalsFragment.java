package com.example.phrapp;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.Scopes;

import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;

import java.util.Hashtable;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VitalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VitalsFragment extends Fragment {
    private FirebaseAuth mAuth;
    private GoogleSignInOptions googleSignInOptions;
    private FitnessOptions fitnessOptions;
    Boolean heart_rate_being_added;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Inside your onCreate() method, initialize the GoogleSignInOptions and GoogleSignInClient instances


    public VitalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VitalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VitalsFragment newInstance(String param1, String param2) {
        VitalsFragment fragment = new VitalsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vitals, container, false);
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Button enterHeartRate = view.findViewById(R.id.EnterHeartRate);
        Button heartRateSubmit = view.findViewById(R.id.HeartRateSubmit);
        Button ViewChart = view.findViewById(R.id.ViewChart);
        Button stepsChart = view.findViewById(R.id.ViewChartStep);
        Button caloriesChart = view.findViewById(R.id.ViewChartCalories);
        FrameLayout bottom_layout = view.findViewById(R.id.bottom_layout);
        heart_rate_being_added=false;


        googleSignInClient = GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);


      Button import_button=view.findViewById(R.id.button3);
      import_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (GoogleSignIn.getLastSignedInAccount(getContext()) == null) {
                  // Launch the sign-in flow
                  Intent signInIntent = googleSignInClient.getSignInIntent();
                  startActivityForResult(signInIntent, RC_SIGN_IN);

              }
              else{
                  replaceFragment(new ImportFitData(),"");
              }


          }
      });






        EditText editTextHeartRate = (EditText)view.findViewById(R.id.heartRateEditText);
        LinearLayout HeartRateInputSection=(LinearLayout) view.findViewById(R.id.EnterHeartRateSection);
        enterHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             bottom_layout.setVisibility(View.VISIBLE);
             heart_rate_being_added=true;
            }
        });
        heartRateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heart_rate_being_added){
                    Log.i("Vitals","Button Pressed");
                    Dictionary<String, Integer> dict = new Hashtable<>();
                    int HeartRate= Integer.parseInt(editTextHeartRate.getText().toString());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                    // on below line we are creating a variable
                    // for current date and time and calling a simple date format in it.
                    String currentDateAndTime = sdf.format(new Date());

                    dict.put(currentDateAndTime, HeartRate);

                    FirebaseDatabase.getInstance().getReference("Vitals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HeartRate").child(currentDateAndTime).setValue(HeartRate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.i("Vitals","Added to Database");
                                bottom_layout.setVisibility(View.INVISIBLE);
                                heart_rate_being_added=false;
                                editTextHeartRate.setText("");

                            }
                            else{
                                Log.i("Vitals","Not Added to Database");
                            }
                        }
                    });

                }
                else {
                    Log.i("Vitals","Button Pressed");
                    Dictionary<String, Integer> dict = new Hashtable<>();
                    int HeartRate= Integer.parseInt(editTextHeartRate.getText().toString());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                    // on below line we are creating a variable
                    // for current date and time and calling a simple date format in it.
                    String currentDateAndTime = sdf.format(new Date());

                    dict.put(currentDateAndTime, HeartRate);

                    FirebaseDatabase.getInstance().getReference("Vitals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sugar").child(currentDateAndTime).setValue(HeartRate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.i("Vitals","Added to Database");
                                bottom_layout.setVisibility(View.INVISIBLE);
                                editTextHeartRate.setText("");
                            }
                            else{
                                Log.i("Vitals","Not Added to Database");
                            }
                        }
                    });

                }


            }
        });



        Button enterSugar = view.findViewById(R.id.EnterSugar);
        Button sugarSubmit = view.findViewById(R.id.HeartRateSubmit);
        Button viewSugar = view.findViewById(R.id.ViewChartSugar);

        EditText editTextSugar = (EditText)view.findViewById(R.id.heartRateEditText);

        enterSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_layout.setVisibility(View.VISIBLE);
                heart_rate_being_added=false;

            }
        });



        ViewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new VitalsGraph(),"heartrate");
            }
        });

        viewSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new VitalsGraph(),"sugar");
            }
        });
        stepsChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new VitalsGraph(),"steps");

            }
        });

      caloriesChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new VitalsGraph(),"calories");

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void replaceFragment(Fragment fragment, String vital){
        Bundle arguments = new Bundle();
        arguments.putString( "Vital" , vital);
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(getActivity() instanceof User_Logged_in_via_abha_HomePage){
            fragmentTransaction.replace(R.id.user_logged_in_via_abha_homepage_fragment_frame,fragment);
        }
        else{
            fragmentTransaction.replace(R.id.frameLayout,fragment);
        }

        fragmentTransaction.commit();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Log.w("TAG", "signInResult:Successful");
            // ...
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}