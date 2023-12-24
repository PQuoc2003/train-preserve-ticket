package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.dao.TripsDAO;
import com.hyperion.train_preserve_ticket.interfaces.TripsRVInterface;
import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.Users;
import com.hyperion.train_preserve_ticket.my_adapter.TripsAdapter;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class TripsActivity extends AppCompatActivity implements TripsRVInterface {

    RecyclerView recyclerView;
    ArrayList<TripsDAO> myTripsDAO;

    TripsAdapter tripsAdapter;

    private FirebaseFirestore db;

    Context context;

    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        context = TripsActivity.this;

        btBack = findViewById(R.id.bt_back_trips);


        recyclerView = findViewById(R.id.rv_trips);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = FirebaseFirestore.getInstance();

        myTripsDAO = new ArrayList<>();


        tripsAdapter = new TripsAdapter(context, myTripsDAO, this);

        recyclerView.setAdapter(tripsAdapter);

        getTrips();

        btBack.setOnClickListener(v -> finish());

    }

    private void getTrips() {
        db.collection("trips")
                .whereEqualTo("run", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.getResult() == null) {
                            Log.d("debug", "no message");
                        }

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Trips trip = document.toObject(Trips.class);
                                myTripsDAO.add(new TripsDAO(document.getId(), trip));
                                tripsAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    @Override
    public void OnButtonStartClick(int position) {
        TripsDAO tripsDAO = myTripsDAO.get(position);

        String documentId = tripsDAO.getDocumentID();
        updateRun(documentId);
        updateStatusOfTicket(documentId);
    }

    /**
     * update status of ticket
     * if not checkout when train started , change status to 2
     *
     * @param documentId trips id
     */
    private void updateStatusOfTicket(String documentId) {

        db.collection("details")
                .whereEqualTo("tripsId", documentId)
                .whereEqualTo("status", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.getResult() == null) {
                            Log.d("debug", "no message");
                        }

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                changeStatus(document.getId());

                            }

                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }

                });

    }

    private void changeStatus(String tripDetailsId) {

        db.collection("details").document(tripDetailsId)
                .update("status", 2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "Update ticket check out success");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error writing document", e);
                    }
                });

    }

    private void updateRun(String documentId) {
        db.collection("trips").document(documentId)
                .update("run", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "Update run success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error writing document", e);
                    }
                });
    }

    @Override
    public void OnButtonEditClick(int position) {

    }
}