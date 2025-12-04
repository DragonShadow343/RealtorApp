package com.example.realtorapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtorapp.Adapters.PropertyAdapter;
import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;

    List<PropertyListing> listings = new ArrayList<>();

    RecyclerView listingsRecycler;
    PropertyAdapter adapter;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


// this is for menu bar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        //this is for the map view
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.homeMap);
            mapFragment.getMapAsync(this);


            // for the list under the map
         listingsRecycler = findViewById(R.id.listingsRecycler);
         // layout manager for vertical list
        listingsRecycler.setLayoutManager(new LinearLayoutManager(this));
        // connectint to the  adapter
        adapter = new PropertyAdapter(this, listings);
        listingsRecycler.setAdapter(adapter);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    public void onMapReady(GoogleMap map) {
        googleMap = map;
        loadPropertiesFromFirebase();

        // UI features to control map
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    private void placeMapMarkers(List<PropertyListing> list) {

        if (googleMap == null) return; // map not ready yet

        googleMap.clear(); // remove old markers

        for (PropertyListing p : list) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getLattitude(), p.getLongitude()))
                    .title(p.getTitle()));
        }

        // move camera to first listing
        if (!list.isEmpty()) {
            PropertyListing first = list.get(0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(first.getLattitude(), first.getLongitude()),
                    12));
        }
    }

    private void loadPropertiesFromFirebase() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("property_listings");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listings.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    PropertyListing listing = child.getValue(PropertyListing.class);
                    listings.add(listing);
                }
                // this changes the list when new data is added
                adapter.notifyDataSetChanged();
                // this changes the map when new data is added
                placeMapMarkers(listings);  // works because map is ready
            }

            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }


    // this is for the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_upload) {
            startActivity(new Intent(this, UploadListingActivity.class));
            return true;
        }

        if (id == R.id.action_contact) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new SMS_Local_Agents()) // R.id.main matches the XML ID above
                    .addToBackStack(null) // Allows pressing "Back" to return to buttons
                    .commit();
        }

        if (id == R.id.action_filter) {
            startActivityForResult(new Intent(this, FilterActivity.class), 101);
            return true;
        }

        if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Log out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log out", (d, w) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {

            int minPrice = data.getIntExtra("minPrice", 800);
            int maxPrice = data.getIntExtra("maxPrice", 5000);
            int beds = data.getIntExtra("bedrooms", 1);
            int baths = data.getIntExtra("bathrooms", 1);
            boolean pets = data.getBooleanExtra("petFriendly", false);

            applyFilters(minPrice, maxPrice, beds, baths, pets);
        }
    }

    private void applyFilters(int min, int max, int beds, int baths, boolean pets) {

        List<PropertyListing> filtered = new ArrayList<>();

        for (PropertyListing p : listings) {

            boolean matchesPrice = p.getPrice() >= min && p.getPrice() <= max;
            boolean matchesBeds = p.getBedroom() >= beds;
            boolean matchesBaths = p.getBathroom() >= baths;
            boolean matchesPets = !pets || p.isPetFriendly();

            if (matchesPrice && matchesBeds && matchesBaths && matchesPets) {
                filtered.add(p);
            }
        }

        // Update list + map
        adapter.updateList(filtered);
        placeMapMarkers(filtered);
    }




}