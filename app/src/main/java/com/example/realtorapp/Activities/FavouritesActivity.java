package com.example.realtorapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtorapp.Adapters.FavouriteAdapter;
import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.example.realtorapp.repository.ListingRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtNoFavorites;

    FavouriteAdapter adapter;
    List<PropertyListing> favouritesList = new ArrayList<>();

    ListingRepo listingRepo = new ListingRepo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.recyclerFavorites);
        txtNoFavorites = findViewById(R.id.txtNoFavorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavourites();

    }

    private void loadFavourites() {
        SharedPreferences prefs = getSharedPreferences("favourites", MODE_PRIVATE);
        Set<String> favouriteIds = prefs.getStringSet("ids", new HashSet<>());

        // Clear any previous data
        favouritesList.clear();

        adapter = new FavouriteAdapter(favouritesList, FavouritesActivity.this);
        recyclerView.setAdapter(adapter);

        if (favouriteIds == null || favouriteIds.isEmpty()) {
            txtNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        txtNoFavorites.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        for (String id : favouriteIds) {
            listingRepo.getListing(id, listing -> {
                if (listing != null) {
                    favouritesList.add(listing);
                }

                // Ensure adapter is created once and notify changes on the UI thread
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            });
        }
    }

    public void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}