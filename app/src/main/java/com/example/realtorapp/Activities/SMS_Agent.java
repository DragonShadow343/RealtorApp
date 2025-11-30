package com.example.realtorapp.Activities;

public class SMS_Agent {
    public String id, name, bio, imageUrl;
    public Double rating;
    public Boolean active;

    // Empty constructor required for Firebase
    public SMS_Agent() {}

    public SMS_Agent(String id, String name, Double rating, String imageUrl, String bio, Boolean active) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.active = active;
    }
}