package com.example.phrapp;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineAnalysisFragment extends Fragment {

    View view;

    private DatabaseReference mDatabase;

    public  List<Document> Fetched_documentList;
    String date_from;
    String date_to;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static HashMap<String,Integer> DoctorIndex;
    private static HashMap<String,Integer>  Doctors;
    private static HashMap<String,Integer> MonthCntr=new HashMap<>();
    private static HashMap<String,Integer> MonthIndex=new HashMap<>();
    private static RecyclerView recyclerView;
    private static NER_Adapter adapter;
    private static String[] nerResults=null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimelineAnalysisFragment(String date_from,String date_to) {
        this.date_from=date_from;
        this.date_to=date_to;

    }
    public TimelineAnalysisFragment(){
        // Required empty public constructor
    }

    public static TimelineAnalysisFragment newInstance(String param1, String param2) {
        TimelineAnalysisFragment fragment = new TimelineAnalysisFragment();
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

        View view = inflater.inflate(R.layout.fragment_timeline_analysis, container, false);
        Bundle data = getArguments();   //contains documents


        ArrayList<Document> arr = null;
        if (data != null) {
            //fetching document list in the period entered by user
            arr = (ArrayList<Document>) data.getSerializable("documentList");



            //Plotting graph for number of visits to every doctor in the given period
            Doctors = new HashMap<>();
            DoctorIndex = new HashMap<>();
            ArrayList<BarEntry> DoctorsVisit = new ArrayList<>();
            int n = arr.size();
            int index = 0;
            int max2 = 1;
            for (int i = 0; i < n; i++) {
                Document d = new Document();
                d = arr.get(i);
                //ArrayList<String,Integer> DoctorsVisited = new ArrayList<String,Integer>();


                int temp = 1;

                if (Doctors.containsKey(d.getDoctorName())) {
                    temp = Doctors.get(d.getDoctorName());

                    Doctors.put(d.getDoctorName(), ++temp);
                    if (temp > max2) {
                        max2 = temp;
                    }
                } else {
                    Doctors.put(d.getDoctorName(), 1);
                    DoctorIndex.put(d.getDoctorName(), index + 1);
                    index++;
                }

            }
            for (Map.Entry m : Doctors.entrySet()) {
                String s = (String) m.getKey();
                int v = (int) m.getValue();
                index = DoctorIndex.get(s);
                DoctorsVisit.add(new BarEntry(index, v));
                System.out.println(m.getKey() + " " + m.getValue());
            }

            BarChart DoctorCount = view.findViewById(R.id.doctorsVisited);
            DoctorCount.setTouchEnabled(true);
            DoctorCount.setPinchZoom(true);
            BarDataSet barDataSet = new BarDataSet(DoctorsVisit, "Doctors");
            barDataSet.setColors(R.color.buttontext);
            barDataSet.setValueTextColor(Color.BLACK);

            BarData barData = new BarData(barDataSet);
            DoctorCount.setData(barData);
            DoctorCount.getDescription().setText("Doctors Visited");
            DoctorCount.animateY(2000);
            XAxis xAxis = DoctorCount.getXAxis();
            xAxis.setValueFormatter(new DoctorFormatter());
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis yAxis = DoctorCount.getAxisLeft();
            xAxis.setLabelRotationAngle(315f);

            // Set the granularity to 1, which means the labels will be displayed at intervals of 1
            yAxis.setGranularity(1f);
            yAxis.setAxisMaximum((float) max2 + 2);
            yAxis.setAxisMinimum(0f);

            xAxis.setDrawGridLines(false);


            yAxis.setDrawGridLines(false);
            // Set a custom value formatter to display the labels as whole numbers
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            YAxis rightYAxis = DoctorCount.getAxisRight();

            // Set the visibility of the right Y-axis to false
            rightYAxis.setEnabled(false);

            //DoctorCount.setBackgroundResource(R.drawable.graphbg);


            // Plotting a line graph for number of monthly checkups in the given period
            Document d2 = new Document();
            d2 = arr.get(0);
            String date2 = d2.getDate();
            String[] rdate = getDate(date2);
            index = 0;
            int max = 1;
            for (int i = 0; i < n; i++) {
                Document d = new Document();
                d = arr.get(i);

                String date = d.getDate();
                String[] Edate = getDate(date);   //elements of date
                String month = Edate[1];
                String year = Edate[2];

                int temp = 1;

                if (MonthCntr.containsKey(month + "/" + year)) {
                    temp = MonthCntr.get(month + "/" + year);

                    MonthCntr.put(month + "/" + year, ++temp);
                    Log.d(month + "/" + year, temp + " ");
                    if (temp > max) {
                        max = temp;
                    }
                } else {
                    MonthCntr.put(month + "/" + year, 1);
                    MonthIndex.put(month + "/" + year, index + 1);
                    Log.d(month + "/" + year, temp + " ");
                    index++;
                }


            }

            ArrayList<Entry> MonthlyCheckups = new ArrayList<>();


            for (Map.Entry m : MonthCntr.entrySet()) {
                String s = (String) m.getKey();
                int v = (int) m.getValue();
                index = MonthIndex.get(s);

                MonthlyCheckups.add(new Entry(index, v));

            }
            ArrayList<Entry> UpdatedMonthlyCheckups = new ArrayList<>();
            for (int i = MonthlyCheckups.size() - 1; i >= 0; i--) {

                // Append the elements in reverse order
                UpdatedMonthlyCheckups.add(MonthlyCheckups.get(i));
            }
            MonthlyCheckups = UpdatedMonthlyCheckups;
            LineChart monthlyChks = view.findViewById(R.id.monthlyCheckup);

            monthlyChks.setTouchEnabled(true);
            monthlyChks.setPinchZoom(true);

            XAxis M_Xaxis = monthlyChks.getXAxis();
            YAxis M_YleftAxis = monthlyChks.getAxisLeft();

            YAxis M_rightYAxis = monthlyChks.getAxisRight();
            M_rightYAxis.setEnabled(false);

            XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
            M_Xaxis.setPosition(position);
            M_Xaxis.setValueFormatter(new ClaimsXAxisValueFormatter());
            M_Xaxis.setGranularityEnabled(true);
            M_Xaxis.setGranularity(1f);
            M_Xaxis.setLabelRotationAngle(315f);
            M_Xaxis.setAxisMinimum(0f);

            float len = (float) MonthCntr.size();
            M_Xaxis.setAxisMaximum(len + 1);
            M_YleftAxis.setGranularityEnabled(true);
            M_YleftAxis.setGranularity(1f);
            M_YleftAxis.setAxisMinimum(0f);
            M_YleftAxis.setAxisMaximum((float) max + 3);
            monthlyChks.getDescription().setEnabled(true);
            Description description = new Description();

            description.setText("Monthly Checkups");
            description.setTextSize(15f);
            monthlyChks.setDescription(description);

            LineDataSet set1;
            set1 = new LineDataSet(MonthlyCheckups, "Number of Checkups");
            set1.setDrawCircles(true);
            set1.enableDashedLine(10f, 0f, 0f);
            set1.enableDashedHighlightLine(10f, 0f, 0f);


            set1.setColor(getResources().getColor(R.color.buttontext));
            set1.setCircleColor(getResources().getColor(R.color.buttontext));
            set1.setLineWidth(2f);//line size
            set1.setCircleRadius(5f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(10f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(5f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);

            set1.setDrawValues(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData monthlyData = new LineData(dataSets);

            monthlyChks.setData(monthlyData);
            monthlyChks.invalidate();



        }


        Log.d("dateTimeline",date_from+" "+date_to);



        LineChart heartRateChart = view.findViewById(R.id.heartRateAnalysis);
        plotVitals(heartRateChart,"HeartRate",  "Heart Rate", "Heart Rate Chart");

        LineChart sugarChart = view.findViewById(R.id.sugarAnalysis);
        plotVitals(sugarChart,"Sugar",  "Sugar", "Sugar Levels");

        LineChart stepsChart = view.findViewById(R.id.stepsAnalysis);
        plotVitals(stepsChart,"Steps",  "Steps", "Steps Chart");

        LineChart calorieChart = view.findViewById(R.id.calorieAnalysis);
        plotVitals(calorieChart,"Calories",  "Calories", "Calories Chart");

        //Table of NER data
        recyclerView=view.findViewById(R.id.recyclerview_ner);
        setRecyclerView();
        return view;
    }
    private void setRecyclerView() {

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        String NER_data=UploadImageFragment.getNERres();
        readJsonString(NER_data);
        adapter=new NER_Adapter(readJsonString(NER_data));
        //adapter=new NER_Adapter(readJSON("{\"timestamp\":\"17.04.2023\",\"report\": \"['cholesterol, total 200.00 mg/dl <200.00','triglycerides 201.00 mg/dl <150.00', 'hdl cholesterol 61.00 mg/dl >50.00', 'ldl cholesterol, calculated 98.80 mg/dl <100.00', 'vldl cholesterol,calculated 40.20 mg/dl <30.00',]\"} "));
        recyclerView.setAdapter(adapter);
    }
    private List<NER_model> getNERList(){
        List<NER_model> NERlist= new ArrayList<>();
        String NERdata=UploadImageFragment.getNERres();
        Log.d("NER",NERdata);
//        NERlist.add(new NER_model("scan","good"));
//        NERlist.add(new NER_model("ct scan","bad"));
//        NERlist.add(new NER_model("ct scan","bad"));
//        NERlist.add(new NER_model("scan","good"));
        return NERlist;
    }
    public List<NER_model> readJSON(String jsonString){
        List<NER_model> NERlist= new ArrayList<>();
        try {

            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(jsonString);

            while (!parser.isClosed()) {
                JsonToken jsonToken = parser.nextToken();

                if (jsonToken == null)
                    break;

                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken();

                    System.out.println(fieldName + ": " + parser.getText());
                    //NERlist.add(new NER_model(fieldName,parser.getText()));

                }
            }

            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NERlist;
    }

    public static List<NER_model> readJsonString(String jsonString) {
        List<NER_model> NERlist= new ArrayList<>();
        try {
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(jsonString);

            while (!parser.isClosed()) {
                JsonToken jsonToken = parser.nextToken();

                if (jsonToken == null)
                    break;

                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken();
                    int cntr=1;
                    if (JsonToken.START_ARRAY.equals(parser.currentToken())) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            if (JsonToken.START_OBJECT.equals(parser.currentToken())) {
                                while (parser.nextToken() != JsonToken.END_OBJECT) {
                                    String subtype = parser.getCurrentName();
                                    parser.nextToken();
                                    String result = parser.getValueAsString();
                                    System.out.println(fieldName + " > " + subtype + " = " + result);
                                    if(cntr==1){
                                        NERlist.add(new NER_model(fieldName,subtype,result));
                                        cntr++;
                                    }
                                    else{
                                        NERlist.add(new NER_model("",subtype,result));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NERlist;
    }
    public static String getMonth(String date){
        String month="";
        for(int i=0;i<date.length();i++){
            if(date.charAt(i)=='/'){
                month=date.substring(0,i);
                break;
            }
        }
        switch(month){
            case "1": return "Jan";
            case "2": return "Feb";
            case "3": return "Mar";
            case "4": return "Apr";
            case "5": return "May";
            case "6": return "Jun";
            case "7": return "Jul";
            case "8": return "Aug";
            case "9": return "Sep";
            case "10": return "Oct";
            case "11": return "Nov";
            case "12": return "Dec";

            default: return "";
        }
    }

    public static String getYear(String date){
        String Year="";
        for(int i=0;i<date.length();i++){
            if(date.charAt(i)=='/'){
                Year=date.substring(i+1);
                break;
            }
        }
        return Year;
    }

    public String[] getDate(String date){
        int len=date.length();
        String day="";
        String month="";
        String year="";
        int flag=0;
        int str=0;  //start index
        for(int i=0;i<len;i++){
            if(date.charAt(i)=='/'){
                flag++;

                if(flag==1){

                    day=day.concat(date.substring(str,i));
                    str=i+1;
                }
                if(flag==2){
                    month=month.concat(date.substring(str,i));
                    year=year.concat(date.substring(i+1));
                }


            }
        }


        String[] rdate={day,month,year};
        return rdate;
    }
    private static class DoctorFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value){
            int len= Doctors.size();

            for(Map.Entry m : DoctorIndex.entrySet()){
                int ind=(Integer)m.getValue();
                if(ind==value){
                    return (String)m.getKey();
                }

            }

            return "";
        }
    }

    public static class ClaimsXAxisValueFormatter extends ValueFormatter {



        @Override
        public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/

            int len= MonthCntr.size();

            for(Map.Entry m : MonthIndex.entrySet()){
                int ind=(Integer)m.getValue();
                if(ind==value){

                    String date= (String)m.getKey();
                    String month=getMonth(date);
                    String year = getYear(date);
                    return month+" "+year;

                }

            }

            return "";


        }
    }


    //returns the string uptill the occurence of first space element
    public String getDateForVitals(String date){
        String dateStr="";
        int pos=date.length();
        for(int i=0;i<date.length();i++){
            if(date.charAt(i)==' '){
                pos=i;
                break;
            }
        }
        dateStr=date.substring(0,pos);
        return dateStr;
    }

    public void plotVitals(LineChart lineChart,String vital,  String EntryLabel, String description){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String loggedInUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HashMap<String, Integer> vitalData = new HashMap<>();
        //counts how many readings for each day
        HashMap<String,Integer> dayCounter=new HashMap<>();


        FirebaseDatabase.getInstance().getReference("Vitals").child(loggedInUserId).child(vital).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey(); // This is the key (timestamp) of the data point
                    int value = snapshot.getValue(Integer.class); // This is the value (heart rate) of the data point
                    key=getDateForVitals(key);
                    if(!compareVitalDateToTimeLine(key)){
                        continue;
                    }
                    //heartRateData.put(key, value);
                    int temp=0;
                    int vitalSum=0;

                    if (vitalData.containsKey(key)) {
                        vitalSum = vitalData.get(key);
                        temp=(int)dayCounter.get(key);
                        vitalData.put(key,value+vitalSum);
                        dayCounter.put(key, ++temp);

                    } else {
                        vitalData.put(key, value);
                        dayCounter.put(key, 1);


                    }

                }
                ArrayList<Entry> vitalEntries = new ArrayList<Entry>();
                int index=0;
                float maxVital=0;
                ArrayList<String> Dates=new ArrayList<String>();
                for (Map.Entry m : vitalData.entrySet()) {
                    String key = (String) m.getKey();
                    int val = (int) m.getValue();
                    int readings = (int)dayCounter.get(key);
                    float avgVital=val/readings;
                    vitalData.put(key,(int)avgVital);
                    Dates.add(key);
                    if(avgVital>maxVital){
                        maxVital=avgVital;
                    }
                    vitalEntries.add(new Entry(index+1, avgVital));

                    index++;

                }


                plotAnalysisLineChart(lineChart, vitalEntries, EntryLabel, description, Dates);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    //This is a generalized function to plot line chart for timeline analysis
    //You need to pass a line chart that is already attached to a view in the fragment
    //Entries is the (x,y) data set pair
    //XLabels are the labels you want on the X axis
    //EntryLabel is the description of entry
    //description is the description of the line chart
    //on passing all the required arguments in the correct order this function then plots a line chart
    public void plotAnalysisLineChart(LineChart lineChart, ArrayList<Entry> Entries, String EntryLabel, String description, ArrayList<String> XLabels){
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        XAxis M_Xaxis = lineChart.getXAxis();
        YAxis M_YleftAxis = lineChart.getAxisLeft();

        YAxis M_rightYAxis = lineChart.getAxisRight();
        M_rightYAxis.setEnabled(false);

        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        M_Xaxis.setPosition(position);
        M_Xaxis.setValueFormatter(new XaxisLabelsFormatter(XLabels));
        M_Xaxis.setGranularityEnabled(true);
        M_Xaxis.setGranularity(1f);
        M_Xaxis.setLabelRotationAngle(315f);
        M_Xaxis.setAxisMinimum(0f);

        float len = (float) XLabels.size();
        M_Xaxis.setAxisMaximum(len + 1);
        M_YleftAxis.setGranularityEnabled(true);
        M_YleftAxis.setGranularity(5f);
        M_YleftAxis.setAxisMinimum(0f);
        float maxY=getMaxEntry(Entries);
        M_YleftAxis.setAxisMaximum((float) maxY+40);
        lineChart.getDescription().setEnabled(true);
        Description des = new Description();
        des.setText(description);
        des.setTextSize(15f);
        lineChart.setDescription(des);


        LineDataSet set1;
        set1 = new LineDataSet(Entries, EntryLabel);
        set1.setDrawCircles(true);
        set1.enableDashedLine(10f, 0f, 0f);
        set1.enableDashedHighlightLine(10f, 0f, 0f);


        set1.setColor(getResources().getColor(R.color.buttontext));
        set1.setCircleColor(getResources().getColor(R.color.buttontext));
        set1.setLineWidth(2f);//line size
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(10f);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(5f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(5.f);

        set1.setDrawValues(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData Data = new LineData(dataSets);

        lineChart.setData(Data);
        lineChart.invalidate();
    }

    private static class XaxisLabelsFormatter extends ValueFormatter {
        ArrayList<String> XLabels;
        XaxisLabelsFormatter(ArrayList<String> XLabels){
            this.XLabels=XLabels;
        }
        @Override
        public String getFormattedValue(float value){
            if(value-1<XLabels.size() && value>0){
                return(XLabels.get((int)(value-1)));
            }


            return "";
        }
    }

    public float getMaxEntry(ArrayList<Entry> Entries){
        Entry e;
        float max=0;
        for(int i=0;i<Entries.size();i++){
            e=Entries.get(i);
            float y=e.getY();
            if(y>max){
                max=y;
            }
        }
        return max;
    }

    public boolean compareVitalDateToTimeLine(String VitalDate){
        String[] date_from_e=getDate(date_from);
        String[] date_to_e=getDate(date_to);
        String[] vital_e=getVitalDateElements(VitalDate);
        int year1=Integer.parseInt(date_from_e[2]);
        int year2=Integer.parseInt(date_to_e[2]);

        int vyear=Integer.parseInt(vital_e[2]);
        int month1=Integer.parseInt(date_from_e[1]);
        int month2=Integer.parseInt(date_to_e[1]);
        int vmonth=Integer.parseInt(vital_e[1]);
        int day1=Integer.parseInt(date_from_e[0]);
        int day2=Integer.parseInt(date_to_e[0]);
        int vday=Integer.parseInt(vital_e[0]);

        if(year1==year2){
            if(vyear==year1){
                if(month1==month2){
                    if(vmonth==month1){
                        if(vday>=day1 && vday<=day2){
                            return true;
                        }
                    }
                }
                else if(vmonth>=month1 && vmonth<=month2){
                    return true;
                }
            }
        }
        else if(vyear>=year1 && vyear<=year2){
            return true;
        }

        return false;
    }

    public String[] getVitalDateElements(String date){
        int len=date.length();
        String day="";
        String month="";
        String year="";
        int flag=0;
        int str=0;  //start index
        for(int i=0;i<len;i++){
            if(date.charAt(i)=='-'){
                flag++;

                if(flag==1){

                    day=day.concat(date.substring(str,i));

                    str=i+1;
                }
                if(flag==2){
                    month=month.concat(date.substring(str,i));
                    year=year.concat(date.substring(i+1));
                }


            }
        }


        String[] rdate={day,month,year};
        return rdate;
    }
}
