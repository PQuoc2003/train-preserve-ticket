package com.hyperion.train_preserve_ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {


    Button btAdd, btTrips, btCheckout, btLogout;

    private FirebaseAuth mAuth;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        btAdd = findViewById(R.id.bt_add_admin);
        btTrips = findViewById(R.id.bt_train_admin);
        btCheckout = findViewById(R.id.bt_checkout_admin);
        btLogout = findViewById(R.id.bt_logout_admin);

        mAuth = FirebaseAuth.getInstance();

        setUpListener();
    }

    private void setUpListener() {

        btAdd.setOnClickListener(v -> {
            intent = new Intent(AdminActivity.this, AddTrainActivity.class);
            startActivity(intent);
        });

        btTrips.setOnClickListener(v -> {
            intent = new Intent(AdminActivity.this, TripsActivity.class);
            startActivity(intent);
        });

        btCheckout.setOnClickListener(v -> {
            intent = new Intent(AdminActivity.this, CheckoutPanelActivity.class);
            startActivity(intent);
        });

        btLogout.setOnClickListener(v -> {
            mAuth.signOut();
            intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}