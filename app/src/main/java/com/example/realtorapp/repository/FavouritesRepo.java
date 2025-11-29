package com.example.realtorapp.repository;

import com.example.realtorapp.model.PropertyListing;
import com.example.realtorapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavouritesRepo {

    private DatabaseReference db;
    public FavouritesRepo() {
        db = FirebaseDatabase.getInstance().getReference("favourites");
    }

    public void saveFavourite(String userId, PropertyListing listing) {
        db.child(userId).setValue(listing);
    }

    public void deleteFavourite(String ListingId) {
        db.child(ListingId).removeValue();
    }

    public void getFavouritesForUser(String userId) {
        db.orderByChild("userId").equalTo(userId);
    }
}
