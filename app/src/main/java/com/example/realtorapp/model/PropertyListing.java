package com.example.realtorapp.model;

public class PropertyListing {
    private String listingId, title, description, address, status, imageUrl, agentId;
    private double price;
    private int bathroom, bedroom;
    private boolean petFriendly;
    private double lattitude, longitude;

    public String getListingId() {
        return listingId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAgentId() {
        return agentId;
    }

    public double getPrice() {
        return price;
    }

    public int getBathroom() {
        return bathroom;
    }

    public int getBedroom() {
        return bedroom;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public double getLattitude() {
        return lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
