package com.example.phrapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import static com.example.phrapp.MainActivity.documentList;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateTimelineFragment extends Fragment {

    private View view;
    String fullDate_from;
    String fullDate_to;
    private DateTimelineAdaptor adp;

    private RecyclerView imageInfoRecyclerView;
    private DatabaseReference mDatabase;
    public List<Document> timelineDocumentsList;
    Integer date_set_flag = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button analysis;

    public DateTimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DateTimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateTimelineFragment newInstance(String param1, String param2) {
        DateTimelineFragment fragment = new DateTimelineFragment();
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
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_date_timeline, container, false);

        imageInfoRecyclerView = view.findViewById(R.id.imageInfoRecyclerViewOfTimeline);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        Date todayDate = Calendar.getInstance().getTime();
        imageInfoRecyclerView.setLayoutManager(linearLayoutManager);
        timelineDocumentsList = new ArrayList<Document>();
        adp = new DateTimelineAdaptor(timelineDocumentsList);
        System.out.println("size: "+timelineDocumentsList.size());
        imageInfoRecyclerView.setAdapter(adp);
        //analysis button

        //date picker from code:
        TextInputLayout date_from = (TextInputLayout)view.findViewById(R.id.date_from);
        ImageView dateicon_from = (ImageView) view.findViewById(R.id.dateiconimage_from);
        dateicon_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                System.out.println("inside onclick listener");
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(container.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fullDate_from=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                date_from.getEditText().setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //date picker to code
        TextInputLayout date_to = (TextInputLayout)view.findViewById(R.id.date_to);
        ImageView dateicon_to = (ImageView) view.findViewById(R.id.dateiconimage_to);
        //analysis button

        dateicon_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                System.out.println("inside onclick listener");
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog

                DatePickerDialog datePickerDialog = new DatePickerDialog(container.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fullDate_to=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                date_to.getEditText().setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                System.out.println("setting datesetflag to 1"+ date_set_flag);


                                // code to bring data:
                                System.out.println("inside date set flag");
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String loggedInUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                timelineDocumentsList = new ArrayList<Document>();
                                mDatabase.child("Documents").orderByChild("loggedInUserId").equalTo(loggedInUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            System.out.println("firebase Error getting data"+ task.getException());
                                        }
                                        else {
//                    System.out.println("Fetched docs "+ String.valueOf(task.getResult().getValue()));
                                            ArrayList<String> list = new ArrayList<>();
                                            for (DataSnapshot snapshot : task.getResult().getChildren() ) {
                                                list.add(snapshot.getValue().toString());
                                                String documentName, description, doctorName, category, reportType,diagnosis;
                                                ArrayList<String> drugs;
                                                String date;
                                                String image;
                                                String firebaseStorageUri;
                                                String loggedInUserId;
                                                long epoc;
                                                date = (String) snapshot.child("date").getValue();
                                                epoc = (long) snapshot.child("epoc").getValue();
                                                reportType = (String) snapshot.child("reportType").getValue();
                                                image = (String) snapshot.child("image").getValue();
                                                documentName = (String) snapshot.child("documentName").getValue();
                                                doctorName = (String) snapshot.child("doctorName").getValue();
                                                description = (String) snapshot.child("description").getValue();
                                                firebaseStorageUri = (String) snapshot.child("firebaseStorageUri").getValue();
                                                category = (String) snapshot.child("category").getValue();
                                                loggedInUserId = (String) snapshot.child("loggedInUser").getValue();
                                                diagnosis = (String)snapshot.child("Diagnosis").getValue();
                                                drugs  = (ArrayList<String>) snapshot.child("Drugs").getValue();

                                                String allDrugs="";
                                                if(drugs!=null){
                                                    for(int i=0;i<drugs.size()-1;i++){
                                                        allDrugs+=drugs.get(i);
                                                        allDrugs+=" , ";
                                                    }
                                                    allDrugs+=drugs.get(drugs.size()-1);
                                                }

                                                System.out.println("date from : "+date_from.getEditText().getText().toString());
                                                System.out.println("date to   : "+date_to.getEditText().getText().toString());

                                                String date_of_doc = date;
                                                //date_to, date_from


                                                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                                Date date_of_doc_format = null;
                                                Date date_to_format = null;
                                                Date date_from_format = null;
                                                try {
                                                    date_of_doc_format = format.parse(date_of_doc);
                                                    date_from_format = format.parse(date_from.getEditText().getText().toString());
                                                    date_to_format = format.parse(date_to.getEditText().getText().toString());
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                System.out.println("date & to & from date "+date_of_doc_format + date_to_format + date_from_format);

                                                if (date_of_doc_format.before(date_to_format) && date_of_doc_format.after(date_from_format)) {
                                                    System.out.println("inside inside inside");
                                                    Document new_doc = new Document(documentName, description, doctorName, category, reportType, date, image, firebaseStorageUri, loggedInUserId, epoc,allDrugs,diagnosis);
                                                    timelineDocumentsList.add(new_doc);
                                                }
                                                // Inflate the layout for this fragment
                                                adp = new DateTimelineAdaptor(timelineDocumentsList);
                                                System.out.println("size: "+timelineDocumentsList.size());
                                                imageInfoRecyclerView.setAdapter(adp);

                                            }
                                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        }


                                    }

                                });
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        System.out.println("datesetflag "+ date_set_flag);
        analysis=view.findViewById(R.id.btnAnalysis);

        analysis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("documentList", (Serializable) timelineDocumentsList);

                Bundle bundle2 =new Bundle();
                String[] dateTimeline={date_from.getEditText().getText().toString(),date_to.getEditText().getText().toString()};

                //bundle.putParcelableArrayList("documentList", (ArrayList<? extends Parcelable>) timelineDocumentsList);
                TimelineAnalysisFragment taf= new TimelineAnalysisFragment(dateTimeline[0],dateTimeline[1]);
                taf.setArguments(bundle);

                replaceFragment(taf);


//                Intent intent = new Intent(getActivity(), TimeLineActivity.class);
//                intent.putParcelableArrayListExtra("myDocsKey", (ArrayList<? extends Parcelable>) new ArrayList<Document>(timelineDocumentsList));
//                startActivity(intent);

            }
        });






        return view;
    }

    private void replaceFragment(Fragment fragment){
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

}