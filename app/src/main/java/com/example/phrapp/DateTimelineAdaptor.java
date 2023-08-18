package com.example.phrapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DateTimelineAdaptor extends RecyclerView.Adapter<DateTimelineAdaptor.ViewHolder> {
    @NonNull
    private List<Document> listdata;
    // RecyclerView recyclerView;
    public DateTimelineAdaptor(List<Document> listdata) {
        this.listdata = listdata;
    }
    @Override
    public DateTimelineAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.timelineitem, parent, false);
        DateTimelineAdaptor.ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DateTimelineAdaptor.ViewHolder holder, int position) {
        final Document myListData = listdata.get(position);



        // holder.description.setText(listdata.get(position).getDescription());
        System.out.println("Holder "+ holder);
        System.out.println("category "+ holder.category);
        holder.category.setText(myListData.getCategory());
        holder.docName.setText(myListData.getDocumentName());
        holder.doctorName.setText(myListData.getDoctorName());
        holder.date.setText(myListData.getDate());
        long timestamp = myListData.getEpoc();
        Date timestamp_string = new Date(timestamp);
        String date_toshow = timestamp_string.toString().substring(11,16);
        holder.time.setText(date_toshow);

        if((myListData.getCategory().equals("Lab Prescription")))
        {
            holder.timelineIcon.setImageResource(R.drawable.labicon);
        }
        else  if((myListData.getCategory().equals("Lab Reports")))
        {
            holder.timelineIcon.setImageResource(R.drawable.labreporticon);
        }
        else  if((myListData.getCategory().equals("Doctor Reports")))
        {
            holder.timelineIcon.setImageResource(R.drawable.pres_icon);
        }
        else
        {
            holder.timelineIcon.setImageResource(R.drawable.docicon);
        }
        holder.viewTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity act = (AppCompatActivity) view.getContext();
                DocumentDetailsFragment detailViewFragment= new DocumentDetailsFragment();

                if(act instanceof User_Logged_in_via_abha_HomePage){
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.user_logged_in_via_abha_homepage_fragment_frame,detailViewFragment).addToBackStack(null).commit();
                }
                else{
                    act.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,detailViewFragment).addToBackStack(null).commit();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("imageInfoObj", (Serializable) listdata.get(holder.getAdapterPosition()));
                detailViewFragment.setArguments(bundle);
            }
        });


    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView time;
        public TextView category;
        public TextView docName;
        public TextView doctorName;
        public ImageView timelineIcon;
        public TextView viewTextField;



        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.timelineDate);
            this.time = (TextView) itemView.findViewById(R.id.timelineTime);
            this.category = (TextView) itemView.findViewById(R.id.timelineCategory);
            this.docName = (TextView) itemView.findViewById(R.id.timelineDocName);
            this.doctorName = (TextView) itemView.findViewById(R.id.timelineDoctor);
            this.timelineIcon = (ImageView) itemView.findViewById(R.id.timelineIcon);
            this.viewTextField = (TextView)itemView.findViewById(R.id.txtViewForViewDetails);
        }
    }

}


