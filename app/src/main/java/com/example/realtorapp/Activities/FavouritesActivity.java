package com.example.realtorapp.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtorapp.Adapters.PropertyAdapter;
import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.example.realtorapp.repository.ListingRepo;
import com.example.realtorapp.utils.FavouriteManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtNoFavorites;

    PropertyAdapter adapter;
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
        FavouriteManager.getFavourites(this, favouriteIds -> {
            // Clear any previous data
            favouritesList.clear();

            if (favouriteIds == null || favouriteIds.isEmpty()) {
                txtNoFavorites.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            }

            txtNoFavorites.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (adapter == null) {
                adapter = new PropertyAdapter(FavouritesActivity.this, favouritesList);
                recyclerView.setAdapter(adapter);
            }

            final int total = favouriteIds.size();
            final int[] loaded = {0};


            for (String id : favouriteIds) {
                listingRepo.getListing(id, listing -> {
                    if (listing != null) {
                        favouritesList.add(listing);
                    }

                    loaded[0]++;

                    // Ensure adapter is created once and notify changes on the UI thread
                    if (loaded[0] == total) {
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                });
            }
        });
    }
}