package com.example.caleb.beecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class AssistanceAdapter extends FirestoreRecyclerAdapter<Assistance, AssistanceAdapter.AssistanceHolder>{


    public AssistanceAdapter(@NonNull FirestoreRecyclerOptions<Assistance> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AssistanceHolder holder, int position, @NonNull Assistance model) {
        holder.txtEmployeeName.setText(model.getEmployeeName());
        holder.txtStatus.setText(model.getStatus());
        holder.txtAssistDate.setText(model.getAssistDate());
    }

    @NonNull
    @Override
    public AssistanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_assistance,viewGroup, false );

        return new AssistanceHolder(v);
    }

    class AssistanceHolder extends RecyclerView.ViewHolder{

        TextView txtEmployeeName;
        TextView txtStatus;
        TextView txtAssistDate;

        public AssistanceHolder(@NonNull View itemView) {
            super(itemView);
            txtEmployeeName = itemView.findViewById(R.id.usuario);
            txtStatus = itemView.findViewById(R.id.condicion);
            txtAssistDate = itemView.findViewById(R.id.fecha);
        }
    }











}






