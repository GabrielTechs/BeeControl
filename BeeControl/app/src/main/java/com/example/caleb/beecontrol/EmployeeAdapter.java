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

public class EmployeeAdapter extends FirestoreRecyclerAdapter<Employee, EmployeeAdapter.EmployeeHolder> {

    private TripAdapter.OnItemClickListener listener;

    public EmployeeAdapter(@NonNull FirestoreRecyclerOptions<Employee> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EmployeeHolder holder, int position, @NonNull Employee model) {
        holder.txtEmployeeFullName.setText(new StringBuilder().append(model.getName()).append(" ").append(model.getLastName()).toString());
        holder.txtEmployeeId.setText(model.getEmail());
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_employees, viewGroup, false);
        return new EmployeeHolder(v);
    }

    class EmployeeHolder extends RecyclerView.ViewHolder{

        TextView txtEmployeeFullName;
        TextView txtEmployeeId;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            txtEmployeeFullName = itemView.findViewById(R.id.txtEmployeeFullName);
            txtEmployeeId = itemView.findViewById(R.id.txtEmployeeId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public void setOnItemClickListener(TripAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}
