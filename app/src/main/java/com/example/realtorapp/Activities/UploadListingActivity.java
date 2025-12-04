package com.example.realtorapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton; // codeSMS
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.example.realtorapp.repository.EditUploadListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class UploadListingActivity extends AppCompatActivity {

    EditText TitleListing, OwnerListing, EmailListing, OwnerPhone, Address1, Address2,
            BedsNo, NoBath, PriceListing, NotesOptional;
    RadioButton PetYes;
    ImageButton btnBackHome; // codeSMS


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_listing);

        TitleListing = findViewById(R.id.TitleListing);
        OwnerListing = findViewById(R.id.OwnerListing);
        EmailListing = findViewById(R.id.EmailListing);
        OwnerPhone = findViewById(R.id.OwnerPhone);
        Address1 = findViewById(R.id.Address1);
        Address2 = findViewById(R.id.Address2);
        BedsNo = findViewById(R.id.BedsNo);
        NoBath = findViewById(R.id.NoBath);
        PriceListing = findViewById(R.id.PriceListing);
        NotesOptional = findViewById(R.id.NotesOptional);
        PetYes = findViewById(R.id.PetYes);
        btnBackHome = findViewById(R.id.btnBackHome); // codeSMS
        
        // codeSMS: Back Button Listener
        if (btnBackHome != null) {
            btnBackHome.setOnClickListener(v -> finish());
        }
        
        findViewById(R.id.CheckButton).setOnClickListener(v -> goToReviewPage());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void goToReviewPage() {

        String title = TitleListing.getText().toString().trim();
        String owner = OwnerListing.getText().toString().trim();
        String email = EmailListing.getText().toString().trim();
        String phone = OwnerPhone.getText().toString().trim();
        String description = NotesOptional.getText().toString().trim();

        String addr1 = Address1.getText().toString().trim();
        String addr2 = Address2.getText().toString().trim();
        String bedsStr = BedsNo.getText().toString().trim();
        String bathsStr = NoBath.getText().toString().trim();
        String priceStr = PriceListing.getText().toString().trim();

        boolean petFriendly = PetYes.isChecked();


        if (title.isEmpty() || owner.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || addr1.isEmpty() ||
                bedsStr.isEmpty() || bathsStr.isEmpty() || priceStr.isEmpty()) {

            Toast.makeText(this, "Please fill in all required fields with '*'", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent reviewPage = new Intent(this, EditUploadListing.class);

        reviewPage.putExtra("title", title);
        reviewPage.putExtra("owner", owner);
        reviewPage.putExtra("email", email);
        reviewPage.putExtra("phone", phone);
        reviewPage.putExtra("description", description);
        reviewPage.putExtra("addr1", addr1);
        reviewPage.putExtra("addr2", addr2);
        reviewPage.putExtra("beds", bedsStr);
        reviewPage.putExtra("baths", bathsStr);
        reviewPage.putExtra("price", priceStr);
        reviewPage.putExtra("petFriendly", petFriendly);

        startActivity(reviewPage);
    }
}