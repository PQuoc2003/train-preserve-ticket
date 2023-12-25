package com.hyperion.train_preserve_ticket.my_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperion.train_preserve_ticket.R;
import com.hyperion.train_preserve_ticket.dao.TripsDAO;
import com.hyperion.train_preserve_ticket.interfaces.TripsRVInterface;

import java.util.ArrayList;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.MyViewHolder> {


    Context context;
    ArrayList<TripsDAO> myTrips;

    TripsRVInterface tripsRVInterface;

    public TripsAdapter(Context context, ArrayList<TripsDAO> myTrips, TripsRVInterface tripsRVInterface) {
        this.context = context;
        this.myTrips = myTrips;
        this.tripsRVInterface = tripsRVInterface;
    }

    @NonNull
    @Override
    public TripsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.items_trips, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.MyViewHolder holder, int position) {

        TripsDAO trip = myTrips.get(position);

        holder.tvStart.setText(trip.getTrips().getStartStation());
        holder.tvEnd.setText(trip.getTrips().getEndStation());
        holder.tvTrain.setText(trip.getTrips().getTrain());

        String dateString = trip.getTrips().getDate() + " - " + trip.getTrips().getTime();

        holder.tvDate.setText(dateString);

        holder.tvPrice.setText(String.valueOf(trip.getTrips().getPrice()));

        holder.btStart.setOnClickListener(v -> {

            tripsRVInterface.OnButtonStartClick(position);
            holder.tvStartBt.setVisibility(View.VISIBLE);
            holder.btEdit.setVisibility(View.GONE);
            holder.btStart.setVisibility(View.GONE);

        });



    }

    @Override
    public int getItemCount() {
        return myTrips.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvStart , tvEnd, tvTrain, tvDate, tvPrice, tvStartBt;

        Button btStart, btEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStart = itemView.findViewById(R.id.tv_items_startStation);
            tvEnd = itemView.findViewById(R.id.tv_items_endStation);
            tvTrain = itemView.findViewById(R.id.tv_items_trainName);
            tvDate = itemView.findViewById(R.id.tv_items_datetime);
            tvPrice = itemView.findViewById(R.id.tv_items_price);
            btStart = itemView.findViewById(R.id.bt_items_start);
            tvStartBt = itemView.findViewById(R.id.tv_start_bt);
            btEdit = itemView.findViewById(R.id.bt_trips_edit);



        }
    }


}
