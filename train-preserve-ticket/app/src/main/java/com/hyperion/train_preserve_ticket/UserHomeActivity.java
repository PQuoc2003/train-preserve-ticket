package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.model.Users;

public class UserHomeActivity extends AppCompatActivity {


    Button btSearch, btHistory, btInfo, btLogout;

    Intent intent;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        btSearch = findViewById(R.id.bt_search_uH);
        btHistory = findViewById(R.id.bt_history_uH);
        btInfo = findViewById(R.id.bt_info_uH);
        btLogout = findViewById(R.id.bt_logout_uH);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setListener();

        checkInfo();

    }

    private void checkInfo() {
        db.collection("users")
                .whereEqualTo("id", mAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.isEmpty()) {
                            intent = new Intent(UserHomeActivity.this, InfoActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void setListener() {

        btSearch.setOnClickListener(v -> {
            intent = new Intent(UserHomeActivity.this, SearchPanelActivity.class);
            startActivity(intent);
        });

        btHistory.setOnClickListener(v -> {
            intent = new Intent(UserHomeActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btInfo.setOnClickListener(v -> {
            intent = new Intent(UserHomeActivity.this, InfoActivity.class);
            startActivity(intent);
        });

        btLogout.setOnClickListener(v -> {
            mAuth.signOut();
            intent = new Intent(UserHomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}

