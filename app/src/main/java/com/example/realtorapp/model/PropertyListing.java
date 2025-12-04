package com.example.realtorapp.model;

public class PropertyListing {
    private String listingId, title, description, address, status, imageUrl, agentId, ownerPhone, ownerEmail;

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

    public PropertyListing() {
    }

    public String getOwnerEmail() { return ownerEmail; }

    public String getOwnerPhone() { return ownerPhone; }


    //Setters:

    public void setListingId(String listingId) {
        this.listingId = listingId; }
    public void setTitle(String title) {
        this.title = title; }
    public void setDescription(String description) {
        this.description = description; }
    public void setAddress(String address) {
        this.address = address; }
    public void setStatus(String status) {
        this.status = status; }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl; }
    public void setAgentId(String agentId) {
        this.agentId = agentId; }
    public void setPrice(double price) {
        this.price = price; }
    public void setBathroom(int bathroom) {
        this.bathroom = bathroom; }
    public void setBedroom(int bedroom) {
        this.bedroom = bedroom; }
    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly; }
    public void setLattitude(double lattitude) {
        this.lattitude = lattitude; }
    public void setLongitude(double longitude) {
        this.longitude = longitude; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }


}
