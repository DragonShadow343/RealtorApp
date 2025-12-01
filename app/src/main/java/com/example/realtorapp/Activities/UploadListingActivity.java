package com.example.realtorapp.Activities;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addListingToFirebase() {

        String title = TitleListing.getText().toString().trim();
        String owner = OwnerListing.getText().toString().trim();
        String email = EmailListing.getText().toString().trim();
        String phone = OwnerPhone.getText().toString().trim();
        String description = NotesOptional.getText().toString().trim();

        String addr1 = Address1.getText().toString().trim();
        String addr2 = Address2.getText().toString().trim();
        int beds = Integer.parseInt(BedsNo.getText().toString().trim());
        int baths = Integer.parseInt(NoBath.getText().toString().trim());
        double price = Double.parseDouble(PriceListing.getText().toString().trim());

        boolean petFriendly = PetYes.isChecked();

        if (title.isEmpty() || owner.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || addr1.isEmpty() || price == 0 ||
                beds == 0 || baths == 0) {

            Toast.makeText(this, "Please fill in all required fields with '*'", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullAddress = addr1 + ", " + addr2;
        double latitude = 0;
        double longitude = 0;

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> locations = geocoder.getFromLocationName(fullAddress, 1);

            if (locations != null && !locations.isEmpty()) {
                Address loc = locations.get(0);
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
            } else {
                Toast.makeText(this, "Unable to find this address", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoder Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


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
        listing.setStatus("Pending");
        listing.setLattitude(latitude);
        listing.setLongitude(longitude);


        DatabaseReference ref =
                FirebaseDatabase.getInstance().getReference("property_listings");

        ref.child(listingId)
                .setValue(listing)
                .addOnSuccessListener(a -> {
                    Toast.makeText(this, "Listing Uploaded Successfully!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public void uploadListing(View view) {
        addListingToFirebase();
    }
}
