package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.dao.TripsDAO;
import com.hyperion.train_preserve_ticket.dao.TripsDetailDAO;
import com.hyperion.train_preserve_ticket.interfaces.AdminTicketCheckRVInterface;
import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.TripsDetail;
import com.hyperion.train_preserve_ticket.model.Users;
import com.hyperion.train_preserve_ticket.my_adapter.AdminCheckAdapter;

import java.util.ArrayList;

public class TicketCheckoutActivity extends AppCompatActivity implements AdminTicketCheckRVInterface {

    RecyclerView recyclerView;

    ArrayList<TripsDetailDAO> tripsDetailDAOS;
    ArrayList<TripsDetailDAO> tripsDetailDAOSChecked;

    AdminCheckAdapter adminCheckAdapter, adminCheckAdapterChecked;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Context context;

    Button btBack, btHistory;

    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        context = TicketCheckoutActivity.this;

        btBack = findViewById(R.id.bt_back_cTA);
        btHistory = findViewById(R.id.bt_history_cTA);
        flag = false;

        recyclerView = findViewById(R.id.rv_cTA);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tripsDetailDAOS = new ArrayList<>();
        tripsDetailDAOSChecked = new ArrayList<>();

        Intent intent = getIntent();


        String phone = intent.getStringExtra("phone");

        adminCheckAdapter = new AdminCheckAdapter(this, tripsDetailDAOS, this);
        adminCheckAdapterChecked = new AdminCheckAdapter(this, tripsDetailDAOSChecked, this);


        recyclerView.setAdapter(adminCheckAdapter);

        getTicket(phone);

        btBack.setOnClickListener(v -> finish());

        btHistory.setOnClickListener(v -> changeAdapter());

    }

    private void changeAdapter() {
        if (!flag) {
            btHistory.setText("CHECK OUT");
            recyclerView.setAdapter(adminCheckAdapterChecked);
            flag = true;
        } else {
            btHistory.setText("HISTORY");
            recyclerView.setAdapter(adminCheckAdapter);
            flag = false;
        }

    }


    private void getTicket(String phone) {

        getUserId(phone);

    }

    private void getUserId(String phone) {
        db.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Users users = document.toObject(Users.class);

                                getTicketId(users.getId());

                            }

                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    private void getTicketId(String id) {
        db.collection("details")
                .whereEqualTo("userId", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                TripsDetail tripsDetail = document.toObject(TripsDetail.class);

                                getTripsDAO(tripsDetail, document.getId());

                            }

                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }

                });
    }


    private void getTripsDAO(TripsDetail tripsDetail, String ticketId) {

        DocumentReference docRef = db.collection("trips").document(tripsDetail.getTripsId());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("phuquoc", "DocumentSnapshot data: " + document.getData());

                        Trips myTrips = document.toObject(Trips.class);

                        TripsDAO tripsDAO = new TripsDAO(tripsDetail.getTripsId(), myTrips);

                        TripsDetailDAO tripsDetailDAO = new TripsDetailDAO(ticketId, tripsDetail, tripsDAO);

                        if (tripsDetail.getStatus() == 0) {
                            tripsDetailDAOS.add(tripsDetailDAO);
                            adminCheckAdapter.notifyDataSetChanged();
                        } else {
                            tripsDetailDAOSChecked.add(tripsDetailDAO);
                            adminCheckAdapterChecked.notifyDataSetChanged();
                        }


                    } else {
                        Log.d("phuquoc", "No such document");
                    }
                } else {
                    Log.d("phuquoc", "get failed with ", task.getException());
                }
            }
        });

    }


    @Override
    public void checkOutTicket(String ticketId, int position) {


        db.collection("details").document(ticketId)
                .update("status", 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "Update ticket check out success");

                        TripsDetailDAO tripsDetailDAO = tripsDetailDAOS.get(position);
                        tripsDetailDAO.getTripsDetail().setStatus(1);

                        tripsDetailDAOS.remove(position);
                        adminCheckAdapter.notifyItemRemoved(position);

                        tripsDetailDAOSChecked.add(tripsDetailDAO);
                        adminCheckAdapterChecked.notifyDataSetChanged();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error writing document", e);
                    }
                });

    }
}