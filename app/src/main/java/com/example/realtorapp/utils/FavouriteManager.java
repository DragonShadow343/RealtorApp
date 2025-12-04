package com.example.realtorapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
        if (userId == null) return null;

        return FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("favourites");
    }

    // Public Methods to manage favourites

    public static void toggleFavourite(Context context, String listingId, FavouriteCheckCallback callback) {
        if (getUserId() == null) {
            Set<String> favs = getLocalFavourites(context);

            boolean nowFav;

            if (favs.contains(listingId)) {
                favs.remove(listingId);
                nowFav=false;
            } else {
                favs.add(listingId);
                nowFav=true;
            }
            saveLocalFavourites(context, favs);
            callback.onResult(nowFav);
            return;
        }

        DatabaseReference ref = getFirebaseRef().child(listingId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ref.removeValue().addOnSuccessListener(unused -> callback.onResult(false)); // remove listing from favourites
                } else {
                    ref.setValue(true).addOnSuccessListener(unused -> callback.onResult(true)); // add listing from favourites
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    public static void isFavourite(Context context, String listingId, FavouriteToggleCallback callback) {

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
        if (getUserId() == null) return;

        Set<String> localFavs = getLocalFavourites(context);

        DatabaseReference ref = getFirebaseRef();
        for (String id: localFavs) {
            ref.child(id).setValue(true);
        }

        saveLocalFavourites(context, new HashSet<>());
    }

    public interface FavouriteCheckCallback {void onResult(boolean isFavourite);}

    public interface FavouriteToggleCallback {void onResult(boolean nowFavourite);}
    public interface FavouritesListCallback {void onResult(Set<String> ids);}
}
