package com.example.caleb.beecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TripAdapter extends FirestoreRecyclerAdapter<Trip, TripAdapter.TripHolder> {

    public TripAdapter(@NonNull FirestoreRecyclerOptions<Trip> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TripHolder holder, int position, @NonNull Trip model) {
        holder.txtTripDate.setText(model.getTripDate());

    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    class TripHolder extends RecyclerView.ViewHolder {

        TextView txtTruckDriverName;
        TextView txtTripTitle;
        TextView txtTripDate;
        TextView txtTripPartingHour;

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            txtTruckDriverName = itemView.findViewById(R.id.txtTripDriverName);
            txtTripTitle = itemView.findViewById(R.id.txtTripTitle);
            txtTripDate = itemView.findViewById(R.id.txtTripDate);
            txtTripPartingHour = itemView.findViewById(R.id.txtTripPartingHour);
        }
    }
}
