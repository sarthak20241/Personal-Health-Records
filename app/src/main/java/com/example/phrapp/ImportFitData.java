package com.example.phrapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImportFitData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImportFitData extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImportFitData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImportFitData.
     */
    // TODO: Rename and change types and number of parameters
    public static ImportFitData newInstance(String param1, String param2) {
        ImportFitData fragment = new ImportFitData();
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

    private GoogleSignInOptions googleSignInOptions;
    private FitnessOptions fitnessOptions;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_import_fit_data, container, false);
        // Inflate the layout for this fragment
        TextView vitals = view.findViewById(R.id.Vitals);
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_HEART_RATE_SUMMARY, FitnessOptions.ACCESS_READ)
                .build();

// Get a GoogleSignInClient object
        googleSignInClient = GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

// Check if the user is signed in
        if (GoogleSignIn.getLastSignedInAccount(getContext()) == null) {
            // Launch the sign-in flow
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            // Check if the user has granted the required fitness permissions
            if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions)) {
                // Launch the permissions dialog
                GoogleSignIn.requestPermissions(
                        this,
                        100,
                        GoogleSignIn.getLastSignedInAccount(getContext()),
                        fitnessOptions);

            } else {
                long endTime = System.currentTimeMillis();
                long startTime = endTime - TimeUnit.DAYS.toMillis(14);

                DataReadRequest readRequest = new DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .build();

                Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(getContext()))
                        .readData(readRequest)
                        .addOnSuccessListener(dataReadResponse -> {
                            // Get the read data
                            List<Bucket> buckets = dataReadResponse.getBuckets();
                            for (Bucket bucket : buckets) {
                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                        .format(bucket.getStartTime(TimeUnit.MILLISECONDS));
                                String text = "Date: " + date + "\n";
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
                                    String type = dataSet.getDataType().getName();
                                    for (DataPoint dataPoint : dataSet.getDataPoints()) {
                                        String value = String.valueOf(dataPoint.getValue(dataPoint.getDataType().getFields().get(0)));
                                        text += type + ": " + value + "\n";
                                        if(type.contains("step_count")){
                                            FirebaseDatabase.getInstance().getReference("Vitals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Steps").child(date).setValue(Integer.parseInt(value.replace("\"", ""))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.i("Vitals","Added to Database");
                                                    }
                                                    else{
                                                        Log.i("Vitals","Not Added to Database");
                                                    }
                                                }
                                            });


                                        }
                                        else if(type.contains("calories")){
                                            FirebaseDatabase.getInstance().getReference("Vitals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Calories").child(date).setValue((int)Double.parseDouble(value.replace("\"", ""))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.i("Vitals","Added to Database");
                                                    }
                                                    else{
                                                        Log.i("Vitals","Not Added to Database");
                                                    }
                                                }
                                            });


                                        }



                                    }
                                }
                                Log.d("TAG", text);
                                // Display text in a text view
                                vitals.append(text + "\n");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("TAG", "There was a problem getting the data.", e);
                        })
                 .addOnCompleteListener(task -> {
                     progressBar.setVisibility(View.GONE);
                     Toast.makeText(getContext(), "Data Imported", Toast.LENGTH_SHORT).show();

                });

            }
        }

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("980102181758-g4fjk2qk74r03octrnc7avnmivrcs2rm.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);


            return view;
        }
    }

