package com.hyperion.train_preserve_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hyperion.train_preserve_ticket.dao.BPanelUSearch;
import com.hyperion.train_preserve_ticket.dao.TripsDAO;
import com.hyperion.train_preserve_ticket.model.Trips;

import java.util.Calendar;

public class SearchPanelActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    String[] station = {"Hà Nội", "Đà Nẵng", "Hồ Chí Minh"};

    ArrayAdapter<String> adapterItems;

    private DatePickerDialog datePickerDialog;
    private Button btDatePicker;

    Context context;

    String selectedStation = "";
    String selectedStation1 = "";

    private Spinner startSpinner, endSpinner;

    Button searchTrip, btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_panel);

        context = SearchPanelActivity.this;

        db = FirebaseFirestore.getInstance();


        startSpinner = findViewById(R.id.sp_start_search);
        endSpinner = findViewById(R.id.sp_end_search);


        searchTrip = findViewById(R.id.bt_search_sP);
        btBack = findViewById(R.id.bt_back_sP);


        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_items, station);

        startSpinner.setAdapter(adapterItems);
        startSpinner.setSelection(0);

        endSpinner.setAdapter(adapterItems);
        endSpinner.setSelection(0);


        btDatePicker = findViewById(R.id.bt_date_search_sP);

        initDatePicker();

        btDatePicker.setText(getTodayDate());

        btDatePicker.setOnClickListener(view -> {
            datePickerDialog.show();
        });

        searchTrip.setOnClickListener(v -> {
            searchForTrip();
        });

        btBack.setOnClickListener(v -> finish());

    }

    private void searchForTrip() {


        String date = btDatePicker.getText().toString();

        String start = startSpinner.getSelectedItem().toString();
        String end = endSpinner.getSelectedItem().toString();

        BPanelUSearch items = new BPanelUSearch(start, end, date);

        Intent intent = new Intent(context, UserSearchActivity.class);

        Bundle bundle = new Bundle();

        bundle.putSerializable("values", items);

        intent.putExtra("myBundle", bundle);

        startActivity(intent);


    }



    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;

        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);

    }

    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            btDatePicker.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }
}