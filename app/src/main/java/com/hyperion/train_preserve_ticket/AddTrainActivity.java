package com.hyperion.train_preserve_ticket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.Users;

import java.util.Calendar;
import java.util.Locale;

public class AddTrainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    String[] station = {"Hà Nội", "Đà Nẵng", "Hồ Chí Minh"};

    String[] trains = {"Thống Nhất", "Độc Lập", "Tự Do"};

    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItemsTrain;


    Button btAddTrain, btBack;

    private DatePickerDialog datePickerDialog;
    private Button btDatePicker;
    private Button btTimePicker;

    private int hour, minute;

    private EditText etdPrice;

    private Spinner  startSpinner, endSpinner, trainSpinner;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_train);

        context = AddTrainActivity.this;

        db = FirebaseFirestore.getInstance();

        btAddTrain = findViewById(R.id.bt_add_train);
        btBack = findViewById(R.id.bt_back_menu);

        etdPrice = findViewById(R.id.edt_price);

        startSpinner = findViewById(R.id.sp_start_add);
        endSpinner = findViewById(R.id.sp_end_add);
        trainSpinner = findViewById(R.id.sp_train_add);

        adapterItems = new ArrayAdapter<String>(this, R.layout.spinner_items, station);
        adapterItemsTrain = new ArrayAdapter<String>(this, R.layout.spinner_items, trains);

        startSpinner.setAdapter(adapterItems);
        startSpinner.setSelection(0);

        endSpinner.setAdapter(adapterItems);
        endSpinner.setSelection(0);

        trainSpinner.setAdapter(adapterItemsTrain);
        trainSpinner.setSelection(0);



        btAddTrain.setOnClickListener(v -> {
            addTrainClick();
        });


        btDatePicker = findViewById(R.id.bt_date_pick_add_train);

        initDatePicker();

        btDatePicker.setText(getTodayDate());

        btDatePicker.setOnClickListener(view -> {
            datePickerDialog.show();
        });

        btTimePicker = findViewById(R.id.bt_time_pick);

        btTimePicker.setOnClickListener(this::popTimePicker);

        btBack.setOnClickListener(v -> finish());

    }

    private void addTrainClick() {

        String start = startSpinner.getSelectedItem().toString();
        String end = endSpinner.getSelectedItem().toString();
        String train = trainSpinner.getSelectedItem().toString();
        String date = btDatePicker.getText().toString();
        String time = btTimePicker.getText().toString();

        if(etdPrice.getText().toString().equals("")){
            Toast.makeText(context, "Please fill price", Toast.LENGTH_LONG).show();
            return;
        }

        double price;

        try {
            price = Double.parseDouble(etdPrice.getText().toString());
        } catch (Exception e){
            Toast.makeText(context, "Invalid price", Toast.LENGTH_LONG).show();
            etdPrice.setText("");
            return;
        }


        Trips newTrip = new Trips(start, end, train, date, time, price);

        addNewTrips(newTrip);

    }

    private void addNewTrips(Trips newTrip) {

        db.collection("trips")
                .add(newTrip)
                .addOnSuccessListener(documentReference -> {

                        Log.d("phuquoc", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(context, "Your trips has been successfully added", Toast.LENGTH_LONG).show();
                        resetField();
                        }


                )
                .addOnFailureListener(e ->
                        Log.w("phuquoc", "Error adding document", e)

                );
    }

    private void resetField() {
        etdPrice.setText("");
        btDatePicker.setText(getTodayDate());
        btTimePicker.setText(R.string.add_train_default_time);
    }

    private void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                btTimePicker.setText(String.format(Locale.getDefault(), "%02d : %02d", hour, minute));

            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
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