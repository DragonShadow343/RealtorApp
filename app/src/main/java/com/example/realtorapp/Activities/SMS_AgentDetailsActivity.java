package com.example.realtorapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

        // Initialize Views
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageView imgProfile = findViewById(R.id.imgDetailProfile);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvRating = findViewById(R.id.tvDetailRating);
        TextView tvBio = findViewById(R.id.tvDetailBio);
        ImageButton btnCall = findViewById(R.id.btnCall);
        ImageButton btnEmail = findViewById(R.id.btnEmail);
        Button btnScheduleMeeting = findViewById(R.id.btnScheduleMeeting);

        // Retrieve data from intent
        String name = getIntent().getStringExtra("name");
        String bio = getIntent().getStringExtra("bio");
        String imageUrl = getIntent().getStringExtra("image");
        double rating = getIntent().getDoubleExtra("rating", 0.0);

        // Populate UI
        if (name != null) tvName.setText(name);
        if (bio != null) tvBio.setText(bio);
        tvRating.setText("Rating: " + rating + " ⭐");

        // Load Profile Image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.ic_delete)
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // Back Navigation
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Dial Button Action
        btnCall.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:1234567890"));
            try {
                startActivity(dialIntent);
            } catch (Exception e) {
                Toast.makeText(SMS_AgentDetailsActivity.this, "Unable to open dialer", Toast.LENGTH_SHORT).show();
            }
        });

        // Email Button Action
        btnEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"agent@example.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry from RealtorApp");

            try {
                startActivity(emailIntent);
            } catch (Exception e) {
                Toast.makeText(SMS_AgentDetailsActivity.this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        // Schedule Meeting Action
        if (btnScheduleMeeting != null) {
            btnScheduleMeeting.setOnClickListener(v -> {
                Intent intent = new Intent(SMS_AgentDetailsActivity.this, BookingActivity.class);
                intent.putExtra("agentName", name);
                startActivity(intent);
            });
        }
    }
}