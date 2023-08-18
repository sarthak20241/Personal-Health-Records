package com.example.phrapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.mikephil.charting.charts.BarChart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VitalsGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VitalsGraph extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private HeartRateAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public VitalsGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VitalsGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static VitalsGraph newInstance(String param1, String param2) {
        VitalsGraph fragment = new VitalsGraph();
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
    int max_chart_yaxis= 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<Entry> entries = new ArrayList<>();
        ArrayList<String> dateLabels= new ArrayList<>();

        List<Map<String, String>> heartRateData= new ArrayList<>();
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        String vital_type = arguments.getString("Vital");
        View view= inflater.inflate(R.layout.fragment_vitals_graph, container, false);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Vitals");
        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        LineChart chart = view.findViewById(R.id.bar_chart);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");


 max_chart_yaxis=0;
        // Assuming you have a Firebase database reference called "databaseRef"
        if(vital_type.equals("heartrate")){
            Log.i("Data Retrived:","inside_loop");

            databaseRef.child(userId).child("HeartRate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int c=1;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String dateTimeString = childSnapshot.getKey();
                        int heartRate = childSnapshot.getValue(Integer.class);
                        Log.i("Data Retrived:",dateTimeString+" "+heartRate+" "+c);
                        Map<String, String> data = new HashMap();
                        data.put("date",dateTimeString);
                        if (max_chart_yaxis< heartRate){
                            max_chart_yaxis=heartRate;
                        }
                        data.put("heart_rate",Integer.toString(heartRate));
                        entries.add(new Entry(c, heartRate));
                        if(c==1){
                            dateLabels.add(dateTimeString);
                        }
                        dateLabels.add(dateTimeString);
                        c++;

                        heartRateData.add(data);

                    }

                    mRecyclerView = view.findViewById(R.id.recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mAdapter = new HeartRateAdapter(heartRateData);
                    mRecyclerView.setAdapter(mAdapter);

                    // Prepare X-axis
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setLabelRotationAngle(45f);



                    xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));

// Prepare Y-axis
                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(max_chart_yaxis+50);

                    LineDataSet dataSet = new LineDataSet(entries, "Heart rate");
                    dataSet.setColor(Color.RED);
                    dataSet.setValueTextSize(10f);
                    dataSet.setValueTextColor(Color.BLACK);

                    LineData lineData = new LineData(dataSet);

                    chart.setPinchZoom(true);
                    chart.setExtraOffsets(10, 10, 10, 10);
                    chart.setData(lineData);
                    chart.getDescription().setEnabled(false);
                    chart.invalidate();




                }




                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", "Failed to read heart rate data from Firebase.", error.toException());
                }
            });






        }

        max_chart_yaxis=0;

        if(vital_type.equals("sugar")){
            Log.i("Data Retrived:","inside_loop");

            databaseRef.child(userId).child("Sugar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int c=1;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String dateTimeString = childSnapshot.getKey();
                        int heartRate = childSnapshot.getValue(Integer.class);
                        Log.i("Data Retrived:",dateTimeString+" "+heartRate+" "+c);
                        Map<String, String> data = new HashMap();

                        if (max_chart_yaxis< heartRate){
                            max_chart_yaxis=heartRate;
                        }
                        data.put("date",dateTimeString);
                        data.put("heart_rate",Integer.toString(heartRate));
                        entries.add(new Entry(c, heartRate));
                        if(c==1){
                            dateLabels.add(dateTimeString);
                        }
                        dateLabels.add(dateTimeString);
                        c++;

                        heartRateData.add(data);

                    }

                    mRecyclerView = view.findViewById(R.id.recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mAdapter = new HeartRateAdapter(heartRateData);
                    mRecyclerView.setAdapter(mAdapter);

                    // Prepare X-axis
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setLabelRotationAngle(45f);



                    xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));

// Prepare Y-axis
                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(max_chart_yaxis);

                    LineDataSet dataSet = new LineDataSet(entries, "Sugar");
                    dataSet.setColor(Color.RED);
                    dataSet.setValueTextSize(10f);
                    dataSet.setValueTextColor(Color.BLACK);

                    LineData lineData = new LineData(dataSet);

                    chart.setPinchZoom(true);
                    chart.setExtraOffsets(10, 10, 10, 10);
                    chart.setData(lineData);
                    chart.getDescription().setEnabled(false);
                    chart.invalidate();




                }




                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", "Failed to read heart rate data from Firebase.", error.toException());
                }
            });






        }



max_chart_yaxis=0;
        if(vital_type.equals("steps")) {
            Log.i("Data Retrived:", "inside_loop");

            databaseRef.child(userId).child("Steps").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int c = 1;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String dateTimeString = childSnapshot.getKey();
                        int heartRate = childSnapshot.getValue(Integer.class);
                        Log.i("Data Retrived:", dateTimeString + " " + heartRate + " " + c);
                        Map<String, String> data = new HashMap();
                        data.put("date", dateTimeString);
                        if (max_chart_yaxis< heartRate){
                            max_chart_yaxis=heartRate;
                        }
                        data.put("heart_rate", Integer.toString(heartRate));
                        entries.add(new Entry(c, heartRate));
                        if (c == 1) {
                            dateLabels.add(dateTimeString);
                        }
                        dateLabels.add(dateTimeString);
                        c++;

                        heartRateData.add(data);

                    }

                    mRecyclerView = view.findViewById(R.id.recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mAdapter = new HeartRateAdapter(heartRateData);
                    mRecyclerView.setAdapter(mAdapter);

                    // Prepare X-axis
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setLabelRotationAngle(45f);


                    xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));

// Prepare Y-axis
                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setAxisMinimum(0f);
                    yAxis.setAxisMaximum(max_chart_yaxis+100);

                    LineDataSet dataSet = new LineDataSet(entries, "Steps");
                    dataSet.setColor(Color.RED);
                    dataSet.setValueTextSize(10f);
                    dataSet.setValueTextColor(Color.BLACK);

                    LineData lineData = new LineData(dataSet);

                    chart.setPinchZoom(true);
                    chart.setExtraOffsets(10, 10, 10, 10);
                    chart.setData(lineData);
                    chart.getDescription().setEnabled(false);
                    chart.invalidate();


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", "Failed to read heart rate data from Firebase.", error.toException());
                }
            });
        }

   max_chart_yaxis=0;

            if(vital_type.equals("calories")){
                Log.i("Data Retrived:","inside_loop");

                databaseRef.child(userId).child("Calories").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int c=1;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String dateTimeString = childSnapshot.getKey();
                            int heartRate = childSnapshot.getValue(Integer.class);
                            Log.i("Data Retrived:",dateTimeString+" "+heartRate+" "+c);
                            Map<String, String> data = new HashMap();
                            if (max_chart_yaxis< heartRate){
                                max_chart_yaxis=heartRate;
                            }
                            data.put("date",dateTimeString);
                            data.put("heart_rate",Integer.toString(heartRate));
                            entries.add(new Entry(c, heartRate));
                            if(c==1){
                                dateLabels.add(dateTimeString);
                            }
                            dateLabels.add(dateTimeString);
                            c++;

                            heartRateData.add(data);

                        }

                        mRecyclerView = view.findViewById(R.id.recycler_view);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mAdapter = new HeartRateAdapter(heartRateData);
                        mRecyclerView.setAdapter(mAdapter);

                        // Prepare X-axis
                        XAxis xAxis = chart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setLabelRotationAngle(45f);



                        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));

// Prepare Y-axis
                        YAxis yAxis = chart.getAxisLeft();
                        yAxis.setAxisMinimum(0f);
                        yAxis.setAxisMaximum(max_chart_yaxis+100);

                        LineDataSet dataSet = new LineDataSet(entries, "Calories");
                        dataSet.setColor(Color.RED);
                        dataSet.setValueTextSize(10f);
                        dataSet.setValueTextColor(Color.BLACK);

                        LineData lineData = new LineData(dataSet);

                        chart.setPinchZoom(true);
                        chart.setExtraOffsets(10, 10, 10, 10);
                        chart.setData(lineData);
                        chart.getDescription().setEnabled(false);
                        chart.invalidate();




                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", "Failed to read heart rate data from Firebase.", error.toException());
                    }
                });






            }









        return  view;
    }
}