package com.example.realtorapp.repository;

import androidx.annotation.NonNull;

import com.example.realtorapp.model.PropertyListing;
import com.google.firebase.database.FirebaseDatabase;
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

    public void getListing(String listingId, ListingCallback callback) {
        db.child(listingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PropertyListing lisiting = dataSnapshot.getValue(PropertyListing.class);
                callback.onResult(lisiting);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                callback.onResult(null);
            }
        });
    }

    public interface ListingCallback {
        void onResult(PropertyListing listing);
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

