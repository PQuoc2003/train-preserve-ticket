package com.hyperion.train_preserve_ticket.service.imp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.model.Users;
import com.hyperion.train_preserve_ticket.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImp implements UserService {


    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    public UserServiceImp() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void addUsers(Users user) {

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Fail", "Error adding document", e));
    }

    @Override
    public List<Users> getUsersById(String id) {

        List<Users> myUsers = new ArrayList<>();

        db.collection("users")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG", document.getId() + " => " + document.getData());


                                Users myUser = document.toObject(Users.class);

                                myUsers.add(myUser);

                            }
                            Log.d("phuquoc", "go 1");
                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }
                });

        if (myUsers.size() != 0){
            Log.d("phuquoc", myUsers.get(0).getName());
        } else {
            Log.d("phuquoc", "nothing");
        }
        return myUsers;
    }
}
