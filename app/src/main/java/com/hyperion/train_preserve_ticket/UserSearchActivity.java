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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.dao.BPanelUSearch;
import com.hyperion.train_preserve_ticket.dao.TripsDAO;
import com.hyperion.train_preserve_ticket.interfaces.UserSearchRVInterface;
import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.TripsDetail;
import com.hyperion.train_preserve_ticket.my_adapter.SearchUserAdapter;

import java.util.ArrayList;

public class UserSearchActivity extends AppCompatActivity implements UserSearchRVInterface {

    RecyclerView recyclerView;

    ArrayList<TripsDAO> myTripsDAO;

    SearchUserAdapter searchUserAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    Context context;

    BPanelUSearch items;

    private String userId;

    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        context = UserSearchActivity.this;

        mAuth = FirebaseAuth.getInstance();

        btBack = findViewById(R.id.bt_back_rS);

        userId = mAuth.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myBundle");

        try {
            items = (BPanelUSearch) bundle.getSerializable("values");
            Log.d("phuquoc", "get bundle success");
        } catch (Exception e){
            Log.d("phuquoc", "can not get bunble");
        }

        recyclerView = findViewById(R.id.rv_sU);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        myTripsDAO = new ArrayList<>();


        searchUserAdapter = new SearchUserAdapter(context, myTripsDAO, this);

        recyclerView.setAdapter(searchUserAdapter);

        getSearchedTrips(items);

        btBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(UserSearchActivity.this, UserHomeActivity.class);
            finish();
            startActivity(intent1);
        });

    }

    private void getSearchedTrips(BPanelUSearch items) {

        if(items == null) return;

        db.collection("trips")
                .whereEqualTo("run", false)
                .whereEqualTo("full", false)
                .whereEqualTo("startStation", items.getStartStation())
                .whereEqualTo("endStation", items.getEndStation())
                .whereEqualTo("date", items.getDateOfTrips())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.getResult() == null){
                            Log.d("debug", "no message");
                        }

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Trips trip = document.toObject(Trips.class);
                                myTripsDAO.add(new TripsDAO(document.getId(), trip));
                                searchUserAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    @Override
    public void addUserToTrip(int position) {
        TripsDAO tripsDAO = myTripsDAO.get(position);

        TripsDetail tripsDetail = new TripsDetail(userId, tripsDAO.getDocumentID());

        addTripToFirebase(tripsDetail);

    }
    private void addTripToFirebase(TripsDetail tripsDetail) {

            db.collection("details")
                .add(tripsDetail)
                .addOnSuccessListener(documentReference -> {

                            Log.d("phuquoc", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(context, "Your trips has been successfully added", Toast.LENGTH_LONG).show();
                            getRemainSeat(tripsDetail.getTripsId(), 0);
                        }
                )
                .addOnFailureListener(e ->
                        Log.w("phuquoc", "Error adding document", e)
                );
    }

    private void getRemainSeat(String tripsId , int choice){

        DocumentReference docRef = db.collection("trips").document(tripsId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("phuquoc", "DocumentSnapshot data: " + document.getData());

                        Trips myTrips = document.toObject(Trips.class);

                        int remainSeat = myTrips.getRemainSeats();


                        if(choice == 0){
                            removeOneSeatFromTrips(tripsId, remainSeat);
                        } else {
                            addOneSeatFree(tripsId, remainSeat);
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

    private void removeOneSeatFromTrips(String tripsId, int remainSeat) {

        db.collection("trips").document(tripsId)
                .update("remainSeats", remainSeat - 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "DocumentSnapshot successfully written!");

                        if(remainSeat - 1 == 0){
                            updateTripsFull(tripsId, 0);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error writing document", e);
                    }
                });
    }

    private void updateTripsFull(String tripsId, int choice) {

        boolean myValue = false;

        if (choice == 0) myValue = true;

        db.collection("trips").document(tripsId)
                .update("full", myValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "Update full seat successfully");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error when update full seat", e);
                    }
                });

    }

    @Override
    public void cancelToTrip(int position) {

        TripsDAO tripsDAO = myTripsDAO.get(position);

        TripsDetail tripsDetail = new TripsDetail(userId, tripsDAO.getDocumentID());

        findTripForDelete(tripsDetail);

    }

    /**
     * Find a tripDetail with specific information (documentId), if find , then delete
     * @param tripsDetail instance contain userId , documentId of trips
     */
    public void findTripForDelete(TripsDetail tripsDetail){

        db.collection("details")
                .whereEqualTo("userId", tripsDetail.getUserId())
                .whereEqualTo("tripsId", tripsDetail.getTripsId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG", document.getId() + " => " + document.getData());

                                String documentId = document.getId();
                                deleteFromFireBase(documentId);
                                getRemainSeat(tripsDetail.getTripsId(), 1);


                            }
                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }

                });

    }

    private void addOneSeatFree(String tripsId, int remainSeat) {
        db.collection("trips").document(tripsId)
                .update("remainSeats", remainSeat + 1 )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "DocumentSnapshot successfully written!");

                        if(remainSeat + 1 == 1){
                            updateTripsFull(tripsId, 1);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error writing document", e);
                    }
                });



    }

    public void deleteFromFireBase(String documentId){
        db.collection("details").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("phuquoc", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("phuquoc", "Error deleting document", e);
                    }
                });
    }



}