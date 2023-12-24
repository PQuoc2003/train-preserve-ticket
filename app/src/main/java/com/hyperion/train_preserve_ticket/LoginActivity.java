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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        context = LoginActivity.this;

        TextView tvRegister = findViewById(R.id.register_tv_login);

        Button btLogin = findViewById(R.id.register_button);
        EditText email = findViewById(R.id.register_edt_email);
        EditText password = findViewById(R.id.register_confirm_pass);

        btLogin.setOnClickListener(v -> {
            String myEmail = email.getText().toString();
            String myPass = password.getText().toString();
            login(myEmail, myPass);
        });

        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent ;

                            if(email.equals("admin@gmail.com")){
                                intent = new Intent(LoginActivity.this, AdminActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                            }

                            startActivity(intent);

                        } else {
                            Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}