package com.example.phrapp;

import static com.example.phrapp.MainActivity.documentList;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    TextInputLayout date,patientName,category,doctorName,datedata;
    EditText description;
    DatePickerDialog datePickerDialog;
    String fullDate;
    View view;
    Document documentObj;
    ImageView imagePrescription;
    Spinner spinnerCategory,spinnerReportType;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageDetailsFragment newInstance(String param1, String param2) {
        ImageDetailsFragment fragment = new ImageDetailsFragment();
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
            System.out.println("In detail view on Create");
            documentObj =(Document) getArguments().getSerializable("imageInfoObj");
            System.out.println(" obj "+ documentObj);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // initiate the date picker and a button

        view = inflater.inflate(R.layout.fragment_image_details, container, false);

        patientName = (TextInputLayout)view.findViewById(R.id.patientNameEdtTxt);
        description = (EditText)view.findViewById(R.id.descriptionEdtTxt);
        //  category = (TextInputLayout)view.findViewById(R.id.categoryEdtTxt);
        doctorName = (TextInputLayout)view.findViewById(R.id.docNameEdtTxt);
        datedata = (TextInputLayout)view.findViewById(R.id.datedata);
        imagePrescription = view.findViewById(R.id.imgPrescription);
        imagePrescription.setImageURI(Uri.parse(documentObj.getImage()));

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // perform click event on edit text
        ImageView dateicon = (ImageView) view.findViewById(R.id.dateiconimage);
        spinnerCategory = (Spinner)view.findViewById(R.id.categorySpinner);
        spinnerReportType= (Spinner)view.findViewById(R.id.reportTypeSpinner);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(container.getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.categories));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCategory.setAdapter(myAdapter1);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(container.getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.reportType));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerReportType.setAdapter(myAdapter2);
        System.out.println("inside onclick listener outside");
        dateicon.setOnClickListener(new View.OnClickListener() {
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


                                System.out.println("Date data obj "+datedata);
                                fullDate=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                datedata.getEditText().setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Button saveDetailsBtn = view.findViewById(R.id.btnSaveDetails);
        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date1=null;
                try{
                    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                    //date1 = dateFormat.parse(date.getEditText().getText().toString());
                }
                catch (Exception e){
                    System.out.println("Error in date conversion");
                }
                if(documentObj !=null){
                    documentObj.setCategory(spinnerCategory.getSelectedItem().toString());
                    documentObj.setReportType(spinnerReportType.getSelectedItem().toString());
                    System.out.println(" On Saving Selected category is "+ documentObj.getCategory());
                    documentObj.setDescription(description.getText().toString());
                    documentObj.setDocumentName(patientName.getEditText().getText().toString());
                    documentObj.setDoctorName(doctorName.getEditText().getText().toString());
                    documentObj.setDate(fullDate);
                    documentObj.setLoggedInUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    documentObj.setEpoc(System.currentTimeMillis());
                    System.out.println("Epoc is: "+documentObj.getEpoc());
                    Date expiry = new Date( documentObj.getEpoc()  );
                    System.out.println("date is; "+expiry);
                    if(documentObj!=null){
                        documentList.add(documentObj);
                    }else{
                        Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                    }

                    Uri uri = Uri.parse(documentObj.getImage());
                    StorageReference ref=null;
                    // UploadImage method
                    if (uri != null) {
                        ref  = storageReference
                                .child(
                                        "images/"
                                                + documentObj.getDocumentName()+""+documentObj.getEpoc());

                        // adding listeners on upload
                        // or failure of image
                        ref.putFile(uri)
                                .addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override
                                            public void onSuccess(
                                                    UploadTask.TaskSnapshot taskSnapshot)
                                            {

                                                // Image uploaded successfully
                                                // Dismiss dialog

                                                Toast
                                                        .makeText(getContext(),
                                                                "Image Uploaded!!",
                                                                Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {

                                        // Error, Image not uploaded

                                        Toast
                                                .makeText(getContext(),
                                                        "Failed " + e.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                        });
                    }

                    System.out.println("In Image details fragement");
                    System.out.println("name "+documentObj.getLoggedInUserId());
                    System.out.println("name r "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    documentObj.setFirebaseStorageUri(documentObj.getDocumentName()+""+documentObj.getEpoc());
                    databaseReference=FirebaseDatabase.getInstance().getReference("Documents").push();
                    databaseReference.setValue(documentObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast toast= Toast.makeText(getActivity(),"Document Added Successfully",Toast.LENGTH_LONG);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                            else{
                                Toast toast= Toast.makeText(getActivity(),"Failed to add Document ",Toast.LENGTH_LONG);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                        }
                    });


                    System.out.println("Image details added to list");

                }
            }
        });
        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}