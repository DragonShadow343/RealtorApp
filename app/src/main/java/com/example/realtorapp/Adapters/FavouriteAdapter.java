package com.example.realtorapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<PropertyListing> favourites;
    private Context context;

    public FavouriteAdapter(List<PropertyListing> favourites, Context context) {
        this.favourites = favourites;
        this.context = context;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_favourite_adapter, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyListing listing = favourites.get(position);

        holder.txtTitle.setText(listing.getTitle());
        holder.txtPrice.setText(String.format("$%.2f / month", listing.getPrice()));
        loadImage(listing.getImageUrl(), holder.imgProperty);
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    private void loadImage(String imageUrl, ImageView imgProperty) {
        // Load image using a library like Glide or Picasso

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProperty;
        TextView txtTitle, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProperty = itemView.findViewById(R.id.imgProperty);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }

}