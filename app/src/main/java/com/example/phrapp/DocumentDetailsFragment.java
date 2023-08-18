package com.example.phrapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentDetailsFragment extends Fragment {

    TextView viewdocname,viewdate, viewdecs,viewcategory,viewtag,viewdoctorname,viewdrugs,viewdiagnosis;
    ImageView viewImage;
    View view;
    Document documentObj;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DocumentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentDetailsFragment newInstance(String param1, String param2) {
        DocumentDetailsFragment fragment = new DocumentDetailsFragment();
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
            documentObj = (Document) getArguments().getSerializable("imageInfoObj");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_document_details, container, false);
        viewdocname=(TextView)view.findViewById(R.id.viewDocName);
        viewdate=(TextView)view.findViewById(R.id.viewDate);
        viewdecs=(TextView)view.findViewById(R.id.viewDesc);
        viewcategory=(TextView)view.findViewById(R.id.viewCategory);
        viewtag=(TextView)view.findViewById(R.id.viewTag);
        viewdoctorname=(TextView)view.findViewById(R.id.viewDoctorName);
        viewImage = (ImageView)view.findViewById(R.id.viewImage);
        viewdrugs = (TextView)view.findViewById(R.id.viewDrugs);
        viewdiagnosis = (TextView)view.findViewById(R.id.viewDiagnosis);
        viewdocname.setText(documentObj.getDocumentName());
        viewdecs.setText(documentObj.getDescription());
        viewcategory.setText(documentObj.getCategory());
        viewtag.setText(documentObj.getReportType());
        viewdoctorname.setText(documentObj.getDoctorName());
        viewdrugs.setText(documentObj.getDrugs());
        viewdiagnosis.setText(documentObj.getDiagnosis());

        //loading image from firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("/images/"+documentObj.getDocumentName()+""+documentObj.getEpoc());

        final Bitmap[] bitmap = {null};
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewImage.setImageBitmap(bitmap[0]);
                System.out.println("Bitmap data: "+bitmap[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Unable to load image from Firebase: "+exception);
            }
        });

       // viewImage.setImageBitmap(documentObj.ge);
      ///  holder.prescriptionImg.setImageURI(Uri.parse(listdata.get(position).getFirebaseStorageUri()));
//        System.out.println("In Document details fragment "+documentObj.getFirebaseStorageUri());
//        viewImage.setImageURI(Uri.parse(documentObj.getFirebaseStorageUri()));
//        viewdate.setText(documentObj.getDate().toString());
        return view;
    }
}