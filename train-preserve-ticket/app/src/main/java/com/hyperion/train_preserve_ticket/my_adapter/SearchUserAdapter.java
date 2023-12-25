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
import com.hyperion.train_preserve_ticket.interfaces.UserSearchRVInterface;

import java.util.ArrayList;


public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {


    Context context;
    ArrayList<TripsDAO> tripsDAOS;

    UserSearchRVInterface userSearchRVInterface;

    public SearchUserAdapter(Context context, ArrayList<TripsDAO> tripsDAOS, UserSearchRVInterface userSearchRVInterface) {
        this.context = context;
        this.tripsDAOS = tripsDAOS;
        this.userSearchRVInterface = userSearchRVInterface;
    }

    @NonNull
    @Override
    public SearchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.items_trips_user, parent, false);

        return new SearchUserAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.MyViewHolder holder, int position) {

        TripsDAO trip = tripsDAOS.get(position);

        holder.tvStart.setText(trip.getTrips().getStartStation());
        holder.tvEnd.setText(trip.getTrips().getEndStation());
        holder.tvTrain.setText(trip.getTrips().getTrain());

        String dateString = trip.getTrips().getDate() + " - " + trip.getTrips().getTime();

        holder.tvDate.setText(dateString);

        holder.tvPrice.setText(String.valueOf(trip.getTrips().getPrice()));


        holder.btPre.setOnClickListener(v -> {
            holder.btCancel.setVisibility(View.VISIBLE);
            holder.btPre.setVisibility(View.GONE);
            userSearchRVInterface.addUserToTrip(position);
        });

        holder.btCancel.setOnClickListener(v -> {
            holder.btCancel.setVisibility(View.GONE);
            holder.btPre.setVisibility(View.VISIBLE);
            userSearchRVInterface.cancelToTrip(position);
        });

    }

    @Override
    public int getItemCount() {
        return tripsDAOS.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvStart , tvEnd, tvTrain, tvDate, tvPrice;

        Button btPre, btCancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStart = itemView.findViewById(R.id.tv_iU_start);
            tvEnd = itemView.findViewById(R.id.tv_iU_end);
            tvTrain = itemView.findViewById(R.id.tv_iU_train);
            tvDate = itemView.findViewById(R.id.tv_iU_date);
            tvPrice = itemView.findViewById(R.id.tv_iU_price);
            btPre = itemView.findViewById(R.id.bt_iU_preserve);
            btCancel = itemView.findViewById(R.id.bt_iU_cancel);

        }
    }


}
