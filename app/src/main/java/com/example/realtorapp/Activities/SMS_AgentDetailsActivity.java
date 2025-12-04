package com.example.realtorapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realtorapp.R;
import com.squareup.picasso.Picasso;

public class SMS_AgentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_agent_details);

        // Setup UI components
        ImageButton backButton = findViewById(R.id.btnBack);
        ImageView profileImage = findViewById(R.id.imgDetailProfile);
        TextView nameView = findViewById(R.id.tvDetailName);
        TextView ratingView = findViewById(R.id.tvDetailRating);
        TextView bioView = findViewById(R.id.tvDetailBio);
        ImageButton callButton = findViewById(R.id.btnCall);
        ImageButton emailButton = findViewById(R.id.btnEmail);
        Button scheduleButton = findViewById(R.id.btnScheduleMeeting);

        // Get intent data
        String agentName = getIntent().getStringExtra("name");
        String agentBio = getIntent().getStringExtra("bio");
        String agentImage = getIntent().getStringExtra("image");
        double agentRating = getIntent().getDoubleExtra("rating", 0.0);

        // Set data to views
        if (agentName != null) nameView.setText(agentName);
        if (agentBio != null) bioView.setText(agentBio);
        ratingView.setText("Rating: " + agentRating + " ⭐");

        // Image loading logic
        if (agentImage != null && !agentImage.isEmpty()) {
            Picasso.get()
                    .load(agentImage)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.ic_delete)
                    .into(profileImage);
        } else {
            profileImage.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // Handle back press
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Handle phone call
        callButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:1234567890"));
            try {
                startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Cannot open dialer", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle email
        emailButton.setOnClickListener(v -> {
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:"));
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"agent@example.com"});
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");

            try {
                startActivity(mailIntent);
            } catch (Exception e) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        // Open booking screen
        if (scheduleButton != null) {
            scheduleButton.setOnClickListener(v -> {
                Intent bookingIntent = new Intent(SMS_AgentDetailsActivity.this, BookingActivity.class);
                bookingIntent.putExtra("agentName", agentName);
                startActivity(bookingIntent);
            });
        }
    }
}