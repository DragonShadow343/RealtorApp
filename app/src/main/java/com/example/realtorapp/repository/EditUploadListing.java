package com.example.realtorapp.repository;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.Activities.ConfirmationOfListing;
import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EditUploadListing extends AppCompatActivity {

    String title, owner, email, phone, description, addr1, addr2;
    String bedsStr, bathsStr, priceStr;
    boolean petFriendly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_upload_listing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = getIntent().getStringExtra("title");
        owner = getIntent().getStringExtra("owner");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        description = getIntent().getStringExtra("description");
        addr1 = getIntent().getStringExtra("addr1");
        addr2 = getIntent().getStringExtra("addr2");
        bedsStr = getIntent().getStringExtra("beds");
        bathsStr = getIntent().getStringExtra("baths");
        priceStr = getIntent().getStringExtra("price");
        petFriendly = getIntent().getBooleanExtra("petFriendly", false);

        TextView InfoText = findViewById(R.id.InfoText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView TitleView = findViewById(R.id.TitleText);
        TextView TextDisplay = findViewById(R.id.TextDisplay);
        Button EditButton = findViewById(R.id.EditButton);
        Button ButtonUpload = findViewById(R.id.ButtonUpload);

        TitleView.setText(title);

        String details = "Owner: " + owner +
                "\nEmail: " + email +
                "\nPhone: " + phone +
                "\nAddress: " + addr1 +" " + addr2 +
                "\nBeds: " + bedsStr +
                "\nBaths: " + bathsStr +
                "\nPrice: $" + priceStr +
                "\nPet Friendly: " + (petFriendly ? "Yes" : "No") +
                "\n\nDescription:\n" + description;

        TextDisplay.setText(details);

        ButtonUpload.setOnClickListener(v -> uploadToFirebase());

        EditButton.setOnClickListener(v ->
                finish()
        );

    }

    private void uploadToFirebase() {

        int beds = Integer.parseInt(bedsStr);
        int baths = Integer.parseInt(bathsStr);
        double price = Double.parseDouble(priceStr);

        // Address + geocode
        String fullAddress = addr1 + ", " + addr2;

        double latitude = 0;
        double longitude = 0;

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> locations = geocoder.getFromLocationName(fullAddress, 1);
            if (locations != null && !locations.isEmpty()) {
                latitude = locations.get(0).getLatitude();
                longitude = locations.get(0).getLongitude();
            }
        } catch (Exception ignored) {}

        // ---- Firebase Upload ----
        String listingId = UUID.randomUUID().toString();
        PropertyListing listing = new PropertyListing();

        listing.setListingId(listingId);
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setAddress(fullAddress);
        listing.setPrice(price);
        listing.setBedroom(beds);
        listing.setBathroom(baths);
        listing.setPetFriendly(petFriendly);
        listing.setAgentId(FirebaseAuth.getInstance().getUid());
        listing.setLattitude(latitude);
        listing.setLongitude(longitude);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("property_listings");

        ref.child(listingId).setValue(listing)
                .addOnSuccessListener(a ->
                        Toast.makeText(this, "Listing Uploaded Successfully!", Toast.LENGTH_LONG).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );

        Intent done = new Intent(this, ConfirmationOfListing.class);

        done.putExtra("title", title);
        done.putExtra("owner", owner);
        done.putExtra("email", email);
        done.putExtra("phone", phone);
        done.putExtra("description", description);
        done.putExtra("addr1", addr1);
        done.putExtra("addr2", addr2);
        done.putExtra("beds", bedsStr);
        done.putExtra("baths", bathsStr);
        done.putExtra("price", priceStr);
        done.putExtra("petFriendly", petFriendly);

        startActivity(done);
        finish();
    }
}

