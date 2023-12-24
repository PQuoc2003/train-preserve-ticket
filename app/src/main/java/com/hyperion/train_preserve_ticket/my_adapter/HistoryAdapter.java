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
import com.hyperion.train_preserve_ticket.interfaces.HistoryRVInterface;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;

    ArrayList<TripsDetailDAO> tripsDetailDAOS;

    HistoryRVInterface historyRVInterface;


    public HistoryAdapter(Context context, ArrayList<TripsDetailDAO> tripsDetailDAOS, HistoryRVInterface historyRVInterface) {
        this.context = context;
        this.tripsDetailDAOS = tripsDetailDAOS;
        this.historyRVInterface = historyRVInterface;
    }



    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.items_checkout_user, parent, false);

        return new HistoryAdapter.MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {

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
            holder.btCancel.setVisibility(View.VISIBLE);
            holder.tvChecked.setVisibility(View.GONE);
        }

        if(tripsDetailDAOS.get(position).getTripsDetail().getStatus() == 1){
            holder.btCancel.setVisibility(View.GONE);
            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.tvChecked.setText("Checked out");
        }

        if(tripsDetailDAOS.get(position).getTripsDetail().getStatus() == 2){
            holder.btCancel.setVisibility(View.GONE);
            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.tvChecked.setText("Cancelled");
        }

        holder.btCancel.setOnClickListener(v -> {

            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.btCancel.setVisibility(View.GONE);
            historyRVInterface.cancelTrips(ticketId, tripsDAO.getDocumentID(), position);

        });

    }

    @Override
    public int getItemCount() {
        return tripsDetailDAOS.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTkID, tvStart , tvEnd, tvTrain, tvDate, tvPrice, tvChecked;

        Button btCancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTkID = itemView.findViewById(R.id.tv_cTU_tkId);
            tvStart = itemView.findViewById(R.id.tv_cTU_start);
            tvEnd = itemView.findViewById(R.id.tv_cTU_end);
            tvTrain = itemView.findViewById(R.id.tv_cTU_train);
            tvDate = itemView.findViewById(R.id.tv_cTU_date);
            tvPrice = itemView.findViewById(R.id.tv_cTU_price);
            btCancel = itemView.findViewById(R.id.bt_ctU_cancel);
            tvChecked = itemView.findViewById(R.id.tv_cTU_checked);

        }
    }
}
