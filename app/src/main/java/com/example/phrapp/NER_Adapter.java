package com.example.phrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NER_Adapter extends RecyclerView.Adapter<NER_Adapter.ViewHolder> {

    //Context context;
    List<NER_model> NER_list;

    public NER_Adapter(List<NER_model> NER_list){
        //this.context=context;
        this.NER_list=NER_list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_ner,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(NER_list !=null && NER_list.size()>0){
            NER_model model = NER_list.get(position);
            holder.type_tv.setText(model.getType());
            holder.subtype_tv.setText(model.getSubtype());
            holder.result_tv.setText(model.getResults());
        }
        else{
            return;
        }
    }

    @Override
    public int getItemCount() {
        return NER_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView type_tv,result_tv,subtype_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
            subtype_tv =(TextView) itemView.findViewById(R.id.subtype_tv);
            result_tv = (TextView) itemView.findViewById(R.id.result_tv);

        }
    }
}
