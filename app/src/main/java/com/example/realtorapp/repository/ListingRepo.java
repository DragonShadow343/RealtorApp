package com.example.realtorapp.repository;

import com.example.realtorapp.model.PropertyListing;
import com.google.firebase.database.FirebaseDatabase;
import com.example.realtorapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ListingRepo {
    private DatabaseReference db;

    public ListingRepo() {
        db = FirebaseDatabase.getInstance().getReference("listings");
    }

    public void getAllListings(String userId) {
        db.orderByChild("userId").equalTo(userId);
    }

    public void getListing(String userId, String listingId) {
        db.child("userId").equalTo(listingId);
    }

    public void addListing(PropertyListing listing) {
        String listingId = db.push().getKey();
        if (listingId != null) {
            // Save the booking object under the new key
            db.child(listingId).setValue(listing);
        }
    }

    public void updateListing(String listingId, PropertyListing listing) {
        db.child(listingId).setValue(listing);
    }

    public void deleteListing(String listingId) {
        db.child(listingId).removeValue();
    }

    public void getListingByAgent(String agentId) {
        db.orderByChild("agentId").equalTo(agentId);
    }

}
