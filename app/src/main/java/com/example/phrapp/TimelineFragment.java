package com.example.phrapp;

import static com.example.phrapp.MainActivity.documentList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    private static View view;
    private static RecyclerView imageInfoRecyclerView;
    private DatabaseReference mDatabase;

    public  List<Document> Fetched_documentList;
    public ArrayList<ArrayList<String>> SearchData = new ArrayList<ArrayList<String>>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
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
    public void LogSearchData(ArrayList<ArrayList<String>> SearchData){
        Log.d("SEARCHDATA", Integer.toString(SearchData.size()));

        for(ArrayList<String> itr : SearchData){
            Log.d("SEARCHDATA", itr.get(0) + " - " + itr.get(1) +" - "+itr.get(2) +" - "+itr.get(3) );
        }
    }
    public void AddSearchData(List<Document> listdata){
        int location = 0;
        for(Document itr : listdata){
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(itr.getReportType().toString());
            temp.add(itr.getCategory().toString());
            temp.add(itr.getDocumentName().toString());
            temp.add(Integer.toString(location)); //Integer
            SearchData.add(temp);
            location++;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get all docs here:

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        // Get a reference to the search view
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
//        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.search).getActionView();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String loggedInUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Fetched_documentList = new ArrayList<Document>();

        mDatabase.child("Documents").orderByChild("loggedInUserId").equalTo(loggedInUserId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("firebase Error getting data"+ task.getException());
                }
                else {
//                    System.out.println("Fetched docs "+ String.valueOf(task.getResult().getValue()));
                    ArrayList<String> list = new ArrayList<>();
                    for (DataSnapshot snapshot : task.getResult().getChildren() ) {

                        String loggedInUserId_afterFetch = (String) snapshot.child("loggedInUserId").getValue();
                        if(loggedInUserId_afterFetch.equals(loggedInUserId))
                        {
                            System.out.println("Cyrrret Loged in User"+loggedInUserId);
                            System.out.println("Document User"+loggedInUserId_afterFetch);
                            list.add(snapshot.getValue().toString());
                            String documentName, description , doctorName , category,reportType , diagnosis;
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

                            Document new_doc = new Document( documentName,  description,  doctorName,  category,  reportType,  date,  image,  firebaseStorageUri,  loggedInUserId,  epoc,allDrugs,diagnosis);
                            //System.out.println(new_doc.toString());
                            Fetched_documentList.add(new_doc);
                            // Inflate the layout for this fragment

                            System.out.println("In Fragment");
                            imageInfoRecyclerView=view.findViewById(R.id.imageInfoRecyclerView);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            Date todayDate = Calendar.getInstance().getTime();
                            imageInfoRecyclerView.setLayoutManager(linearLayoutManager);
                            //                            Log.d("FRAGMENT", Integer.toString(Fetched_documentList.size()));
                            AddSearchData(Fetched_documentList);
                            LogSearchData(SearchData);

                            ImageInfoAdapter adp = new ImageInfoAdapter(Fetched_documentList);
                            imageInfoRecyclerView.setAdapter(adp);
                            System.out.println("Matched");
                            LogSearchData(SearchData);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    // Handle search query submission
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    // Filter the Fetched_documentList based on the search query

                                    List<Document> filteredList = new ArrayList<>();
                                    for (Document doc : Fetched_documentList) {
                                        if (doc.getDocumentName().toLowerCase().contains(newText.toLowerCase()) || doc.getCategory().toLowerCase().contains(newText.toLowerCase()) || doc.getReportType()
                                                .toLowerCase().contains(newText.toLowerCase())  ) {
                                            filteredList.add(doc);
                                        }
                                    }

                                    // Update the ImageInfoAdapter with the filtered list
                                    adp.filterList(filteredList);

                                    return false;
                                }

                            });
                        }
                    }

                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }


            }


        });
        return view;
    }


//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.search_menu,menu);
//        MenuItem searchItem=menu.findItem(R.id.actionSearch);
//        SearchView searchView=(SearchView) searchItem.getActionView();
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Log.d("entered on text",s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d("entered on query",newText);
//                String query = newText.toLowerCase();
//                ArrayList<String> matchingData = new ArrayList<String>();
//
//                for (ArrayList<String> sublist : SearchData) {
//                    if (sublist.size() < 4) {
//                        continue;
//                    }
//
//                    String firstValue = sublist.get(0).toLowerCase();
//                    String secondValue = sublist.get(1).toLowerCase();
//                    String thirdValue = sublist.get(2).toLowerCase();
//
//                    if (firstValue.startsWith(query) || secondValue.startsWith(query) || thirdValue.startsWith(query)) {
//                        matchingData.add(sublist.get(3));
//                    }
//                }
//                return false;
//            }
//        });
//    }

//    private void filter(String text) {
//        // creating a new array list to filter our data.
//        ArrayList<Document> filteredlist = new ArrayList<Document>();
//
//        // running a for loop to compare elements.
//        for (Document item : courseModelArrayList) {
//            // checking if the entered string matched with any item of our recycler view.
//            if (item.getCourseName().toLowerCase().contains(text.toLowerCase())) {
//                // if the item is matched we are
//                // adding it to our filtered list.
//                filteredlist.add(item);
//            }
//        }
//        if (filteredlist.isEmpty()) {
//            // if no item is added in filtered list we are
//            // displaying a toast message as no data found.
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            // at last we are passing that filtered
//            // list to our adapter class.
//            adapter.filterList(filteredlist);
//        }
//    }

}