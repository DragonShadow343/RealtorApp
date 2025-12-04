package com.example.realtorapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtorapp.Activities.ListingDetailActivity;
import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;
import com.example.realtorapp.utils.FavouriteManager;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private Context context;
    private List<PropertyListing> listings;

    public PropertyAdapter(Context context, List<PropertyListing> listings) {
        this.context = context;
        this.listings = listings;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_listing, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        PropertyListing item = listings.get(position);

        holder.title.setText(item.getTitle());
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.address.setText(item.getAddress());

        // Set heart state
        FavouriteManager.isFavourite(context, item.getListingId(), isFav -> {
            holder.favCheck.setImageResource(
                    isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
            );
        });

        // Toggle favourite
        holder.favCheck.setOnClickListener(v -> {
            FavouriteManager.toggleFavourite(context, item.getListingId());

            FavouriteManager.isFavourite(context, item.getListingId(), isFav -> {
                holder.favCheck.setImageResource(
                        isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
                );
            });
        });

        // Open detail page
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ListingDetailActivity.class);
            i.putExtra("listingId", item.getListingId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, address;
        ImageButton favCheck;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.address);
            favCheck = itemView.findViewById(R.id.favCheck);
        }
    }

    public void updateList(List<PropertyListing> newList) {
        this.listings = newList;
        notifyDataSetChanged();
    }
}
