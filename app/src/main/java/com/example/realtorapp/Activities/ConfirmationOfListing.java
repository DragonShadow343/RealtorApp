package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;

public class ConfirmationOfListing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation_of_listing);

        TextView finalMessage = findViewById(R.id.FinalDisplay);
        Button goHome = findViewById(R.id.GoHomeButton);

        // Get all data from intent
        String title = getIntent().getStringExtra("title");
        String owner = getIntent().getStringExtra("owner");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String description = getIntent().getStringExtra("description");
        String addr1 = getIntent().getStringExtra("addr1");
        String addr2 = getIntent().getStringExtra("addr2");
        String beds = getIntent().getStringExtra("beds");
        String baths = getIntent().getStringExtra("baths");
        String price = getIntent().getStringExtra("price");
        boolean petFriendly = getIntent().getBooleanExtra("petFriendly", false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    String FinalText = "• Title: " + title + "\n" +
                    "• Owner: " + owner + "\n" +
                    "• Email: " + email + "\n" +
                    "• Phone: " + phone + "\n" +
                    "• Address 1: " + addr1 + " " + addr2 + "\n" +
                    "• Beds: " + beds + "\n" +
                    "• Baths: " + baths + "\n" +
                    "• Price: $" + price + "\n" +
                    "• Pet Friendly: " + (petFriendly ? "Yes" : "No") + "\n\n" +
                    "Description:\n" + description;
        finalMessage.setText(FinalText);

        goHome.setOnClickListener(v -> {
        Intent home = new Intent(this, HomeActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(home);
        finish();
    });
}
}
