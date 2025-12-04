package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realtorapp.R;

public class SMS_BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity_booking_confirmed);

        // Views
        TextView tvDetails = findViewById(R.id.tvConfirmationDetails);
        Button btnGoHome = findViewById(R.id.btnGoHome);

        // Get data
        String agentName = getIntent().getStringExtra("agentName");
        String slot = getIntent().getStringExtra("slot");

        if (agentName != null && slot != null) {
            tvDetails.setText("You have successfully booked an appointment with " + agentName + " for " + slot + ".");
        }

        // Go Home Logic
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMS_BookingConfirmedActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}