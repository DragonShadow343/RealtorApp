package com.example.realtorapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

public class FavouriteManager {
    private static final String FAVOURITES_KEY = "favourites";
    private static final String KEY_IDS = "ids";

    private static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private static String getUserId() {
        FirebaseUser user = getUser();
        return user != null ? user.getUid() : null;
    }

    // Local Storage using SharedPreferences

    private static Set<String> getLocalFavourites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FAVOURITES_KEY, Context.MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(KEY_IDS, new HashSet<>()));
    }

    private static void saveLocalFavourites(Context context, Set<String> favs) {
        SharedPreferences prefs = context.getSharedPreferences(FAVOURITES_KEY, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(KEY_IDS, favs).apply();
    }

    // Firebase Storage when logged in

    private static DatabaseReference getFirebaseRef() {
        String userId = getUserId();
        return FirebaseDatabase.getInstance().getReference("users").child(userId).child("favourites");
    }

    // Public Methods to manage favourites

    public static void toggleFavourite(Context context, String listingId) {
        if (getUserId() == null) {
            Set<String> favs = getLocalFavourites(context);
            if (favs.contains(listingId)) {
                favs.remove(listingId);
            } else {
                favs.add(listingId);
            }
            saveLocalFavourites(context, favs);
            return;
        }

        DatabaseReference ref = getFirebaseRef().child(listingId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ref.setValue(null); // remove listing from favourites
                } else {
                    ref.setValue(true); // add listing from favourites
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public static void isFavourite(Context context, String listingId, FavouriteCheckCallback callback) {

        // Local
        if (getUserId() == null) {
            callback.onResult(getLocalFavourites(context).contains(listingId));
            return;
        }

        // Firebase
        getFirebaseRef().child(listingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onResult(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public static void getFavourites(Context context, FavouritesListCallback callback) {

        // Local
        if (getUserId() == null) {
            callback.onResult(new HashSet<>(getLocalFavourites(context)));
            return;
        }

        // Firebase
        getFirebaseRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> ids = new HashSet<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ids.add(child.getKey());
                }
                callback.onResult(ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(new HashSet<>());
            }
        });

    }
    public static void syncLocalToFirebase(Context context) {
        String userId = getUserId();
        if (userId == null) return;

        Set<String> localFavs = getLocalFavourites(context);

        DatabaseReference ref = getFirebaseRef();
        for (String id: localFavs) {
            ref.child(id).setValue(true);
        }

        saveLocalFavourites(context, new HashSet<>());
    }

    private interface FavouriteCheckCallback {void onResult(boolean isFavourite);}

    private interface FavouritesListCallback {void onResult(Set<String> ids);}
}
