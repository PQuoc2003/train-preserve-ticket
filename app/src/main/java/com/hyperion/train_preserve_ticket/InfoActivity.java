package com.hyperion.train_preserve_ticket;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.model.Users;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private boolean existedUser;

    private FirebaseFirestore db;

    EditText edtName, edtAge, edtPhone, edtAddress;

    Context context;

    Button btUpdate, btBack;

    String userId;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        userId = mAuth.getUid();

        existedUser = false;

        context = InfoActivity.this;


        btUpdate = findViewById(R.id.bt_update_info);
        btBack = findViewById(R.id.bt_back_info);

        edtName = findViewById(R.id.et_info_name);
        edtAge = findViewById(R.id.edt_info_age);
        edtPhone = findViewById(R.id.edt_info_phone);
        edtAddress = findViewById(R.id.edt_info_address);


        TextView tvEmail = findViewById(R.id.tv_info_email);


        tvEmail.setText(mAuth.getCurrentUser().getEmail());


        btUpdate.setOnClickListener(view -> {
            updateInfo();
        });

        btBack.setOnClickListener(v -> finish());


        getInfoUser();

    }

    private void getInfoUser() {
        getUsersById();
    }

    private void updateInfo() {

        if (validate()) {

            String name = edtName.getText().toString();
            int age = Integer.parseInt(edtAge.getText().toString());
            String phone = edtPhone.getText().toString();
            String address = edtAddress.getText().toString();

            Users myUser = new Users(userId, name, age, phone, address);

            if (existedUser) {
                updateUser(myUser);

            } else {
                addUsers(myUser);

            }

        }

    }

    public void presentInfo(Users myUser) {

        edtName.setText(myUser.getName());
        edtAge.setText(String.valueOf(myUser.getAge()));
        edtPhone.setText(myUser.getPhone());
        edtAddress.setText(myUser.getAddress());
    }


    private boolean validate() {
        if (edtName.getText().toString().equals("")) {
            return false;
        }

        if (edtAge.getText().toString().equals("")) {
            return false;
        }
        if (edtPhone.getText().toString().equals("")) {
            return false;
        }
        if (edtAddress.getText().toString().equals("")) {
            return false;
        }

        try {
            int age = Integer.parseInt(edtAge.getText().toString());

        } catch (Exception e) {
            Toast.makeText(this, "age must be a number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    public void addUsers(Users user) {

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                            Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(context, "Add user success", Toast.LENGTH_LONG).show();
                            redirectToHome();
                        }
                )
                .addOnFailureListener(e ->
                        Log.w("Fail", "Error adding document", e)
                );
    }

    private void updateInner(Users user, String documentId) {
        db.collection("users").document(documentId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Debug", "DocumentSnapshot successfully written!");
                        Toast.makeText(context, "Update user success", Toast.LENGTH_LONG).show();
                        redirectToHome();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error writing document", e);
                    }
                });
    }

    private void redirectToHome() {
        intent = new Intent(InfoActivity.this, UserHomeActivity.class);
        finish();
        startActivity(intent);
    }

    public void updateUser(Users user) {
        db.collection("users")
                .whereEqualTo("id", user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG", document.getId() + " => " + document.getData());
                                updateInner(user, document.getId());
                            }

                        } else {
                            Log.w("DEBUG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getUsersById() {

        db.collection("users")
                .whereEqualTo("id", mAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("DEBUG", document.getId() + " => " + document.getData());

                                    Users users = document.toObject(Users.class);
                                    existedUser = true;
                                    presentInfo(users);
                                }

                            } else {
                                Log.w("DEBUG", "Error getting documents.", task.getException());
                            }
                        }
                    }
                });

    }


}