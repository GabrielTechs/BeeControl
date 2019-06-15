package com.example.caleb.beecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SupportAdapter extends FirestoreRecyclerAdapter<Support, SupportAdapter.SupportHolder> {

    private OnItemClickListener listener;

    public SupportAdapter(@NonNull FirestoreRecyclerOptions<Support> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SupportHolder holder, int position, @NonNull Support model) {
        holder.txtEmployeeName.setText(model.getEmployeeName());
        holder.txtSupportMessageId.setText(model.getId());
        holder.txtSupportMessageDate.setText(model.getSupportDate());
        holder. txtSupportMessage.setText((model.getSubject()));

    }

    @NonNull
    @Override
    public SupportHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_support_messages,viewGroup, false );

        return new SupportHolder(v);
    }

    class SupportHolder extends RecyclerView.ViewHolder{

        TextView txtEmployeeName;
        TextView txtSupportMessageId;
        TextView txtSupportMessageDate;
        TextView txtSupportMessage;

        public SupportHolder(@NonNull View itemView) {
            super(itemView);
            txtEmployeeName = itemView.findViewById(R.id.txtSupportMessageName);
            txtSupportMessageId = itemView.findViewById(R.id.txtSupportMessageId);
            txtSupportMessageDate = itemView.findViewById(R.id.txtSupportMessageDate);
            txtSupportMessage = itemView.findViewById(R.id.txtSupportMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

}
