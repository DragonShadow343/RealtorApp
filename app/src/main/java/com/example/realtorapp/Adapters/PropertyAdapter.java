package com.example.realtorapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtorapp.R;
import com.example.realtorapp.model.PropertyListing;

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

        // krish add your code here to add it to favourites
        // add this code to gpt and ask it
        // You can wire favourite logic here later
        holder.favCheck.setChecked(false);   // default
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, address;
        CheckBox favCheck;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.address);
            favCheck = itemView.findViewById(R.id.favCheck);
        }
    }
}
