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

public class TripAdapter extends FirestoreRecyclerAdapter<Trip, TripAdapter.TripHolder> {

    private OnItemClickListener listener;

    public TripAdapter(@NonNull FirestoreRecyclerOptions<Trip> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TripHolder holder, int position, @NonNull Trip model) {
        holder.txtTripDate.setText(model.getTripDate());
        holder.txtTripDriverName.setText((model.getTripDriverName()));
        holder.txtTripTitle.setText(model.getTripTitle());
        holder.txtTripCreatedHour.setText(model.getTripCreatedHour());
        holder.txtTripEntryHour.setText(model.getTripEntryHour());
        holder.txtTripPartingHour.setText(model.getTripPartingHour());
        holder.txtTripId.setText(String.valueOf(model.getTripId()));

    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_trip,viewGroup, false );

        return new TripHolder(v);
    }

    class TripHolder extends RecyclerView.ViewHolder {

        TextView txtTripDriverName;
        TextView txtTripTitle;
        TextView txtTripDate;
        TextView txtTripCreatedHour;
        TextView txtTripEntryHour;
        TextView txtTripPartingHour;
        TextView txtTripId;

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            txtTripDriverName = itemView.findViewById(R.id.txtTripDriverName);
            txtTripTitle = itemView.findViewById(R.id.txtTripTitle);
            txtTripDate = itemView.findViewById(R.id.txtTripDate);
            txtTripCreatedHour = itemView.findViewById(R.id.txtTripCreatedHour);
            txtTripEntryHour = itemView.findViewById(R.id.txtTripEntryHour);
            txtTripPartingHour = itemView.findViewById(R.id.txtTripPartingHour);
            txtTripId = itemView.findViewById(R.id.txtTripId);

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

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
