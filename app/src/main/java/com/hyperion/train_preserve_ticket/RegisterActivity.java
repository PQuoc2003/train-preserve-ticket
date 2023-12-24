package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Context context;

    EditText email, password, confirm;

    Button btRegister;

    TextView tvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        context = RegisterActivity.this;

        tvLogin = findViewById(R.id.register_tv_login);

        btRegister = findViewById(R.id.register_button);

        email = findViewById(R.id.register_edt_email);

        password = findViewById(R.id.register_pass);

        confirm = findViewById(R.id.register_confirm_pass);

        btRegister.setOnClickListener(view -> {

            if (!password.getText().toString().equals(confirm.getText().toString())) {
                Toast.makeText(context, "Password and confirm does not match", Toast.LENGTH_LONG).show();
                password.setText("");
                confirm.setText("");
                return;
            }

            if (password.getText().toString().length() < 6) {
                Toast.makeText(context, "Password must at least 6 character", Toast.LENGTH_LONG).show();
                password.setText("");
                confirm.setText("");
                return;
            }


            register(email.getText().toString(), password.getText().toString(), email);

        });


        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
            startActivity(intent);
        });

    }

    private void register(String email, String password, EditText edt_email) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                            startActivity(intent);

                        } else {
                            Toast.makeText(context, "Email has been register before", Toast.LENGTH_LONG).show();
                            edt_email.setText("");
                        }
                    }
                });
    }
}