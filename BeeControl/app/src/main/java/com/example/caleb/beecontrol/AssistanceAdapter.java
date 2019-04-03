package com.example.caleb.beecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;


public class AssistanceAdapter extends FirestoreRecyclerAdapter<Assitance, AssistanceAdapter.AssistanceHolder>{


    public AssistanceAdapter(@NonNull FirestoreRecyclerOptions<Assitance> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AssistanceHolder holder, int position, @NonNull Assitance model) {
        holder.usuario.setText(model.getUsuario());
        holder.condicion.setText(model.getCondicion());
        holder.fecha.setText(model.getFecha());
    }

    @NonNull
    @Override
    public AssistanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_assistance,viewGroup, false );

        return new AssistanceHolder(v);
    }

    class AssistanceHolder extends RecyclerView.ViewHolder{

        TextView usuario ;
        TextView condicion;
        TextView fecha;

        public AssistanceHolder(@NonNull View itemView) {
            super(itemView);
            usuario = itemView.findViewById(R.id.usuario);
            condicion = itemView.findViewById(R.id.condicion);
            fecha = itemView.findViewById(R.id.fecha);
        }
    }











}






