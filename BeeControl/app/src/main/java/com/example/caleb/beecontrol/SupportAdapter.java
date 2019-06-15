package com.example.caleb.beecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SupportAdapter extends FirestoreRecyclerAdapter<Support, SupportAdapter.SupportHolder> {

    public SupportAdapter(@NonNull FirestoreRecyclerOptions<Support> options) {
        super(options);
    }



    @NonNull
    @Override
    public SupportHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_support_messages,viewGroup, false );

        return new SupportHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull SupportHolder holder, int position, @NonNull Support model) {
        holder.txtEmployeeName.setText(model.getEmployeeName());
        holder.txtSupportMessageId.setText(model.getId());
        holder.txtSupportMessageDate.setText(model.getSupportDate());
        holder. txtSupportMessage.setText((model.getSubject()));

    }

    class SupportHolder extends RecyclerView.ViewHolder{

        TextView txtEmployeeName;
        TextView txtSupportMessageId;
        TextView txtSupportMessageDate;
        TextView txtSupportMessage;

        public SupportHolder(@NonNull View itemView) {
            super(itemView);
            txtEmployeeName = itemView.findViewById(R.id.txtSupportMessageDate);
            txtSupportMessageId = itemView.findViewById(R.id.txtSupportMessageId);
            txtSupportMessageDate = itemView.findViewById(R.id.txtSupportMessageDate);
            txtSupportMessage = itemView.findViewById(R.id.txtSupportMessage);
        }
    }






}
