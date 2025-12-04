package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realtorapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView tvTitle;
    CalendarView calendarView;
    RadioGroup radioGroupSlots;
    LinearLayout layoutDetails;
    EditText etFirstName, etLastName, etPhone, etEmail;
    Button btnConfirmBooking;

    String agentName;
    String selectedDate;
    String selectedSlot;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity_booking);

        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvBookingTitle);
        calendarView = findViewById(R.id.calendarView);
        radioGroupSlots = findViewById(R.id.radioGroupSlots);
        layoutDetails = findViewById(R.id.layoutDetails);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        // Reference to bookings node
        databaseReference = FirebaseDatabase.getInstance().getReference("Shukan").child("bookings");

        agentName = getIntent().getStringExtra("agentName");
        if (agentName != null) {
            tvTitle.setText("Book with " + agentName);
        }

        long currentTime = System.currentTimeMillis();
        calendarView.setMinDate(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(currentTime);

        checkFirebaseAvailability();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Ensure consistent format
                selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                
                radioGroupSlots.clearCheck();
                layoutDetails.setVisibility(View.GONE);
                selectedSlot = null;

                checkFirebaseAvailability();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        radioGroupSlots.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRb = findViewById(checkedId);
            if (selectedRb != null) {
                selectedSlot = selectedRb.getText().toString();
                layoutDetails.setVisibility(View.VISIBLE);
            }
        });

        btnConfirmBooking.setOnClickListener(v -> {
            String first = etFirstName.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (first.isEmpty() || last.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(BookingActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            saveBookingToFirebase(first, last, phone, email);
        });
    }

    private void checkFirebaseAvailability() {
        databaseReference.orderByChild("agentName").equalTo(agentName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Reset visibility
                        for (int i = 0; i < radioGroupSlots.getChildCount(); i++) {
                            radioGroupSlots.getChildAt(i).setVisibility(View.VISIBLE);
                        }

                        // Hide slots that match date & agent
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String date = data.child("date").getValue(String.class);
                            String slot = data.child("slot").getValue(String.class);

                            if (selectedDate.equals(date) && slot != null) {
                                hideSlot(slot);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BookingActivity.this, "Failed to load slots", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hideSlot(String bookedSlot) {
        for (int i = 0; i < radioGroupSlots.getChildCount(); i++) {
            View view = radioGroupSlots.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton rb = (RadioButton) view;
                if (rb.getText().toString().equals(bookedSlot)) {
                    rb.setVisibility(View.GONE);
                }
            }
        }
    }

    private void saveBookingToFirebase(String first, String last, String phone, String email) {
        String bookingId = databaseReference.push().getKey();
        if (bookingId == null) return;

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("agentName", agentName);
        bookingData.put("date", selectedDate);
        bookingData.put("slot", selectedSlot);
        bookingData.put("firstName", first);
        bookingData.put("lastName", last);
        bookingData.put("phone", phone);
        bookingData.put("email", email);

        databaseReference.child(bookingId).setValue(bookingData)
                .addOnSuccessListener(aVoid -> {
                    Intent intent = new Intent(BookingActivity.this, SMS_BookingConfirmedActivity.class);
                    intent.putExtra("agentName", agentName);
                    intent.putExtra("slot", selectedDate + " at " + selectedSlot);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookingActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}