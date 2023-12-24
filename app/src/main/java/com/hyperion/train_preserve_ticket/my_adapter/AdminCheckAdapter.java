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
import com.hyperion.train_preserve_ticket.dao.TripsDetailDAO;
import com.hyperion.train_preserve_ticket.interfaces.AdminTicketCheckRVInterface;

import java.util.ArrayList;

public class AdminCheckAdapter extends RecyclerView.Adapter<AdminCheckAdapter.MyViewHolder> {

    Context context;

    ArrayList<TripsDetailDAO> tripsDetailDAOS;

    AdminTicketCheckRVInterface adminTicketCheckRVInterface;

    public AdminCheckAdapter(Context context, ArrayList<TripsDetailDAO> tripsDetailDAOS, AdminTicketCheckRVInterface adminTicketCheckRVInterface) {
        this.context = context;
        this.tripsDetailDAOS = tripsDetailDAOS;
        this.adminTicketCheckRVInterface = adminTicketCheckRVInterface;
    }


    @NonNull
    @Override
    public AdminCheckAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.items_checkout_ticket, parent, false);

        return new AdminCheckAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCheckAdapter.MyViewHolder holder, int position) {

        TripsDAO tripsDAO = tripsDetailDAOS.get(position).getTripsDAO();

        String ticketId = tripsDetailDAOS.get(position).getTicketId();

        holder.tvTkID.setText(ticketId);

        holder.tvStart.setText(tripsDAO.getTrips().getStartStation());
        holder.tvEnd.setText(tripsDAO.getTrips().getEndStation());
        holder.tvTrain.setText(tripsDAO.getTrips().getTrain());

        String dateString = tripsDAO.getTrips().getDate() + " - " + tripsDAO.getTrips().getTime();

        holder.tvDate.setText(dateString);

        holder.tvPrice.setText(String.valueOf(tripsDAO.getTrips().getPrice()));

        if(tripsDetailDAOS.get(position).getTripsDetail().getStatus() == 0){
            holder.btCheck.setVisibility(View.VISIBLE);
            holder.tvChecked.setVisibility(View.GONE);
        }

        if(tripsDetailDAOS.get(position).getTripsDetail().getStatus() == 1){
            holder.btCheck.setVisibility(View.GONE);
            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.tvChecked.setText("Checked out");
        }

        if(tripsDetailDAOS.get(position).getTripsDetail().getStatus() == 2){
            holder.btCheck.setVisibility(View.GONE);
            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.tvChecked.setText("Cancelled");
        }

        holder.btCheck.setOnClickListener(v -> {

            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.btCheck.setVisibility(View.GONE);
            adminTicketCheckRVInterface.checkOutTicket(ticketId, position);

        });
    }

    @Override
    public int getItemCount() {
        return tripsDetailDAOS.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTkID, tvStart , tvEnd, tvTrain, tvDate, tvPrice, tvChecked;

        Button btCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTkID = itemView.findViewById(R.id.tv_cT_tkId);
            tvStart = itemView.findViewById(R.id.tv_cT_start);
            tvEnd = itemView.findViewById(R.id.tv_cT_end);
            tvTrain = itemView.findViewById(R.id.tv_cT_train);
            tvDate = itemView.findViewById(R.id.tv_cT_date);
            tvPrice = itemView.findViewById(R.id.tv_cT_price);
            btCheck = itemView.findViewById(R.id.bt_cT_checkout);
            tvChecked = itemView.findViewById(R.id.tv_cTA_checked);

        }
    }
}
