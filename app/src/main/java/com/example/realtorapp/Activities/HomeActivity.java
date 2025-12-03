package com.example.realtorapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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


}