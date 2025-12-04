package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.example.realtorapp.repository.ListingRepo;
import com.example.realtorapp.utils.FavouriteManager;

public class ListingDetailActivity extends AppCompatActivity {

    private ImageView detailImage;
    private ImageButton favouriteBtn;
    private TextView detailPrice, detailAddress, detailBeds, detailBaths, detailSqft, detailDescription;
    private Button contactAgentBtn;

    private ListingRepo ListingRepository;
    private String listingId;
    private PropertyListing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        ListingRepository = new ListingRepo();

        listingId = getIntent().getStringExtra("listingId");

        bindViews();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); // empty, since your page already shows title below
        }

        loadListing();
    }

    private void bindViews() {
        detailImage = findViewById(R.id.detailImage);
        favouriteBtn = findViewById(R.id.detailFavouriteButton);

        detailPrice = findViewById(R.id.detailPrice);
        detailAddress = findViewById(R.id.detailAddress);
        detailBeds = findViewById(R.id.detailBeds);
        detailBaths = findViewById(R.id.detailBaths);
        detailSqft = findViewById(R.id.detailSqft);
        detailDescription = findViewById(R.id.detailDescription);

        contactAgentBtn = findViewById(R.id.contactAgentBtn);
    }

    private void loadListing() {
        ListingRepository.getListing(listingId, result -> {
            listing = result;
            if (listing != null) {
                updateUI();
                setupFavouriteButton();
                contactAgentBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(ListingDetailActivity.this, ContactAgentInfoCard.class);
                    intent.putExtra("ownerPhone", listing.getOwnerPhone());
                    intent.putExtra("ownerEmail", listing.getOwnerEmail());
                    startActivity(intent);
                });
            }
        });
    }

    private void updateUI() {
        detailPrice.setText("$" + listing.getPrice());
        detailAddress.setText(listing.getTitle());
        detailBeds.setText(listing.getBedroom() + " Beds");
        detailBaths.setText(listing.getBathroom() + " Baths");
        detailDescription.setText(listing.getDescription());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();   // go back to previous activity (home or favourites automatically)
        return true;
    }

    private void setupFavouriteButton() {
        FavouriteManager.isFavourite(this, listingId, isFav -> {
            favouriteBtn.setImageResource(
                    isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
            );
        });

        favouriteBtn.setOnClickListener(v -> {
            FavouriteManager.toggleFavourite(this, listingId, nowFav -> {
                favouriteBtn.setImageResource(
                        nowFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
                );
            });

            FavouriteManager.isFavourite(this, listingId, isFav -> {
                favouriteBtn.setImageResource(
                        isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
                );
            });
        });
    }
}