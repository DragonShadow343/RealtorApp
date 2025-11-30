package com.example.realtorapp.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realtorapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SMS_AgentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SMS_Agent> list;

    public SMS_AgentAdapter(Context context, ArrayList<SMS_Agent> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sms_agent_item, parent, false);
        }

        SMS_Agent currentAgent = list.get(position);

        TextView nameView = convertView.findViewById(R.id.tvName);
        TextView bioView = convertView.findViewById(R.id.tvBio);
        TextView ratingView = convertView.findViewById(R.id.tvRating); // Bind the rating view
        ImageView imageView = convertView.findViewById(R.id.imgAgent);

        // Set agent name
        if (currentAgent.name != null) {
            nameView.setText(currentAgent.name);
        } else {
            nameView.setText("Unknown Agent");
        }

        // Set agent bio
        if (currentAgent.bio != null) {
            bioView.setText(currentAgent.bio);
        } else {
            bioView.setText("No bio available");
        }

        // Set agent rating
        if (currentAgent.rating != null) {
            ratingView.setText(String.valueOf(currentAgent.rating) + " ⭐");
        } else {
            ratingView.setText("N/A ⭐");
        }

        // Load profile image
        if (currentAgent.imageUrl != null && !currentAgent.imageUrl.isEmpty()) {
            Picasso.get()
                   .load(currentAgent.imageUrl)
                   .placeholder(android.R.drawable.sym_def_app_icon)
                   .error(android.R.drawable.ic_delete)
                   .into(imageView);
        } else {
            imageView.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        return convertView;
    }
}