package com.example.realtorapp.repository;

import com.example.realtorapp.model.Booking;
import com.example.realtorapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingRepo {
    private DatabaseReference db;
    public BookingRepo() {
        db = FirebaseDatabase.getInstance().getReference("bookings");
    }

    public void createBooking(Booking booking) {
        String bookingId = db.push().getKey();
        if (bookingId != null) {
            // Save the booking object under the new key
            db.child(bookingId).setValue(booking);
        }
    }

    public void updateBooking(String bookingId, Booking booking) {
        db.child(bookingId).setValue(booking);
    }

    public void deleteBooking(String bookingId) {
        db.child(bookingId).removeValue();
    }

    public void getBookingsForUser(String userId) {
        db.orderByChild("userId").equalTo(userId);
    }

    public void getBookingsForAgent(String agentId) {
        db.orderByChild("agentId").equalTo(agentId);
    }

}
