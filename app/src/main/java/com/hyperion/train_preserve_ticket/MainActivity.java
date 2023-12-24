package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Context context;
    EditText email , password;

    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        context = MainActivity.this;

        btRegister = findViewById(R.id.btRegister);
        email = findViewById(R.id.edEmail);
        password = findViewById(R.id.edPass);

        btRegister.setOnClickListener(v -> {
            String myEmail = email.getText().toString();
            String myPass = password.getText().toString();
            register(myEmail, myPass);
        });

    }

    private void register(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(MainActivity.this, Main1Activity.class);

                            startActivity(intent);

                        } else {
                            Toast.makeText(context, "Đăng kí thất bại", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}