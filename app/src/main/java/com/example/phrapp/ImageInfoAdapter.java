package com.example.phrapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ImageInfoAdapter extends RecyclerView.Adapter<ImageInfoAdapter.ViewHolder> {
    private List<Document> listdata;
    public ArrayList<ArrayList<String>> SearchData = new ArrayList<ArrayList<String>>();

    // RecyclerView recyclerView;
    public ImageInfoAdapter(List<Document> listdata) {
        this.listdata = listdata;
        AddSearchData(listdata);
        LogSearchData(SearchData);

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



    //    private RecyclerView recyclerView;
//    private ImageInfoAdapter adapter;
//    private List<Document> documentList;
//    private TextInputEditText searchEditText;
//    private ImageView searchIcon;
//    private FrameLayout searchContainer;
//    private Button searchButton;



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.image_info_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Document myListData = listdata.get(position);
        // holder.description.setText(listdata.get(position).getDescription());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("/images/"+listdata.get(position).getDocumentName()+""+listdata.get(position).getEpoc());

        final Bitmap[] bitmap = {null};
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.prescriptionImg.setImageBitmap(bitmap[0]);
                System.out.println("Bitmap data: "+bitmap[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Unable to load image from Firebase: "+exception);
            }
        });
        holder.category.setText(listdata.get(position).getCategory());
        holder.reportType.setText(listdata.get(position).getReportType());
        System.out.println("In image info adap "+listdata.get(position).getFirebaseStorageUri());
      //  holder.prescriptionImg.setImageURI(Uri.parse(listdata.get(position).getFirebaseStorageUri()));
        holder.docName.setText(listdata.get(position).getDocumentName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity act = (AppCompatActivity) view.getContext();
                DocumentDetailsFragment detailViewFragment= new DocumentDetailsFragment();

                if( act instanceof User_Logged_in_via_abha_HomePage){
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.user_logged_in_via_abha_homepage_fragment_frame,detailViewFragment).addToBackStack(null).commit();
                }
                else{
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,detailViewFragment).addToBackStack(null).commit();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("imageInfoObj", (Serializable) listdata.get(position));
                detailViewFragment.setArguments(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println(listdata.size());
        return listdata.size();
    }

    public void filterList(List<Document> filteredList) {
        listdata = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView category;
        public TextView reportType;
        public TextView docName;
        public ImageView prescriptionImg;

        public ViewHolder(View itemView) {
            super(itemView);
            this.docName = (TextView) itemView.findViewById(R.id.txtViewDescription);
            this.category = (TextView) itemView.findViewById(R.id.txtViewCategory);
            this.reportType = (TextView) itemView.findViewById(R.id.txtViewReportType);
            this.prescriptionImg = (ImageView) itemView.findViewById(R.id.imageViewPrescription);
        }
    }
}