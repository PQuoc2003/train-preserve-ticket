package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.hyperion.train_preserve_ticket.interfaces.HistoryRVInterface;
import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.TripsDetail;
import com.hyperion.train_preserve_ticket.my_adapter.HistoryAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements HistoryRVInterface {

    RecyclerView recyclerView;
    private FirebaseFirestore db;
    ArrayList<TripsDetailDAO> tripsDetailDAOS;

    HistoryAdapter historyAdapter;

    private FirebaseAuth mAuth;

    Context context;

    String userId;

    Button btBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        context = HistoryActivity.this;

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btBack = findViewById(R.id.bt_back_history);


        recyclerView = findViewById(R.id.rv_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripsDetailDAOS = new ArrayList<>();

        historyAdapter = new HistoryAdapter(this, tripsDetailDAOS, this);

        recyclerView.setAdapter(historyAdapter);

        userId = mAuth.getUid();

        getTicket(userId);

        btBack.setOnClickListener(v -> {
            returnBack();
        });


    }

    private void returnBack() {
        finish();
    }

    private void getTicket(String userId) {

        db.collection("details")
                .whereEqualTo("userId", userId)
                .orderBy("status")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG", document.getId() + " => " + document.getData());

                                String ticketId = document.getId();

                                TripsDetail tripsDetail = document.toObject(TripsDetail.class);

                                getDetailOfTrip(ticketId, tripsDetail);

                            }
                        } else {
                            Log.w("DEBUG", "Error getting documents. details", task.getException());
                        }
                    }

                });

    }

    /**
     * get full information of trips
     *
     * @param ticketId    documentId of tripsDetail
     * @param tripsDetail the tripsDetail object
     */
    private void getDetailOfTrip(String ticketId, TripsDetail tripsDetail) {

        DocumentReference docRef = db.collection("trips").document(tripsDetail.getTripsId());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("phuquoc", "DocumentSnapshot data: " + document.getData());

                        Trips myTrips = document.toObject(Trips.class);

                        TripsDAO tripsDAO = new TripsDAO(document.getId(), myTrips);

                        TripsDetailDAO tripsDetailDAO = new TripsDetailDAO(ticketId, tripsDetail, tripsDAO);

                        tripsDetailDAOS.add(tripsDetailDAO);

                        historyAdapter.notifyDataSetChanged();


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
    public void cancelTrips(String ticketId, String tripsId, int position) {

        removeTicket(ticketId);
        getRemainSeat(tripsId);
        tripsDetailDAOS.remove(position);
        historyAdapter.notifyItemRemoved(position);

    }

    private void removeTicket(String ticketId) {
        db.collection("details").document(ticketId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("phuquoc", "DocumentSnapshot successfully deleted!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("phuquoc", "Error deleting document", e);
                    }
                });
    }

    private void getRemainSeat(String tripsId) {

        DocumentReference docRef = db.collection("trips").document(tripsId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("phuquoc", "DocumentSnapshot data: " + document.getData());

                        Trips myTrips = document.toObject(Trips.class);

                        int remainSeats = myTrips.getRemainSeats();

                        addOneSeatToTrips(tripsId, remainSeats, myTrips.getSeats());


                    } else {
                        Log.d("phuquoc", "No such document");
                    }
                } else {
                    Log.d("phuquoc", "get failed with ", task.getException());
                }
            }
        });
    }

    private void addOneSeatToTrips(String tripsId, int remainSeats, int seats) {

        db.collection("trips").document(tripsId)
                .update("remainSeats", remainSeats + 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Debug", "DocumentSnapshot successfully written!");

                if (remainSeats + 1 == 1) {
                    updateTripsFull(tripsId);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Debug", "Error writing document", e);
            }
        });

    }

    private void updateTripsFull(String tripsId) {
        db.collection("trips").document(tripsId)
                .update("full", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Debug", "Update full seat successfully");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Debug", "Error when update full seat", e);
            }
        });


    }
}