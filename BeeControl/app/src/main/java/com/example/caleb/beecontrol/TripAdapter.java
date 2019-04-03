package com.example.caleb.beecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TripAdapter extends FirestoreRecyclerAdapter<TripNew, TripAdapter.TripHolder> {

    public TripAdapter(@NonNull FirestoreRecyclerOptions<TripNew> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TripHolder holder, int position, @NonNull TripNew model) {
        holder.txtTripDate.setText(model.getTripDate());
        holder.txtTripDriverName.setText((model.getTripDriverName()));
        holder.txtTripTitle.setText(model.getTripTitle());
        holder.txtTripId.setText(String.valueOf(model.getTripId()));

    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_trip,viewGroup, false );

        return  new TripHolder(v );
    }

    class TripHolder extends RecyclerView.ViewHolder {

        TextView txtTripDriverName;
        TextView txtTripTitle;
        TextView txtTripDate;
        TextView txtTripId;

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            txtTripDriverName = itemView.findViewById(R.id.txtTripDriverName);
            txtTripTitle = itemView.findViewById(R.id.txtTripTitle);
            txtTripDate = itemView.findViewById(R.id.txtTripDate);
            txtTripId = itemView.findViewById(R.id.txtTripId);
        }
    }
}
