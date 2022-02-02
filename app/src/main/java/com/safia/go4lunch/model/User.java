package com.safia.go4lunch.model;

import androidx.annotation.Nullable;

public class User {
    public String uid;
    public String username;
    @Nullable
    private String urlPicture;
    private Restaurant restaurant;
    public User() { }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public Restaurant getRestaurantPicked() { return restaurant; }


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantPicked(Restaurant restaurantPicked) {
        restaurant = restaurantPicked;
    }

}
