package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Main1Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextView tvEmail;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        mAuth = FirebaseAuth.getInstance();

        tvEmail = findViewById(R.id.tv_email);


        FirebaseUser user = mAuth.getCurrentUser();


        db = FirebaseFirestore.getInstance();


        if(user != null){
            tvEmail.setText(user.getEmail());
        } else {
            tvEmail.setText(R.string.main1_result);
        }


        Map<String, Object> myUser = new HashMap<>();
        myUser.put("id", user.getUid());
        myUser.put("first", "Mama");
        myUser.put("last", "Lovelace");
        myUser.put("born", 1815);

//        addUser(myUser);

        getUser();


    }



    public void addUser(Map myUser){

        db.collection("users")
                .add(myUser)
                .addOnSuccessListener(documentReference -> Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Fail", "Error adding document", e));

    }

    public void getUser(){
        db.collection("users")
                .whereEqualTo("born", 1815)
                .whereEqualTo("first","Ada")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}