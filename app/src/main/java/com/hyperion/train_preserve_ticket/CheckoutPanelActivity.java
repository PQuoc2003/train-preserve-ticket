package com.hyperion.train_preserve_ticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutPanelActivity extends AppCompatActivity {

    private Button btSearch, btBack;

    private EditText edtTicket;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_panel);

        context = CheckoutPanelActivity.this;

        btSearch = findViewById(R.id.bt_search_cP);
        btBack = findViewById(R.id.bt_back_sP);

        edtTicket = findViewById(R.id.edt_tkId_cP);


        btSearch.setOnClickListener(view -> {
            searchForCheckOut();
        });

        btBack.setOnClickListener(v -> finish());

    }

    private void searchForCheckOut() {

        String phone = edtTicket.getText().toString();

        if(phone.equals("")){
            Toast.makeText(context, "Please fill ticket ID field", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(CheckoutPanelActivity.this, TicketCheckoutActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);


    }
}