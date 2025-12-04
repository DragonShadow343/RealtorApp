package com.example.realtorapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;

import java.util.Calendar;
import java.util.Date;

public class ContactAgentInfoCard extends AppCompatActivity {

    private Button callBtn, emailBtn, ConfirmMeeting;
    private CalendarView calendarView;
    private Spinner timeSlotPick;
    private String phone, email;
    private long selectedDate = Calendar.getInstance().getTimeInMillis();



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_agent_info_card);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton backButton = findViewById(R.id.btnBack);

        callBtn = findViewById(R.id.CallBtn);
        emailBtn = findViewById(R.id.MailBtn);
        calendarView = findViewById(R.id.calendarView);
        timeSlotPick = findViewById(R.id.TimeSlot);
        ConfirmMeeting = findViewById(R.id.ScheduleConfirmBtn);

        phone = getIntent().getStringExtra("ownerPhone");
        email = getIntent().getStringExtra("ownerEmail");

        setupTimeSlots();

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            selectedDate = calendar.getTimeInMillis();
        });

    }
        private void setupTimeSlots() {
            String[] slots = {"9:00 - 10:00AM", "10:00-11:00 AM", "11:00- 12:00pm" ,"12:00-1:00 PM", "1:00-2:00PM", "2:00-3:00 PM"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    slots
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSlotPick.setAdapter(adapter);
        }
    private void makePhoneCall() {
        if (phone == null || phone.isEmpty()) {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    private void sendEmail() {
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "No email available", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        startActivity(emailIntent);
    }


    private void confirmMeeting() {
        if (selectedDate == 0) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeSlot = timeSlotPick.getSelectedItem().toString();

        Toast.makeText(
                this,
                "Meeting booked for " + new Date(selectedDate) + " at " + timeSlot,
                Toast.LENGTH_LONG
        ).show();
        Intent intent = new Intent(ContactAgentInfoCard.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public boolean onSupportNavigateUp() {
        finish();   // go back to previous activity (home or favourites automatically)
        return true;
    }

    public void onCallClicked(View view) {
        makePhoneCall();
    }

    public void onEmailClicked(View view) {
        sendEmail();
    }

    public void onConfirmMeetingClicked(View view) {
        confirmMeeting();
    }

}


